package com.ailearn.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("group_chat_members")
public class GroupChatMember extends BaseEntity {

    private Long groupId;
    private Long userId;
    private String role;
}
