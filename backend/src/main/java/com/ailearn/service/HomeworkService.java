package com.ailearn.service;

import com.ailearn.common.BusinessException;
import com.ailearn.common.PageParam;
import com.ailearn.common.PageResult;
import com.ailearn.entity.Homework;
import com.ailearn.entity.HomeworkQuestion;
import com.ailearn.entity.HomeworkSubmission;
import com.ailearn.entity.Question;
import com.ailearn.entity.enums.AssessmentStatus;
import com.ailearn.mapper.HomeworkMapper;
import com.ailearn.mapper.HomeworkQuestionMapper;
import com.ailearn.mapper.HomeworkSubmissionMapper;
import com.ailearn.mapper.QuestionMapper;
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
public class HomeworkService {

    private final HomeworkMapper homeworkMapper;
    private final HomeworkQuestionMapper homeworkQuestionMapper;
    private final HomeworkSubmissionMapper submissionMapper;
    private final QuestionMapper questionMapper;
    private final UserContext userContext;

    public HomeworkService(HomeworkMapper homeworkMapper, HomeworkQuestionMapper homeworkQuestionMapper,
                           HomeworkSubmissionMapper submissionMapper, QuestionMapper questionMapper,
                           UserContext userContext) {
        this.homeworkMapper = homeworkMapper;
        this.homeworkQuestionMapper = homeworkQuestionMapper;
        this.submissionMapper = submissionMapper;
        this.questionMapper = questionMapper;
        this.userContext = userContext;
    }

    public PageResult<Homework> list(PageParam pageParam, Long courseId, Long lessonId) {
        IPage<Homework> page = new Page<>(pageParam.getCurrent(), pageParam.getSize());
        var q = Wrappers.<Homework>lambdaQuery();
        if (courseId != null) q.eq(Homework::getCourseId, courseId);
        if (lessonId != null) q.eq(Homework::getLessonId, lessonId);
        q.orderByDesc(Homework::getCreatedAt);
        IPage<Homework> result = homeworkMapper.selectPage(page, q);
        return PageResult.of(result.getRecords(), result.getTotal(), result.getCurrent(), result.getSize());
    }

    public Homework get(Long id) {
        Homework homework = homeworkMapper.selectById(id);
        if (homework == null) throw new BusinessException(404, "作业不存在");
        List<HomeworkQuestion> qs = homeworkQuestionMapper.selectList(
                Wrappers.<HomeworkQuestion>lambdaQuery().eq(HomeworkQuestion::getHomeworkId, id)
                        .orderByAsc(HomeworkQuestion::getSortOrder));
        List<Question> questions = qs.stream()
                .map(q -> questionMapper.selectById(q.getQuestionId()))
                .filter(q -> q != null).toList();
        homework.setQuestions(questions);
        return homework;
    }

    public HomeworkSubmission submit(Long homeworkId, String content, String attachmentUrl,
                                     Map<Long, String> answers) {
        Homework homework = homeworkMapper.selectById(homeworkId);
        if (homework == null) throw new BusinessException(404, "作业不存在");
        HomeworkSubmission submission = new HomeworkSubmission();
        submission.setUserId(userContext.getUserId());
        submission.setHomeworkId(homeworkId);
        submission.setStatus(AssessmentStatus.SUBMITTED);
        submission.setContent(content);
        submission.setAttachmentUrl(attachmentUrl);
        submission.setSubmittedAt(LocalDateTime.now());
        submissionMapper.insert(submission);
        return submission;
    }

    public HomeworkSubmission grade(Long submissionId, BigDecimal score, String feedback) {
        HomeworkSubmission submission = submissionMapper.selectById(submissionId);
        if (submission == null) throw new BusinessException(404, "提交记录不存在");
        submission.setScore(score);
        submission.setFeedback(feedback);
        submission.setStatus(AssessmentStatus.GRADED);
        submission.setGradedAt(LocalDateTime.now());
        submission.setGradedBy(userContext.getUserId());
        submissionMapper.updateById(submission);
        return submission;
    }

    public List<HomeworkSubmission> listSubmissions(Long homeworkId) {
        return submissionMapper.selectList(Wrappers.<HomeworkSubmission>lambdaQuery()
                .eq(HomeworkSubmission::getHomeworkId, homeworkId)
                .orderByDesc(HomeworkSubmission::getCreatedAt));
    }
}
