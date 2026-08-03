package com.ailearn.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("testimonials")
public class Testimonial extends BaseEntity {

    private Long userId;
    private String content;
    private Integer rating;
    private Boolean isApproved;
}
