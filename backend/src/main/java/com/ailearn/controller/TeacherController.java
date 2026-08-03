package com.ailearn.controller;

import com.ailearn.common.PageParam;
import com.ailearn.common.PageResult;
import com.ailearn.common.Result;
import com.ailearn.entity.Course;
import com.ailearn.entity.HomeworkSubmission;
import com.ailearn.entity.Question;
import com.ailearn.service.TeacherService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/teacher")
public class TeacherController {

    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @GetMapping("/dashboard")
    public Result<Map<String, Object>> dashboard() {
        return Result.success(teacherService.dashboard());
    }

    // ---- 课程管理 ----
    @PostMapping("/courses")
    public Result<Course> createCourse(@RequestBody Course course) {
        return Result.success(teacherService.createCourse(course));
    }

    @PutMapping("/courses/{id}")
    public Result<Course> updateCourse(@PathVariable Long id, @RequestBody Course course) {
        return Result.success(teacherService.updateCourse(id, course));
    }

    @DeleteMapping("/courses/{id}")
    public Result<Void> deleteCourse(@PathVariable Long id) {
        teacherService.deleteCourse(id);
        return Result.success();
    }

    @GetMapping("/courses")
    public Result<PageResult<Course>> myCourses(PageParam pageParam) {
        return Result.success(teacherService.myCourses(pageParam));
    }

    @PostMapping("/courses/{id}/publish")
    public Result<Void> publish(@PathVariable Long id, @RequestParam Boolean published) {
        teacherService.publishCourse(id, published);
        return Result.success();
    }

    // ---- 题库管理 ----
    @PostMapping("/questions")
    public Result<Question> createQuestion(@RequestBody Question question) {
        return Result.success(teacherService.createQuestion(question));
    }

    @PutMapping("/questions/{id}")
    public Result<Question> updateQuestion(@PathVariable Long id, @RequestBody Question question) {
        return Result.success(teacherService.updateQuestion(id, question));
    }

    @DeleteMapping("/questions/{id}")
    public Result<Void> deleteQuestion(@PathVariable Long id) {
        teacherService.deleteQuestion(id);
        return Result.success();
    }

    @GetMapping("/questions")
    public Result<PageResult<Question>> myQuestions(PageParam pageParam,
                                                    @RequestParam(required = false) Long courseId) {
        return Result.success(teacherService.myQuestions(pageParam, courseId));
    }

    // ---- 批改 ----
    @GetMapping("/homework/{homeworkId}/submissions")
    public Result<List<HomeworkSubmission>> pending(@PathVariable Long homeworkId) {
        return Result.success(teacherService.pendingSubmissions(homeworkId));
    }

    @PostMapping("/submissions/{submissionId}/grade")
    public Result<HomeworkSubmission> grade(@PathVariable Long submissionId,
                                            @RequestParam(required = false) BigDecimal score,
                                            @RequestParam(required = false) String feedback) {
        return Result.success(teacherService.grade(submissionId, score, feedback));
    }
}
