package com.ailearn.entity;

import com.ailearn.entity.enums.AssessmentStatus;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("exercise_attempts")
public class ExerciseAttempt extends BaseEntity {

    private Long userId;
    private Long exerciseId;
    private AssessmentStatus status;
    private BigDecimal score;
    private Integer correctCount;
    private Integer totalCount;
    private LocalDateTime startedAt;
    private LocalDateTime submittedAt;
}
