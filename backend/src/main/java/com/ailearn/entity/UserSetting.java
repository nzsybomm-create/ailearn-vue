package com.ailearn.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("user_settings")
public class UserSetting extends BaseEntity {

    private Long userId;
    private String theme;
    private String language;
    private Boolean emailNotification;
    private Boolean pushNotification;
    private Boolean studyReminder;
    private String reminderTime;
}
