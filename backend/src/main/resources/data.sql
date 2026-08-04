-- AI Learn platform sample data (MySQL 8.0+)
-- Insert with INSERT IGNORE so it is safe to re-run.
-- Demo accounts password is 123456 (BCrypt hash below).
--   student@ailearn.com (student)
--   teacher@ailearn.com  (teacher)
--   admin@ailearn.com    (admin)

USE ailearn;
SET FOREIGN_KEY_CHECKS = 0;

-- ============ users ============
INSERT IGNORE INTO users (id, email, name, avatar, role, student_id, bio, phone, major, grade, created_at, updated_at, deleted)
VALUES
  (1001, 'student@ailearn.com', 'Ming Xiao', 'https://i.pravatar.cc/150?img=11', 'STUDENT', 'S2024001', 'A student who loves coding', '13800000001', 'Computer Science', 2, NOW(), NOW(), 0),
  (1002, 'teacher@ailearn.com', 'Li Laoshi', 'https://i.pravatar.cc/150?img=12', 'TEACHER', NULL, '10 years teaching experience', '13800000002', 'Education Tech', NULL, NOW(), NOW(), 0),
  (1003, 'admin@ailearn.com', 'Admin', 'https://i.pravatar.cc/150?img=13', 'ADMIN', NULL, 'Platform admin', '13800000003', NULL, NULL, NOW(), NOW(), 0);

-- ============ accounts (password 123456) ============
-- BCrypt hash of '123456' -> $2a$10$40jrM686IuIZnCb3uxuC3.zGG9PDoBberywvCXHee4KbQhAaZ5Uja
INSERT IGNORE INTO accounts (id, user_id, provider, provider_account_id, password_hash, created_at, updated_at, deleted)
VALUES
  (2001, 1001, 'credentials', 'student@ailearn.com', '$2a$10$40jrM686IuIZnCb3uxuC3.zGG9PDoBberywvCXHee4KbQhAaZ5Uja', NOW(), NOW(), 0),
  (2002, 1002, 'credentials', 'teacher@ailearn.com', '$2a$10$40jrM686IuIZnCb3uxuC3.zGG9PDoBberywvCXHee4KbQhAaZ5Uja', NOW(), NOW(), 0),
  (2003, 1003, 'credentials', 'admin@ailearn.com', '$2a$10$40jrM686IuIZnCb3uxuC3.zGG9PDoBberywvCXHee4KbQhAaZ5Uja', NOW(), NOW(), 0);

-- ============ user_settings ============
INSERT IGNORE INTO user_settings (id, user_id, theme, language, email_notification, push_notification, study_reminder, reminder_time, created_at, updated_at, deleted)
VALUES
  (3001, 1001, 'light', 'zh-CN', 1, 1, 1, '20:00', NOW(), NOW(), 0),
  (3002, 1002, 'light', 'zh-CN', 1, 0, 0, '09:00', NOW(), NOW(), 0);

-- ============ courses ============
INSERT IGNORE INTO courses (id, title, description, cover_image, level, category, tags, teacher_id, price, duration_minutes, is_published, published_at, created_at, updated_at, deleted)
VALUES
  (4001, 'Java Programming from Zero', 'Learn Java from scratch: OOP, collections, IO and threads, then build a project.', 'https://picsum.photos/seed/java/400/240', 'BEGINNER', 'Programming', 'Java,OOP,Basics', 1002, 0.00, 480, 1, NOW(), NOW(), NOW(), 0),
  (4002, 'Web Frontend in Practice', 'HTML/CSS/JS basics plus building SPA with Vue 3.', 'https://picsum.photos/seed/web/400/240', 'BEGINNER', 'Programming', 'HTML,CSS,JS,Vue', 1002, 0.00, 360, 1, NOW(), NOW(), NOW(), 0),
  (4003, 'Data Structures and Algorithms', 'Core data structures and algorithm thinking with exercises.', 'https://picsum.photos/seed/dsa/400/240', 'INTERMEDIATE', 'CS Fundamentals', 'Algorithm,DS,Interview', 1002, 99.00, 600, 1, NOW(), NOW(), NOW(), 0);

