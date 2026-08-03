package com.ailearn.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("exercise_questions")
public class ExerciseQuestion extends BaseEntity {

    private Long exerciseId;
    private Long questionId;
    private Integer sortOrder;
}
