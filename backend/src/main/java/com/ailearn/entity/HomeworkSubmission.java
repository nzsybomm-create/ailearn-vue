package com.ailearn.entity;

import com.ailearn.entity.enums.AssessmentStatus;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("homework_submissions")
public class HomeworkSubmission extends BaseEntity {

    private Long userId;
    private Long homeworkId;
    private AssessmentStatus status;
    private BigDecimal score;
    private String content;
    private String attachmentUrl;
    private String feedback;
    private LocalDateTime submittedAt;
    private LocalDateTime gradedAt;
    private Long gradedBy;
}
