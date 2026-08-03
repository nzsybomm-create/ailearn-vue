package com.ailearn.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("recommendations")
public class Recommendation extends BaseEntity {

    private Long userId;
    private Long courseId;
    private String reason;
    private String algorithm;
    private Boolean isClicked;
}
