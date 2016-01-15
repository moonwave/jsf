-- ----------------- Warning :: Warning :: Warning -----------------------------
--
--          ALL DATA WILL BE LOST AFTER RUNNING THIS SCRIPT
--
--          BACKUP ANY DATA BEFORE ATTEMPT TO RUN THIS SCRIPT
--
-- ----------------- Warning :: Warning :: Warning -----------------------------

-- -----------------------------------------------------------------------------
-- The following SQL statements have been executed successfully on MySQL 5.5.46
-- -----------------------------------------------------------------------------

DROP USER 'flagshiptut'@'localhost';
DROP USER 'flagshiptut'@'%';
DROP DATABASE IF EXISTS flagshiptut;


CREATE DATABASE flagshiptut;

CREATE USER 'flagshiptut'@'localhost' IDENTIFIED BY 'QjzwFd!5'; 
GRANT ALL PRIVILEGES ON flagshiptut.* TO 'flagshiptut'@'localhost' WITH GRANT OPTION; 

CREATE USER 'flagshiptut'@'%' IDENTIFIED BY 'QjzwFd!5'; 
GRANT ALL PRIVILEGES ON flagshiptut.* TO 'flagshiptut'@'%' WITH GRANT OPTION;

USE flagshiptut;

-- -----------------------------------------------------------------------------
-- Create table setup

CREATE TABLE setup (
    id                      SMALLINT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    alias                   VARCHAR(30) NOT NULL,
    name                    VARCHAR(50) NOT NULL,
    school_start_date       DATE, -- inclusive
    school_end_date         DATE, -- inclusive
    update_time             TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    create_time             TIMESTAMP
);

CREATE TABLE system_info (
    id                      SMALLINT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    system_info_date        DATE NOT NULL, -- the message creation date
    message                 VARCHAR(1024), -- message to be dispalyed on screen
    severity                CHAR(1) NOT NULL, -- 'I': info, 'W': warning, 'M': major, 'C': critical. Note: 'I' message can be acknowledged by user
    msg_kick_in_time        DATETIME, -- system info (message) kick in time
    msg_kick_out_time       DATETIME, -- system info (message) kick out time
    functional_locking      BOOLEAN, -- True: lock all functions for all users except super user
    locking_kick_in_time    DATETIME, -- system info kick in time
    active                  BOOLEAN, -- true: this message is active, false: this message is inactive. Note: only one message can be active
    update_time             TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    create_time             TIMESTAMP
);

CREATE INDEX system_info_idx1 ON system_info (active DESC, system_info_date);

-- -----------------------------------------------------------------------------
-- Create table role

CREATE TABLE role (
    id                      SMALLINT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    alias                   VARCHAR(15) NOT NULL,
    name                    VARCHAR(50) NOT NULL,
    privileges              VARCHAR(2000), -- object permissions in xml
    UNIQUE (alias)
);

-- -----------------------------------------------------------------------------
-- Create table user

CREATE TABLE user (
    id                      INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL,
    first_name              VARCHAR(18) NOT NULL,
    last_name               VARCHAR(18) NOT NULL,
    chinese_name            VARCHAR(18) CHARACTER SET utf8,
    login_id                VARCHAR(50) NOT NULL, -- can use email
    password                VARCHAR(80) NOT NULL,
    email                   VARCHAR(50) NOT NULL,
    phone                   VARCHAR(20),
    active                  BOOLEAN DEFAULT TRUE, -- true: active, false: inactive; control individual user
    generic_user            BOOLEAN DEFAULT TRUE, -- true: generic user, false: not participate normal activities 
    hint                    VARCHAR(50),
    answer                  VARCHAR(50),
    timezone                VARCHAR(50) DEFAULT 'America/Phoenix',
    tag                     CHAR(32),
    action_code             VARCHAR(40),
    login_attempt           SMALLINT, -- keep the login attept count, if 5 times attempted within 5 minutes, lock this account
    last_attempt_ts         DATETIME, -- savea the first timestamp for a repeat login attempt
    last_login_ip           VARCHAR(50),
    update_time             TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    create_time             TIMESTAMP,
    UNIQUE (login_id),
    UNIQUE (email)
);

