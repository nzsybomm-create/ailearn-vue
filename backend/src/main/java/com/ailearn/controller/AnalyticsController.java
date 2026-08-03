package com.ailearn.controller;

import com.ailearn.common.Result;
import com.ailearn.entity.ErrorBook;
import com.ailearn.entity.ErrorBookItem;
import com.ailearn.entity.LearningAnalytics;
import com.ailearn.service.AnalyticsService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/me")
    public Result<List<LearningAnalytics>> my() {
        return Result.success(analyticsService.myAnalytics());
    }

    @GetMapping("/range")
    public Result<List<LearningAnalytics>> range(@RequestParam(required = false) LocalDate start,
                                                 @RequestParam(required = false) LocalDate end) {
        return Result.success(analyticsService.analyticsByRange(start, end));
    }

    // ---- 错题本 ----
    @GetMapping("/error-books")
    public Result<List<ErrorBook>> errorBooks() {
        return Result.success(analyticsService.myErrorBooks());
    }

    @PostMapping("/error-books")
    public Result<ErrorBook> createErrorBook(@RequestParam String title,
                                             @RequestParam(required = false) String description) {
        return Result.success(analyticsService.createErrorBook(title, description));
    }

    @GetMapping("/error-books/{bookId}/items")
    public Result<List<ErrorBookItem>> items(@PathVariable Long bookId) {
        return Result.success(analyticsService.errorBookItems(bookId));
    }

    @PostMapping("/error-books/{bookId}/items")
    public Result<ErrorBookItem> addItem(@PathVariable Long bookId,
                                         @RequestParam Long questionId,
                                         @RequestParam(required = false) String userAnswer,
                                         @RequestParam(required = false) String correctAnswer,
                                         @RequestParam(required = false) String note) {
        return Result.success(analyticsService.addItem(bookId, questionId, userAnswer, correctAnswer, note));
    }

    @PostMapping("/error-book-items/{itemId}/resolve")
    public Result<Void> resolve(@PathVariable Long itemId,
                                @RequestParam(required = false) Boolean resolved) {
        analyticsService.resolveItem(itemId, resolved);
        return Result.success();
    }
}
