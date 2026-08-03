package com.ailearn.service;

import com.ailearn.common.BusinessException;
import com.ailearn.common.PageParam;
import com.ailearn.common.PageResult;
import com.ailearn.entity.Question;
import com.ailearn.entity.Quiz;
import com.ailearn.entity.QuizAttempt;
import com.ailearn.entity.QuizQuestion;
import com.ailearn.entity.enums.AssessmentStatus;
import com.ailearn.mapper.QuestionMapper;
import com.ailearn.mapper.QuizAttemptMapper;
import com.ailearn.mapper.QuizMapper;
import com.ailearn.mapper.QuizQuestionMapper;
import com.ailearn.security.UserContext;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class QuizService {

    private final QuizMapper quizMapper;
    private final QuizQuestionMapper quizQuestionMapper;
    private final QuizAttemptMapper attemptMapper;
    private final QuestionMapper questionMapper;
    private final UserContext userContext;

    public QuizService(QuizMapper quizMapper, QuizQuestionMapper quizQuestionMapper,
                       QuizAttemptMapper attemptMapper, QuestionMapper questionMapper,
                       UserContext userContext) {
        this.quizMapper = quizMapper;
        this.quizQuestionMapper = quizQuestionMapper;
        this.attemptMapper = attemptMapper;
        this.questionMapper = questionMapper;
        this.userContext = userContext;
    }

    public PageResult<Quiz> list(PageParam pageParam, Long courseId, Long unitId, Long lessonId) {
        IPage<Quiz> page = new Page<>(pageParam.getCurrent(), pageParam.getSize());
        var q = Wrappers.<Quiz>lambdaQuery();
        if (courseId != null) q.eq(Quiz::getCourseId, courseId);
        if (unitId != null) q.eq(Quiz::getUnitId, unitId);
        if (lessonId != null) q.eq(Quiz::getLessonId, lessonId);
        q.orderByDesc(Quiz::getCreatedAt);
        IPage<Quiz> result = quizMapper.selectPage(page, q);
        return PageResult.of(result.getRecords(), result.getTotal(), result.getCurrent(), result.getSize());
    }

    public Quiz get(Long id) {
        Quiz quiz = quizMapper.selectById(id);
        if (quiz == null) throw new BusinessException(404, "测验不存在");
        List<QuizQuestion> qs = quizQuestionMapper.selectList(
                Wrappers.<QuizQuestion>lambdaQuery().eq(QuizQuestion::getQuizId, id)
                        .orderByAsc(QuizQuestion::getSortOrder));
        List<Question> questions = qs.stream()
                .map(q -> questionMapper.selectById(q.getQuestionId()))
                .filter(q -> q != null).toList();
        quiz.setQuestions(questions);
        return quiz;
    }

    public QuizAttempt start(Long quizId) {
        Long userId = userContext.getUserId();
        Quiz quiz = quizMapper.selectById(quizId);
        if (quiz == null) throw new BusinessException(404, "测验不存在");
        Long attempts = attemptMapper.selectCount(Wrappers.<QuizAttempt>lambdaQuery()
                .eq(QuizAttempt::getUserId, userId).eq(QuizAttempt::getQuizId, quizId));
        if (quiz.getMaxAttempts() != null && attempts >= quiz.getMaxAttempts()) {
            throw new BusinessException(400, "已达到最大尝试次数");
        }
        QuizAttempt attempt = new QuizAttempt();
        attempt.setUserId(userId);
        attempt.setQuizId(quizId);
        attempt.setStatus(AssessmentStatus.IN_PROGRESS);
        attempt.setStartedAt(LocalDateTime.now());
        attemptMapper.insert(attempt);
        return attempt;
    }

    public QuizAttempt submit(Long attemptId, Map<Long, String> answers) {
        QuizAttempt attempt = attemptMapper.selectById(attemptId);
        if (attempt == null) throw new BusinessException(404, "测验记录不存在");
        Quiz quiz = quizMapper.selectById(attempt.getQuizId());
        List<QuizQuestion> qs = quizQuestionMapper.selectList(
                Wrappers.<QuizQuestion>lambdaQuery().eq(QuizQuestion::getQuizId, attempt.getQuizId()));
        int correct = 0, wrong = 0;
        BigDecimal total = BigDecimal.ZERO;
        for (QuizQuestion qq : qs) {
            Question q = questionMapper.selectById(qq.getQuestionId());
            if (q == null) continue;
            String userAns = answers.get(q.getId());
            boolean isCorrect = userAns != null && userAns.trim().equalsIgnoreCase(
                    q.getCorrectAnswer() != null ? q.getCorrectAnswer().trim() : "");
            if (isCorrect) correct++; else wrong++;
            BigDecimal s = qq.getScore() != null ? qq.getScore() : BigDecimal.ZERO;
            if (isCorrect) total = total.add(s);
        }
        attempt.setStatus(AssessmentStatus.GRADED);
        attempt.setSubmittedAt(LocalDateTime.now());
        attempt.setCorrectCount(correct);
        attempt.setWrongCount(wrong);
        attempt.setScore(total);
        attemptMapper.updateById(attempt);
        return attempt;
    }

    public List<QuizAttempt> myAttempts(Long quizId) {
        return attemptMapper.selectList(Wrappers.<QuizAttempt>lambdaQuery()
                .eq(QuizAttempt::getUserId, userContext.getUserId())
                .eq(QuizAttempt::getQuizId, quizId).orderByDesc(QuizAttempt::getCreatedAt));
    }
}
