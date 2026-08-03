-- AI Learn 平台示例数据 (MySQL 8.0+)
-- 执行方式：在已 source schema.sql 并 USE ailearn 后执行：
--   source backend/src/main/resources/data.sql
-- 说明：
--   - 使用 INSERT IGNORE，可重复执行而不会报主键冲突
--   - 所有演示账号密码统一为 123456（BCrypt 哈希见下）
--   - 演示账号：
--        student@ailearn.com  (学生)
--        teacher@ailearn.com   (教师)
--        admin@ailearn.com     (管理员)

USE ailearn;
SET FOREIGN_KEY_CHECKS = 0;

-- ============ 用户 ============
INSERT IGNORE INTO users (id, email, name, avatar, role, student_id, bio, phone, major, grade, created_at, updated_at, deleted)
VALUES
  (1001, 'student@ailearn.com', '小明同学', 'https://i.pravatar.cc/150?img=11', 'STUDENT', 'S2024001', '热爱编程的在校学生', '13800000001', '计算机科学', 2, NOW(), NOW(), 0),
  (1002, 'teacher@ailearn.com', '李老师的课堂', 'https://i.pravatar.cc/150?img=12', 'TEACHER', NULL, '十年一线教学经验', '13800000002', '教育技术', NULL, NOW(), NOW(), 0),
  (1003, 'admin@ailearn.com', '系统管理员', 'https://i.pravatar.cc/150?img=13', 'ADMIN', NULL, '平台运维', '13800000003', NULL, NULL, NOW(), NOW(), 0);

-- ============ 账号 (密码统一 123456) ============
-- BCrypt hash of '123456' -> $2a$10$40jrM686IuIZnCb3uxuC3.zGG9PDoBberywvCXHee4KbQhAaZ5Uja
INSERT IGNORE INTO accounts (id, user_id, provider, provider_account_id, password_hash, created_at, updated_at, deleted)
VALUES
  (2001, 1001, 'credentials', 'student@ailearn.com', '$2a$10$40jrM686IuIZnCb3uxuC3.zGG9PDoBberywvCXHee4KbQhAaZ5Uja', NOW(), NOW(), 0),
  (2002, 1002, 'credentials', 'teacher@ailearn.com', '$2a$10$40jrM686IuIZnCb3uxuC3.zGG9PDoBberywvCXHee4KbQhAaZ5Uja', NOW(), NOW(), 0),
  (2003, 1003, 'credentials', 'admin@ailearn.com', '$2a$10$40jrM686IuIZnCb3uxuC3.zGG9PDoBberywvCXHee4KbQhAaZ5Uja', NOW(), NOW(), 0);

-- ============ 用户设置 ============
INSERT IGNORE INTO user_settings (id, user_id, theme, language, email_notification, push_notification, study_reminder, reminder_time, created_at, updated_at, deleted)
VALUES
  (3001, 1001, 'light', 'zh-CN', 1, 1, 1, '20:00', NOW(), NOW(), 0),
  (3002, 1002, 'light', 'zh-CN', 1, 0, 0, '09:00', NOW(), NOW(), 0);

-- ============ 课程 ============
INSERT IGNORE INTO courses (id, title, description, cover_image, level, category, tags, teacher_id, price, duration_minutes, is_published, published_at, created_at, updated_at, deleted)
VALUES
  (4001, 'Java 编程入门到实战', '从零开始学习 Java 语言，掌握面向对象、集合、IO 与多线程等核心知识，并完成一个综合项目。', 'https://picsum.photos/seed/java/400/240', 'BEGINNER', '编程开发', 'Java,面向对象,基础', 1002, 0.00, 480, 1, NOW(), NOW(), NOW(), 0),
  (4002, 'Web 前端开发实战', 'HTML / CSS / JavaScript 基础，结合 Vue 3 构建现代化单页应用。', 'https://picsum.photos/seed/web/400/240', 'BEGINNER', '编程开发', 'HTML,CSS,JavaScript,Vue', 1002, 0.00, 360, 1, NOW(), NOW(), NOW(), 0),
  (4003, '数据结构与算法精讲', '系统讲解常见数据结构与算法思想，配套大量练习与真题。', 'https://picsum.photos/seed/dsa/400/240', 'INTERMEDIATE', '计算机基础', '算法,数据结构,面试', 1002, 99.00, 600, 1, NOW(), NOW(), NOW(), 0);

