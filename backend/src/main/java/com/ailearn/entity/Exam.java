package com.ailearn.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("exams")
public class Exam extends BaseEntity {

    private String title;
    private String description;
    private Long courseId;
    private Integer durationMinutes;
    private Integer totalScore;
    private Integer passScore;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Boolean isPublished;
    private Boolean allowReview;

    @TableField(exist = false)
    private List<Question> questions;
}
