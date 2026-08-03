package com.ailearn.entity;

import com.ailearn.entity.enums.Difficulty;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("exercises")
public class Exercise extends BaseEntity {

    private String title;
    private String description;
    private Long courseId;
    private Long lessonId;
    private Long categoryId;
    private Difficulty difficulty;
    private Integer timeLimitMinutes;
    private Integer totalQuestions;

    @TableField(exist = false)
    private List<Question> questions;
}