-- ============ enrollments ============
INSERT IGNORE INTO enrollments (id, user_id, course_id, status, progress_percent, enrolled_at, last_accessed_lesson_id, created_at, updated_at, deleted)
VALUES
  (5001, 1001, 4001, 'ACTIVE', 35, NOW(), 6002, NOW(), NOW(), 0),
  (5002, 1001, 4002, 'ACTIVE', 10, NOW(), 6004, NOW(), NOW(), 0);

-- ============ favorites ============
INSERT IGNORE INTO favorites (id, user_id, course_id, created_at, updated_at, deleted)
VALUES
  (6001, 1001, 4003, NOW(), NOW(), 0);

-- ============ course_reviews ============
INSERT IGNORE INTO course_reviews (id, course_id, user_id, rating, comment, created_at, updated_at, deleted)
VALUES
  (7001, 4001, 1001, 5, 'Very clear, learned a lot by following along!', NOW(), NOW(), 0),
  (7002, 4001, 1003, 4, 'Solid content, good for beginners.', NOW(), NOW(), 0);

-- ============ units ============
INSERT IGNORE INTO units (id, course_id, title, description, sort_order, created_at, updated_at, deleted)
VALUES
  (8001, 4001, 'Ch1 Environment and Basics', 'Setup JDK and understand basic syntax', 1, NOW(), NOW(), 0),
  (8002, 4001, 'Ch2 Object Oriented', 'Classes, objects, inheritance and polymorphism', 2, NOW(), NOW(), 0),
  (8003, 4002, 'Ch1 Three Pillars of Web', 'HTML / CSS / JavaScript intro', 1, NOW(), NOW(), 0);

-- ============ lessons ============
INSERT IGNORE INTO lessons (id, unit_id, course_id, title, description, content, sort_order, duration_minutes, is_published, created_at, updated_at, deleted)
VALUES
  (6001, 8001, 4001, '1.1 Install JDK and IDE', 'Setup dev environment', 'Install JDK 17, configure PATH and initialize IntelliJ IDEA.', 1, 25, 1, NOW(), NOW(), 0),
  (6002, 8001, 4001, '1.2 First Java Program', 'Hello World and structure', 'Learn main method, class structure and System.out.println.', 2, 30, 1, NOW(), NOW(), 0),
  (6003, 8002, 4001, '2.1 Classes and Objects', 'OOP basics', 'Understand class definition, object creation and this keyword.', 1, 40, 1, NOW(), NOW(), 0),
  (6004, 8003, 4002, '1.1 HTML Basics', 'Page structure', 'HTML tags, headings, paragraphs and lists.', 1, 20, 1, NOW(), NOW(), 0),
  (6005, 8003, 4002, '1.2 CSS Styling', 'Beautify pages', 'Selectors, box model and common layouts.', 2, 25, 1, NOW(), NOW(), 0);

-- ============ materials ============
INSERT IGNORE INTO materials (id, lesson_id, course_id, title, type, url, content, sort_order, file_size, created_at, updated_at, deleted)
VALUES
  (9001, 6001, 4001, 'JDK Install Guide', 'DOC', NULL, 'Steps to install JDK 17 on Windows/macOS and common issues.', 1, NULL, NOW(), NOW(), 0),
  (9002, 6002, 4001, 'Demo Code Pack', 'CODE', 'https://example.com/demo/hello.zip', NULL, 2, 2048, NOW(), NOW(), 0),
  (9003, 6004, 4002, 'HTML Cheat Sheet', 'DOC', NULL, 'List of common HTML tags.', 1, NULL, NOW(), NOW(), 0);

-- ============ progress ============
INSERT IGNORE INTO progress (id, user_id, course_id, lesson_id, percent, is_completed, completed_at, watch_seconds, created_at, updated_at, deleted)
VALUES
  (10001, 1001, 4001, 6001, 100, 1, NOW(), 1500, NOW(), NOW(), 0),
  (10002, 1001, 4001, 6002, 60, 0, NULL, 900, NOW(), NOW(), 0);

-- ============ question_categories ============
INSERT IGNORE INTO question_categories (id, name, description, parent_id, created_at, updated_at, deleted)
VALUES
  (11001, 'Java Basics', 'Java language basics', NULL, NOW(), NOW(), 0),
  (11002, 'Frontend Basics', 'Web frontend basics', NULL, NOW(), NOW(), 0);

