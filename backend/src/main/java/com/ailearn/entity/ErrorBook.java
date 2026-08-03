package com.ailearn.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("error_books")
public class ErrorBook extends BaseEntity {

    private Long userId;
    private String title;
    private String description;
}
