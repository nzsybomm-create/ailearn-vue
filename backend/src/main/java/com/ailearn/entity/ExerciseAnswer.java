package com.ailearn.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("exercise_answers")
public class ExerciseAnswer extends BaseEntity {

    private Long attemptId;
    private Long questionId;
    private String answer;
    private String correctAnswer;
    private Boolean isCorrect;
    private BigDecimal score;
    private String explanation;
}
