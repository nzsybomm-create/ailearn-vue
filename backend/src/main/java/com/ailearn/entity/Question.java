package com.ailearn.entity;

import com.ailearn.entity.enums.Difficulty;
import com.ailearn.entity.enums.QuestionType;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("questions")
public class Question extends BaseEntity {

    private String content;
    private String options;
    private String correctAnswer;
    private String explanation;
    private QuestionType type;
    private Difficulty difficulty;
    private Long categoryId;
    private Long courseId;
    private Long createdBy;
    private BigDecimal score;
    private String tags;
}
