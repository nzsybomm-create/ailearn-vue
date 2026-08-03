-- AI Learn 平台数据库脚本 (MySQL 8.0+)
-- 执行顺序：先创建数据库，再 source 本文件

CREATE DATABASE IF NOT EXISTS ailearn
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;
USE ailearn;

SET FOREIGN_KEY_CHECKS = 0;

-- 统一基础字段：id BIGINT, created_at, updated_at, deleted
DROP TABLE IF EXISTS users;
CREATE TABLE users (
  id BIGINT PRIMARY KEY,
  email VARCHAR(191) UNIQUE,
  name VARCHAR(191),
  avatar VARCHAR(500),
  role VARCHAR(20) DEFAULT 'STUDENT',
  student_id VARCHAR(100),
  bio TEXT,
  phone VARCHAR(50),
  major VARCHAR(100),
  grade INT,
  created_at DATETIME,
  updated_at DATETIME,
  deleted INT DEFAULT 0
);

DROP TABLE IF EXISTS accounts;
CREATE TABLE accounts (
  id BIGINT PRIMARY KEY,
  user_id BIGINT,
  provider VARCHAR(50),
  provider_account_id VARCHAR(191),
  password_hash VARCHAR(255),
  refresh_token TEXT,
  access_token TEXT,
  expires_at BIGINT,
  created_at DATETIME,
  updated_at DATETIME,
  deleted INT DEFAULT 0
);

DROP TABLE IF EXISTS password_resets;
CREATE TABLE password_resets (
  id BIGINT PRIMARY KEY,
  email VARCHAR(191),
  token VARCHAR(255),
  expires_at DATETIME,
  created_at DATETIME,
  updated_at DATETIME,
  deleted INT DEFAULT 0
);

DROP TABLE IF EXISTS courses;
CREATE TABLE courses (
  id BIGINT PRIMARY KEY,
  title VARCHAR(255),
  description TEXT,
  cover_image VARCHAR(500),
  level VARCHAR(20),
  category VARCHAR(100),
  tags VARCHAR(500),
  teacher_id BIGINT,
  price DECIMAL(10,2),
  duration_minutes INT,
  is_published TINYINT(1),
  published_at DATETIME,
  created_at DATETIME,
  updated_at DATETIME,
  deleted INT DEFAULT 0
);

DROP TABLE IF EXISTS enrollments;
CREATE TABLE enrollments (
  id BIGINT PRIMARY KEY,
  user_id BIGINT,
  course_id BIGINT,
  status VARCHAR(20),
  progress_percent INT DEFAULT 0,
  enrolled_at DATETIME,
  completed_at DATETIME,
  last_accessed_lesson_id INT,
  created_at DATETIME,
  updated_at DATETIME,
  deleted INT DEFAULT 0
);

DROP TABLE IF EXISTS course_reviews;
CREATE TABLE course_reviews (
  id BIGINT PRIMARY KEY,
  course_id BIGINT,
  user_id BIGINT,
  rating INT,
  comment TEXT,
  created_at DATETIME,
  updated_at DATETIME,
  deleted INT DEFAULT 0
);

DROP TABLE IF EXISTS favorites;
CREATE TABLE favorites (
  id BIGINT PRIMARY KEY,
  user_id BIGINT,
  course_id BIGINT,
  created_at DATETIME,
  updated_at DATETIME,
  deleted INT DEFAULT 0
);

DROP TABLE IF EXISTS units;
CREATE TABLE units (
  id BIGINT PRIMARY KEY,
  course_id BIGINT,
  title VARCHAR(255),
  description TEXT,
  sort_order INT,
  created_at DATETIME,
  updated_at DATETIME,
  deleted INT DEFAULT 0
);

DROP TABLE IF EXISTS lessons;
CREATE TABLE lessons (
  id BIGINT PRIMARY KEY,
  unit_id BIGINT,
  course_id BIGINT,
  title VARCHAR(255),
  description TEXT,
  content TEXT,
  sort_order INT,
  duration_minutes INT,
  is_published TINYINT(1),
  created_at DATETIME,
  updated_at DATETIME,
  deleted INT DEFAULT 0
);

