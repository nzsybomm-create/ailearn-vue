package com.ailearn.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("study_tasks")
public class StudyTask extends BaseEntity {

    private Long planId;
    private String title;
    private LocalDate scheduledDate;
    private Boolean isCompleted;
    private Integer durationMinutes;
}