CREATE INDEX user_idx1 ON `user` (first_name, last_name);
CREATE INDEX user_idx2 ON `user` (last_name, first_name);
CREATE INDEX user_idx3 ON `user` (action_code);
CREATE INDEX user_idx4 ON `user` (email);

-- -----------------------------------------------------------------------------
-- Create table user_role
-- One user can have multiple roles, one role can have multiple users
-- user * <==> * role
CREATE TABLE user_role (
    id                      INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL,
    user_id                 INTEGER NOT NULL REFERENCES user(id),
    role_id                 SMALLINT REFERENCES role(id),
    update_time             TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE (user_id, role_id)
);

CREATE UNIQUE INDEX user_role_idx1 ON user_role (user_id, role_id);
CREATE UNIQUE INDEX user_role_idx2 ON user_role (role_id, user_id);

-- -----------------------------------------------------------------------------
-- Create table tut_group

CREATE TABLE tutor_group (
    id                      SMALLINT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    alias                   VARCHAR(30) NOT NULL,
    name                    VARCHAR(50) NOT NULL,
    ordinal                 SMALLINT,
    UNIQUE (alias)
);

-- -----------------------------------------------------------------------------
-- Create table user_tutor_group
-- One user ==> many tutor_group, one tutor_group ==> many users
-- user * <==> * tutor_group 
CREATE TABLE user_tutor_group (
    id                      INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL,
    user_id                 INTEGER NOT NULL REFERENCES user(id),
    tutor_group_id          SMALLINT REFERENCES tutor_group(id),
    update_time             TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE (user_id, tutor_group_id)
);
CREATE INDEX user_tutor_group_idx1 ON user_tutor_group (user_id, tutor_group_id);
CREATE INDEX user_tutor_group_idx2 ON user_tutor_group (tutor_group_id, user_id);

-- -----------------------------------------------------------------------------
-- Create table semester

CREATE TABLE semester (
    id                      SMALLINT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    alias                   VARCHAR(15) NOT NULL,
    name                    VARCHAR(50) NOT NULL,
    start_date              DATE NOT NULL,
    end_date                DATE,
    start_weekday           CHAR(3) DEFAULT 'MON', -- SUN, MON, TUE, WED, THU, FRI, SAT
    number_of_weeks         SMALLINT DEFAULT 16,
    UNIQUE (alias)
);

-- -----------------------------------------------------------------------------
-- Create table week

CREATE TABLE week (
    id                      SMALLINT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    week                    VARCHAR(15) NOT NULL,
    UNIQUE (week)
);

-- -----------------------------------------------------------------------------
-- Create table semester_week (This table may not need)
-- semener * <==> * week
CREATE TABLE semester_week (
    id                      INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL,
    semester                VARCHAR(15) REFERENCES semester(alias),
    week                    VARCHAR(15) REFERENCES week(week),
    start_day               DATE,
    UNIQUE (semester, week)
);
CREATE UNIQUE INDEX semester_week_idx1 ON semester_week (semester, start_day);

-- -----------------------------------------------------------------------------
-- Create table semester_no_school_day

CREATE TABLE semester_no_school_day (
    id                      INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL,
    semester                VARCHAR(15) REFERENCES semester(alias),
    no_school_day           DATE NOT NULL,
    UNIQUE (semester, no_school_day)
);
CREATE UNIQUE INDEX semester_no_school_day_idx1 ON semester_no_school_day (semester, no_school_day);

-- -----------------------------------------------------------------------------
-- Create table schedule

CREATE TABLE schedule (
    id                      INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL,
    user_id                 INTEGER REFERENCES `user` (user_id),
    tutor_id                INTEGER REFERENCES `user` (user_id),
    event                   VARCHAR(255) CHARACTER SET utf8,
    start_time              DATETIME NOT NULL,
    end_time                DATETIME NOT NULL,
    all_day_event           BOOLEAN, -- True: all day event
    tutor_event             BOOLEAN, -- True: tutor event
    parent_event_id         INTEGER REFERENCES schedule (id),
    update_time             TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    create_time             TIMESTAMP,
    UNIQUE (user_id, tutor_id, start_time, end_time)
);

