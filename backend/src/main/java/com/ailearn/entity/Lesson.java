package com.ailearn.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("lessons")
public class Lesson extends BaseEntity {

    private Long unitId;
    private Long courseId;
    private String title;
    private String description;
    private String content;
    private Integer sortOrder;
    private Integer durationMinutes;
    private Boolean isPublished;
}
