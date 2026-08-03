package com.ailearn.service;

import com.ailearn.common.BusinessException;
import com.ailearn.common.PageParam;
import com.ailearn.common.PageResult;
import com.ailearn.entity.Comment;
import com.ailearn.entity.Discussion;
import com.ailearn.mapper.CommentMapper;
import com.ailearn.mapper.DiscussionMapper;
import com.ailearn.security.UserContext;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class DiscussionService {

    private final DiscussionMapper discussionMapper;
    private final CommentMapper commentMapper;
    private final UserContext userContext;

    public DiscussionService(DiscussionMapper discussionMapper, CommentMapper commentMapper,
                             UserContext userContext) {
        this.discussionMapper = discussionMapper;
        this.commentMapper = commentMapper;
        this.userContext = userContext;
    }

    public PageResult<Discussion> list(PageParam pageParam, Long courseId, String keyword) {
        IPage<Discussion> page = new Page<>(pageParam.getCurrent(), pageParam.getSize());
        var q = Wrappers.<Discussion>lambdaQuery();
        if (courseId != null) q.eq(Discussion::getCourseId, courseId);
        if (StringUtils.hasText(keyword)) q.like(Discussion::getTitle, keyword);
        q.orderByDesc(Discussion::getIsPinned)
                .orderByDesc(Discussion::getCreatedAt);
        IPage<Discussion> result = discussionMapper.selectPage(page, q);
        return PageResult.of(result.getRecords(), result.getTotal(), result.getCurrent(), result.getSize());
    }

    public Discussion get(Long id) {
        Discussion discussion = discussionMapper.selectById(id);
        if (discussion == null) throw new BusinessException(404, "讨论不存在");
        discussion.setViewCount(discussion.getViewCount() == null ? 1 : discussion.getViewCount() + 1);
        discussionMapper.updateById(discussion);
        return discussion;
    }

    public Discussion create(Long courseId, String title, String content) {
        if (!StringUtils.hasText(title)) throw new BusinessException(400, "标题不能为空");
        Discussion discussion = new Discussion();
        discussion.setUserId(userContext.getUserId());
        discussion.setCourseId(courseId);
        discussion.setTitle(title);
        discussion.setContent(content);
        discussion.setReplyCount(0);
        discussion.setViewCount(0);
        discussionMapper.insert(discussion);
        return discussion;
    }

    public void delete(Long id) {
        Discussion discussion = discussionMapper.selectById(id);
        if (discussion == null) throw new BusinessException(404, "讨论不存在");
        if (!discussion.getUserId().equals(userContext.getUserId())) {
            throw new BusinessException(403, "无权删除");
        }
        discussionMapper.deleteById(id);
    }

    public Comment reply(Long discussionId, Long parentId, String content) {
        if (!StringUtils.hasText(content)) throw new BusinessException(400, "内容不能为空");
        Discussion discussion = discussionMapper.selectById(discussionId);
        if (discussion == null) throw new BusinessException(404, "讨论不存在");
        Comment comment = new Comment();
        comment.setUserId(userContext.getUserId());
        comment.setDiscussionId(discussionId);
        comment.setParentId(parentId);
        comment.setContent(content);
        comment.setLikeCount(0);
        commentMapper.insert(comment);

        discussion.setReplyCount(discussion.getReplyCount() == null ? 1 : discussion.getReplyCount() + 1);
        discussionMapper.updateById(discussion);
        return comment;
    }

    public List<Comment> comments(Long discussionId) {
        return commentMapper.selectList(Wrappers.<Comment>lambdaQuery()
                .eq(Comment::getDiscussionId, discussionId).orderByAsc(Comment::getCreatedAt));
    }

    public void likeComment(Long commentId) {
        Comment comment = commentMapper.selectById(commentId);
        if (comment == null) throw new BusinessException(404, "评论不存在");
        comment.setLikeCount(comment.getLikeCount() == null ? 1 : comment.getLikeCount() + 1);
        commentMapper.updateById(comment);
    }
}