CREATE INDEX schedule_idx1 ON schedule (user_id, start_time, end_time);
CREATE INDEX schedule_idx2 ON schedule (tutor_id, start_time, end_time);
CREATE INDEX schedule_idx3 ON schedule (user_id, tutor_id, start_time, end_time);
CREATE INDEX schedule_idx4 ON schedule (tutor_event);
CREATE INDEX schedule_idx5 ON schedule (parent_event_id);

-- -----------------------------------------------------------------------------
-- Create table announcement

CREATE TABLE announcement ( -- anouncement to all people
    id                      INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL,
    subject                 VARCHAR(255) CHARACTER SET utf8 NOT NULL,
    body                    TEXT CHARACTER SET utf8, -- 64K
    published               BOOLEAN,
    user_id                 INTEGER NOT NULL REFERENCES user(id),
    update_time             TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    create_time             TIMESTAMP
);

-- -----------------------------------------------------------------------------
-- Create table group_post

CREATE TABLE group_post (-- base group post
    id                      INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL,
    subject                 VARCHAR(255) CHARACTER SET utf8 NOT NULL,
    body                    TEXT CHARACTER SET utf8, -- 64K
    published               BOOLEAN,
    group_post_type         CHAR(1) NOT NULL DEFAULT '1', -- '1' regular post, '5' - assignment
    due_date                DATETIME,
    user_id                 INTEGER NOT NULL REFERENCES user(id),
    update_time             TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    create_time             TIMESTAMP
);

-- -----------------------------------------------------------------------------
-- Create table group_post_to_group
-- group_post * <==> * tutor_group
CREATE TABLE group_post_to_group (
    id                      INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL,
    group_post_id           INTEGER REFERENCES group_post(id),
    tutor_group_id          SMALLINT REFERENCES tutor_group(id),
    UNIQUE (group_post_id, tutor_group_id)
);

