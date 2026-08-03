package com.ailearn.controller;

import com.ailearn.common.PageParam;
import com.ailearn.common.PageResult;
import com.ailearn.common.Result;
import com.ailearn.entity.Comment;
import com.ailearn.entity.Discussion;
import com.ailearn.service.DiscussionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/discussions")
public class DiscussionController {

    private final DiscussionService discussionService;

    public DiscussionController(DiscussionService discussionService) {
        this.discussionService = discussionService;
    }

    @GetMapping
    public Result<PageResult<Discussion>> list(PageParam pageParam,
                                               @RequestParam(required = false) Long courseId,
                                               @RequestParam(required = false) String keyword) {
        return Result.success(discussionService.list(pageParam, courseId, keyword));
    }

    @GetMapping("/{id}")
    public Result<Discussion> get(@PathVariable Long id) {
        return Result.success(discussionService.get(id));
    }

    @PostMapping
    public Result<Discussion> create(@RequestParam(required = false) Long courseId,
                                     @RequestParam String title,
                                     @RequestParam String content) {
        return Result.success(discussionService.create(courseId, title, content));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        discussionService.delete(id);
        return Result.success();
    }

    @PostMapping("/{id}/replies")
    public Result<Comment> reply(@PathVariable Long id,
                                 @RequestParam(required = false) Long parentId,
                                 @RequestParam String content) {
        return Result.success(discussionService.reply(id, parentId, content));
    }

    @GetMapping("/{id}/replies")
    public Result<List<Comment>> comments(@PathVariable Long id) {
        return Result.success(discussionService.comments(id));
    }

    @PostMapping("/replies/{commentId}/like")
    public Result<Void> like(@PathVariable Long commentId) {
        discussionService.likeComment(commentId);
        return Result.success();
    }
}
