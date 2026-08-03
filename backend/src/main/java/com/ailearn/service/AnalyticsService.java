package com.ailearn.service;

import com.ailearn.common.BusinessException;
import com.ailearn.entity.ErrorBook;
import com.ailearn.entity.ErrorBookItem;
import com.ailearn.entity.LearningAnalytics;
import com.ailearn.mapper.ErrorBookItemMapper;
import com.ailearn.mapper.ErrorBookMapper;
import com.ailearn.mapper.LearningAnalyticsMapper;
import com.ailearn.security.UserContext;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;

@Service
public class AnalyticsService {

    private final LearningAnalyticsMapper analyticsMapper;
    private final ErrorBookMapper errorBookMapper;
    private final ErrorBookItemMapper errorBookItemMapper;
    private final UserContext userContext;

    public AnalyticsService(LearningAnalyticsMapper analyticsMapper, ErrorBookMapper errorBookMapper,
                            ErrorBookItemMapper errorBookItemMapper, UserContext userContext) {
        this.analyticsMapper = analyticsMapper;
        this.errorBookMapper = errorBookMapper;
        this.errorBookItemMapper = errorBookItemMapper;
        this.userContext = userContext;
    }

    public List<LearningAnalytics> myAnalytics() {
        return analyticsMapper.selectList(Wrappers.<LearningAnalytics>lambdaQuery()
                .eq(LearningAnalytics::getUserId, userContext.getUserId())
                .orderByDesc(LearningAnalytics::getStatDate));
    }

    public List<LearningAnalytics> analyticsByRange(LocalDate start, LocalDate end) {
        var q = Wrappers.<LearningAnalytics>lambdaQuery()
                .eq(LearningAnalytics::getUserId, userContext.getUserId())
                .orderByAsc(LearningAnalytics::getStatDate);
        if (start != null) q.ge(LearningAnalytics::getStatDate, start);
        if (end != null) q.le(LearningAnalytics::getStatDate, end);
        return analyticsMapper.selectList(q);
    }

    // ---------------- 错题本 ----------------
    public List<ErrorBook> myErrorBooks() {
        return errorBookMapper.selectList(Wrappers.<ErrorBook>lambdaQuery()
                .eq(ErrorBook::getUserId, userContext.getUserId())
                .orderByDesc(ErrorBook::getCreatedAt));
    }

    public ErrorBook createErrorBook(String title, String description) {
        if (!StringUtils.hasText(title)) throw new BusinessException(400, "标题不能为空");
        ErrorBook book = new ErrorBook();
        book.setUserId(userContext.getUserId());
        book.setTitle(title);
        book.setDescription(description);
        errorBookMapper.insert(book);
        return book;
    }

    public List<ErrorBookItem> errorBookItems(Long bookId) {
        return errorBookItemMapper.selectList(Wrappers.<ErrorBookItem>lambdaQuery()
                .eq(ErrorBookItem::getErrorBookId, bookId)
                .orderByDesc(ErrorBookItem::getCreatedAt));
    }

    public ErrorBookItem addItem(Long bookId, Long questionId, String userAnswer,
                                 String correctAnswer, String note) {
        ErrorBookItem item = new ErrorBookItem();
        item.setErrorBookId(bookId);
        item.setQuestionId(questionId);
        item.setUserAnswer(userAnswer);
        item.setCorrectAnswer(correctAnswer);
        item.setNote(note);
        item.setMistakeCount(1);
        item.setIsResolved(false);
        errorBookItemMapper.insert(item);
        return item;
    }

    public void resolveItem(Long itemId, Boolean resolved) {
        ErrorBookItem item = errorBookItemMapper.selectById(itemId);
        if (item == null) throw new BusinessException(404, "错题不存在");
        item.setIsResolved(resolved != null && resolved);
        errorBookItemMapper.updateById(item);
    }
}
