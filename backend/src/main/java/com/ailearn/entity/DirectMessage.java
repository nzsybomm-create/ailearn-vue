package com.ailearn.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("direct_messages")
public class DirectMessage extends BaseEntity {

    private Long senderId;
    private Long receiverId;
    private String content;
    private Boolean isRead;
    private LocalDateTime readAt;
}
