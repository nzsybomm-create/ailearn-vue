package com.ailearn.entity;

import com.ailearn.entity.enums.MaterialType;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("materials")
public class Material extends BaseEntity {

    private Long lessonId;
    private Long courseId;
    private String title;
    private MaterialType type;
    private String url;
    private String content;
    private Integer sortOrder;
    private Long fileSize;
}
