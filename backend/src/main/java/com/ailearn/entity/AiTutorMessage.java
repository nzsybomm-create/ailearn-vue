package com.ailearn.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ai_tutor_messages")
public class AiTutorMessage extends BaseEntity {

    private Long sessionId;
    private String role;
    private String content;
    private String promptTokens;
}
