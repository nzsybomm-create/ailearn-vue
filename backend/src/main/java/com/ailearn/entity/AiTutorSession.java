package com.ailearn.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ai_tutor_sessions")
public class AiTutorSession extends BaseEntity {

    private Long userId;
    private Long courseId;
    private String title;
    private String model;
}
