package com.ailearn.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("question_categories")
public class QuestionCategory extends BaseEntity {

    private String name;
    private String description;
    private Long parentId;
}
