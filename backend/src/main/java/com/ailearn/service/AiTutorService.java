package com.ailearn.service;

import com.ailearn.common.BusinessException;
import com.ailearn.entity.AiTutorMessage;
import com.ailearn.entity.AiTutorSession;
import com.ailearn.mapper.AiTutorMessageMapper;
import com.ailearn.mapper.AiTutorSessionMapper;
import com.ailearn.security.UserContext;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * AI 助教服务（Mock 实现）。
 * 预留真实 LLM 接入点：将 sendMessage 内的 mock 回复替换为调用 LLM API 即可。
 */
@Service
public class AiTutorService {

    private final AiTutorSessionMapper sessionMapper;
    private final AiTutorMessageMapper messageMapper;
    private final UserContext userContext;

    public AiTutorService(AiTutorSessionMapper sessionMapper, AiTutorMessageMapper messageMapper,
                          UserContext userContext) {
        this.sessionMapper = sessionMapper;
        this.messageMapper = messageMapper;
        this.userContext = userContext;
    }

    public AiTutorSession createSession(Long courseId, String title) {
        AiTutorSession session = new AiTutorSession();
        session.setUserId(userContext.getUserId());
        session.setCourseId(courseId);
        session.setTitle(StringUtils.hasText(title) ? title : "新对话");
        session.setModel("mock");
        sessionMapper.insert(session);
        return session;
    }

    public List<AiTutorSession> mySessions() {
        return sessionMapper.selectList(Wrappers.<AiTutorSession>lambdaQuery()
                .eq(AiTutorSession::getUserId, userContext.getUserId())
                .orderByDesc(AiTutorSession::getCreatedAt));
    }

    public List<AiTutorMessage> messages(Long sessionId) {
        return messageMapper.selectList(Wrappers.<AiTutorMessage>lambdaQuery()
                .eq(AiTutorMessage::getSessionId, sessionId).orderByAsc(AiTutorMessage::getCreatedAt));
    }

    public AiTutorMessage sendMessage(Long sessionId, String content) {
        AiTutorSession session = sessionMapper.selectById(sessionId);
        if (session == null) throw new BusinessException(404, "会话不存在");
        AiTutorMessage userMsg = new AiTutorMessage();
        userMsg.setSessionId(sessionId);
        userMsg.setRole("user");
        userMsg.setContent(content);
        messageMapper.insert(userMsg);

        // ---- 真实 LLM 接入点（当前为 Mock）----
        String reply = mockReply(content);
        AiTutorMessage aiMsg = new AiTutorMessage();
        aiMsg.setSessionId(sessionId);
        aiMsg.setRole("assistant");
        aiMsg.setContent(reply);
        messageMapper.insert(aiMsg);
        return aiMsg;
    }

    private String mockReply(String content) {
        if (!StringUtils.hasText(content)) return "你好，我是 AI 助教，有什么可以帮你？";
        return "（Mock 回复）你问的是：" + content + "\n\n" +
                "这是 AI 助教的占位回答。接入真实 LLM 后，这里会返回模型生成的内容。";
    }
}
