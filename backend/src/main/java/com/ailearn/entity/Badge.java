package com.ailearn.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("badges")
public class Badge extends BaseEntity {

    private String name;
    private String description;
    private String icon;
    private String conditionType;
    private Integer conditionValue;
}
