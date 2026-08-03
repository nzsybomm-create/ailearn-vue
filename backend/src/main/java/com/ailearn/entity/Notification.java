package com.ailearn.entity;

import com.ailearn.entity.enums.NotificationType;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("notifications")
public class Notification extends BaseEntity {

    private Long userId;
    private NotificationType type;
    private String title;
    private String content;
    private String link;
    private Boolean isRead;
}
