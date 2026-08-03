package com.ailearn.entity;

import com.ailearn.entity.enums.EnrollmentStatus;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("enrollments")
public class Enrollment extends BaseEntity {

    private Long userId;
    private Long courseId;
    private EnrollmentStatus status;
    private Integer progressPercent;
    private LocalDateTime enrolledAt;
    private LocalDateTime completedAt;
    private Integer lastAccessedLessonId;
}