-- ============ 选课 ============
INSERT IGNORE INTO enrollments (id, user_id, course_id, status, progress_percent, enrolled_at, last_accessed_lesson_id, created_at, updated_at, deleted)
VALUES
  (5001, 1001, 4001, 'ACTIVE', 35, NOW(), 6002, NOW(), NOW(), 0),
  (5002, 1001, 4002, 'ACTIVE', 10, NOW(), 6004, NOW(), NOW(), 0);

-- ============ 收藏 ============
INSERT IGNORE INTO favorites (id, user_id, course_id, created_at, updated_at, deleted)
VALUES
  (6001, 1001, 4003, NOW(), NOW(), 0);

-- ============ 课程评价 ============
INSERT IGNORE INTO course_reviews (id, course_id, user_id, rating, comment, created_at, updated_at, deleted)
VALUES
  (7001, 4001, 1001, 5, '讲得非常清晰，跟着做收获很大！', NOW(), NOW(), 0),
  (7002, 4001, 1003, 4, '内容详实，适合入门。', NOW(), NOW(), 0);

-- ============ 单元 ============
INSERT IGNORE INTO units (id, course_id, title, description, sort_order, created_at, updated_at, deleted)
VALUES
  (8001, 4001, '第一章 环境与基础', '搭建 Java 开发环境，理解基础语法', 1, NOW(), NOW(), 0),
  (8002, 4001, '第二章 面向对象', '类、对象、继承与多态', 2, NOW(), NOW(), 0),
  (8003, 4002, '第一章 网页三剑客', 'HTML / CSS / JavaScript 入门', 1, NOW(), NOW(), 0);

-- ============ 课时 ============
INSERT IGNORE INTO lessons (id, unit_id, course_id, title, description, content, sort_order, duration_minutes, is_published, created_at, updated_at, deleted)
VALUES
  (6001, 8001, 4001, '1.1 安装 JDK 与 IDE', '配置开发环境', '本节课带领大家完成 JDK 安装、环境变量配置以及 IDE（如 IntelliJ IDEA）的初始化。', 1, 25, 1, NOW(), NOW(), 0),
  (6002, 8001, 4001, '1.2 第一个 Java 程序', 'Hello World 与程序结构', '认识 main 方法、类结构与基础输出语句 System.out.println。', 2, 30, 1, NOW(), NOW(), 0),
  (6003, 8002, 4001, '2.1 类与对象', '面向对象基础', '理解类的定义、对象的创建以及 this 关键字。', 1, 40, 1, NOW(), NOW(), 0),
  (6004, 8003, 4002, '1.1 HTML 基础', '页面结构', '学习 HTML 标签、标题、段落与列表。', 1, 20, 1, NOW(), NOW(), 0),
  (6005, 8003, 4002, '1.2 CSS 样式', '美化页面', '选择器、盒模型与常用布局方式。', 2, 25, 1, NOW(), NOW(), 0);

-- ============ 课件 ============
INSERT IGNORE INTO materials (id, lesson_id, course_id, title, type, url, content, sort_order, file_size, created_at, updated_at, deleted)
VALUES
  (9001, 6001, 4001, 'JDK 安装指引', 'DOC', NULL, 'Windows / macOS 下 JDK 17 的安装步骤与常见问题。', 1, NULL, NOW(), NOW(), 0),
  (9002, 6002, 4001, '示例代码包', 'CODE', 'https://example.com/demo/hello.zip', NULL, 2, 2048, NOW(), NOW(), 0),
  (9003, 6004, 4002, 'HTML 速查表', 'DOC', NULL, '常用 HTML 标签清单。', 1, NULL, NOW(), NOW(), 0);

-- ============ 学习进度 ============
INSERT IGNORE INTO progress (id, user_id, course_id, lesson_id, percent, is_completed, completed_at, watch_seconds, created_at, updated_at, deleted)
VALUES
  (10001, 1001, 4001, 6001, 100, 1, NOW(), 1500, NOW(), NOW(), 0),
  (10002, 1001, 4001, 6002, 60, 0, NULL, 900, NOW(), NOW(), 0);

-- ============ 题库分类 ============
INSERT IGNORE INTO question_categories (id, name, description, parent_id, created_at, updated_at, deleted)
VALUES
  (11001, 'Java 基础', 'Java 语言基础', NULL, NOW(), NOW(), 0),
  (11002, '前端基础', 'Web 前端基础', NULL, NOW(), NOW(), 0);

