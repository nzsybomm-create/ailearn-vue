package com.ailearn.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("comments")
public class Comment extends BaseEntity {

    private Long userId;
    private Long discussionId;
    private Long parentId;
    private String content;
    private Integer likeCount;
}
