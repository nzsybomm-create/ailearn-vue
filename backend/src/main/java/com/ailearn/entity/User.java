package com.ailearn.entity;

import com.ailearn.entity.enums.Role;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("users")
public class User extends BaseEntity {

    private String email;
    private String name;
    private String avatar;
    private Role role;
    private String studentId;
    private String bio;
    private String phone;
    private String major;
    private Integer grade;
}