-- ============ question_tags ============
INSERT IGNORE INTO question_tags (id, name, created_at, updated_at, deleted)
VALUES
  (12001, 'syntax', NOW(), NOW(), 0),
  (12002, 'oop', NOW(), NOW(), 0),
  (12003, 'html', NOW(), NOW(), 0);

-- ============ questions ============
INSERT IGNORE INTO questions (id, content, options, correct_answer, explanation, type, difficulty, category_id, course_id, created_by, score, tags, created_at, updated_at, deleted)
VALUES
  (13001, 'Which keyword defines a class in Java?', '["class","struct","def","func"]', 'A', 'Java uses the class keyword to define a class.', 'SINGLE', 'EASY', 11001, 4001, 1002, 5.00, 'syntax', NOW(), NOW(), 0),
  (13002, 'Which is a Java primitive type?', '["int","String","Integer","List"]', 'A', 'int is a primitive type; the others are reference types.', 'SINGLE', 'EASY', 11001, 4001, 1002, 5.00, 'syntax', NOW(), NOW(), 0),
  (13003, 'About OOP, which are correct?', '["A class is a template of objects","Objects cannot create classes","Inheritance uses extends","Polymorphism improves extensibility"]', 'A,C,D', 'A class is a template, inheritance uses extends, polymorphism improves extensibility.', 'MULTIPLE', 'MEDIUM', 11001, 4001, 1002, 8.00, 'oop', NOW(), NOW(), 0),
  (13004, 'Which tag defines a paragraph in HTML?', '["<p>","<div>","<span>","<br>"]', 'A', '<p> defines a paragraph.', 'SINGLE', 'EASY', 11002, 4002, 1002, 5.00, 'html', NOW(), NOW(), 0);

INSERT IGNORE INTO question_tag_relations (id, question_id, tag_id, created_at, updated_at, deleted)
VALUES
  (14001, 13001, 12001, NOW(), NOW(), 0),
  (14002, 13002, 12001, NOW(), NOW(), 0),
  (14003, 13003, 12002, NOW(), NOW(), 0),
  (14004, 13004, 12003, NOW(), NOW(), 0);

-- ============ exercises ============
INSERT IGNORE INTO exercises (id, title, description, course_id, lesson_id, category_id, difficulty, time_limit_minutes, total_questions, created_at, updated_at, deleted)
VALUES
  (15001, 'Java Basics Quiz', 'Consolidate chapter 1 syntax', 4001, 6002, 11001, 'EASY', 15, 3, NOW(), NOW(), 0);

INSERT IGNORE INTO exercise_questions (id, exercise_id, question_id, sort_order, created_at, updated_at, deleted)
VALUES
  (16001, 15001, 13001, 1, NOW(), NOW(), 0),
  (16002, 15001, 13002, 2, NOW(), NOW(), 0),
  (16003, 15001, 13003, 3, NOW(), NOW(), 0);

-- ============ quizzes ============
INSERT IGNORE INTO quizzes (id, title, description, course_id, unit_id, lesson_id, duration_minutes, total_score, pass_score, max_attempts, start_time, end_time, is_published, created_at, updated_at, deleted)
VALUES
  (17001, 'Chapter 1 Quiz', 'Check environment and basics', 4001, 8001, NULL, 20, 15, 9, 3, NOW(), DATE_ADD(NOW(), INTERVAL 30 DAY), 1, NOW(), NOW(), 0);

INSERT IGNORE INTO quiz_questions (id, quiz_id, question_id, sort_order, score, created_at, updated_at, deleted)
VALUES
  (18001, 17001, 13001, 1, 5.00, NOW(), NOW(), 0),
  (18002, 17001, 13002, 2, 5.00, NOW(), NOW(), 0),
  (18003, 17001, 13003, 3, 5.00, NOW(), NOW(), 0);

-- ============ exams ============
INSERT IGNORE INTO exams (id, title, description, course_id, duration_minutes, total_score, pass_score, start_time, end_time, is_published, allow_review, created_at, updated_at, deleted)
VALUES
  (19001, 'Java Midterm Mock', 'Covers chapter 1 and 2', 4001, 60, 18, 10, NOW(), DATE_ADD(NOW(), INTERVAL 60 DAY), 1, 1, NOW(), NOW(), 0);

