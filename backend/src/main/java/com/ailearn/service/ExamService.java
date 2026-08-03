package com.ailearn.service;

import com.ailearn.common.BusinessException;
import com.ailearn.common.PageParam;
import com.ailearn.common.PageResult;
import com.ailearn.entity.Exam;
import com.ailearn.entity.ExamAttempt;
import com.ailearn.entity.ExamQuestion;
import com.ailearn.entity.Question;
import com.ailearn.entity.enums.AssessmentStatus;
import com.ailearn.mapper.ExamAttemptMapper;
import com.ailearn.mapper.ExamMapper;
import com.ailearn.mapper.ExamQuestionMapper;
import com.ailearn.mapper.QuestionMapper;
import com.ailearn.security.UserContext;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class ExamService {

    private final ExamMapper examMapper;
    private final ExamQuestionMapper examQuestionMapper;
    private final ExamAttemptMapper attemptMapper;
    private final QuestionMapper questionMapper;
    private final UserContext userContext;

    public ExamService(ExamMapper examMapper, ExamQuestionMapper examQuestionMapper,
                       ExamAttemptMapper attemptMapper, QuestionMapper questionMapper,
                       UserContext userContext) {
        this.examMapper = examMapper;
        this.examQuestionMapper = examQuestionMapper;
        this.attemptMapper = attemptMapper;
        this.questionMapper = questionMapper;
        this.userContext = userContext;
    }

    public PageResult<Exam> list(PageParam pageParam, Long courseId) {
        IPage<Exam> page = new Page<>(pageParam.getCurrent(), pageParam.getSize());
        var q = Wrappers.<Exam>lambdaQuery();
        if (courseId != null) q.eq(Exam::getCourseId, courseId);
        q.orderByDesc(Exam::getCreatedAt);
        IPage<Exam> result = examMapper.selectPage(page, q);
        return PageResult.of(result.getRecords(), result.getTotal(), result.getCurrent(), result.getSize());
    }

    public Exam get(Long id) {
        Exam exam = examMapper.selectById(id);
        if (exam == null) throw new BusinessException(404, "考试不存在");
        List<ExamQuestion> qs = examQuestionMapper.selectList(
                Wrappers.<ExamQuestion>lambdaQuery().eq(ExamQuestion::getExamId, id)
                        .orderByAsc(ExamQuestion::getSortOrder));
        List<Question> questions = qs.stream()
                .map(q -> questionMapper.selectById(q.getQuestionId()))
                .filter(q -> q != null).toList();
        exam.setQuestions(questions);
        return exam;
    }

    public ExamAttempt start(Long examId) {
        Long userId = userContext.getUserId();
        Exam exam = examMapper.selectById(examId);
        if (exam == null) throw new BusinessException(404, "考试不存在");
        if (exam.getStartTime() != null && exam.getStartTime().isAfter(LocalDateTime.now())) {
            throw new BusinessException(400, "考试尚未开始");
        }
        if (exam.getEndTime() != null && exam.getEndTime().isBefore(LocalDateTime.now())) {
            throw new BusinessException(400, "考试已结束");
        }
        ExamAttempt attempt = new ExamAttempt();
        attempt.setUserId(userId);
        attempt.setExamId(examId);
        attempt.setStatus(AssessmentStatus.IN_PROGRESS);
        attempt.setStartedAt(LocalDateTime.now());
        attemptMapper.insert(attempt);
        return attempt;
    }

    public ExamAttempt submit(Long attemptId, Map<Long, String> answers, String cheatingLog) {
        ExamAttempt attempt = attemptMapper.selectById(attemptId);
        if (attempt == null) throw new BusinessException(404, "考试记录不存在");
        Exam exam = examMapper.selectById(attempt.getExamId());
        List<ExamQuestion> qs = examQuestionMapper.selectList(
                Wrappers.<ExamQuestion>lambdaQuery().eq(ExamQuestion::getExamId, attempt.getExamId()));
        int correct = 0, wrong = 0;
        BigDecimal total = BigDecimal.ZERO;
        for (ExamQuestion eq : qs) {
            Question q = questionMapper.selectById(eq.getQuestionId());
            if (q == null) continue;
            String userAns = answers.get(q.getId());
            boolean isCorrect = userAns != null && userAns.trim().equalsIgnoreCase(
                    q.getCorrectAnswer() != null ? q.getCorrectAnswer().trim() : "");
            if (isCorrect) correct++; else wrong++;
            BigDecimal s = eq.getScore() != null ? eq.getScore() : BigDecimal.ZERO;
            if (isCorrect) total = total.add(s);
        }
        attempt.setStatus(AssessmentStatus.SUBMITTED);
        attempt.setSubmittedAt(LocalDateTime.now());
        attempt.setCorrectCount(correct);
        attempt.setWrongCount(wrong);
        attempt.setScore(total);
        attempt.setCheatingLog(cheatingLog);
        attemptMapper.updateById(attempt);
        return attempt;
    }

    public List<ExamAttempt> myAttempts(Long examId) {
        return attemptMapper.selectList(Wrappers.<ExamAttempt>lambdaQuery()
                .eq(ExamAttempt::getUserId, userContext.getUserId())
                .eq(ExamAttempt::getExamId, examId).orderByDesc(ExamAttempt::getCreatedAt));
    }
}
