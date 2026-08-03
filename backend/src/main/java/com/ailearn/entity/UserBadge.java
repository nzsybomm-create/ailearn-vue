package com.ailearn.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("user_badges")
public class UserBadge extends BaseEntity {

    private Long userId;
    private Long badgeId;
}
