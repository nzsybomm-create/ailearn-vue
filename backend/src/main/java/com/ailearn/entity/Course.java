package com.ailearn.entity;

import com.ailearn.entity.enums.CourseLevel;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("courses")
public class Course extends BaseEntity {

    private String title;
    private String description;
    private String coverImage;
    private CourseLevel level;
    private String category;
    private String tags;
    private Long teacherId;
    private BigDecimal price;
    private Integer durationMinutes;
    private Boolean isPublished;
    private LocalDateTime publishedAt;
}
