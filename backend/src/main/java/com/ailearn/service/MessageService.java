package com.ailearn.service;

import com.ailearn.common.BusinessException;
import com.ailearn.entity.DirectMessage;
import com.ailearn.entity.Notification;
import com.ailearn.mapper.DirectMessageMapper;
import com.ailearn.mapper.NotificationMapper;
import com.ailearn.security.UserContext;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService {

    private final DirectMessageMapper messageMapper;
    private final NotificationMapper notificationMapper;
    private final UserContext userContext;

    public MessageService(DirectMessageMapper messageMapper, NotificationMapper notificationMapper,
                          UserContext userContext) {
        this.messageMapper = messageMapper;
        this.notificationMapper = notificationMapper;
        this.userContext = userContext;
    }

    // ---------------- 私信 ----------------
    public DirectMessage send(Long receiverId, String content) {
        if (!StringUtils.hasText(content)) throw new BusinessException(400, "内容不能为空");
        DirectMessage msg = new DirectMessage();
        msg.setSenderId(userContext.getUserId());
        msg.setReceiverId(receiverId);
        msg.setContent(content);
        msg.setIsRead(false);
        messageMapper.insert(msg);
        return msg;
    }

    public List<DirectMessage> conversation(Long otherId) {
        Long me = userContext.getUserId();
        return messageMapper.selectList(Wrappers.<DirectMessage>lambdaQuery()
                .and(w -> w.eq(DirectMessage::getSenderId, me).eq(DirectMessage::getReceiverId, otherId)
                        .or().eq(DirectMessage::getSenderId, otherId).eq(DirectMessage::getReceiverId, me))
                .orderByAsc(DirectMessage::getCreatedAt));
    }

    public void read(Long messageId) {
        DirectMessage msg = messageMapper.selectById(messageId);
        if (msg == null) return;
        if (!msg.getReceiverId().equals(userContext.getUserId())) return;
        msg.setIsRead(true);
        msg.setReadAt(LocalDateTime.now());
        messageMapper.updateById(msg);
    }

    // ---------------- 通知 ----------------
    public List<Notification> myNotifications(Boolean unreadOnly) {
        var q = Wrappers.<Notification>lambdaQuery()
                .eq(Notification::getUserId, userContext.getUserId())
                .orderByDesc(Notification::getCreatedAt);
        if (unreadOnly != null && unreadOnly) q.eq(Notification::getIsRead, false);
        return notificationMapper.selectList(q);
    }

    public void readNotification(Long id) {
        Notification n = notificationMapper.selectById(id);
        if (n == null) return;
        if (!n.getUserId().equals(userContext.getUserId())) return;
        n.setIsRead(true);
        notificationMapper.updateById(n);
    }

    public Long unreadCount() {
        return notificationMapper.selectCount(Wrappers.<Notification>lambdaQuery()
                .eq(Notification::getUserId, userContext.getUserId()).eq(Notification::getIsRead, false));
    }
}
