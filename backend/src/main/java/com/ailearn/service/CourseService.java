package com.ailearn.service;

import com.ailearn.common.BusinessException;
import com.ailearn.common.PageParam;
import com.ailearn.common.PageResult;
import com.ailearn.entity.Course;
import com.ailearn.entity.CourseReview;
import com.ailearn.entity.Enrollment;
import com.ailearn.entity.Favorite;
import com.ailearn.entity.enums.EnrollmentStatus;
import com.ailearn.mapper.CourseMapper;
import com.ailearn.mapper.CourseReviewMapper;
import com.ailearn.mapper.EnrollmentMapper;
import com.ailearn.mapper.FavoriteMapper;
import com.ailearn.security.UserContext;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CourseService {

    private final CourseMapper courseMapper;
    private final EnrollmentMapper enrollmentMapper;
    private final FavoriteMapper favoriteMapper;
    private final CourseReviewMapper courseReviewMapper;
    private final UserContext userContext;

    public CourseService(CourseMapper courseMapper, EnrollmentMapper enrollmentMapper,
                         FavoriteMapper favoriteMapper, CourseReviewMapper courseReviewMapper,
                         UserContext userContext) {
        this.courseMapper = courseMapper;
        this.enrollmentMapper = enrollmentMapper;
        this.favoriteMapper = favoriteMapper;
        this.courseReviewMapper = courseReviewMapper;
        this.userContext = userContext;
    }

    public PageResult<Course> listCourses(PageParam pageParam, String keyword, String category,
                                           String level, Boolean isPublished) {
        IPage<Course> page = new Page<>(pageParam.getCurrent(), pageParam.getSize());
        var query = Wrappers.<Course>lambdaQuery();
        if (StringUtils.hasText(keyword)) {
            query.like(Course::getTitle, keyword).or().like(Course::getDescription, keyword);
        }
        if (StringUtils.hasText(category)) {
            query.eq(Course::getCategory, category);
        }
        if (StringUtils.hasText(level)) {
            query.eq(Course::getLevel, level);
        }
        if (isPublished != null) {
            query.eq(Course::getIsPublished, isPublished);
        }
        query.orderByDesc(Course::getCreatedAt);
        IPage<Course> result = courseMapper.selectPage(page, query);
        return PageResult.of(result.getRecords(), result.getTotal(),
                result.getCurrent(), result.getSize());
    }

    public Course getDetail(Long id) {
        Course course = courseMapper.selectById(id);
        if (course == null) {
            throw new BusinessException(404, "课程不存在");
        }
        return course;
    }

    public Enrollment enroll(Long courseId) {
        Long userId = userContext.getUserId();
        Course course = courseMapper.selectById(courseId);
        if (course == null) {
            throw new BusinessException(404, "课程不存在");
        }
        if (enrollmentMapper.selectOne(Wrappers.<Enrollment>lambdaQuery()
                .eq(Enrollment::getUserId, userId).eq(Enrollment::getCourseId, courseId)) != null) {
            throw new BusinessException(400, "已选过该课程");
        }
        Enrollment enrollment = new Enrollment();
        enrollment.setUserId(userId);
        enrollment.setCourseId(courseId);
        enrollment.setStatus(EnrollmentStatus.ACTIVE);
        enrollment.setProgressPercent(0);
        enrollment.setEnrolledAt(LocalDateTime.now());
        enrollmentMapper.insert(enrollment);
        return enrollment;
    }

    public void cancelEnroll(Long courseId) {
        Long userId = userContext.getUserId();
        enrollmentMapper.delete(Wrappers.<Enrollment>lambdaQuery()
                .eq(Enrollment::getUserId, userId).eq(Enrollment::getCourseId, courseId));
    }

    public PageResult<Course> myCourses(PageParam pageParam) {
        Long userId = userContext.getUserId();
        IPage<Course> page = new Page<>(pageParam.getCurrent(), pageParam.getSize());
        var query = Wrappers.<Course>lambdaQuery()
                .inSql(Course::getId, "select course_id from enrollments where user_id = " + userId
                        + " and deleted = 0");
        IPage<Course> result = courseMapper.selectPage(page, query);
        return PageResult.of(result.getRecords(), result.getTotal(),
                result.getCurrent(), result.getSize());
    }

    public List<Course> myFavorites() {
        Long userId = userContext.getUserId();
        List<Favorite> favorites = favoriteMapper.selectList(
                Wrappers.<Favorite>lambdaQuery().eq(Favorite::getUserId, userId));
        if (favorites.isEmpty()) {
            return List.of();
        }
        List<Long> ids = favorites.stream().map(Favorite::getCourseId).toList();
        return courseMapper.selectBatchIds(ids);
    }

    public void toggleFavorite(Long courseId) {
        Long userId = userContext.getUserId();
        Favorite exist = favoriteMapper.selectOne(Wrappers.<Favorite>lambdaQuery()
                .eq(Favorite::getUserId, userId).eq(Favorite::getCourseId, courseId));
        if (exist != null) {
            favoriteMapper.deleteById(exist.getId());
        } else {
            Favorite favorite = new Favorite();
            favorite.setUserId(userId);
            favorite.setCourseId(courseId);
            favoriteMapper.insert(favorite);
        }
    }

    public CourseReview review(Long courseId, Integer rating, String comment) {
        if (rating == null || rating < 1 || rating > 5) {
            throw new BusinessException(400, "评分需在 1-5 之间");
        }
        CourseReview review = new CourseReview();
        review.setCourseId(courseId);
        review.setUserId(userContext.getUserId());
        review.setRating(rating);
        review.setComment(comment);
        courseReviewMapper.insert(review);
        return review;
    }

    public List<CourseReview> listReviews(Long courseId) {
        return courseReviewMapper.selectList(Wrappers.<CourseReview>lambdaQuery()
                .eq(CourseReview::getCourseId, courseId).orderByDesc(CourseReview::getCreatedAt));
    }
}
