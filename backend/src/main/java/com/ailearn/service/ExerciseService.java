package com.ailearn.service;

import com.ailearn.common.BusinessException;
import com.ailearn.common.PageParam;
import com.ailearn.common.PageResult;
import com.ailearn.entity.Exercise;
import com.ailearn.entity.ExerciseAnswer;
import com.ailearn.entity.ExerciseAttempt;
import com.ailearn.entity.ExerciseQuestion;
import com.ailearn.entity.Question;
import com.ailearn.entity.enums.AssessmentStatus;
import com.ailearn.mapper.ExerciseAnswerMapper;
import com.ailearn.mapper.ExerciseAttemptMapper;
import com.ailearn.mapper.ExerciseMapper;
import com.ailearn.mapper.ExerciseQuestionMapper;
import com.ailearn.mapper.QuestionMapper;
import com.ailearn.security.UserContext;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ExerciseService {

    private final ExerciseMapper exerciseMapper;
    private final ExerciseQuestionMapper exerciseQuestionMapper;
    private final ExerciseAttemptMapper attemptMapper;
    private final ExerciseAnswerMapper answerMapper;
    private final QuestionMapper questionMapper;
    private final UserContext userContext;

    public ExerciseService(ExerciseMapper exerciseMapper, ExerciseQuestionMapper exerciseQuestionMapper,
                           ExerciseAttemptMapper attemptMapper, ExerciseAnswerMapper answerMapper,
                           QuestionMapper questionMapper, UserContext userContext) {
        this.exerciseMapper = exerciseMapper;
        this.exerciseQuestionMapper = exerciseQuestionMapper;
        this.attemptMapper = attemptMapper;
        this.answerMapper = answerMapper;
        this.questionMapper = questionMapper;
        this.userContext = userContext;
    }

    public PageResult<Exercise> list(PageParam pageParam, Long courseId, Long lessonId) {
        IPage<Exercise> page = new Page<>(pageParam.getCurrent(), pageParam.getSize());
        var q = Wrappers.<Exercise>lambdaQuery();
        if (courseId != null) q.eq(Exercise::getCourseId, courseId);
        if (lessonId != null) q.eq(Exercise::getLessonId, lessonId);
        q.orderByDesc(Exercise::getCreatedAt);
        IPage<Exercise> result = exerciseMapper.selectPage(page, q);
        return PageResult.of(result.getRecords(), result.getTotal(), result.getCurrent(), result.getSize());
    }

    public Exercise get(Long id) {
        Exercise exercise = exerciseMapper.selectById(id);
        if (exercise == null) throw new BusinessException(404, "练习不存在");
        List<ExerciseQuestion> eqs = exerciseQuestionMapper.selectList(
                Wrappers.<ExerciseQuestion>lambdaQuery().eq(ExerciseQuestion::getExerciseId, id)
                        .orderByAsc(ExerciseQuestion::getSortOrder));
        List<Question> questions = eqs.stream()
                .map(eq -> questionMapper.selectById(eq.getQuestionId()))
                .filter(q -> q != null).toList();
        exercise.setQuestions(questions);
        return exercise;
    }

    public ExerciseAttempt start(Long exerciseId) {
        ExerciseAttempt attempt = new ExerciseAttempt();
        attempt.setUserId(userContext.getUserId());
        attempt.setExerciseId(exerciseId);
        attempt.setStatus(AssessmentStatus.IN_PROGRESS);
        attempt.setStartedAt(LocalDateTime.now());
        attemptMapper.insert(attempt);
        return attempt;
    }

    public ExerciseAttempt submit(Long attemptId, Map<Long, String> answers) {
        ExerciseAttempt attempt = attemptMapper.selectById(attemptId);
        if (attempt == null) throw new BusinessException(404, "练习记录不存在");
        Exercise exercise = exerciseMapper.selectById(attempt.getExerciseId());
        List<ExerciseQuestion> eqs = exerciseQuestionMapper.selectList(
                Wrappers.<ExerciseQuestion>lambdaQuery().eq(ExerciseQuestion::getExerciseId, attempt.getExerciseId()));
        List<Question> questions = questionMapper.selectByIds(
                eqs.stream().map(ExerciseQuestion::getQuestionId).toList());

        int correct = 0;
        List<ExerciseAnswer> toSave = new ArrayList<>();
        for (Question q : questions) {
            String userAns = answers.get(q.getId());
            boolean isCorrect = userAns != null && userAns.trim().equalsIgnoreCase(
                    q.getCorrectAnswer() != null ? q.getCorrectAnswer().trim() : "");
            if (isCorrect) correct++;
            ExerciseAnswer ea = new ExerciseAnswer();
            ea.setAttemptId(attemptId);
            ea.setQuestionId(q.getId());
            ea.setAnswer(userAns);
            ea.setCorrectAnswer(q.getCorrectAnswer());
            ea.setIsCorrect(isCorrect);
            ea.setScore(isCorrect ? (q.getScore() != null ? q.getScore() : BigDecimal.ONE) : BigDecimal.ZERO);
            ea.setExplanation(q.getExplanation());
            toSave.add(ea);
        }
        answerMapper.insert(toSave);

        attempt.setStatus(AssessmentStatus.GRADED);
        attempt.setSubmittedAt(LocalDateTime.now());
        attempt.setCorrectCount(correct);
        attempt.setTotalCount(questions.size());
        BigDecimal score = toSave.stream().map(ExerciseAnswer::getScore)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        attempt.setScore(score);
        attemptMapper.updateById(attempt);
        return attempt;
    }

    public List<ExerciseAnswer> result(Long attemptId) {
        return answerMapper.selectList(Wrappers.<ExerciseAnswer>lambdaQuery()
                .eq(ExerciseAnswer::getAttemptId, attemptId));
    }
}
