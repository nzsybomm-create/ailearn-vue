package com.ailearn.controller;

import com.ailearn.common.PageParam;
import com.ailearn.common.PageResult;
import com.ailearn.common.Result;
import com.ailearn.entity.Exam;
import com.ailearn.entity.ExamAttempt;
import com.ailearn.service.ExamService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/exams")
public class ExamController {

    private final ExamService examService;

    public ExamController(ExamService examService) {
        this.examService = examService;
    }

    @GetMapping
    public Result<PageResult<Exam>> list(PageParam pageParam,
                                         @RequestParam(required = false) Long courseId) {
        return Result.success(examService.list(pageParam, courseId));
    }

    @GetMapping("/{id}")
    public Result<Exam> get(@PathVariable Long id) {
        return Result.success(examService.get(id));
    }

    @PostMapping("/{id}/start")
    public Result<ExamAttempt> start(@PathVariable Long id) {
        return Result.success(examService.start(id));
    }

    @PostMapping("/attempts/{attemptId}/submit")
    public Result<ExamAttempt> submit(@PathVariable Long attemptId,
                                      @RequestBody SubmitExamReq req) {
        return Result.success(examService.submit(attemptId, req.answers(), req.cheatingLog()));
    }

    @GetMapping("/{id}/attempts")
    public Result<List<ExamAttempt>> attempts(@PathVariable Long id) {
        return Result.success(examService.myAttempts(id));
    }

    public record SubmitExamReq(Map<Long, String> answers, String cheatingLog) {}
}
