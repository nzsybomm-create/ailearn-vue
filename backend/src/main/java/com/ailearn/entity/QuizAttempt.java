package com.ailearn.entity;

import com.ailearn.entity.enums.AssessmentStatus;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("quiz_attempts")
public class QuizAttempt extends BaseEntity {

    private Long userId;
    private Long quizId;
    private AssessmentStatus status;
    private BigDecimal score;
    private Integer correctCount;
    private Integer wrongCount;
    private LocalDateTime startedAt;
    private LocalDateTime submittedAt;
}
