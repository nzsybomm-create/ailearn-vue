package com.ailearn.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("group_chat_messages")
public class GroupChatMessage extends BaseEntity {

    private Long groupId;
    private Long senderId;
    private String content;
    private String messageType;
}
