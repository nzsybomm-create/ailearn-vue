package com.ailearn.controller;

import com.ailearn.common.PageParam;
import com.ailearn.common.PageResult;
import com.ailearn.common.Result;
import com.ailearn.entity.Quiz;
import com.ailearn.entity.QuizAttempt;
import com.ailearn.service.QuizService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/quizzes")
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @GetMapping
    public Result<PageResult<Quiz>> list(PageParam pageParam,
                                         @RequestParam(required = false) Long courseId,
                                         @RequestParam(required = false) Long unitId,
                                         @RequestParam(required = false) Long lessonId) {
        return Result.success(quizService.list(pageParam, courseId, unitId, lessonId));
    }

    @GetMapping("/{id}")
    public Result<Quiz> get(@PathVariable Long id) {
        return Result.success(quizService.get(id));
    }

    @PostMapping("/{id}/start")
    public Result<QuizAttempt> start(@PathVariable Long id) {
        return Result.success(quizService.start(id));
    }

    @PostMapping("/attempts/{attemptId}/submit")
    public Result<QuizAttempt> submit(@PathVariable Long attemptId,
                                      @RequestBody Map<Long, String> answers) {
        return Result.success(quizService.submit(attemptId, answers));
    }

    @GetMapping("/{id}/attempts")
    public Result<List<QuizAttempt>> attempts(@PathVariable Long id) {
        return Result.success(quizService.myAttempts(id));
    }
}
