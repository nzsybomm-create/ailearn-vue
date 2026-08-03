package com.ailearn.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("homework_questions")
public class HomeworkQuestion extends BaseEntity {

    private Long homeworkId;
    private Long questionId;
    private Integer sortOrder;
    private BigDecimal score;
}