-- ============ 题目标签 ============
INSERT IGNORE INTO question_tags (id, name, created_at, updated_at, deleted)
VALUES
  (12001, '语法', NOW(), NOW(), 0),
  (12002, '面向对象', NOW(), NOW(), 0),
  (12003, 'HTML', NOW(), NOW(), 0);

-- ============ 题库 ============
INSERT IGNORE INTO questions (id, content, options, correct_answer, explanation, type, difficulty, category_id, course_id, created_by, score, tags, created_at, updated_at, deleted)
VALUES
  (13001, 'Java 中用于定义类的关键字是？', '["class","struct","def","func"]', 'A', 'Java 使用 class 关键字定义类。', 'SINGLE', 'EASY', 11001, 4001, 1002, 5.00, '语法', NOW(), NOW(), 0),
  (13002, '下列哪个是 Java 的基本数据类型？', '["int","String","Integer","List"]', 'A', 'int 是基本数据类型，其余为引用类型。', 'SINGLE', 'EASY', 11001, 4001, 1002, 5.00, '语法', NOW(), NOW(), 0),
  (13003, '关于面向对象，下列说法正确的是？', '["类是对象的模板","对象不能创建类","继承用 extends","多态提高扩展性"]', 'A,C,D', '类是对象的模板，继承用 extends 关键字，多态可提升扩展性。', 'MULTIPLE', 'MEDIUM', 11001, 4001, 1002, 8.00, '面向对象', NOW(), NOW(), 0),
  (13004, 'HTML 中用于定义段落的标签是？', '["<p>","<div>","<span>","<br>"]', 'A', '<p> 用于定义段落。', 'SINGLE', 'EASY', 11002, 4002, 1002, 5.00, 'HTML', NOW(), NOW(), 0);

INSERT IGNORE INTO question_tag_relations (id, question_id, tag_id, created_at, updated_at, deleted)
VALUES
  (14001, 13001, 12001, NOW(), NOW(), 0),
  (14002, 13002, 12001, NOW(), NOW(), 0),
  (14003, 13003, 12002, NOW(), NOW(), 0),
  (14004, 13004, 12003, NOW(), NOW(), 0);

-- ============ 练习 ============
INSERT IGNORE INTO exercises (id, title, description, course_id, lesson_id, category_id, difficulty, time_limit_minutes, total_questions, created_at, updated_at, deleted)
VALUES
  (15001, 'Java 基础小测', '巩固第一章语法知识', 4001, 6002, 11001, 'EASY', 15, 3, NOW(), NOW(), 0);

INSERT IGNORE INTO exercise_questions (id, exercise_id, question_id, sort_order, created_at, updated_at, deleted)
VALUES
  (16001, 15001, 13001, 1, NOW(), NOW(), 0),
  (16002, 15001, 13002, 2, NOW(), NOW(), 0),
  (16003, 15001, 13003, 3, NOW(), NOW(), 0);

-- ============ 测验 ============
INSERT IGNORE INTO quizzes (id, title, description, course_id, unit_id, lesson_id, duration_minutes, total_score, pass_score, max_attempts, start_time, end_time, is_published, created_at, updated_at, deleted)
VALUES
  (17001, '第一章单元测验', '检验环境与基础掌握情况', 4001, 8001, NULL, 20, 15, 9, 3, NOW(), DATE_ADD(NOW(), INTERVAL 30 DAY), 1, NOW(), NOW(), 0);

INSERT IGNORE INTO quiz_questions (id, quiz_id, question_id, sort_order, score, created_at, updated_at, deleted)
VALUES
  (18001, 17001, 13001, 1, 5.00, NOW(), NOW(), 0),
  (18002, 17001, 13002, 2, 5.00, NOW(), NOW(), 0),
  (18003, 17001, 13003, 3, 5.00, NOW(), NOW(), 0);

-- ============ 考试 ============
INSERT IGNORE INTO exams (id, title, description, course_id, duration_minutes, total_score, pass_score, start_time, end_time, is_published, allow_review, created_at, updated_at, deleted)
VALUES
  (19001, 'Java 期中模拟考试', '综合考查前两章内容', 4001, 60, 18, 10, NOW(), DATE_ADD(NOW(), INTERVAL 60 DAY), 1, 1, NOW(), NOW(), 0);

