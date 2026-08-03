package com.ailearn.controller;

import com.ailearn.common.Result;
import com.ailearn.entity.GroupChat;
import com.ailearn.entity.GroupChatMember;
import com.ailearn.entity.GroupChatMessage;
import com.ailearn.service.GroupChatService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/group-chats")
public class GroupChatController {

    private final GroupChatService groupChatService;

    public GroupChatController(GroupChatService groupChatService) {
        this.groupChatService = groupChatService;
    }

    @PostMapping
    public Result<GroupChat> create(@RequestParam(required = false) Long courseId,
                                    @RequestParam String name,
                                    @RequestParam(required = false) String description,
                                    @RequestParam(required = false) Integer maxMembers) {
        return Result.success(groupChatService.create(courseId, name, description, maxMembers));
    }

    @PostMapping("/{groupId}/join")
    public Result<GroupChatMember> join(@PathVariable Long groupId) {
        return Result.success(groupChatService.join(groupId));
    }

    @GetMapping
    public Result<List<GroupChat>> list(@RequestParam(required = false) Long courseId) {
        return Result.success(groupChatService.listByCourse(courseId));
    }

    @GetMapping("/{groupId}/messages")
    public Result<List<GroupChatMessage>> messages(@PathVariable Long groupId) {
        return Result.success(groupChatService.messages(groupId));
    }

    @PostMapping("/{groupId}/messages")
    public Result<GroupChatMessage> send(@PathVariable Long groupId,
                                         @RequestParam String content,
                                         @RequestParam(required = false) String messageType) {
        return Result.success(groupChatService.send(groupId, content, messageType));
    }
}
