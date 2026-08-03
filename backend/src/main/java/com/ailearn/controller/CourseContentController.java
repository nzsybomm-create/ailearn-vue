package com.ailearn.controller;

import com.ailearn.common.Result;
import com.ailearn.entity.Lesson;
import com.ailearn.entity.Material;
import com.ailearn.entity.Progress;
import com.ailearn.entity.Unit;
import com.ailearn.service.CourseContentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseContentController {

    private final CourseContentService courseContentService;

    public CourseContentController(CourseContentService courseContentService) {
        this.courseContentService = courseContentService;
    }

    @GetMapping("/{courseId}/units")
    public Result<List<Unit>> units(@PathVariable Long courseId) {
        return Result.success(courseContentService.listUnits(courseId));
    }

    @GetMapping("/{courseId}/lessons")
    public Result<List<Lesson>> lessons(@PathVariable Long courseId,
                                        @RequestParam(required = false) Long unitId) {
        return Result.success(courseContentService.listLessons(courseId, unitId));
    }

    @GetMapping("/lessons/{lessonId}")
    public Result<Lesson> lesson(@PathVariable Long lessonId) {
        return Result.success(courseContentService.getLesson(lessonId));
    }

    @GetMapping("/lessons/{lessonId}/materials")
    public Result<List<Material>> materials(@PathVariable Long lessonId) {
        return Result.success(courseContentService.listMaterials(lessonId));
    }

    @PostMapping("/lessons/{lessonId}/progress")
    public Result<Void> progress(@PathVariable Long lessonId,
                                 @RequestParam(required = false) Integer percent,
                                 @RequestParam(required = false) Integer watchSeconds) {
        courseContentService.updateProgress(lessonId, percent, watchSeconds);
        return Result.success();
    }

    @GetMapping("/{courseId}/progress")
    public Result<List<Progress>> progressList(@PathVariable Long courseId) {
        return Result.success(courseContentService.myProgress(courseId));
    }
}
