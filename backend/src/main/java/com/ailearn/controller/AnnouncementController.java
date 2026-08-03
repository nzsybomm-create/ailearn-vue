package com.ailearn.controller;

import com.ailearn.common.PageParam;
import com.ailearn.common.PageResult;
import com.ailearn.common.Result;
import com.ailearn.entity.Announcement;
import com.ailearn.service.AnnouncementService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/announcements")
public class AnnouncementController {

    private final AnnouncementService announcementService;

    public AnnouncementController(AnnouncementService announcementService) {
        this.announcementService = announcementService;
    }

    @GetMapping
    public Result<PageResult<Announcement>> list(PageParam pageParam,
                                                 @RequestParam(required = false) Long courseId) {
        return Result.success(announcementService.list(pageParam, courseId));
    }

    @PostMapping
    public Result<Announcement> create(@RequestParam(required = false) Long courseId,
                                       @RequestParam String title,
                                       @RequestParam(required = false) String content,
                                       @RequestParam(required = false) Boolean pinned) {
        return Result.success(announcementService.create(courseId, title, content, pinned));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        announcementService.delete(id);
        return Result.success();
    }
}
