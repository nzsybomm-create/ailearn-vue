package com.ailearn.controller;

import com.ailearn.common.PageParam;
import com.ailearn.common.PageResult;
import com.ailearn.common.Result;
import com.ailearn.entity.Exercise;
import com.ailearn.entity.ExerciseAnswer;
import com.ailearn.entity.ExerciseAttempt;
import com.ailearn.service.ExerciseService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/exercises")
public class ExerciseController {

    private final ExerciseService exerciseService;

    public ExerciseController(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    @GetMapping
    public Result<PageResult<Exercise>> list(PageParam pageParam,
                                             @RequestParam(required = false) Long courseId,
                                             @RequestParam(required = false) Long lessonId) {
        return Result.success(exerciseService.list(pageParam, courseId, lessonId));
    }

    @GetMapping("/{id}")
    public Result<Exercise> get(@PathVariable Long id) {
        return Result.success(exerciseService.get(id));
    }

    @PostMapping("/{id}/start")
    public Result<ExerciseAttempt> start(@PathVariable Long id) {
        return Result.success(exerciseService.start(id));
    }

    @PostMapping("/attempts/{attemptId}/submit")
    public Result<ExerciseAttempt> submit(@PathVariable Long attemptId,
                                          @RequestBody Map<Long, String> answers) {
        return Result.success(exerciseService.submit(attemptId, answers));
    }

    @GetMapping("/attempts/{attemptId}/result")
    public Result<List<ExerciseAnswer>> result(@PathVariable Long attemptId) {
        return Result.success(exerciseService.result(attemptId));
    }
}
