package com.ailearn.service;

import com.ailearn.common.BusinessException;
import com.ailearn.common.PageParam;
import com.ailearn.common.PageResult;
import com.ailearn.entity.Announcement;
import com.ailearn.mapper.AnnouncementMapper;
import com.ailearn.security.UserContext;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AnnouncementService {

    private final AnnouncementMapper announcementMapper;
    private final UserContext userContext;

    public AnnouncementService(AnnouncementMapper announcementMapper, UserContext userContext) {
        this.announcementMapper = announcementMapper;
        this.userContext = userContext;
    }

    public PageResult<Announcement> list(PageParam pageParam, Long courseId) {
        IPage<Announcement> page = new Page<>(pageParam.getCurrent(), pageParam.getSize());
        var q = Wrappers.<Announcement>lambdaQuery();
        if (courseId != null) q.eq(Announcement::getCourseId, courseId);
        q.orderByDesc(Announcement::getIsPinned).orderByDesc(Announcement::getCreatedAt);
        IPage<Announcement> result = announcementMapper.selectPage(page, q);
        return PageResult.of(result.getRecords(), result.getTotal(), result.getCurrent(), result.getSize());
    }

    public Announcement create(Long courseId, String title, String content, Boolean pinned) {
        if (!StringUtils.hasText(title)) throw new BusinessException(400, "标题不能为空");
        Announcement announcement = new Announcement();
        announcement.setCourseId(courseId);
        announcement.setAuthorId(userContext.getUserId());
        announcement.setTitle(title);
        announcement.setContent(content);
        announcement.setIsPinned(pinned != null && pinned);
        announcement.setPublishedAt(LocalDateTime.now());
        announcementMapper.insert(announcement);
        return announcement;
    }

    public void delete(Long id) {
        announcementMapper.deleteById(id);
    }
}