DROP TABLE IF EXISTS materials;
CREATE TABLE materials (
  id BIGINT PRIMARY KEY,
  lesson_id BIGINT,
  course_id BIGINT,
  title VARCHAR(255),
  type VARCHAR(20),
  url VARCHAR(500),
  content TEXT,
  sort_order INT,
  file_size BIGINT,
  created_at DATETIME,
  updated_at DATETIME,
  deleted INT DEFAULT 0
);

DROP TABLE IF EXISTS progress;
CREATE TABLE progress (
  id BIGINT PRIMARY KEY,
  user_id BIGINT,
  course_id BIGINT,
  lesson_id BIGINT,
  percent INT,
  is_completed TINYINT(1),
  completed_at DATETIME,
  watch_seconds INT,
  created_at DATETIME,
  updated_at DATETIME,
  deleted INT DEFAULT 0
);

DROP TABLE IF EXISTS question_categories;
CREATE TABLE question_categories (
  id BIGINT PRIMARY KEY,
  name VARCHAR(191),
  description TEXT,
  parent_id BIGINT,
  created_at DATETIME,
  updated_at DATETIME,
  deleted INT DEFAULT 0
);

DROP TABLE IF EXISTS question_tags;
CREATE TABLE question_tags (
  id BIGINT PRIMARY KEY,
  name VARCHAR(191),
  created_at DATETIME,
  updated_at DATETIME,
  deleted INT DEFAULT 0
);

DROP TABLE IF EXISTS questions;
CREATE TABLE questions (
  id BIGINT PRIMARY KEY,
  content TEXT,
  options TEXT,
  correct_answer TEXT,
  explanation TEXT,
  type VARCHAR(30),
  difficulty VARCHAR(20),
  category_id BIGINT,
  course_id BIGINT,
  created_by BIGINT,
  score DECIMAL(10,2),
  tags VARCHAR(500),
  created_at DATETIME,
  updated_at DATETIME,
  deleted INT DEFAULT 0
);

DROP TABLE IF EXISTS question_tag_relations;
CREATE TABLE question_tag_relations (
  id BIGINT PRIMARY KEY,
  question_id BIGINT,
  tag_id BIGINT,
  created_at DATETIME,
  updated_at DATETIME,
  deleted INT DEFAULT 0
);

DROP TABLE IF EXISTS exercises;
CREATE TABLE exercises (
  id BIGINT PRIMARY KEY,
  title VARCHAR(255),
  description TEXT,
  course_id BIGINT,
  lesson_id BIGINT,
  category_id BIGINT,
  difficulty VARCHAR(20),
  time_limit_minutes INT,
  total_questions INT,
  created_at DATETIME,
  updated_at DATETIME,
  deleted INT DEFAULT 0
);

DROP TABLE IF EXISTS exercise_questions;
CREATE TABLE exercise_questions (
  id BIGINT PRIMARY KEY,
  exercise_id BIGINT,
  question_id BIGINT,
  sort_order INT,
  created_at DATETIME,
  updated_at DATETIME,
  deleted INT DEFAULT 0
);

DROP TABLE IF EXISTS exercise_attempts;
CREATE TABLE exercise_attempts (
  id BIGINT PRIMARY KEY,
  user_id BIGINT,
  exercise_id BIGINT,
  status VARCHAR(20),
  score DECIMAL(10,2),
  correct_count INT,
  total_count INT,
  started_at DATETIME,
  submitted_at DATETIME,
  created_at DATETIME,
  updated_at DATETIME,
  deleted INT DEFAULT 0
);

DROP TABLE IF EXISTS exercise_answers;
CREATE TABLE exercise_answers (
  id BIGINT PRIMARY KEY,
  attempt_id BIGINT,
  question_id BIGINT,
  answer TEXT,
  correct_answer TEXT,
  is_correct TINYINT(1),
  score DECIMAL(10,2),
  explanation TEXT,
  created_at DATETIME,
  updated_at DATETIME,
  deleted INT DEFAULT 0
);

