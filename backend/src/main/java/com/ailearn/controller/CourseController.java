package com.ailearn.controller;

import com.ailearn.common.PageParam;
import com.ailearn.common.PageResult;
import com.ailearn.common.Result;
import com.ailearn.entity.Course;
import com.ailearn.entity.CourseReview;
import com.ailearn.entity.Enrollment;
import com.ailearn.security.UserContext;
import com.ailearn.service.CourseService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;
    private final UserContext userContext;

    public CourseController(CourseService courseService, UserContext userContext) {
        this.courseService = courseService;
        this.userContext = userContext;
    }

    @GetMapping
    public Result<PageResult<Course>> list(PageParam pageParam,
                                           @RequestParam(required = false) String keyword,
                                           @RequestParam(required = false) String category,
                                           @RequestParam(required = false) String level) {
        return Result.success(courseService.listCourses(pageParam, keyword, category, level, true));
    }

    @GetMapping("/{id}")
    public Result<Course> detail(@PathVariable Long id) {
        return Result.success(courseService.getDetail(id));
    }

    @PostMapping("/{id}/enroll")
    public Result<Enrollment> enroll(@PathVariable Long id) {
        return Result.success(courseService.enroll(id));
    }

    @DeleteMapping("/{id}/enroll")
    public Result<Void> cancelEnroll(@PathVariable Long id) {
        courseService.cancelEnroll(id);
        return Result.success();
    }

    @GetMapping("/my")
    public Result<PageResult<Course>> my(PageParam pageParam) {
        return Result.success(courseService.myCourses(pageParam));
    }

    @GetMapping("/favorites")
    public Result<List<Course>> favorites() {
        return Result.success(courseService.myFavorites());
    }

    @PostMapping("/{id}/favorite")
    public Result<Void> toggleFavorite(@PathVariable Long id) {
        courseService.toggleFavorite(id);
        return Result.success();
    }

    @PostMapping("/{id}/reviews")
    public Result<CourseReview> review(@PathVariable Long id,
                                       @RequestParam Integer rating,
                                       @RequestParam(required = false) String comment) {
        return Result.success(courseService.review(id, rating, comment));
    }

    @GetMapping("/{id}/reviews")
    public Result<List<CourseReview>> reviews(@PathVariable Long id) {
        return Result.success(courseService.listReviews(id));
    }
}