-- -----------------------------------------------------------------------------
-- 
CREATE TABLE upload (
    id                      INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL,
    user_id                 INTEGER NOT NULL REFERENCES user(id),
    tutor_group_id          SMALLINT REFERENCES tutor_group(id),
    announcement_id         INTEGER REFERENCES announcement(id),
    group_post_id           INTEGER REFERENCES group_post(id),
    description             VARCHAR(255),
    filepath                VARCHAR(255), -- full filepath
    create_time             TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE INDEX upload_idx1 ON upload (user_id, tutor_group_id, announcement_id, group_post_id);
CREATE INDEX upload_idx2 ON upload (create_time);

-- -----------------------------------------------------------------------------
-- Create table evaluation_performance

CREATE TABLE evaluation_performance (
    id                      INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL,
    user_id                 INTEGER REFERENCES `user`(user_id),
    tutor_id                INTEGER REFERENCES `user`(user_id),
    semester                VARCHAR(15) REFERENCES semester(alias),
    week                    VARCHAR(15) REFERENCES week(week),
    attendance              SMALLINT,
    participation           SMALLINT,
    performance             SMALLINT,
    total                   SMALLINT,
    note                    VARCHAR(255) CHARACTER SET utf8,
    published               BOOLEAN,
    update_time             TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    create_time             TIMESTAMP
);

CREATE INDEX eva_performance_idx1 ON evaluation_performance (user_id, semester, week);
CREATE INDEX eva_performance_idx2 ON evaluation_performance (create_time);

-- -----------------------------------------------------------------------------
-- Create table evaluation_objective

CREATE TABLE evaluation_objective (
    id                      INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL,
    user_id                 INTEGER REFERENCES `user`(user_id),
    tutor_id                INTEGER REFERENCES `user`(user_id),
    semester                VARCHAR(15) REFERENCES semester(alias),
    week                    VARCHAR(15) REFERENCES week(week),
    -- Part I Interpretive Listening Mode
    eval_part1_1            BOOLEAN, -- I can understand the general idea chronologically
    eval_part1_2            BOOLEAN, -- I can understand key words / phrases in the materials that I am listening or reading
    eval_part1_3            BOOLEAN, -- I can understand the main idea of the text
    eval_part1_comments     VARCHAR(255) CHARACTER SET utf8, -- Other comments
    -- Part I Presentational Mode
    eval_part2_1            BOOLEAN, -- I can present / write my opinion in a correct order
    eval_part2_2            BOOLEAN, -- I can apply the words and phrases to present or to write I have learned or just learned
    eval_part2_3            BOOLEAN, -- I can use connected series of sentenses to present my opinion in presentation or writing
    eval_part2_comments     VARCHAR(255) CHARACTER SET utf8, -- Other comments
    -- Part III Interpretive Communication Mode
    eval_part3_1            BOOLEAN, -- I can apply what I have learned to communicate with tutor
    eval_part3_2            BOOLEAN, -- I can express myself logically
    eval_part3_3            BOOLEAN, -- I can interact with tutor with correct forms
    eval_part3_comments     VARCHAR(255) CHARACTER SET utf8, -- Other comments

    student_evaluation      BOOLEAN, -- true student evaluation, false tutor evaluation
    published               BOOLEAN,
    parent_evaluation_id    INTEGER REFERENCES evaluation_objective (id), -- self evaluation is the parent of a tutor evaluation
    update_time             TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    create_time             TIMESTAMP
);

CREATE INDEX eva_objective_idx1 ON evaluation_objective (user_id, semester, week);
CREATE INDEX eva_objective_idx2 ON evaluation_objective (create_time);
CREATE INDEX eva_objective_idx3 ON evaluation_objective (parent_evaluation_id);

-- -----------------------------------------------------------------------------
-- Create table course

CREATE TABLE course  (
    id                      SMALLINT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    name                    VARCHAR(50),
    description             TEXT CHARACTER SET utf8, -- 64K
    update_time             TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    create_time             TIMESTAMP
);

CREATE INDEX course_idx1 ON course (name);

-- -----------------------------------------------------------------------------
-- Create table course_reg

CREATE TABLE course_reg (
    id                      INTEGER  PRIMARY KEY AUTO_INCREMENT NOT NULL,
    student_id              INTEGER  REFERENCES user(id),
    course_id               SMALLINT REFERENCES course(id),
    reg_date                DATE,
    update_time             TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    create_time             TIMESTAMP,
    UNIQUE (student_id, course_id)
);

CREATE UNIQUE INDEX course_reg_idx1 ON course_reg (student_id, course_id);

-- +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
-- Generic page
-- +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

-- -----------------------------------------------------------------------------
-- Create table contact_us_category

CREATE TABLE contact_us_category (
    id                      SMALLINT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    category                VARCHAR(30),
    email                   VARCHAR(250) NOT NULL,
    ordinal                 SMALLINT,
    update_time             TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    create_time             TIMESTAMP
);

CREATE INDEX category_idx1 ON contact_us_category (ordinal);

-- -----------------------------------------------------------------------------
-- Create table generic_page

CREATE TABLE generic_page (
    id                      SMALLINT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    page_key                VARCHAR(30) NOT NULL, -- sample keys: homepage, administration, about.us, contact.us
    publish_date            DATE  NOT NULL,
    subject                 VARCHAR(255) CHARACTER SET utf8,
    show_subject            BOOLEAN, -- true: show subject at the center, false: do not show the subject
    content                 TEXT CHARACTER SET utf8,
    active                  BOOLEAN, -- true: this page is active, false: this page is inactive. Note: only one generic page for one key can be active
    update_time             TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    create_time             TIMESTAMP
);

CREATE INDEX generic_page_idx1 ON generic_page (page_key, active DESC);

-- -----------------------------------------------------------------------------
-- Create table menu

CREATE TABLE menu (
    id                      INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL,
    menu_group              VARCHAR(50) NOT NULL, -- latestLinks, account, etc.
    menu_item               VARCHAR(50) NOT NULL, -- menu description
    parent_menu             VARCHAR(50) NOT NULL,
    menu_location           CHAR(1) NOT NULL, -- 'L' - left, 'T' - top, 'R' - right
    target_class            VARCHAR(200) NOT NULL, -- target page class name or target page id
    panel_name              VARCHAR(30), -- subpage id   
    ordinal                 SMALLINT,
    access_role             VARCHAR(200), -- comma separated role alias list, if null the menu can be accessed by anyone 
    active                  BOOLEAN DEFAULT true, -- true: active, false: inactive
    update_time             TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    create_time             TIMESTAMP
);

CREATE INDEX menu_idx1 ON menu (menu_group, parent_menu, ordinal);