DROP TABLE IF EXISTS quizzes;
CREATE TABLE quizzes (
  id BIGINT PRIMARY KEY,
  title VARCHAR(255),
  description TEXT,
  course_id BIGINT,
  unit_id BIGINT,
  lesson_id BIGINT,
  duration_minutes INT,
  total_score INT,
  pass_score INT,
  max_attempts INT,
  start_time DATETIME,
  end_time DATETIME,
  is_published TINYINT(1),
  created_at DATETIME,
  updated_at DATETIME,
  deleted INT DEFAULT 0
);

DROP TABLE IF EXISTS quiz_questions;
CREATE TABLE quiz_questions (
  id BIGINT PRIMARY KEY,
  quiz_id BIGINT,
  question_id BIGINT,
  sort_order INT,
  score DECIMAL(10,2),
  created_at DATETIME,
  updated_at DATETIME,
  deleted INT DEFAULT 0
);

DROP TABLE IF EXISTS quiz_attempts;
CREATE TABLE quiz_attempts (
  id BIGINT PRIMARY KEY,
  user_id BIGINT,
  quiz_id BIGINT,
  status VARCHAR(20),
  score DECIMAL(10,2),
  correct_count INT,
  wrong_count INT,
  started_at DATETIME,
  submitted_at DATETIME,
  created_at DATETIME,
  updated_at DATETIME,
  deleted INT DEFAULT 0
);

DROP TABLE IF EXISTS exams;
CREATE TABLE exams (
  id BIGINT PRIMARY KEY,
  title VARCHAR(255),
  description TEXT,
  course_id BIGINT,
  duration_minutes INT,
  total_score INT,
  pass_score INT,
  start_time DATETIME,
  end_time DATETIME,
  is_published TINYINT(1),
  allow_review TINYINT(1),
  created_at DATETIME,
  updated_at DATETIME,
  deleted INT DEFAULT 0
);

DROP TABLE IF EXISTS exam_questions;
CREATE TABLE exam_questions (
  id BIGINT PRIMARY KEY,
  exam_id BIGINT,
  question_id BIGINT,
  sort_order INT,
  score DECIMAL(10,2),
  created_at DATETIME,
  updated_at DATETIME,
  deleted INT DEFAULT 0
);

DROP TABLE IF EXISTS exam_attempts;
CREATE TABLE exam_attempts (
  id BIGINT PRIMARY KEY,
  user_id BIGINT,
  exam_id BIGINT,
  status VARCHAR(20),
  score DECIMAL(10,2),
  correct_count INT,
  wrong_count INT,
  started_at DATETIME,
  submitted_at DATETIME,
  cheating_log TEXT,
  created_at DATETIME,
  updated_at DATETIME,
  deleted INT DEFAULT 0
);

DROP TABLE IF EXISTS homework;
CREATE TABLE homework (
  id BIGINT PRIMARY KEY,
  title VARCHAR(255),
  description TEXT,
  course_id BIGINT,
  lesson_id BIGINT,
  total_score INT,
  deadline DATETIME,
  allow_late_submission TINYINT(1),
  is_published TINYINT(1),
  created_at DATETIME,
  updated_at DATETIME,
  deleted INT DEFAULT 0
);

DROP TABLE IF EXISTS homework_questions;
CREATE TABLE homework_questions (
  id BIGINT PRIMARY KEY,
  homework_id BIGINT,
  question_id BIGINT,
  sort_order INT,
  score DECIMAL(10,2),
  created_at DATETIME,
  updated_at DATETIME,
  deleted INT DEFAULT 0
);

DROP TABLE IF EXISTS homework_submissions;
CREATE TABLE homework_submissions (
  id BIGINT PRIMARY KEY,
  user_id BIGINT,
  homework_id BIGINT,
  status VARCHAR(20),
  score DECIMAL(10,2),
  content TEXT,
  attachment_url VARCHAR(500),
  feedback TEXT,
  submitted_at DATETIME,
  graded_at DATETIME,
  graded_by BIGINT,
  created_at DATETIME,
  updated_at DATETIME,
  deleted INT DEFAULT 0
);

