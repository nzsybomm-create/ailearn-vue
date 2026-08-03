package com.ailearn.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("learning_analytics")
public class LearningAnalytics extends BaseEntity {

    private Long userId;
    private LocalDate statDate;
    private Integer studyMinutes;
    private Integer completedLessons;
    private Integer exerciseCount;
    private Integer correctCount;
    private BigDecimal averageScore;
    private Integer streakDays;
}
