package com.ailearn.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("question_tag_relations")
public class QuestionTagRelation extends BaseEntity {

    private Long questionId;
    private Long tagId;
}
