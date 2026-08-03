package com.ailearn.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("progress")
public class Progress extends BaseEntity {

    private Long userId;
    private Long courseId;
    private Long lessonId;
    private Integer percent;
    private Boolean isCompleted;
    private LocalDateTime completedAt;
    private Integer watchSeconds;
}