DROP TABLE IF EXISTS notes;
CREATE TABLE notes (
  id BIGINT PRIMARY KEY,
  user_id BIGINT,
  course_id BIGINT,
  lesson_id BIGINT,
  title VARCHAR(255),
  content TEXT,
  is_public TINYINT(1),
  created_at DATETIME,
  updated_at DATETIME,
  deleted INT DEFAULT 0
);

DROP TABLE IF EXISTS discussions;
CREATE TABLE discussions (
  id BIGINT PRIMARY KEY,
  user_id BIGINT,
  course_id BIGINT,
  title VARCHAR(255),
  content TEXT,
  is_pinned TINYINT(1),
  view_count INT,
  reply_count INT,
  created_at DATETIME,
  updated_at DATETIME,
  deleted INT DEFAULT 0
);

DROP TABLE IF EXISTS comments;
CREATE TABLE comments (
  id BIGINT PRIMARY KEY,
  user_id BIGINT,
  discussion_id BIGINT,
  parent_id BIGINT,
  content TEXT,
  like_count INT,
  created_at DATETIME,
  updated_at DATETIME,
  deleted INT DEFAULT 0
);

DROP TABLE IF EXISTS announcements;
CREATE TABLE announcements (
  id BIGINT PRIMARY KEY,
  course_id BIGINT,
  author_id BIGINT,
  title VARCHAR(255),
  content TEXT,
  is_pinned TINYINT(1),
  published_at DATETIME,
  created_at DATETIME,
  updated_at DATETIME,
  deleted INT DEFAULT 0
);

DROP TABLE IF EXISTS notifications;
CREATE TABLE notifications (
  id BIGINT PRIMARY KEY,
  user_id BIGINT,
  type VARCHAR(20),
  title VARCHAR(255),
  content TEXT,
  link VARCHAR(500),
  is_read TINYINT(1),
  created_at DATETIME,
  updated_at DATETIME,
  deleted INT DEFAULT 0
);

DROP TABLE IF EXISTS direct_messages;
CREATE TABLE direct_messages (
  id BIGINT PRIMARY KEY,
  sender_id BIGINT,
  receiver_id BIGINT,
  content TEXT,
  is_read TINYINT(1),
  read_at DATETIME,
  created_at DATETIME,
  updated_at DATETIME,
  deleted INT DEFAULT 0
);

DROP TABLE IF EXISTS ai_tutor_sessions;
CREATE TABLE ai_tutor_sessions (
  id BIGINT PRIMARY KEY,
  user_id BIGINT,
  course_id BIGINT,
  title VARCHAR(255),
  model VARCHAR(50),
  created_at DATETIME,
  updated_at DATETIME,
  deleted INT DEFAULT 0
);

DROP TABLE IF EXISTS ai_tutor_messages;
CREATE TABLE ai_tutor_messages (
  id BIGINT PRIMARY KEY,
  session_id BIGINT,
  role VARCHAR(20),
  content TEXT,
  prompt_tokens VARCHAR(50),
  created_at DATETIME,
  updated_at DATETIME,
  deleted INT DEFAULT 0
);

DROP TABLE IF EXISTS study_plans;
CREATE TABLE study_plans (
  id BIGINT PRIMARY KEY,
  user_id BIGINT,
  course_id BIGINT,
  title VARCHAR(255),
  start_date DATE,
  end_date DATE,
  daily_minutes INT,
  is_active TINYINT(1),
  created_at DATETIME,
  updated_at DATETIME,
  deleted INT DEFAULT 0
);

DROP TABLE IF EXISTS study_tasks;
CREATE TABLE study_tasks (
  id BIGINT PRIMARY KEY,
  plan_id BIGINT,
  title VARCHAR(255),
  scheduled_date DATE,
  is_completed TINYINT(1),
  duration_minutes INT,
  created_at DATETIME,
  updated_at DATETIME,
  deleted INT DEFAULT 0
);

DROP TABLE IF EXISTS badges;
CREATE TABLE badges (
  id BIGINT PRIMARY KEY,
  name VARCHAR(191),
  description TEXT,
  icon VARCHAR(255),
  condition_type VARCHAR(50),
  condition_value INT,
  created_at DATETIME,
  updated_at DATETIME,
  deleted INT DEFAULT 0
);

