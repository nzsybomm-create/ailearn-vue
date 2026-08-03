package com.ailearn.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("group_chats")
public class GroupChat extends BaseEntity {

    private Long courseId;
    private String name;
    private String description;
    private Long createdBy;
    private Integer maxMembers;
}
