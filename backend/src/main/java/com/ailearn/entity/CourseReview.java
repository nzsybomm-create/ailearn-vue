package com.ailearn.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("course_reviews")
public class CourseReview extends BaseEntity {

    private Long courseId;
    private Long userId;
    private Integer rating;
    private String comment;
}
