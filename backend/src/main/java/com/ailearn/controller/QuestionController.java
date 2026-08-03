package com.ailearn.controller;

import com.ailearn.common.PageParam;
import com.ailearn.common.PageResult;
import com.ailearn.common.Result;
import com.ailearn.entity.Question;
import com.ailearn.entity.QuestionTag;
import com.ailearn.service.QuestionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/questions")
public class QuestionController {

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping
    public Result<PageResult<Question>> list(PageParam pageParam,
                                             @RequestParam(required = false) Long categoryId,
                                             @RequestParam(required = false) Long courseId,
                                             @RequestParam(required = false) String type,
                                             @RequestParam(required = false) String difficulty,
                                             @RequestParam(required = false) String keyword) {
        return Result.success(questionService.list(pageParam, categoryId, courseId, type, difficulty, keyword));
    }

    @GetMapping("/{id}")
    public Result<Question> get(@PathVariable Long id) {
        return Result.success(questionService.get(id));
    }

    @PostMapping
    public Result<Question> create(@RequestBody Question question) {
        return Result.success(questionService.create(question));
    }

    @PutMapping("/{id}")
    public Result<Question> update(@PathVariable Long id, @RequestBody Question question) {
        return Result.success(questionService.update(id, question));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        questionService.delete(id);
        return Result.success();
    }

    @GetMapping("/tags")
    public Result<List<QuestionTag>> tags() {
        return Result.success(questionService.tags());
    }

    @GetMapping("/random")
    public Result<List<Question>> random(@RequestParam(required = false) Long categoryId,
                                         @RequestParam(defaultValue = "10") Integer count) {
        return Result.success(questionService.randomQuestions(categoryId, count));
    }
}
