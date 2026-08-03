package com.ailearn.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("study_plans")
public class StudyPlan extends BaseEntity {

    private Long userId;
    private Long courseId;
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer dailyMinutes;
    private Boolean isActive;
}
