package com.ailearn.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("exam_questions")
public class ExamQuestion extends BaseEntity {

    private Long examId;
    private Long questionId;
    private Integer sortOrder;
    private BigDecimal score;
}
