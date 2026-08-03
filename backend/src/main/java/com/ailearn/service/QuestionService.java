package com.ailearn.service;

import com.ailearn.common.BusinessException;
import com.ailearn.common.PageParam;
import com.ailearn.common.PageResult;
import com.ailearn.entity.Question;
import com.ailearn.entity.QuestionTag;
import com.ailearn.entity.QuestionTagRelation;
import com.ailearn.mapper.QuestionMapper;
import com.ailearn.mapper.QuestionTagMapper;
import com.ailearn.mapper.QuestionTagRelationMapper;
import com.ailearn.security.UserContext;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class QuestionService {

    private final QuestionMapper questionMapper;
    private final QuestionTagMapper questionTagMapper;
    private final QuestionTagRelationMapper relationMapper;
    private final UserContext userContext;

    public QuestionService(QuestionMapper questionMapper, QuestionTagMapper questionTagMapper,
                           QuestionTagRelationMapper relationMapper, UserContext userContext) {
        this.questionMapper = questionMapper;
        this.questionTagMapper = questionTagMapper;
        this.relationMapper = relationMapper;
        this.userContext = userContext;
    }

    public PageResult<Question> list(PageParam pageParam, Long categoryId, Long courseId,
                                     String type, String difficulty, String keyword) {
        IPage<Question> page = new Page<>(pageParam.getCurrent(), pageParam.getSize());
        var q = Wrappers.<Question>lambdaQuery();
        if (categoryId != null) q.eq(Question::getCategoryId, categoryId);
        if (courseId != null) q.eq(Question::getCourseId, courseId);
        if (StringUtils.hasText(type)) q.eq(Question::getType, type);
        if (StringUtils.hasText(difficulty)) q.eq(Question::getDifficulty, difficulty);
        if (StringUtils.hasText(keyword)) q.like(Question::getContent, keyword);
        q.orderByDesc(Question::getCreatedAt);
        IPage<Question> result = questionMapper.selectPage(page, q);
        return PageResult.of(result.getRecords(), result.getTotal(), result.getCurrent(), result.getSize());
    }

    public Question get(Long id) {
        Question question = questionMapper.selectById(id);
        if (question == null) throw new BusinessException(404, "题目不存在");
        return question;
    }

    public Question create(Question question) {
        if (question.getCreatedBy() == null) question.setCreatedBy(userContext.getUserId());
        questionMapper.insert(question);
        return question;
    }

    public Question update(Long id, Question question) {
        Question exist = questionMapper.selectById(id);
        if (exist == null) throw new BusinessException(404, "题目不存在");
        question.setId(id);
        questionMapper.updateById(question);
        return question;
    }

    public void delete(Long id) {
        questionMapper.deleteById(id);
    }

    public List<QuestionTag> tags() {
        return questionTagMapper.selectList(null);
    }

    public List<Question> randomQuestions(Long categoryId, Integer count) {
        var q = Wrappers.<Question>lambdaQuery();
        if (categoryId != null) q.eq(Question::getCategoryId, categoryId);
        q.last("order by rand() limit " + (count == null ? 10 : count));
        return questionMapper.selectList(q);
    }
}
