package com.ailearn.service;

import com.ailearn.common.BusinessException;
import com.ailearn.common.PageParam;
import com.ailearn.common.PageResult;
import com.ailearn.entity.Course;
import com.ailearn.entity.Homework;
import com.ailearn.entity.HomeworkSubmission;
import com.ailearn.entity.Question;
import com.ailearn.mapper.CourseMapper;
import com.ailearn.mapper.HomeworkMapper;
import com.ailearn.mapper.HomeworkSubmissionMapper;
import com.ailearn.mapper.QuestionMapper;
import com.ailearn.security.UserContext;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TeacherService {

    private final CourseMapper courseMapper;
    private final QuestionMapper questionMapper;
    private final HomeworkMapper homeworkMapper;
    private final HomeworkSubmissionMapper submissionMapper;
    private final UserContext userContext;

    public TeacherService(CourseMapper courseMapper, QuestionMapper questionMapper,
                          HomeworkMapper homeworkMapper, HomeworkSubmissionMapper submissionMapper,
                          UserContext userContext) {
        this.courseMapper = courseMapper;
        this.questionMapper = questionMapper;
        this.homeworkMapper = homeworkMapper;
        this.submissionMapper = submissionMapper;
        this.userContext = userContext;
    }

    private Long teacherId() {
        if (!userContext.isTeacher()) throw new BusinessException(403, "仅教师可访问");
        return userContext.getUserId();
    }

    // ---------------- 课程管理 ----------------
    public Course createCourse(Course course) {
        course.setTeacherId(teacherId());
        if (course.getIsPublished() == null) course.setIsPublished(false);
        courseMapper.insert(course);
        return course;
    }

    public Course updateCourse(Long id, Course course) {
        Course exist = courseMapper.selectById(id);
        if (exist == null) throw new BusinessException(404, "课程不存在");
        if (!exist.getTeacherId().equals(teacherId())) throw new BusinessException(403, "无权操作");
        course.setId(id);
        course.setTeacherId(exist.getTeacherId());
        courseMapper.updateById(course);
        return course;
    }

    public void deleteCourse(Long id) {
        Course exist = courseMapper.selectById(id);
        if (exist == null || !exist.getTeacherId().equals(teacherId())) throw new BusinessException(403, "无权操作");
        courseMapper.deleteById(id);
    }

    public PageResult<Course> myCourses(PageParam pageParam) {
        IPage<Course> page = new Page<>(pageParam.getCurrent(), pageParam.getSize());
        IPage<Course> result = courseMapper.selectPage(page, Wrappers.<Course>lambdaQuery()
                .eq(Course::getTeacherId, teacherId()).orderByDesc(Course::getCreatedAt));
        return PageResult.of(result.getRecords(), result.getTotal(), result.getCurrent(), result.getSize());
    }

    public void publishCourse(Long id, Boolean published) {
        Course exist = courseMapper.selectById(id);
        if (exist == null || !exist.getTeacherId().equals(teacherId())) throw new BusinessException(403, "无权操作");
        exist.setIsPublished(published);
        exist.setPublishedAt(published ? LocalDateTime.now() : null);
        courseMapper.updateById(exist);
    }

    // ---------------- 题库管理 ----------------
    public Question createQuestion(Question question) {
        question.setCreatedBy(teacherId());
        questionMapper.insert(question);
        return question;
    }

    public Question updateQuestion(Long id, Question question) {
        Question exist = questionMapper.selectById(id);
        if (exist == null) throw new BusinessException(404, "题目不存在");
        question.setId(id);
        questionMapper.updateById(question);
        return question;
    }

    public void deleteQuestion(Long id) {
        questionMapper.deleteById(id);
    }

    public PageResult<Question> myQuestions(PageParam pageParam, Long courseId) {
        IPage<Question> page = new Page<>(pageParam.getCurrent(), pageParam.getSize());
        var q = Wrappers.<Question>lambdaQuery().eq(Question::getCreatedBy, teacherId());
        if (courseId != null) q.eq(Question::getCourseId, courseId);
        q.orderByDesc(Question::getCreatedAt);
        IPage<Question> result = questionMapper.selectPage(page, q);
        return PageResult.of(result.getRecords(), result.getTotal(), result.getCurrent(), result.getSize());
    }

    // ---------------- 作业批改 ----------------
    public List<HomeworkSubmission> pendingSubmissions(Long homeworkId) {
        return submissionMapper.selectList(Wrappers.<HomeworkSubmission>lambdaQuery()
                .eq(HomeworkSubmission::getHomeworkId, homeworkId)
                .eq(HomeworkSubmission::getStatus, com.ailearn.entity.enums.AssessmentStatus.SUBMITTED)
                .orderByDesc(HomeworkSubmission::getCreatedAt));
    }

    public HomeworkSubmission grade(Long submissionId, java.math.BigDecimal score, String feedback) {
        HomeworkSubmission submission = submissionMapper.selectById(submissionId);
        if (submission == null) throw new BusinessException(404, "提交不存在");
        submission.setScore(score);
        submission.setFeedback(feedback);
        submission.setStatus(com.ailearn.entity.enums.AssessmentStatus.GRADED);
        submission.setGradedAt(LocalDateTime.now());
        submission.setGradedBy(teacherId());
        submissionMapper.updateById(submission);
        return submission;
    }

    // ---------------- 仪表盘 ----------------
    public Map<String, Object> dashboard() {
        Long tid = teacherId();
        Map<String, Object> data = new HashMap<>();
        Long courseCount = courseMapper.selectCount(Wrappers.<Course>lambdaQuery().eq(Course::getTeacherId, tid));
        Long questionCount = questionMapper.selectCount(Wrappers.<Question>lambdaQuery().eq(Question::getCreatedBy, tid));
        data.put("courseCount", courseCount);
        data.put("questionCount", questionCount);
        data.put("teacherId", tid);
        return data;
    }
}
