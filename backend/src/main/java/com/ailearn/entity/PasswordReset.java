package com.ailearn.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("password_resets")
public class PasswordReset extends BaseEntity {

    private String email;
    private String token;
    private LocalDateTime expiresAt;
}
