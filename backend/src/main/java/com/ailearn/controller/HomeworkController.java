package com.ailearn.controller;

import com.ailearn.common.PageParam;
import com.ailearn.common.PageResult;
import com.ailearn.common.Result;
import com.ailearn.entity.Homework;
import com.ailearn.entity.HomeworkSubmission;
import com.ailearn.service.HomeworkService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/homework")
public class HomeworkController {

    private final HomeworkService homeworkService;

    public HomeworkController(HomeworkService homeworkService) {
        this.homeworkService = homeworkService;
    }

    @GetMapping
    public Result<PageResult<Homework>> list(PageParam pageParam,
                                             @RequestParam(required = false) Long courseId,
                                             @RequestParam(required = false) Long lessonId) {
        return Result.success(homeworkService.list(pageParam, courseId, lessonId));
    }

    @GetMapping("/{id}")
    public Result<Homework> get(@PathVariable Long id) {
        return Result.success(homeworkService.get(id));
    }

    @PostMapping("/{id}/submit")
    public Result<HomeworkSubmission> submit(@PathVariable Long id,
                                             @RequestParam(required = false) String content,
                                             @RequestParam(required = false) String attachmentUrl,
                                             @RequestBody(required = false) Map<Long, String> answers) {
        return Result.success(homeworkService.submit(id, content, attachmentUrl, answers));
    }

    @PostMapping("/submissions/{submissionId}/grade")
    public Result<HomeworkSubmission> grade(@PathVariable Long submissionId,
                                            @RequestParam(required = false) BigDecimal score,
                                            @RequestParam(required = false) String feedback) {
        return Result.success(homeworkService.grade(submissionId, score, feedback));
    }

    @GetMapping("/{id}/submissions")
    public Result<List<HomeworkSubmission>> submissions(@PathVariable Long id) {
        return Result.success(homeworkService.listSubmissions(id));
    }
}
