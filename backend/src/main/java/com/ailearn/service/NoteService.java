package com.ailearn.service;

import com.ailearn.common.BusinessException;
import com.ailearn.common.PageParam;
import com.ailearn.common.PageResult;
import com.ailearn.entity.Note;
import com.ailearn.mapper.NoteMapper;
import com.ailearn.security.UserContext;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class NoteService {

    private final NoteMapper noteMapper;
    private final UserContext userContext;

    public NoteService(NoteMapper noteMapper, UserContext userContext) {
        this.noteMapper = noteMapper;
        this.userContext = userContext;
    }

    public PageResult<Note> myNotes(PageParam pageParam, Long courseId) {
        Long userId = userContext.getUserId();
        IPage<Note> page = new Page<>(pageParam.getCurrent(), pageParam.getSize());
        var q = Wrappers.<Note>lambdaQuery().eq(Note::getUserId, userId);
        if (courseId != null) q.eq(Note::getCourseId, courseId);
        q.orderByDesc(Note::getCreatedAt);
        IPage<Note> result = noteMapper.selectPage(page, q);
        return PageResult.of(result.getRecords(), result.getTotal(), result.getCurrent(), result.getSize());
    }

    public List<Note> publicNotes(Long courseId) {
        var q = Wrappers.<Note>lambdaQuery().eq(Note::getIsPublic, true);
        if (courseId != null) q.eq(Note::getCourseId, courseId);
        return noteMapper.selectList(q);
    }

    public Note create(Long courseId, Long lessonId, String title, String content, Boolean isPublic) {
        if (!StringUtils.hasText(title)) throw new BusinessException(400, "标题不能为空");
        Note note = new Note();
        note.setUserId(userContext.getUserId());
        note.setCourseId(courseId);
        note.setLessonId(lessonId);
        note.setTitle(title);
        note.setContent(content);
        note.setIsPublic(isPublic != null && isPublic);
        noteMapper.insert(note);
        return note;
    }

    public Note update(Long id, String title, String content, Boolean isPublic) {
        Note note = noteMapper.selectById(id);
        if (note == null) throw new BusinessException(404, "笔记不存在");
        if (!note.getUserId().equals(userContext.getUserId())) throw new BusinessException(403, "无权修改");
        if (StringUtils.hasText(title)) note.setTitle(title);
        if (StringUtils.hasText(content)) note.setContent(content);
        if (isPublic != null) note.setIsPublic(isPublic);
        noteMapper.updateById(note);
        return note;
    }

    public void delete(Long id) {
        Note note = noteMapper.selectById(id);
        if (note == null) throw new BusinessException(404, "笔记不存在");
        if (!note.getUserId().equals(userContext.getUserId())) throw new BusinessException(403, "无权删除");
        noteMapper.deleteById(id);
    }
}
