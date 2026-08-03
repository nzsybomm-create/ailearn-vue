package com.ailearn.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("announcements")
public class Announcement extends BaseEntity {

    private Long courseId;
    private Long authorId;
    private String title;
    private String content;
    private Boolean isPinned;
    private LocalDateTime publishedAt;
}
