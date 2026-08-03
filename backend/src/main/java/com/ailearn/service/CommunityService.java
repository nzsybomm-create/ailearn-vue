package com.ailearn.service;

import com.ailearn.common.BusinessException;
import com.ailearn.common.PageParam;
import com.ailearn.common.PageResult;
import com.ailearn.entity.Badge;
import com.ailearn.entity.Leaderboard;
import com.ailearn.entity.Recommendation;
import com.ailearn.entity.Testimonial;
import com.ailearn.entity.UserBadge;
import com.ailearn.mapper.BadgeMapper;
import com.ailearn.mapper.LeaderboardMapper;
import com.ailearn.mapper.RecommendationMapper;
import com.ailearn.mapper.TestimonialMapper;
import com.ailearn.mapper.UserBadgeMapper;
import com.ailearn.security.UserContext;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class CommunityService {

    private final LeaderboardMapper leaderboardMapper;
    private final BadgeMapper badgeMapper;
    private final UserBadgeMapper userBadgeMapper;
    private final TestimonialMapper testimonialMapper;
    private final RecommendationMapper recommendationMapper;
    private final UserContext userContext;

    public CommunityService(LeaderboardMapper leaderboardMapper, BadgeMapper badgeMapper,
                            UserBadgeMapper userBadgeMapper, TestimonialMapper testimonialMapper,
                            RecommendationMapper recommendationMapper, UserContext userContext) {
        this.leaderboardMapper = leaderboardMapper;
        this.badgeMapper = badgeMapper;
        this.userBadgeMapper = userBadgeMapper;
        this.testimonialMapper = testimonialMapper;
        this.recommendationMapper = recommendationMapper;
        this.userContext = userContext;
    }

    public List<Leaderboard> leaderboard(String period) {
        String p = StringUtils.hasText(period) ? period : "all";
        return leaderboardMapper.selectList(Wrappers.<Leaderboard>lambdaQuery()
                .eq(Leaderboard::getPeriod, p).orderByAsc(Leaderboard::getRank));
    }

    public List<Badge> allBadges() {
        return badgeMapper.selectList(null);
    }

    public List<Badge> myBadges() {
        List<UserBadge> ub = userBadgeMapper.selectList(Wrappers.<UserBadge>lambdaQuery()
                .eq(UserBadge::getUserId, userContext.getUserId()));
        if (ub.isEmpty()) return List.of();
        return badgeMapper.selectByIds(ub.stream().map(UserBadge::getBadgeId).toList());
    }

    public Testimonial createTestimonial(String content, Integer rating) {
        if (!StringUtils.hasText(content)) throw new BusinessException(400, "内容不能为空");
        Testimonial t = new Testimonial();
        t.setUserId(userContext.getUserId());
        t.setContent(content);
        t.setRating(rating);
        t.setIsApproved(false);
        testimonialMapper.insert(t);
        return t;
    }

    public PageResult<Testimonial> approvedTestimonials(PageParam pageParam) {
        IPage<Testimonial> page = new Page<>(pageParam.getCurrent(), pageParam.getSize());
        IPage<Testimonial> result = testimonialMapper.selectPage(page,
                Wrappers.<Testimonial>lambdaQuery().eq(Testimonial::getIsApproved, true)
                        .orderByDesc(Testimonial::getCreatedAt));
        return PageResult.of(result.getRecords(), result.getTotal(), result.getCurrent(), result.getSize());
    }

    public List<Recommendation> myRecommendations() {
        return recommendationMapper.selectList(Wrappers.<Recommendation>lambdaQuery()
                .eq(Recommendation::getUserId, userContext.getUserId())
                .orderByDesc(Recommendation::getCreatedAt));
    }

    public List<Recommendation> recommend(Long userId) {
        // Mock 推荐：随机取若干未点击的推荐记录（真实场景可由算法生成）
        return recommendationMapper.selectList(Wrappers.<Recommendation>lambdaQuery()
                .eq(Recommendation::getUserId, userId).eq(Recommendation::getIsClicked, false)
                .last("limit 10"));
    }
}