INSERT IGNORE INTO exam_questions (id, exam_id, question_id, sort_order, score, created_at, updated_at, deleted)
VALUES
  (20001, 19001, 13001, 1, 6.00, NOW(), NOW(), 0),
  (20002, 19001, 13002, 2, 6.00, NOW(), NOW(), 0),
  (20003, 19001, 13003, 3, 6.00, NOW(), NOW(), 0);

-- ============ 作业 ============
INSERT IGNORE INTO homework (id, title, description, course_id, lesson_id, total_score, deadline, allow_late_submission, is_published, created_at, updated_at, deleted)
VALUES
  (21001, '动手写第一个 Java 程序', '编写一个打印个人信息的 Java 程序并提交源码。', 4001, 6002, 10, DATE_ADD(NOW(), INTERVAL 14 DAY), 1, 1, NOW(), NOW(), 0);

INSERT IGNORE INTO homework_questions (id, homework_id, question_id, sort_order, score, created_at, updated_at, deleted)
VALUES
  (22001, 21001, 13002, 1, 10.00, NOW(), NOW(), 0);

-- ============ 笔记 ============
INSERT IGNORE INTO notes (id, user_id, course_id, lesson_id, title, content, is_public, created_at, updated_at, deleted)
VALUES
  (23001, 1001, 4001, 6002, 'main 方法记忆点', 'public static void main(String[] args) 是程序入口。', 1, NOW(), NOW(), 0);

-- ============ 讨论 ============
INSERT IGNORE INTO discussions (id, user_id, course_id, title, content, is_pinned, view_count, reply_count, created_at, updated_at, deleted)
VALUES
  (24001, 1001, 4001, 'JDK 和 JRE 有什么区别？', '刚接触 Java，不太理解两者的关系，求解答。', 0, 12, 1, NOW(), NOW(), 0);
  (24002, 1003, 4002, '前端学习路线推荐', '想系统学习前端，求一份学习路线。', 0, 8, 0, NOW(), NOW(), 0);

INSERT IGNORE INTO comments (id, user_id, discussion_id, parent_id, content, like_count, created_at, updated_at, deleted)
VALUES
  (25001, 1002, 24001, NULL, 'JDK 是开发工具包，JRE 是运行环境，JDK 包含 JRE。', 3, NOW(), NOW(), 0);

-- ============ 公告 ============
INSERT IGNORE INTO announcements (id, course_id, author_id, title, content, is_pinned, published_at, created_at, updated_at, deleted)
VALUES
  (26001, 4001, 1002, '欢迎来到 Java 编程入门', '本课程共三章，建议每周学习两个课时并完成作业。', 1, NOW(), NOW(), NOW(), 0);

-- ============ 通知 ============
INSERT IGNORE INTO notifications (id, user_id, type, title, content, link, is_read, created_at, updated_at, deleted)
VALUES
  (27001, 1001, 'ANNOUNCEMENT', '你有新公告', '课程《Java 编程入门到实战》发布了新公告。', '/course/4001', 0, NOW(), NOW(), 0),
  (27002, 1001, 'HOMEWORK', '作业待提交', '《动手写第一个 Java 程序》将于两周后截止。', '/learn/homework', 0, NOW(), NOW(), 0);

-- ============ 私信 ============
INSERT IGNORE INTO direct_messages (id, sender_id, receiver_id, content, is_read, read_at, created_at, updated_at, deleted)
VALUES
  (28001, 1002, 1001, '同学你好，作业有问题随时在讨论区提问～', 0, NULL, NOW(), NOW(), 0);

-- ============ AI 助教会话 ============
INSERT IGNORE INTO ai_tutor_sessions (id, user_id, course_id, title, model, created_at, updated_at, deleted)
VALUES
  (29001, 1001, 4001, 'Java 入门问答', 'mock', NOW(), NOW(), 0);

INSERT IGNORE INTO ai_tutor_messages (id, session_id, role, content, prompt_tokens, created_at, updated_at, deleted)
VALUES
  (30001, 29001, 'USER', '什么是面向对象？', NULL, NOW(), NOW(), 0),
  (30002, 29001, 'ASSISTANT', '面向对象（OOP）是一种编程范式，核心概念包括封装、继承和多态。', NULL, NOW(), NOW(), 0);

