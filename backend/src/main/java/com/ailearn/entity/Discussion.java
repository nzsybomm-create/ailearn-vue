package com.ailearn.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("discussions")
public class Discussion extends BaseEntity {

    private Long userId;
    private Long courseId;
    private String title;
    private String content;
    private Boolean isPinned;
    private Integer viewCount;
    private Integer replyCount;
}
