package com.ailearn.service;

import com.ailearn.common.BusinessException;
import com.ailearn.entity.GroupChat;
import com.ailearn.entity.GroupChatMember;
import com.ailearn.entity.GroupChatMessage;
import com.ailearn.mapper.GroupChatMapper;
import com.ailearn.mapper.GroupChatMemberMapper;
import com.ailearn.mapper.GroupChatMessageMapper;
import com.ailearn.security.UserContext;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class GroupChatService {

    private final GroupChatMapper groupChatMapper;
    private final GroupChatMemberMapper memberMapper;
    private final GroupChatMessageMapper messageMapper;
    private final UserContext userContext;

    public GroupChatService(GroupChatMapper groupChatMapper, GroupChatMemberMapper memberMapper,
                            GroupChatMessageMapper messageMapper, UserContext userContext) {
        this.groupChatMapper = groupChatMapper;
        this.memberMapper = memberMapper;
        this.messageMapper = messageMapper;
        this.userContext = userContext;
    }

    public GroupChat create(Long courseId, String name, String description, Integer maxMembers) {
        GroupChat chat = new GroupChat();
        chat.setCourseId(courseId);
        chat.setName(name);
        chat.setDescription(description);
        chat.setMaxMembers(maxMembers == null ? 200 : maxMembers);
        chat.setCreatedBy(userContext.getUserId());
        groupChatMapper.insert(chat);
        join(chat.getId());
        return chat;
    }

    public GroupChatMember join(Long groupId) {
        Long userId = userContext.getUserId();
        if (memberMapper.selectOne(Wrappers.<GroupChatMember>lambdaQuery()
                .eq(GroupChatMember::getGroupId, groupId).eq(GroupChatMember::getUserId, userId)) != null) {
            throw new BusinessException(400, "已在群聊中");
        }
        GroupChatMember member = new GroupChatMember();
        member.setGroupId(groupId);
        member.setUserId(userId);
        member.setRole("member");
        memberMapper.insert(member);
        return member;
    }

    public List<GroupChat> listByCourse(Long courseId) {
        return groupChatMapper.selectList(Wrappers.<GroupChat>lambdaQuery()
                .eq(GroupChat::getCourseId, courseId));
    }

    public List<GroupChatMessage> messages(Long groupId) {
        return messageMapper.selectList(Wrappers.<GroupChatMessage>lambdaQuery()
                .eq(GroupChatMessage::getGroupId, groupId).orderByAsc(GroupChatMessage::getCreatedAt));
    }

    public GroupChatMessage send(Long groupId, String content, String messageType) {
        if (!StringUtils.hasText(content)) throw new BusinessException(400, "消息内容为空");
        if (memberMapper.selectOne(Wrappers.<GroupChatMember>lambdaQuery()
                .eq(GroupChatMember::getGroupId, groupId).eq(GroupChatMember::getUserId, userContext.getUserId())) == null) {
            throw new BusinessException(403, "未加入群聊");
        }
        GroupChatMessage msg = new GroupChatMessage();
        msg.setGroupId(groupId);
        msg.setSenderId(userContext.getUserId());
        msg.setContent(content);
        msg.setMessageType(messageType == null ? "text" : messageType);
        messageMapper.insert(msg);
        return msg;
    }
}