-- ============ 学习计划 ============
INSERT IGNORE INTO study_plans (id, user_id, course_id, title, start_date, end_date, daily_minutes, is_active, created_at, updated_at, deleted)
VALUES
  (31001, 1001, 4001, '两周攻克 Java 基础', CURDATE(), DATE_ADD(CURDATE(), INTERVAL 14 DAY), 40, 1, NOW(), NOW(), 0);

INSERT IGNORE INTO study_tasks (id, plan_id, title, scheduled_date, is_completed, duration_minutes, created_at, updated_at, deleted)
VALUES
  (32001, 31001, '完成 1.1 安装 JDK', DATE_ADD(CURDATE(), INTERVAL 1 DAY), 1, 40, NOW(), NOW(), 0),
  (32002, 31001, '完成 1.2 第一个程序', DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 40, NOW(), NOW(), 0);

-- ============ 群聊 ============
INSERT IGNORE INTO group_chats (id, course_id, name, description, created_by, max_members, created_at, updated_at, deleted)
VALUES
  (33001, 4001, 'Java 入门交流群', '本课程学员交流答疑', 1002, 200, NOW(), NOW(), 0);

INSERT IGNORE INTO group_chat_members (id, group_id, user_id, role, created_at, updated_at, deleted)
VALUES
  (34001, 33001, 1002, 'OWNER', NOW(), NOW(), 0),
  (34002, 33001, 1001, 'MEMBER', NOW(), NOW(), 0);

INSERT IGNORE INTO group_chat_messages (id, group_id, sender_id, content, message_type, created_at, updated_at, deleted)
VALUES
  (35001, 33001, 1002, '欢迎大家加入本课程交流群！', 'TEXT', NOW(), NOW(), 0);

-- ============ 徽章 ============
INSERT IGNORE INTO badges (id, name, description, icon, condition_type, condition_value, created_at, updated_at, deleted)
VALUES
  (36001, '初次选课', '完成第一次选课', '🎓', 'ENROLL', 1, NOW(), NOW(), 0),
  (36002, '练习达人', '完成 10 次练习', '✍️', 'EXERCISE', 10, NOW(), NOW(), 0),
  (36003, '全勤学员', '连续学习 7 天', '🔥', 'STREAK', 7, NOW(), NOW(), 0);

INSERT IGNORE INTO user_badges (id, user_id, badge_id, created_at, updated_at, deleted)
VALUES
  (37001, 1001, 36001, NOW(), NOW(), 0);

-- ============ 排行榜 ============
INSERT IGNORE INTO leaderboards (id, user_id, period, total_score, rank, study_minutes, completed_lessons, created_at, updated_at, deleted)
VALUES
  (38001, 1001, 'WEEKLY', 120, 1, 320, 5, NOW(), NOW(), 0),
  (38002, 1003, 'WEEKLY', 90, 2, 260, 3, NOW(), NOW(), 0);

-- ============ 学习感言 ============
INSERT IGNORE INTO testimonials (id, user_id, content, rating, is_approved, created_at, updated_at, deleted)
VALUES
  (39001, 1001, '在 AI Learn 上学习非常高效，推荐给大家！', 5, 1, NOW(), NOW(), 0);

-- ============ 错题本 ============
INSERT IGNORE INTO error_books (id, user_id, title, description, created_at, updated_at, deleted)
VALUES
  (40001, 1001, '我的错题本', '收集做错的题目', NOW(), NOW(), 0);

INSERT IGNORE INTO error_book_items (id, error_book_id, question_id, user_answer, correct_answer, note, mistake_count, is_resolved, created_at, updated_at, deleted)
VALUES
  (41001, 40001, 13003, 'A,C', 'A,C,D', '漏选了多态相关选项', 1, 0, NOW(), NOW(), 0);

-- ============ 学习分析 ============
INSERT IGNORE INTO learning_analytics (id, user_id, stat_date, study_minutes, completed_lessons, exercise_count, correct_count, average_score, streak_days, created_at, updated_at, deleted)
VALUES
  (42001, 1001, CURDATE(), 80, 1, 3, 2, 6.67, 2, NOW(), NOW(), 0);

-- ============ 推荐 ============
INSERT IGNORE INTO recommendations (id, user_id, course_id, reason, algorithm, is_clicked, created_at, updated_at, deleted)
VALUES
  (43001, 1001, 4003, '基于你对编程开发的兴趣', 'INTEREST', 0, NOW(), NOW(), 0);

SET FOREIGN_KEY_CHECKS = 1;