INSERT IGNORE INTO exam_questions (id, exam_id, question_id, sort_order, score, created_at, updated_at, deleted)
VALUES
  (20001, 19001, 13001, 1, 6.00, NOW(), NOW(), 0),
  (20002, 19001, 13002, 2, 6.00, NOW(), NOW(), 0),
  (20003, 19001, 13003, 3, 6.00, NOW(), NOW(), 0);

-- ============ homework ============
INSERT IGNORE INTO homework (id, title, description, course_id, lesson_id, total_score, deadline, allow_late_submission, is_published, created_at, updated_at, deleted)
VALUES
  (21001, 'Write Your First Java Program', 'Write a Java program that prints your profile and submit the source.', 4001, 6002, 10, DATE_ADD(NOW(), INTERVAL 14 DAY), 1, 1, NOW(), NOW(), 0);

INSERT IGNORE INTO homework_questions (id, homework_id, question_id, sort_order, score, created_at, updated_at, deleted)
VALUES
  (22001, 21001, 13002, 1, 10.00, NOW(), NOW(), 0);

-- ============ notes ============
INSERT IGNORE INTO notes (id, user_id, course_id, lesson_id, title, content, is_public, created_at, updated_at, deleted)
VALUES
  (23001, 1001, 4001, 6002, 'main method reminder', 'public static void main(String[] args) is the entry point.', 1, NOW(), NOW(), 0);

-- ============ discussions ============
INSERT IGNORE INTO discussions (id, user_id, course_id, title, content, is_pinned, view_count, reply_count, created_at, updated_at, deleted)
VALUES
  (24001, 1001, 4001, 'JDK vs JRE?', 'New to Java, not sure about the difference. Please explain.', 0, 12, 1, NOW(), NOW(), 0),
  (24002, 1003, 4002, 'Frontend learning path', 'Want to learn frontend systematically, any roadmap?', 0, 8, 0, NOW(), NOW(), 0);

INSERT IGNORE INTO comments (id, user_id, discussion_id, parent_id, content, like_count, created_at, updated_at, deleted)
VALUES
  (25001, 1002, 24001, NULL, 'JDK is the dev kit, JRE is the runtime; JDK includes JRE.', 3, NOW(), NOW(), 0);

-- ============ announcements ============
INSERT IGNORE INTO announcements (id, course_id, author_id, title, content, is_pinned, published_at, created_at, updated_at, deleted)
VALUES
  (26001, 4001, 1002, 'Welcome to Java', '3 chapters total, finish 2 lessons per week and submit homework.', 1, NOW(), NOW(), NOW(), 0);

-- ============ notifications ============
INSERT IGNORE INTO notifications (id, user_id, type, title, content, link, is_read, created_at, updated_at, deleted)
VALUES
  (27001, 1001, 'ANNOUNCEMENT', 'New announcement', 'Course Java has a new announcement.', '/course/4001', 0, NOW(), NOW(), 0),
  (27002, 1001, 'HOMEWORK', 'Homework due', 'Write Your First Java Program due in 2 weeks.', '/learn/homework', 0, NOW(), NOW(), 0);

-- ============ direct_messages ============
INSERT IGNORE INTO direct_messages (id, sender_id, receiver_id, content, is_read, read_at, created_at, updated_at, deleted)
VALUES
  (28001, 1002, 1001, 'Hi, ask in the forum if you have homework questions.', 0, NULL, NOW(), NOW(), 0);

-- ============ ai_tutor_sessions ============
INSERT IGNORE INTO ai_tutor_sessions (id, user_id, course_id, title, model, created_at, updated_at, deleted)
VALUES
  (29001, 1001, 4001, 'Java Q&A', 'mock', NOW(), NOW(), 0);

INSERT IGNORE INTO ai_tutor_messages (id, session_id, role, content, prompt_tokens, created_at, updated_at, deleted)
VALUES
  (30001, 29001, 'USER', 'What is object oriented?', NULL, NOW(), NOW(), 0),
  (30002, 29001, 'ASSISTANT', 'OOP is a paradigm with encapsulation, inheritance and polymorphism.', NULL, NOW(), NOW(), 0);

-- ============ study_plans ============
INSERT IGNORE INTO study_plans (id, user_id, course_id, title, start_date, end_date, daily_minutes, is_active, created_at, updated_at, deleted)
VALUES
  (31001, 1001, 4001, 'Two weeks to Java basics', CURDATE(), DATE_ADD(CURDATE(), INTERVAL 14 DAY), 40, 1, NOW(), NOW(), 0);

