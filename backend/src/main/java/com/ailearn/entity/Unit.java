package com.ailearn.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("units")
public class Unit extends BaseEntity {

    private Long courseId;
    private String title;
    private String description;
    private Integer sortOrder;
}