DROP TABLE IF EXISTS user_badges;
CREATE TABLE user_badges (
  id BIGINT PRIMARY KEY,
  user_id BIGINT,
  badge_id BIGINT,
  created_at DATETIME,
  updated_at DATETIME,
  deleted INT DEFAULT 0
);

DROP TABLE IF EXISTS leaderboards;
CREATE TABLE leaderboards (
  id BIGINT PRIMARY KEY,
  user_id BIGINT,
  period VARCHAR(20),
  total_score INT,
  rank INT,
  study_minutes INT,
  completed_lessons INT,
  created_at DATETIME,
  updated_at DATETIME,
  deleted INT DEFAULT 0
);

DROP TABLE IF EXISTS testimonials;
CREATE TABLE testimonials (
  id BIGINT PRIMARY KEY,
  user_id BIGINT,
  content TEXT,
  rating INT,
  is_approved TINYINT(1),
  created_at DATETIME,
  updated_at DATETIME,
  deleted INT DEFAULT 0
);

DROP TABLE IF EXISTS group_chats;
CREATE TABLE group_chats (
  id BIGINT PRIMARY KEY,
  course_id BIGINT,
  name VARCHAR(191),
  description TEXT,
  created_by BIGINT,
  max_members INT,
  created_at DATETIME,
  updated_at DATETIME,
  deleted INT DEFAULT 0
);

DROP TABLE IF EXISTS group_chat_members;
CREATE TABLE group_chat_members (
  id BIGINT PRIMARY KEY,
  group_id BIGINT,
  user_id BIGINT,
  role VARCHAR(20),
  created_at DATETIME,
  updated_at DATETIME,
  deleted INT DEFAULT 0
);

DROP TABLE IF EXISTS group_chat_messages;
CREATE TABLE group_chat_messages (
  id BIGINT PRIMARY KEY,
  group_id BIGINT,
  sender_id BIGINT,
  content TEXT,
  message_type VARCHAR(20),
  created_at DATETIME,
  updated_at DATETIME,
  deleted INT DEFAULT 0
);

DROP TABLE IF EXISTS error_books;
CREATE TABLE error_books (
  id BIGINT PRIMARY KEY,
  user_id BIGINT,
  title VARCHAR(255),
  description TEXT,
  created_at DATETIME,
  updated_at DATETIME,
  deleted INT DEFAULT 0
);

DROP TABLE IF EXISTS error_book_items;
CREATE TABLE error_book_items (
  id BIGINT PRIMARY KEY,
  error_book_id BIGINT,
  question_id BIGINT,
  user_answer TEXT,
  correct_answer TEXT,
  note TEXT,
  mistake_count INT,
  is_resolved TINYINT(1),
  created_at DATETIME,
  updated_at DATETIME,
  deleted INT DEFAULT 0
);

DROP TABLE IF EXISTS learning_analytics;
CREATE TABLE learning_analytics (
  id BIGINT PRIMARY KEY,
  user_id BIGINT,
  stat_date DATE,
  study_minutes INT,
  completed_lessons INT,
  exercise_count INT,
  correct_count INT,
  average_score DECIMAL(10,2),
  streak_days INT,
  created_at DATETIME,
  updated_at DATETIME,
  deleted INT DEFAULT 0
);

DROP TABLE IF EXISTS recommendations;
CREATE TABLE recommendations (
  id BIGINT PRIMARY KEY,
  user_id BIGINT,
  course_id BIGINT,
  reason VARCHAR(255),
  algorithm VARCHAR(50),
  is_clicked TINYINT(1),
  created_at DATETIME,
  updated_at DATETIME,
  deleted INT DEFAULT 0
);

DROP TABLE IF EXISTS user_settings;
CREATE TABLE user_settings (
  id BIGINT PRIMARY KEY,
  user_id BIGINT,
  theme VARCHAR(20),
  language VARCHAR(20),
  email_notification TINYINT(1),
  push_notification TINYINT(1),
  study_reminder TINYINT(1),
  reminder_time VARCHAR(20),
  created_at DATETIME,
  updated_at DATETIME,
  deleted INT DEFAULT 0
);

SET FOREIGN_KEY_CHECKS = 1;
