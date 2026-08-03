package com.ailearn.service;

import com.ailearn.common.BusinessException;
import com.ailearn.common.PageParam;
import com.ailearn.common.PageResult;
import com.ailearn.entity.StudyPlan;
import com.ailearn.entity.StudyTask;
import com.ailearn.mapper.StudyPlanMapper;
import com.ailearn.mapper.StudyTaskMapper;
import com.ailearn.security.UserContext;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;

@Service
public class PlanService {

    private final StudyPlanMapper planMapper;
    private final StudyTaskMapper taskMapper;
    private final UserContext userContext;

    public PlanService(StudyPlanMapper planMapper, StudyTaskMapper taskMapper, UserContext userContext) {
        this.planMapper = planMapper;
        this.taskMapper = taskMapper;
        this.userContext = userContext;
    }

    public PageResult<StudyPlan> myPlans(PageParam pageParam) {
        IPage<StudyPlan> page = new Page<>(pageParam.getCurrent(), pageParam.getSize());
        IPage<StudyPlan> result = planMapper.selectPage(page, Wrappers.<StudyPlan>lambdaQuery()
                .eq(StudyPlan::getUserId, userContext.getUserId())
                .orderByDesc(StudyPlan::getCreatedAt));
        return PageResult.of(result.getRecords(), result.getTotal(), result.getCurrent(), result.getSize());
    }

    public StudyPlan create(Long courseId, String title, LocalDate startDate, LocalDate endDate,
                            Integer dailyMinutes, Boolean active) {
        if (!StringUtils.hasText(title)) throw new BusinessException(400, "标题不能为空");
        StudyPlan plan = new StudyPlan();
        plan.setUserId(userContext.getUserId());
        plan.setCourseId(courseId);
        plan.setTitle(title);
        plan.setStartDate(startDate);
        plan.setEndDate(endDate);
        plan.setDailyMinutes(dailyMinutes);
        plan.setIsActive(active != null && active);
        planMapper.insert(plan);
        return plan;
    }

    public void remove(Long id) {
        StudyPlan plan = planMapper.selectById(id);
        if (plan == null || !plan.getUserId().equals(userContext.getUserId())) {
            throw new BusinessException(404, "计划不存在");
        }
        planMapper.deleteById(id);
    }

    public List<StudyTask> tasks(Long planId) {
        return taskMapper.selectList(Wrappers.<StudyTask>lambdaQuery()
                .eq(StudyTask::getPlanId, planId).orderByAsc(StudyTask::getScheduledDate));
    }

    public StudyTask addTask(Long planId, String title, LocalDate scheduledDate,
                             Integer durationMinutes) {
        if (!StringUtils.hasText(title)) throw new BusinessException(400, "标题不能为空");
        StudyTask task = new StudyTask();
        task.setPlanId(planId);
        task.setTitle(title);
        task.setScheduledDate(scheduledDate);
        task.setDurationMinutes(durationMinutes);
        task.setIsCompleted(false);
        taskMapper.insert(task);
        return task;
    }

    public void completeTask(Long taskId, Boolean completed) {
        StudyTask task = taskMapper.selectById(taskId);
        if (task == null) throw new BusinessException(404, "任务不存在");
        task.setIsCompleted(completed != null && completed);
        taskMapper.updateById(task);
    }
}
