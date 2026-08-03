package com.ailearn.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("quiz_questions")
public class QuizQuestion extends BaseEntity {

    private Long quizId;
    private Long questionId;
    private Integer sortOrder;
    private BigDecimal score;
}
