package com.ailearn.controller;

import com.ailearn.common.Result;
import com.ailearn.entity.DirectMessage;
import com.ailearn.entity.Notification;
import com.ailearn.service.MessageService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping
    public Result<DirectMessage> send(@RequestParam Long receiverId, @RequestParam String content) {
        return Result.success(messageService.send(receiverId, content));
    }

    @GetMapping("/conversation")
    public Result<List<DirectMessage>> conversation(@RequestParam Long otherId) {
        return Result.success(messageService.conversation(otherId));
    }

    @PostMapping("/{messageId}/read")
    public Result<Void> read(@PathVariable Long messageId) {
        messageService.read(messageId);
        return Result.success();
    }

    @GetMapping("/notifications")
    public Result<List<Notification>> notifications(@RequestParam(required = false) Boolean unreadOnly) {
        return Result.success(messageService.myNotifications(unreadOnly));
    }

    @PostMapping("/notifications/{id}/read")
    public Result<Void> readNotification(@PathVariable Long id) {
        messageService.readNotification(id);
        return Result.success();
    }

    @GetMapping("/notifications/unread-count")
    public Result<Long> unreadCount() {
        return Result.success(messageService.unreadCount());
    }
}
