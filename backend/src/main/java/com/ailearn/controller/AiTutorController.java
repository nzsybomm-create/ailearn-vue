package com.ailearn.controller;

import com.ailearn.common.Result;
import com.ailearn.entity.AiTutorMessage;
import com.ailearn.entity.AiTutorSession;
import com.ailearn.service.AiTutorService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ai-tutor")
public class AiTutorController {

    private final AiTutorService aiTutorService;

    public AiTutorController(AiTutorService aiTutorService) {
        this.aiTutorService = aiTutorService;
    }

    @PostMapping("/sessions")
    public Result<AiTutorSession> createSession(@RequestParam(required = false) Long courseId,
                                                @RequestParam(required = false) String title) {
        return Result.success(aiTutorService.createSession(courseId, title));
    }

    @GetMapping("/sessions")
    public Result<List<AiTutorSession>> sessions() {
        return Result.success(aiTutorService.mySessions());
    }

    @GetMapping("/sessions/{sessionId}/messages")
    public Result<List<AiTutorMessage>> messages(@PathVariable Long sessionId) {
        return Result.success(aiTutorService.messages(sessionId));
    }

    @PostMapping("/sessions/{sessionId}/messages")
    public Result<AiTutorMessage> send(@PathVariable Long sessionId,
                                       @RequestParam String content) {
        return Result.success(aiTutorService.sendMessage(sessionId, content));
    }
}
