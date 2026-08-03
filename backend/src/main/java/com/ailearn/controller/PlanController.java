package com.ailearn.controller;

import com.ailearn.common.PageParam;
import com.ailearn.common.PageResult;
import com.ailearn.common.Result;
import com.ailearn.entity.StudyPlan;
import com.ailearn.entity.StudyTask;
import com.ailearn.service.PlanService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/plans")
public class PlanController {

    private final PlanService planService;

    public PlanController(PlanService planService) {
        this.planService = planService;
    }

    @GetMapping
    public Result<PageResult<StudyPlan>> list(PageParam pageParam) {
        return Result.success(planService.myPlans(pageParam));
    }

    @PostMapping
    public Result<StudyPlan> create(@RequestParam(required = false) Long courseId,
                                    @RequestParam String title,
                                    @RequestParam(required = false) LocalDate startDate,
                                    @RequestParam(required = false) LocalDate endDate,
                                    @RequestParam(required = false) Integer dailyMinutes,
                                    @RequestParam(required = false) Boolean active) {
        return Result.success(planService.create(courseId, title, startDate, endDate, dailyMinutes, active));
    }

    @DeleteMapping("/{id}")
    public Result<Void> remove(@PathVariable Long id) {
        planService.remove(id);
        return Result.success();
    }

    @GetMapping("/{id}/tasks")
    public Result<List<StudyTask>> tasks(@PathVariable Long id) {
        return Result.success(planService.tasks(id));
    }

    @PostMapping("/{id}/tasks")
    public Result<StudyTask> addTask(@PathVariable Long id,
                                     @RequestParam String title,
                                     @RequestParam(required = false) LocalDate scheduledDate,
                                     @RequestParam(required = false) Integer durationMinutes) {
        return Result.success(planService.addTask(id, title, scheduledDate, durationMinutes));
    }

    @PostMapping("/tasks/{taskId}/complete")
    public Result<Void> complete(@PathVariable Long taskId,
                                 @RequestParam(required = false) Boolean completed) {
        planService.completeTask(taskId, completed);
        return Result.success();
    }
}
