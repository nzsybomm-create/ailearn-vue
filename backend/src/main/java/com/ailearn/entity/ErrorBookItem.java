package com.ailearn.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("error_book_items")
public class ErrorBookItem extends BaseEntity {

    private Long errorBookId;
    private Long questionId;
    private String userAnswer;
    private String correctAnswer;
    private String note;
    private Integer mistakeCount;
    private Boolean isResolved;
}
