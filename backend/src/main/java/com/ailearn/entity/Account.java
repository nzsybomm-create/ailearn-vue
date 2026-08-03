package com.ailearn.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("accounts")
public class Account extends BaseEntity {

    private Long userId;
    private String provider;
    private String providerAccountId;
    private String passwordHash;
    private String refreshToken;
    private String accessToken;
    private Long expiresAt;
}