INSERT IGNORE INTO study_tasks (id, plan_id, title, scheduled_date, is_completed, duration_minutes, created_at, updated_at, deleted)
VALUES
  (32001, 31001, 'Install JDK (1.1)', DATE_ADD(CURDATE(), INTERVAL 1 DAY), 1, 40, NOW(), NOW(), 0),
  (32002, 31001, 'First program (1.2)', DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 40, NOW(), NOW(), 0);

-- ============ group_chats ============
INSERT IGNORE INTO group_chats (id, course_id, name, description, created_by, max_members, created_at, updated_at, deleted)
VALUES
  (33001, 4001, 'Java Beginners Group', 'Q&A for this course', 1002, 200, NOW(), NOW(), 0);

INSERT IGNORE INTO group_chat_members (id, group_id, user_id, role, created_at, updated_at, deleted)
VALUES
  (34001, 33001, 1002, 'OWNER', NOW(), NOW(), 0),
  (34002, 33001, 1001, 'MEMBER', NOW(), NOW(), 0);

INSERT IGNORE INTO group_chat_messages (id, group_id, sender_id, content, message_type, created_at, updated_at, deleted)
VALUES
  (35001, 33001, 1002, 'Welcome to the group!', 'TEXT', NOW(), NOW(), 0);

-- ============ badges ============
INSERT IGNORE INTO badges (id, name, description, icon, condition_type, condition_value, created_at, updated_at, deleted)
VALUES
  (36001, 'First Enroll', 'Complete first enrollment', 'medal', 'ENROLL', 1, NOW(), NOW(), 0),
  (36002, 'Exercise Pro', 'Finish 10 exercises', 'pencil', 'EXERCISE', 10, NOW(), NOW(), 0),
  (36003, 'Full Attendance', 'Study 7 days in a row', 'fire', 'STREAK', 7, NOW(), NOW(), 0);

INSERT IGNORE INTO user_badges (id, user_id, badge_id, created_at, updated_at, deleted)
VALUES
  (37001, 1001, 36001, NOW(), NOW(), 0);

-- ============ leaderboards ============
INSERT IGNORE INTO leaderboards (id, user_id, period, total_score, ranking, study_minutes, completed_lessons, created_at, updated_at, deleted)
VALUES
  (38001, 1001, 'WEEKLY', 120, 1, 320, 5, NOW(), NOW(), 0),
  (38002, 1003, 'WEEKLY', 90, 2, 260, 3, NOW(), NOW(), 0);

-- ============ testimonials ============
INSERT IGNORE INTO testimonials (id, user_id, content, rating, is_approved, created_at, updated_at, deleted)
VALUES
  (39001, 1001, 'Learning on AI Learn is very efficient, recommended!', 5, 1, NOW(), NOW(), 0);

-- ============ error_books ============
INSERT IGNORE INTO error_books (id, user_id, title, description, created_at, updated_at, deleted)
VALUES
  (40001, 1001, 'My Error Book', 'Collect wrong answers', NOW(), NOW(), 0);

INSERT IGNORE INTO error_book_items (id, error_book_id, question_id, user_answer, correct_answer, note, mistake_count, is_resolved, created_at, updated_at, deleted)
VALUES
  (41001, 40001, 13003, 'A,C', 'A,C,D', 'Missed the polymorphism option', 1, 0, NOW(), NOW(), 0);

-- ============ learning_analytics ============
INSERT IGNORE INTO learning_analytics (id, user_id, stat_date, study_minutes, completed_lessons, exercise_count, correct_count, average_score, streak_days, created_at, updated_at, deleted)
VALUES
  (42001, 1001, CURDATE(), 80, 1, 3, 2, 6.67, 2, NOW(), NOW(), 0);

-- ============ recommendations ============
INSERT IGNORE INTO recommendations (id, user_id, course_id, reason, algorithm, is_clicked, created_at, updated_at, deleted)
VALUES
  (43001, 1001, 4003, 'Based on your interest in programming', 'INTEREST', 0, NOW(), NOW(), 0);

SET FOREIGN_KEY_CHECKS = 1;
