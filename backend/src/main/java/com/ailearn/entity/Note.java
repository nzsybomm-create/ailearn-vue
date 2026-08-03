package com.ailearn.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("notes")
public class Note extends BaseEntity {

    private Long userId;
    private Long courseId;
    private Long lessonId;
    private String title;
    private String content;
    private Boolean isPublic;
}
