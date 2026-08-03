package com.ailearn.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("homework")
public class Homework extends BaseEntity {

    private String title;
    private String description;
    private Long courseId;
    private Long lessonId;
    private Integer totalScore;
    private LocalDateTime deadline;
    private Boolean allowLateSubmission;
    private Boolean isPublished;

    @TableField(exist = false)
    private List<Question> questions;
}
