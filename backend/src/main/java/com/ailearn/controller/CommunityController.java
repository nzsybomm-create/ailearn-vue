package com.ailearn.controller;

import com.ailearn.common.PageParam;
import com.ailearn.common.PageResult;
import com.ailearn.common.Result;
import com.ailearn.entity.Badge;
import com.ailearn.entity.Leaderboard;
import com.ailearn.entity.Recommendation;
import com.ailearn.entity.Testimonial;
import com.ailearn.service.CommunityService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/community")
public class CommunityController {

    private final CommunityService communityService;

    public CommunityController(CommunityService communityService) {
        this.communityService = communityService;
    }

    @GetMapping("/leaderboard")
    public Result<List<Leaderboard>> leaderboard(@RequestParam(required = false) String period) {
        return Result.success(communityService.leaderboard(period));
    }

    @GetMapping("/badges")
    public Result<List<Badge>> badges() {
        return Result.success(communityService.allBadges());
    }

    @GetMapping("/badges/mine")
    public Result<List<Badge>> myBadges() {
        return Result.success(communityService.myBadges());
    }

    @PostMapping("/testimonials")
    public Result<Testimonial> testimonial(@RequestParam String content,
                                           @RequestParam(required = false) Integer rating) {
        return Result.success(communityService.createTestimonial(content, rating));
    }

    @GetMapping("/testimonials")
    public Result<PageResult<Testimonial>> testimonials(PageParam pageParam) {
        return Result.success(communityService.approvedTestimonials(pageParam));
    }

    @GetMapping("/recommendations")
    public Result<List<Recommendation>> recommendations() {
        return Result.success(communityService.myRecommendations());
    }
}
