package com.ailearn.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("leaderboards")
public class Leaderboard extends BaseEntity {

    private Long userId;
    private String period;
    private Integer totalScore;
    private Integer rank;
    private Integer studyMinutes;
    private Integer completedLessons;
}
