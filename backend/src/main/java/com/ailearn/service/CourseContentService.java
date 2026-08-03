package com.ailearn.service;

import com.ailearn.common.BusinessException;
import com.ailearn.entity.Lesson;
import com.ailearn.entity.Material;
import com.ailearn.entity.Progress;
import com.ailearn.entity.Unit;
import com.ailearn.mapper.LessonMapper;
import com.ailearn.mapper.MaterialMapper;
import com.ailearn.mapper.ProgressMapper;
import com.ailearn.mapper.UnitMapper;
import com.ailearn.security.UserContext;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CourseContentService {

    private final UnitMapper unitMapper;
    private final LessonMapper lessonMapper;
    private final MaterialMapper materialMapper;
    private final ProgressMapper progressMapper;
    private final UserContext userContext;

    public CourseContentService(UnitMapper unitMapper, LessonMapper lessonMapper,
                                MaterialMapper materialMapper, ProgressMapper progressMapper,
                                UserContext userContext) {
        this.unitMapper = unitMapper;
        this.lessonMapper = lessonMapper;
        this.materialMapper = materialMapper;
        this.progressMapper = progressMapper;
        this.userContext = userContext;
    }

    public List<Unit> listUnits(Long courseId) {
        return unitMapper.selectList(Wrappers.<Unit>lambdaQuery()
                .eq(Unit::getCourseId, courseId).orderByAsc(Unit::getSortOrder));
    }

    public List<Lesson> listLessons(Long courseId, Long unitId) {
        var q = Wrappers.<Lesson>lambdaQuery().eq(Lesson::getCourseId, courseId);
        if (unitId != null) {
            q.eq(Lesson::getUnitId, unitId);
        }
        q.orderByAsc(Lesson::getSortOrder);
        return lessonMapper.selectList(q);
    }

    public Lesson getLesson(Long lessonId) {
        Lesson lesson = lessonMapper.selectById(lessonId);
        if (lesson == null) {
            throw new BusinessException(404, "课时不存在");
        }
        return lesson;
    }

    public List<Material> listMaterials(Long lessonId) {
        return materialMapper.selectList(Wrappers.<Material>lambdaQuery()
                .eq(Material::getLessonId, lessonId).orderByAsc(Material::getSortOrder));
    }

    public void updateProgress(Long lessonId, Integer percent, Integer watchSeconds) {
        Long userId = userContext.getUserId();
        Lesson lesson = lessonMapper.selectById(lessonId);
        if (lesson == null) {
            throw new BusinessException(404, "课时不存在");
        }
        Progress progress = progressMapper.selectOne(Wrappers.<Progress>lambdaQuery()
                .eq(Progress::getUserId, userId).eq(Progress::getLessonId, lessonId));
        boolean completed = percent != null && percent >= 100;
        if (progress == null) {
            progress = new Progress();
            progress.setUserId(userId);
            progress.setCourseId(lesson.getCourseId());
            progress.setLessonId(lessonId);
            progress.setPercent(percent == null ? 0 : percent);
            progress.setIsCompleted(completed);
            progress.setWatchSeconds(watchSeconds == null ? 0 : watchSeconds);
            if (completed) progress.setCompletedAt(LocalDateTime.now());
            progressMapper.insert(progress);
        } else {
            progress.setPercent(percent == null ? progress.getPercent() : percent);
            progress.setWatchSeconds(watchSeconds == null ? progress.getWatchSeconds() : watchSeconds);
            progress.setIsCompleted(completed);
            if (completed && progress.getCompletedAt() == null) progress.setCompletedAt(LocalDateTime.now());
            progressMapper.updateById(progress);
        }
    }

    public List<Progress> myProgress(Long courseId) {
        Long userId = userContext.getUserId();
        return progressMapper.selectList(Wrappers.<Progress>lambdaQuery()
                .eq(Progress::getUserId, userId).eq(Progress::getCourseId, courseId));
    }
}
