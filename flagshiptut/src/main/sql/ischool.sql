-- ----------------- Warning :: Warning :: Warning -----------------------------
--
--          ALL DATA WILL BE LOST AFTER RUNNING THIS SCRIPT
--
--          BACKUP ANY DATA BEFORE ATTEMPT TO RUN THIS SCRIPT
--
-- ----------------- Warning :: Warning :: Warning -----------------------------

-- 1. Assign postgres user with an password
-- postgres@your-pc:~$ psql
-- Welcome to psql 8.3.1, the PostgreSQL interactive terminal.

-- Type: \copyright for distribution terms
--       \h for help with SQL commands
--       \? for help with psql commands
--       \g or terminate with semicolon to execute query
--       \q to quit
--
-- postgres=# alter user postgres ENCRYPTED password 'postgres';
--
-- 2. Connect to jdbc:postgresql://localhost:5432/postgres as postgres user
-- 3. CREATE DATABASE ischool WITH ENCODING = 'UNICODE';
-- 4. Connect to jdbc:postgresql://localhost:5432/ischool as postgres user
-- 5. Run the following sql command lines through a SQL command window
-- postgres@mypc:~$  psql ischool < ischool_ddl.sql

--------------------------------------------------------------------------------
--                      ischool_ddl.sql
--------------------------------------------------------------------------------

--------------------------------------------------------------------------------
-- The following SQL statements have been executed on Postgres 8.2.6

-- Warning: ALL DATA WILL BE LOST WHEN EXECUTING DROP TABLES

DROP TABLE IF EXISTS ischool_contact_us_category;
DROP TABLE IF EXISTS ischool_volunteer_service;
DROP TABLE IF EXISTS ischool_duty;
DROP TABLE IF EXISTS ischool_duty_type;
DROP TABLE IF EXISTS ischool_vote_result;
DROP TABLE IF EXISTS ischool_vote;
DROP TABLE IF EXISTS ischool_payment;
DROP TABLE IF EXISTS ischool_payment_type;
DROP TABLE IF EXISTS ischool_email_log; -- using email server's capbility to look up sent mails
DROP TABLE IF EXISTS ischool_course_reg;
DROP TABLE IF EXISTS ischool_course_reg_log;
DROP TABLE IF EXISTS ischool_course;
DROP TABLE IF EXISTS ischool_course_type;
DROP TABLE IF EXISTS ischool_teacher;
DROP TABLE IF EXISTS ischool_user_role;
DROP TABLE IF EXISTS ischool_phone;
DROP TABLE IF EXISTS ischool_email;
DROP TABLE IF EXISTS ischool_url;
DROP TABLE IF EXISTS ischool_user;
DROP TABLE IF EXISTS ischool_account;
DROP TABLE IF EXISTS ischool_address;
DROP TABLE IF EXISTS ischool_communication_type;
DROP TABLE IF EXISTS ischool_role;
DROP TABLE IF EXISTS ischool_calendar;
DROP TABLE IF EXISTS ischool_setup; -- semester or quarter
DROP TABLE IF EXISTS ischool_news;
DROP TABLE IF EXISTS ischool_lost_n_found;
DROP TABLE IF EXISTS ischool_system_info;
DROP TABLE IF EXISTS ischool_generic_page;
DROP TABLE IF EXISTS ischool_menu;

DROP SEQUENCE IF EXISTS ischool_contact_us_category_seq;
DROP SEQUENCE IF EXISTS ischool_volunteer_service_seq;
DROP SEQUENCE IF EXISTS ischool_duty_seq;
DROP SEQUENCE IF EXISTS ischool_vote_result_seq;
DROP SEQUENCE IF EXISTS ischool_vote_seq;
DROP SEQUENCE IF EXISTS ischool_payment_seq;
DROP SEQUENCE IF EXISTS ischool_email_log_seq;
DROP SEQUENCE IF EXISTS ischool_course_reg_seq;
DROP SEQUENCE IF EXISTS ischool_course_reg_log_seq;
DROP SEQUENCE IF EXISTS ischool_course_seq;
DROP SEQUENCE IF EXISTS ischool_teacher_seq;
DROP SEQUENCE IF EXISTS ischool_user_role_seq;
DROP SEQUENCE IF EXISTS ischool_user_seq;
DROP SEQUENCE IF EXISTS ischool_account_seq;
DROP SEQUENCE IF EXISTS ischool_address_seq;
DROP SEQUENCE IF EXISTS ischool_phone_seq;
DROP SEQUENCE IF EXISTS ischool_email_seq;
DROP SEQUENCE IF EXISTS ischool_url_seq;
DROP SEQUENCE IF EXISTS ischool_calendar_seq;
DROP SEQUENCE IF EXISTS ischool_setup_seq;
DROP SEQUENCE IF EXISTS ischool_news_seq;
DROP SEQUENCE IF EXISTS ischool_lost_n_found_seq;
DROP SEQUENCE IF EXISTS ischool_system_info_seq;
DROP SEQUENCE IF EXISTS ischool_generic_page_seq;
DROP SEQUENCE IF EXISTS ischool_menu_seq;

DROP TABLE IF EXISTS dconfig_system CASCADE;
DROP TABLE IF EXISTS dconfig_key CASCADE;
DROP TABLE IF EXISTS dconfig_datatype CASCADE;
DROP TABLE IF EXISTS dconfig_attribute CASCADE;

DROP SEQUENCE IF EXISTS ischool_contact_seq;
DROP SEQUENCE IF EXISTS dconfig_key_seq;
DROP SEQUENCE IF EXISTS dconfig_attribute_seq;

--------------------------------------------------------------------------------
-- Create table ischool_setup

CREATE SEQUENCE ischool_setup_seq;
CREATE TABLE    ischool_setup (
    id                     SMALLINT PRIMARY KEY DEFAULT nextval('ischool_setup_seq') NOT NULL,
    alias                  VARCHAR(30) NOT NULL,
    name                   VARCHAR(50) NOT NULL,
    school_start_date      DATE, -- inclusive
    school_end_date        DATE, -- inclusive
    early_reg_start_date   DATE, -- inclusive starts at 12:01 AM
    early_reg_end_date     DATE, -- inclusive, stops at 23:59:59, usually earlyRegEndDate is one day before regStartDate
    early_reg_discount     FLOAT,
    reg_start_date         DATE, -- Regular / Onsite registration start date
    reg_close_date         DATE, -- Regular / Onsite registration stop date, inclusive. System will not take individual registration anymore after this date
    app_fee_4_family       FLOAT, -- application fee for each family
    app_fee_4_student      FLOAT, -- application fee for each student
    late_fee               FLOAT, -- late fee for each family if payment does pay in full as of late_fee_date
    late_fee_date          DATE, -- inclusive, stops at 23:59:59, the date start to change late fee if passed
    multi_student_discount_amount  FLOAT, -- mutiple student discount amount for each student
    multi_student_discount_percent FLOAT, -- mutiple student discount percentage for total tuition for a family
    discount_percent       FLOAT, -- not used
    discount_method        CHAR(1), -- 'A' - amount, '%' percentage, -- not used
    current_school         BOOLEAN, -- true: if use this as the currrent school, only one can be true
    board_of_directors     TEXT, -- HTML format
    record_ts              TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE SEQUENCE ischool_system_info_seq;
CREATE TABLE ischool_system_info (
    id                   SMALLINT PRIMARY KEY DEFAULT nextval('ischool_system_info_seq') NOT NULL,
    system_info_date     DATE NOT NULL, -- the message creation date
    message              VARCHAR(1024), -- message to be dispalyed on screen
    severity             CHAR(1) NOT NULL, -- 'I': info, 'W': warning, 'M': major, 'C': critical. Note: 'I' message can be acknowledged by user
    msg_kick_in_time     TIMESTAMP, -- system info (message) kick in time
    msg_kick_out_time    TIMESTAMP, -- system info (message) kick out time
    functional_locking   BOOLEAN, -- True: lock all functions for all users except super user
    locking_kick_in_time TIMESTAMP, -- system info kick in time
    active               BOOLEAN, -- true: this message is active, false: this message is inactive. Note: only one message can be active
    record_ts            TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX ischool_system_info_idx1 ON ischool_system_info (active DESC, system_info_date);

--------------------------------------------------------------------------------
-- Create table ischool_role

CREATE TABLE    ischool_role (
    id          SMALLINT PRIMARY KEY NOT NULL,
    alias       VARCHAR(15),
    name        VARCHAR(50) NOT NULL,
    privileges  VARCHAR(2000), -- object permissions in xml
    record_ts   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

--------------------------------------------------------------------------------
-- Create table ischool_address

CREATE SEQUENCE ischool_address_seq;
CREATE TABLE    ischool_address (
    id              INTEGER PRIMARY KEY DEFAULT nextval('ischool_address_seq') NOT NULL,
    street          VARCHAR(220) NOT NULL,
    city            VARCHAR(30) NOT NULL,
    state           VARCHAR(30) NOT NULL, -- or province
    zip             VARCHAR(10) NOT NULL,
    country         VARCHAR(30), -- or region, default U.S.
    record_ts       TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

--------------------------------------------------------------------------------
-- Create table ischool_communication_type

CREATE TABLE    ischool_communication_type (
    id          SMALLINT PRIMARY KEY NOT NULL,
    alias       VARCHAR(5) NOT NULL,
    name        VARCHAR(15) NOT NULL,
    record_ts   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
--  11  H-ph home_phone
--  12  W-ph work_phone
--  13  M-ph mobile_phone
--  14  H-fax home_fax
--  15  W-fax work_fax
--  16  O-ph other_phone
--  31  H-em home_email
--  32  W-em work_email
--  33  O-em other_email
--  51  H-url home_url
--  52  W-url work_url
--  53  HPurl home_page_url
--  54  B-url blog_url
--  55  O-url other_url

--------------------------------------------------------------------------------
-- Create table ischool_account

CREATE SEQUENCE ischool_account_seq;
CREATE TABLE    ischool_account      (
    id              INTEGER PRIMARY KEY DEFAULT nextval('ischool_account_seq') NOT NULL,
    account_no      INTEGER, -- saves legacy family id, and it is used as family account#, this could be different from previous id
    account_type    CHAR(1) NOT NULL, -- 'F': family account, 'I': individual account
    active          BOOLEAN, -- true: active, false: inactive, this flag control all associated user accounts
    remove_me       BOOLEAN, -- true to remove this account info, including address, phone, etc.
                             -- set active as false, set news_subscribe as false
                             -- if this account does not have any external references, remove this account completely
    address_id      INTEGER REFERENCES ischool_address(id),
    record_ts       TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (account_no)
);

--------------------------------------------------------------------------------
-- Create table ischool_user

CREATE SEQUENCE ischool_user_seq;
CREATE TABLE ischool_user (
    id                INTEGER PRIMARY KEY DEFAULT nextval('ischool_user_seq') NOT NULL,
    login_id          VARCHAR(18),
    student_id        VARCHAR(18), -- reserved, current using fields id as the student id
    password          VARCHAR(32),
    hint              VARCHAR(50),
    answer            VARCHAR(50),
    first_name        VARCHAR(18) NOT NULL,
    middle_name       VARCHAR(18),
    last_name         VARCHAR(18) NOT NULL,
    nickname          VARCHAR(18),
    initial           VARCHAR(18),
    cn_name           VARCHAR(30),
    gender            CHAR(1), -- M: Male, F: Female
    family_role       CHAR(1) NOT NULL, -- M: Mother, F: Father, G-Guardian, I-Individual, C - Child
    account_holder    BOOLEAN, -- true: yes, false: no
    employer          VARCHAR(50),
    age               INTEGER,
    birth_day         DATE,
    active            BOOLEAN DEFAULT TRUE, -- true: active, false: inactive; control individual user. If account is inactive, this user is inactive even its own active flag = true    eligible          BOOLEAN DEFAULT FALSE, -- true: eligible for register courses; false: no eligible for registration
    news_subscribe    BOOLEAN, -- true (default): yes, false: no - no news emails will be sent to this account
    login_attempt     SMALLINT, -- keep the login attept count, if 5 times attempted within 5 minutes, lock this account
    last_attempt_ts   TIMESTAMP, -- savea the first timestamp for a repeat login attempt
    account           INTEGER REFERENCES ischool_account(id),
    record_ts         TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (login_id),
    UNIQUE (student_id)
);

CREATE INDEX ischool_user_idx1 ON ischool_user (first_name, last_name);
CREATE INDEX ischool_user_idx2 ON ischool_user (last_name, first_name);
CREATE INDEX ischool_user_idx4 ON ischool_user (account_holder);

--------------------------------------------------------------------------------
-- Create table ischool_phone

CREATE SEQUENCE ischool_phone_seq;
CREATE TABLE    ischool_phone (
    id                  INTEGER PRIMARY KEY DEFAULT nextval('ischool_phone_seq') NOT NULL,
    user_id             INTEGER NOT NULL REFERENCES ischool_user(id),
    communication_type  SMALLINT NOT NULL REFERENCES ischool_communication_type(id),
    phone               VARCHAR(20),
    record_ts           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE UNIQUE INDEX ischool_phone_idx1 ON ischool_phone (user_id, communication_type, phone);
CREATE UNIQUE INDEX ischool_phone_idx2 ON ischool_phone (phone, user_id, communication_type);

--------------------------------------------------------------------------------
-- Create table ischool_email

CREATE SEQUENCE ischool_email_seq;
CREATE TABLE    ischool_email (
    id                  INTEGER PRIMARY KEY DEFAULT nextval('ischool_email_seq') NOT NULL,
    user_id             INTEGER NOT NULL REFERENCES ischool_user(id),
    communication_type  SMALLINT NOT NULL REFERENCES ischool_communication_type(id),
    email               VARCHAR(50),
    action_code         VARCHAR(50),
    active              BOOLEAN DEFAULT TRUE, -- true or null: active, false: inactive
    record_ts           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE UNIQUE INDEX ischool_email_idx1 ON ischool_email (user_id, communication_type, email);
CREATE UNIQUE INDEX ischool_email_idx2 ON ischool_email (email, user_id, communication_type);
CREATE INDEX ischool_email_idx3 ON ischool_email (action_code);

--------------------------------------------------------------------------------
-- Create table ischool_url

CREATE SEQUENCE ischool_url_seq;
CREATE TABLE    ischool_url (
    id                  INTEGER PRIMARY KEY DEFAULT nextval('ischool_url_seq') NOT NULL,
    user_id             INTEGER NOT NULL REFERENCES ischool_user(id),
    communication_type  SMALLINT NOT NULL REFERENCES ischool_communication_type(id),
    url                 VARCHAR(200),
    record_ts           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

--------------------------------------------------------------------------------
-- Create table ischool_teacher

CREATE SEQUENCE ischool_teacher_seq;
CREATE TABLE ischool_teacher (
    id                      INTEGER PRIMARY KEY DEFAULT nextval('ischool_teacher_seq') NOT NULL,
    --school_id               SMALLINT NOT NULL REFERENCES ischool_setup(id),
    user_id                 INTEGER NOT NULL REFERENCES ischool_user(id),
    teacher_type            SMALLINT NULL, -- bit 1 - chn, bit 2 - math, bit 3 - extra cur, bit 4 - other
    active                  BOOLEAN, -- true: active, false: inactive
    biography               TEXT,
    biography_approved      BOOLEAN, -- true: approved, false: not approved
    biography_approved_by   INTEGER REFERENCES ischool_user(id),
    biography_approved_date DATE,
    record_ts               TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

--CREATE UNIQUE INDEX ischool_teacher_idx1 ON ischool_teacher (school_id, user_id);
CREATE UNIQUE INDEX ischool_teacher_idx1 ON ischool_teacher (user_id);

--------------------------------------------------------------------------------
-- Create table ischool_user_role

CREATE SEQUENCE ischool_user_role_seq;
CREATE TABLE    ischool_user_role (
    id              INTEGER PRIMARY KEY DEFAULT nextval('ischool_user_role_seq') NOT NULL,
    user_id         INTEGER NOT NULL REFERENCES ischool_user(id),
    role_id         SMALLINT REFERENCES ischool_role(id),
    privileges      VARCHAR(2000), -- object permissions in xml
    record_ts       TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
-- Create ischool_course_type table
-- +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
CREATE TABLE    ischool_course_type (
    id          SMALLINT PRIMARY KEY NOT NULL,
    name        VARCHAR(30) NOT NULL,
    record_ts   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);


CREATE UNIQUE INDEX ischool_user_role_idx1 ON ischool_user_role (user_id, role_id);
CREATE UNIQUE INDEX ischool_user_role_idx2 ON ischool_user_role (role_id, user_id);
--------------------------------------------------------------------------------
-- Create table ischool_course

CREATE SEQUENCE ischool_course_seq;
CREATE TABLE    ischool_course  (
    id                      INTEGER  PRIMARY KEY DEFAULT nextval('ischool_course_seq') NOT NULL,
    school_id               SMALLINT NOT NULL REFERENCES ischool_setup(id),
    alias                   VARCHAR(30), -- maps to course_id, it's better no to have space
    name                    VARCHAR(50),
    cn_name                 VARCHAR(50), -- Chinese name
    location                VARCHAR(50),
    teacher_id              INTEGER REFERENCES ischool_teacher(id),
    tid                     VARCHAR(30),
    taid                    VARCHAR(30),
    capacity                INTEGER,
    active                  BOOLEAN, -- true: active, false: inactive
    start_date              DATE,
    end_date                DATE,
    begin_time              TIMESTAMP,
    end_time                TIMESTAMP,
    tuition                 FLOAT,
    other_fee               FLOAT, -- tuition2
    other_fee_description   VARCHAR(50),
    show_tuition_public     BOOLEAN DEFAULT TRUE, -- true: show tuition to public, false: not to show tuition to public
    classDay                CHAR(3), -- SAT:Saturday, SUN:Sunday
    syllabus_path           VARCHAR(200), -- save syllabus path and name for external syllabus files (.pdf, .doc, etc.)
    syllabus                TEXT, -- save syllabus content with html format
    syllabus_approved       BOOLEAN, -- true: approved, false: not approved
    syllabus_approved_by    INTEGER REFERENCES ischool_user(id),
    syllabus_approved_date  DATE,
    course_type             SMALLINT NULL REFERENCES ischool_course_type(id),
    record_ts               TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

--ALTER TABLE ischool_course DROP CONSTRAINT ischool_course_alias_key;
CREATE        INDEX ischool_course_idx1 ON ischool_course (school_id, active);
CREATE UNIQUE INDEX ischool_course_idx2 ON ischool_course (school_id, alias);


-- +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
-- Create ischool_payment_type table
-- +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
CREATE TABLE    ischool_payment_type (
    id          SMALLINT PRIMARY KEY NOT NULL,
    name        VARCHAR(30) NOT NULL,
    comment     VARCHAR(255),
    record_ts   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);


--------------------------------------------------------------------------------
-- Create table ischool_payment

CREATE SEQUENCE ischool_payment_seq;
CREATE TABLE ischool_payment (
    id              INTEGER PRIMARY KEY DEFAULT nextval('ischool_payment_seq') NOT NULL,
    school_id       SMALLINT NOT NULL REFERENCES ischool_setup(id),
    account         INTEGER NOT NULL REFERENCES ischool_account(id),
    receipt_number  VARCHAR(12),
    check_number    VARCHAR(12),
    amount          FLOAT NOT NULL,
    payment_type    SMALLINT NOT NULL REFERENCES ischool_payment_type(id),
    cashier_name    VARCHAR(24) NOT NULL,
    day_paid        TIMESTAMP NOT NULL,
    memo            VARCHAR(255),
    break_down      BOOLEAN, -- true if it is a break down item, not to be counted again in total
    record_ts       TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX ischool_payment_idx1 ON ischool_payment (school_id, account);
CREATE INDEX ischool_payment_idx2 ON ischool_payment (school_id);
CREATE INDEX ischool_payment_idx3 ON ischool_payment (memo);

--------------------------------------------------------------------------------
-- Create table ischool_course_reg

CREATE SEQUENCE ischool_course_reg_seq;
CREATE TABLE ischool_course_reg (
    id              INTEGER PRIMARY KEY DEFAULT nextval('ischool_course_reg_seq') NOT NULL,
    student_id      INTEGER REFERENCES ischool_user(id),
    course_id       INTEGER REFERENCES ischool_course(id),
    reg_date        TIMESTAMP,
    day_paid        TIMESTAMP, -- NULL if not paid
    wait_pool       BOOLEAN,
    record_ts       TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE UNIQUE INDEX ischool_course_reg_idx1 ON ischool_course_reg (student_id, course_id);

--------------------------------------------------------------------------------
-- Create table ischool_course_reg_log

CREATE SEQUENCE ischool_course_reg_log_seq;
CREATE TABLE ischool_course_reg_log (
    id              INTEGER PRIMARY KEY DEFAULT nextval('ischool_course_reg_log_seq') NOT NULL,
    student_id      INTEGER REFERENCES ischool_user(id),
    course_id       INTEGER REFERENCES ischool_course(id),
    action          CHAR(1), -- 'E' enroll, 'U' - un-enroll
    wait_pool       BOOLEAN,
    record_ts       TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE UNIQUE INDEX ischool_course_reg_log_idx1 ON ischool_course_reg_log (student_id, course_id);

--------------------------------------------------------------------------------
-- Create table ischool_email_log

CREATE SEQUENCE ischool_email_log_seq;
CREATE TABLE ischool_email_log (
    id              INTEGER PRIMARY KEY DEFAULT nextval('ischool_email_log_seq') NOT NULL,
    user_id         INTEGER NOT NULL REFERENCES ischool_user(id),
    mail_from       VARCHAR(150),
    subject         VARCHAR(150),
    mail_to         TEXT,
    cc              TEXT,
    bcc             TEXT,
    body            TEXT,
    attachment      BYTEA,
    record_ts       TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

--------------------------------------------------------------------------------
-- Create table ischool_news

CREATE SEQUENCE ischool_news_seq;
CREATE TABLE ischool_news (
    id              INTEGER PRIMARY KEY DEFAULT nextval('ischool_news_seq') NOT NULL,
    news_date       DATE NOT NULL,
    subject         VARCHAR(255) NOT NULL,
    content         TEXT,
    active          BOOLEAN, -- true: active, false: inactive
    record_ts       TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX ischool_news_idx1 ON ischool_news (news_date DESC);
--------------------------------------------------------------------------------
-- Create table ischool_lost_n_found

CREATE SEQUENCE ischool_lost_n_found_seq;
CREATE TABLE ischool_lost_n_found (
    id              INTEGER PRIMARY KEY DEFAULT nextval('ischool_lost_n_found_seq') NOT NULL,
    item_date       DATE NOT NULL,
    subject         VARCHAR(255) NOT NULL,
    content         TEXT,
    active          BOOLEAN, -- true: active, false: inactive
    record_ts       TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX ischool_lost_n_found_idx1 ON ischool_lost_n_found (item_date DESC);
--------------------------------------------------------------------------------
-- Create table ischool_calendar

CREATE SEQUENCE ischool_calendar_seq;
CREATE TABLE ischool_calendar (
    id              INTEGER PRIMARY KEY DEFAULT nextval('ischool_calendar_seq') NOT NULL,
    school_id       SMALLINT NOT NULL REFERENCES ischool_setup(id),
    calendar_date   DATE,
    subject         VARCHAR(1000),
    no_school_day   BOOLEAN DEFAULT FALSE,
    record_ts       TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

--------------------------------------------------------------------------------
-- Create table ischool_vote

CREATE SEQUENCE ischool_vote_seq;
CREATE TABLE ischool_vote (
    id              INTEGER PRIMARY KEY DEFAULT nextval('ischool_vote_seq') NOT NULL,
    creation_date   DATE NOT NULL,
    description     VARCHAR(255),
    question_type   CHAR(1), -- 'R' - radio button, select one answer only; 'C' - checkbox, multiple answers allowed
    user_type       CHAR(1), -- 'A' - by account, one account one vote; 'I' - individual, one person one vote
    active_account  BOOLEAN NULL, -- true: if reqiure this account register current school; false: does not require current school registration
    vote_once       BOOLEAN NULL, -- true: only allow vote once between vote start / close dates; false - allow update vote between vote start / close dates
    required        BOOLEAN NULL, -- true: vote is mandatory before course enrollment is allowed
    question_a      VARCHAR(512) NULL,
    question_b      VARCHAR(512) NULL,
    question_c      VARCHAR(512) NULL,
    question_d      VARCHAR(512) NULL,
    question_e      VARCHAR(512) NULL,
    question_f      VARCHAR(512) NULL,
    write_in        BOOLEAN, -- true: to allow one write in; false: no write in
    comments        VARCHAR(1024) NULL,
    vote_open_date  DATE,
    vote_close_date DATE,
    active          BOOLEAN, -- true: active, false: inactive
    record_ts       TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX ischool_vote_idx1 ON ischool_vote (active);
--------------------------------------------------------------------------------
-- Create table ischool_vote_result

CREATE SEQUENCE ischool_vote_result_seq;
CREATE TABLE ischool_vote_result (
    id              INTEGER PRIMARY KEY DEFAULT nextval('ischool_vote_result_seq') NOT NULL,
    vote_id         INTEGER NOT NULL REFERENCES ischool_vote(id),
    account_id      INTEGER NOT NULL REFERENCES ischool_account(id),
    user_id         INTEGER NOT NULL REFERENCES ischool_user(id),
    answer_a        BOOLEAN,
    answer_b        BOOLEAN,
    answer_c        BOOLEAN,
    answer_d        BOOLEAN,
    answer_e        BOOLEAN,
    answer_f        BOOLEAN,
    write_in        VARCHAR(512), -- the write in value when ischool_vote::write_in is true
    record_ts       TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX ischool_vote_result_idx1 ON ischool_vote_result (account_id);

--------------------------------------------------------------------------------
-- Create ischool_duty_type table

CREATE TABLE    ischool_duty_type (
    id                      SMALLINT PRIMARY KEY NOT NULL,
    alias                   VARCHAR(10) NOT NULL,
    name                    VARCHAR(30) NOT NULL,
    time_sign_up_allowed    VARCHAR(120),
    parent_duty             BOOLEAN NOT NULL DEFAULT TRUE,
    active                  BOOLEAN, -- true: this duty type is active, false: inactive
    record_ts               TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

--------------------------------------------------------------------------------
-- Create table ischool_duty (parent duty)

CREATE SEQUENCE ischool_duty_seq;
CREATE TABLE ischool_duty (
    id              INTEGER PRIMARY KEY DEFAULT nextval('ischool_duty_seq') NOT NULL,
    school_id       SMALLINT NOT NULL REFERENCES ischool_setup(id),
    calendar_id     SMALLINT NOT NULL REFERENCES ischool_calendar(id),
    begin_time      TIMESTAMP,
    end_time        TIMESTAMP,
    account_id      INTEGER NOT NULL REFERENCES ischool_account(id),
    user_id         INTEGER NOT NULL REFERENCES ischool_user(id),
    duty_type_id    SMALLINT NOT NULL REFERENCES ischool_duty_type(id),
    reminder_sent   BOOLEAN DEFAULT FALSE, -- true: emial reminder has been sent
    record_ts       TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX ischool_duty_idx1 ON ischool_duty (school_id);
CREATE INDEX ischool_duty_idx2 ON ischool_duty (school_id, calendar_id);
CREATE INDEX ischool_duty_idx3 ON ischool_duty (school_id, account_id);

-- +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
-- Generic page
-- +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
--------------------------------------------
-- Create table ischool_generic_page

CREATE SEQUENCE ischool_generic_page_seq;
CREATE TABLE ischool_generic_page (
    id              SMALLINT PRIMARY KEY DEFAULT nextval('ischool_generic_page_seq') NOT NULL,
    page_key        VARCHAR(30) NOT NULL, -- sample keys: homepage, administration, about.us, contact.us
    publish_date    DATE  NOT NULL,
    subject         VARCHAR(255),
    show_subject    BOOLEAN, -- true: show subject at the center, false: do not show the subject
    content         TEXT,
    active          BOOLEAN, -- true: this page is active, false: this page is inactive. Note: only one generic page for one key can be active
    record_ts       TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX ischool_generic_page_idx1 ON ischool_generic_page (page_key, active DESC);

--------------------------------------------------------------------------------
-- Create table ischool_volunteer_service

CREATE SEQUENCE ischool_volunteer_service_seq;
CREATE TABLE ischool_volunteer_service (
    id              INTEGER PRIMARY KEY DEFAULT nextval('ischool_volunteer_service_seq') NOT NULL,
    school_id       SMALLINT NOT NULL REFERENCES ischool_setup(id),
    student_id      INTEGER NOT NULL REFERENCES ischool_user(id),
    service_date    TIMESTAMP NOT NULL,
    service_hours   FLOAT NOT NULL,
    duty_type_id    SMALLINT NOT NULL REFERENCES ischool_duty_type(id),
    note            VARCHAR(255),
    credit          BOOLEAN, -- true: credit has been entered to the account
    record_ts       TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX ischool_volu_service_idx1 ON ischool_volunteer_service (school_id);
CREATE INDEX ischool_volu_service_idx2 ON ischool_volunteer_service (school_id, student_id);
CREATE INDEX ischool_volu_service_idx3 ON ischool_volunteer_service (school_id, student_id, service_date);

--------------------------------------------------------------------------------
-- Create table ischool_contact_us_category

CREATE SEQUENCE ischool_contact_us_category_seq;
CREATE TABLE    ischool_contact_us_category (
    id          SMALLINT PRIMARY KEY DEFAULT nextval('ischool_contact_us_category_seq') NOT NULL,
    category    VARCHAR(30),
    email       VARCHAR(250) NOT NULL,
    ordinal     SMALLINT,
    record_ts   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX ischool_category_idx1 ON ischool_contact_us_category (ordinal);

--------------------------------------------------------------------------------
-- Create table ischool_menu

CREATE SEQUENCE ischool_menu_seq;
CREATE TABLE    ischool_menu (
    id              INTEGER PRIMARY KEY DEFAULT nextval('ischool_menu_seq') NOT NULL,
    menu_group      VARCHAR(50) NOT NULL, -- latestLinks, account, etc.
    menu_item       VARCHAR(50) NOT NULL, -- menu description
    parent_menu     VARCHAR(50) NOT NULL,
    menu_location   CHAR(1) NOT NULL, -- 'L' - left, 'T' - top, 'R' - right
    target_class    VARCHAR(200) NOT NULL, -- target page class name or target page id
    panel_name      VARCHAR(30), -- subpage id   
    ordinal         SMALLINT,
    access_role     VARCHAR(200), -- comma separated role alias list, if null the menu can be accessed by anyone 
    active          BOOLEAN DEFAULT true, -- true: active, false: inactive
    record_ts       TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX ischool_menu_idx1 ON ischool_menu (menu_group, parent_menu, ordinal);

--------------------------------------------------------------------------------
--                      ischool_dml.sql
--------------------------------------------------------------------------------
START TRANSACTION;
INSERT INTO ischool_menu (menu_group, menu_item, parent_menu, menu_location, target_class, panel_name, ordinal, access_role) VALUES ('latestLinks', 'News 学校最新动态', '', 'T', 'org.moonwave.ischool.wicket.page.GenericPublicPage', 'news', 1, null);
INSERT INTO ischool_menu (menu_group, menu_item, parent_menu, menu_location, target_class, panel_name, ordinal, access_role) VALUES ('latestLinks', 'Newsletter 学校简讯', '', 'T', 'org.moonwave.ischool.wicket.page.GenericPublicPage', 'newsletter', 2, null);
INSERT INTO ischool_menu (menu_group, menu_item, parent_menu, menu_location, target_class, panel_name, ordinal, access_role) VALUES ('latestLinks', 'Calendar 本学期校历', '', 'T', 'org.moonwave.ischool.wicket.page.GenericPublicPage', 'calendar', 3, null);
INSERT INTO ischool_menu (menu_group, menu_item, parent_menu, menu_location, target_class, panel_name, ordinal, access_role) VALUES ('latestLinks', 'Word List 生字表', '', 'T', 'org.moonwave.ischool.wicket.page.GenericPublicPage', 'wordlist', 4, null);
INSERT INTO ischool_menu (menu_group, menu_item, parent_menu, menu_location, target_class, panel_name, ordinal, access_role) VALUES ('latestLinks', 'Lost and Found', '', 'T', 'org.moonwave.ischool.wicket.page.GenericPublicPage', 'lostNFound', 5, null);
INSERT INTO ischool_menu (menu_group, menu_item, parent_menu, menu_location, target_class, panel_name, ordinal, access_role) VALUES ('latestLinks', 'School Location 校址', '', 'T', 'org.moonwave.ischool.wicket.page.GenericPublicPage', 'schoolLocation', 6, null);
INSERT INTO ischool_menu (menu_group, menu_item, parent_menu, menu_location, target_class, panel_name, ordinal, access_role) VALUES ('latestLinks', 'Class Schedule 课表', '', 'T', 'org.moonwave.ischool.wicket.page.open.CourseLandscapeView', 'classSchedule', 7, null);
INSERT INTO ischool_menu (menu_group, menu_item, parent_menu, menu_location, target_class, panel_name, ordinal, access_role) VALUES ('latestLinks', 'Classrooms 教室', '', 'T', 'org.moonwave.ischool.wicket.page.GenericPublicPage', 'classrooms', 8, null);
INSERT INTO ischool_menu (menu_group, menu_item, parent_menu, menu_location, target_class, panel_name, ordinal, access_role) VALUES ('latestLinks', 'Clean Classroom 清洁', '', 'T', 'org.moonwave.ischool.wicket.page.GenericPublicPage', 'cleanClassroom', 9, null);
INSERT INTO ischool_menu (menu_group, menu_item, parent_menu, menu_location, target_class, panel_name, ordinal, access_role) VALUES ('latestLinks', 'Donation 捐款', '', 'T', 'org.moonwave.ischool.wicket.page.GenericPublicPage', 'donation', 10, null);
INSERT INTO ischool_menu (menu_group, menu_item, parent_menu, menu_location, target_class, panel_name, ordinal, access_role) VALUES ('latestLinks', 'VIP Card 优惠卡', '', 'T', 'org.moonwave.ischool.wicket.page.GenericPublicPage', 'vipCard', 11, null);
INSERT INTO ischool_menu (menu_group, menu_item, parent_menu, menu_location, target_class, panel_name, ordinal, access_role) VALUES ('latestLinks', 'MCC Form', '', 'T', 'org.moonwave.ischool.wicket.page.GenericPublicPage', 'mccForm', 12, null);
INSERT INTO ischool_menu (menu_group, menu_item, parent_menu, menu_location, target_class, panel_name, ordinal, access_role) VALUES ('latestLinks', 'FAQ', '', 'T', 'org.moonwave.ischool.wicket.page.GenericPublicPage', 'faq', 13, null);

INSERT INTO ischool_menu (menu_group, menu_item, parent_menu, menu_location, target_class, panel_name, ordinal, access_role) VALUES ('gardenList', 'Student Essays 学生作文', '', 'T', 'org.moonwave.ischool.wicket.page.GenericPublicPage', 'studentEssays', 1, null);
INSERT INTO ischool_menu (menu_group, menu_item, parent_menu, menu_location, target_class, panel_name, ordinal, access_role) VALUES ('gardenList', 'Teacher Articles 教师文章', '', 'T', 'org.moonwave.ischool.wicket.page.GenericPublicPage', 'teacherArticles', 2, null);

INSERT INTO ischool_menu (menu_group, menu_item, parent_menu, menu_location, target_class, panel_name, ordinal, access_role) VALUES ('courses', 'Course List 课程表', '', 'T', 'org.moonwave.ischool.wicket.page.GenericPublicPage', 'course', 1, null);
INSERT INTO ischool_menu (menu_group, menu_item, parent_menu, menu_location, target_class, panel_name, ordinal, access_role) VALUES ('courses', 'Admissions 招生', '', 'T', 'org.moonwave.ischool.wicket.page.GenericPublicPage', 'admissions', 2, null);

INSERT INTO ischool_menu (menu_group, menu_item, parent_menu, menu_location, target_class, panel_name, ordinal, access_role) VALUES ('resources', 'Dictionary 中文字词典', '', 'T', 'org.moonwave.ischool.wicket.page.GenericPublicPage', 'dictionary', 1, null);
INSERT INTO ischool_menu (menu_group, menu_item, parent_menu, menu_location, target_class, panel_name, ordinal, access_role) VALUES ('resources', 'Chinese Online 在线学中文', '', 'T', 'org.moonwave.ischool.wicket.page.GenericPublicPage', 'chineseOnline', 2, null);
INSERT INTO ischool_menu (menu_group, menu_item, parent_menu, menu_location, target_class, panel_name, ordinal, access_role) VALUES ('resources', 'Chirography 中文书法，笔划', '', 'T', 'org.moonwave.ischool.wicket.page.GenericPublicPage', 'chirography', 3, null);
INSERT INTO ischool_menu (menu_group, menu_item, parent_menu, menu_location, target_class, panel_name, ordinal, access_role) VALUES ('resources', 'Library 华星书屋', '', 'T', 'org.moonwave.ischool.wicket.page.GenericPublicPage', 'library', 4, null);

INSERT INTO ischool_menu (menu_group, menu_item, parent_menu, menu_location, target_class, panel_name, ordinal, access_role) VALUES ('accounts', 'Create a new Account 新用户', '', 'T', 'org.moonwave.ischool.wicket.page.account.NewAccount', '', 1, null);
INSERT INTO ischool_menu (menu_group, menu_item, parent_menu, menu_location, target_class, panel_name, ordinal, access_role) VALUES ('accounts', 'Forgot Password 忘记密码', '', 'T', 'org.moonwave.ischool.wicket.page.account.ForgotPassword', '', 2, null);
INSERT INTO ischool_menu (menu_group, menu_item, parent_menu, menu_location, target_class, panel_name, ordinal, access_role) VALUES ('accounts', 'Unsubscribe', '', 'T', 'org.moonwave.ischool.wicket.page.account.Unsubscribe', null, 3, null);

INSERT INTO ischool_menu (menu_group, menu_item, parent_menu, menu_location, target_class, panel_name, ordinal, access_role) VALUES ('aboutus', 'About School 学校', '', 'T', 'org.moonwave.ischool.wicket.page.GenericPublicPage', 'aboutSchool', 1, null);
INSERT INTO ischool_menu (menu_group, menu_item, parent_menu, menu_location, target_class, panel_name, ordinal, access_role) VALUES ('aboutus', 'Board of Directors 董事会', '', 'T', 'org.moonwave.ischool.wicket.page.GenericPublicPage', 'schoolBoard', 2, null);
INSERT INTO ischool_menu (menu_group, menu_item, parent_menu, menu_location, target_class, panel_name, ordinal, access_role) VALUES ('aboutus', 'Administration 校委会', '', 'T', 'org.moonwave.ischool.wicket.page.GenericPublicPage', 'administrationTeam', 3, null);
INSERT INTO ischool_menu (menu_group, menu_item, parent_menu, menu_location, target_class, panel_name, ordinal, access_role) VALUES ('aboutus', 'Student Council 学生会', '', 'T', 'org.moonwave.ischool.wicket.page.GenericPublicPage', 'studentCouncil', 4, null);
INSERT INTO ischool_menu (menu_group, menu_item, parent_menu, menu_location, target_class, panel_name, ordinal, access_role) VALUES ('aboutus', 'PTA 家长教师协会', '', 'T', 'org.moonwave.ischool.wicket.page.GenericPublicPage', 'pta', 5, null);
INSERT INTO ischool_menu (menu_group, menu_item, parent_menu, menu_location, target_class, panel_name, ordinal, access_role) VALUES ('aboutus', 'Contact Us 联系我们', '', 'T', 'org.moonwave.ischool.wicket.page.open.ContactUs', 'contactUs', 6, null);

INSERT INTO ischool_menu (menu_group, menu_item, parent_menu, menu_location, target_class, panel_name, ordinal, access_role) VALUES ('account', 'My Account', '', 'L', 'org.moonwave.ischool.wicket.page.account.MyAccount', null, 1, null);
INSERT INTO ischool_menu (menu_group, menu_item, parent_menu, menu_location, target_class, panel_name, ordinal, access_role) VALUES ('account', 'My Service', '', 'L', 'org.moonwave.ischool.wicket.page.admin.volunteerservice.VolunteerSetupHolder', null, 2, null);
INSERT INTO ischool_menu (menu_group, menu_item, parent_menu, menu_location, target_class, panel_name, ordinal, access_role) VALUES ('account', 'My Receipt', '', 'L', 'org.moonwave.ischool.wicket.page.account.receipt.MyReceipt', null, 3, null);
INSERT INTO ischool_menu (menu_group, menu_item, parent_menu, menu_location, target_class, panel_name, ordinal, access_role) VALUES ('account', 'My Vote', '', 'L', 'org.moonwave.ischool.wicket.page.account.vote.MyVote', null, 4, null);
INSERT INTO ischool_menu (menu_group, menu_item, parent_menu, menu_location, target_class, panel_name, ordinal, access_role) VALUES ('account', 'Parent Duty', '', 'L', 'org.moonwave.ischool.wicket.page.account.duty.ParentDuty', null, 5, null);
INSERT INTO ischool_menu (menu_group, menu_item, parent_menu, menu_location, target_class, panel_name, ordinal, access_role) VALUES ('account', 'Enrollment', '', 'L', 'org.moonwave.ischool.wicket.page.enroll.Enrollment', null, 6, null);
INSERT INTO ischool_menu (menu_group, menu_item, parent_menu, menu_location, target_class, panel_name, ordinal, access_role) VALUES ('account', 'Change Password', '', 'L', 'org.moonwave.ischool.wicket.page.GenericPage', 'changePassword', 7, null);

INSERT INTO ischool_menu (menu_group, menu_item, parent_menu, menu_location, target_class, panel_name, ordinal, access_role) VALUES ('userAdmin', 'Active Users', '', 'L', 'org.moonwave.ischool.wicket.page.admin.user.UserAdmin', null, 1, 'cashier,vp,vpr,superuser,principal');
INSERT INTO ischool_menu (menu_group, menu_item, parent_menu, menu_location, target_class, panel_name, ordinal, access_role) VALUES ('userAdmin', 'All Users', '', 'L', 'org.moonwave.ischool.wicket.page.admin.user.UserAdmin4AllUsers', null, 2, 'cashier,vp,vpr,superuser,principal');

INSERT INTO ischool_menu (menu_group, menu_item, parent_menu, menu_location, target_class, panel_name, ordinal, access_role) VALUES ('schoolConfig', 'School Setup', '', 'L', 'org.moonwave.ischool.wicket.page.admin.school.SchoolSetup', null, 1, 'vp,vpr,superuser,principal');
INSERT INTO ischool_menu (menu_group, menu_item, parent_menu, menu_location, target_class, panel_name, ordinal, access_role) VALUES ('schoolConfig', 'Copy Courses', '', 'L', 'org.moonwave.ischool.wicket.page.admin.course.CopyCourses', null, 2, 'vp,vpr,superuser,principal');
INSERT INTO ischool_menu (menu_group, menu_item, parent_menu, menu_location, target_class, panel_name, ordinal, access_role) VALUES ('schoolConfig', 'Teacher Setup', '', 'L', 'org.moonwave.ischool.wicket.page.admin.teacher.TeacherSetup', null, 3, 'vp,vpr,superuser,principal');
INSERT INTO ischool_menu (menu_group, menu_item, parent_menu, menu_location, target_class, panel_name, ordinal, access_role) VALUES ('schoolConfig', 'Course Setup', '', 'L', 'org.moonwave.ischool.wicket.page.admin.course.CourseSetup', null, 4, 'vp,vpr,superuser,principal');

INSERT INTO ischool_menu (menu_group, menu_item, parent_menu, menu_location, target_class, panel_name, ordinal, access_role) VALUES ('tools', 'Rich Text Email', '', 'L', 'org.moonwave.ischool.wicket.page.admin.email.Email', 'richEmail', 1, 'teacher,teacherLead,board,vp,vpr,superuser,principal');
INSERT INTO ischool_menu (menu_group, menu_item, parent_menu, menu_location, target_class, panel_name, ordinal, access_role) VALUES ('tools', 'Plain Text Email', '', 'L', 'org.moonwave.ischool.wicket.page.admin.email.Email', 'plainEmail', 2, 'teacher,teacherLead,board,vp,vpr,superuser,principal');
INSERT INTO ischool_menu (menu_group, menu_item, parent_menu, menu_location, target_class, panel_name, ordinal, access_role) VALUES ('tools', 'My Classes', '', 'L', 'org.moonwave.ischool.wicket.page.GenericPage', 'myClasses', 3, 'teacher');
INSERT INTO ischool_menu (menu_group, menu_item, parent_menu, menu_location, target_class, panel_name, ordinal, access_role) VALUES ('tools', 'GenericPageSetup', '', 'L', 'org.moonwave.ischool.wicket.page.tool.genericpage.GenericPageSetup', 'genericPageSetup', 4, 'staff,vp,vpr,superuser,principal');
INSERT INTO ischool_menu (menu_group, menu_item, parent_menu, menu_location, target_class, panel_name, ordinal, access_role) VALUES ('tools', 'News Setup', '', 'L', 'org.moonwave.ischool.wicket.page.tool.news.NewsSetup', 'newsSetup', 5, 'staff,vp,vpr,superuser,principal');
INSERT INTO ischool_menu (menu_group, menu_item, parent_menu, menu_location, target_class, panel_name, ordinal, access_role) VALUES ('tools', 'Vote Setup', '', 'L', 'org.moonwave.ischool.wicket.page.tool.vote.VoteSetup', 'voteSetup', 6, 'vp,vpr,superuser,principal');
INSERT INTO ischool_menu (menu_group, menu_item, parent_menu, menu_location, target_class, panel_name, ordinal, access_role) VALUES ('tools', 'CalendarSetup', '', 'L', 'org.moonwave.ischool.wicket.page.tool.calendar.CalendarSetup', 'calendarSetup', 7, 'vp,vpr,superuser,principal');
INSERT INTO ischool_menu (menu_group, menu_item, parent_menu, menu_location, target_class, panel_name, ordinal, access_role) VALUES ('tools', 'Lost and Found Setup', '', 'L', 'org.moonwave.ischool.wicket.page.tool.lostnfound.LostNFoundSetup', null, 8, 'staff,vp,vpr,superuser,principal');
INSERT INTO ischool_menu (menu_group, menu_item, parent_menu, menu_location, target_class, panel_name, ordinal, access_role) VALUES ('tools', 'Volunteer Service', '', 'L', 'org.moonwave.ischool.wicket.page.GenericPage', 'volunteerService', 9, 'staff,vp,vpr,superuser,principal');
INSERT INTO ischool_menu (menu_group, menu_item, parent_menu, menu_location, target_class, panel_name, ordinal, access_role) VALUES ('tools', 'Course Summary', '', 'L', 'org.moonwave.ischool.wicket.page.GenericPage', 'courseSummary', 10, 'vp,vpr,superuser,principal');
INSERT INTO ischool_menu (menu_group, menu_item, parent_menu, menu_location, target_class, panel_name, ordinal, access_role) VALUES ('tools', 'Payment Summary', '', 'L', 'org.moonwave.ischool.wicket.page.GenericPage', 'paymentSummary', 11, 'vp,vpr,superuser,principal');
INSERT INTO ischool_menu (menu_group, menu_item, parent_menu, menu_location, target_class, panel_name, ordinal, access_role) VALUES ('tools', 'Payment Search', '', 'L', 'org.moonwave.ischool.wicket.page.PaymentSearch', 'paymentSearch', 12, 'vp,vpr,superuser,principal');
INSERT INTO ischool_menu (menu_group, menu_item, parent_menu, menu_location, target_class, panel_name, ordinal, access_role) VALUES ('tools', 'Paid Students', '', 'L', 'org.moonwave.ischool.wicket.page.GenericPage', 'paidStudent', 13, 'vp,vpr,superuser,principal');
INSERT INTO ischool_menu (menu_group, menu_item, parent_menu, menu_location, target_class, panel_name, ordinal, access_role) VALUES ('tools', 'New Students', '', 'L', 'org.moonwave.ischool.wicket.page.GenericPage', 'newStudent', 14, 'vp,vpr,superuser,principal');
INSERT INTO ischool_menu (menu_group, menu_item, parent_menu, menu_location, target_class, panel_name, ordinal, access_role) VALUES ('tools', 'Dropped Students', '', 'L', 'org.moonwave.ischool.wicket.page.GenericPage', 'droppedStudent', 15, 'vp,vpr,superuser,principal');
INSERT INTO ischool_menu (menu_group, menu_item, parent_menu, menu_location, target_class, panel_name, ordinal, access_role) VALUES ('tools', 'All Students', '', 'L', 'org.moonwave.ischool.wicket.page.GenericPage', 'student', 16, 'vp,vpr,superuser,principal');
INSERT INTO ischool_menu (menu_group, menu_item, parent_menu, menu_location, target_class, panel_name, ordinal, access_role) VALUES ('tools', 'Class Rosters', '', 'L', 'org.moonwave.ischool.wicket.page.GenericPage', 'classRosters', 17, 'vp,vpr,superuser,principal');
INSERT INTO ischool_menu (menu_group, menu_item, parent_menu, menu_location, target_class, panel_name, ordinal, access_role) VALUES ('tools', 'Class Rosters w/ Payment Info', '', 'L', 'org.moonwave.ischool.wicket.page.GenericPage', 'classRostersPayment', 18, 'vp,vpr,superuser,principal');
INSERT INTO ischool_menu (menu_group, menu_item, parent_menu, menu_location, target_class, panel_name, ordinal, access_role) VALUES ('tools', 'Clear Wait Pool', '', 'L', 'org.moonwave.ischool.wicket.page.GenericPage', 'clearWaitPool', 19, 'vp,vpr,superuser,principal');
INSERT INTO ischool_menu (menu_group, menu_item, parent_menu, menu_location, target_class, panel_name, ordinal, access_role) VALUES ('tools', 'Balance Transfer', '', 'L', 'org.moonwave.ischool.wicket.page.GenericPage', 'balanceTransfer', 20, 'vp,vpr,superuser,principal');

INSERT INTO ischool_menu (menu_group, menu_item, parent_menu, menu_location, target_class, panel_name, ordinal, access_role) VALUES ('miscLink', 'System Message', '', 'L', 'org.moonwave.ischool.wicket.page.admin.systeminfo.SystemInfoSetup', null, 1, null);
INSERT INTO ischool_menu (menu_group, menu_item, parent_menu, menu_location, target_class, panel_name, ordinal, access_role) VALUES ('miscLink', 'User Role', '', 'L', 'org.moonwave.ischool.wicket.page.admin.userrole.UserRoleConfig', null, 2, null);
INSERT INTO ischool_menu (menu_group, menu_item, parent_menu, menu_location, target_class, panel_name, ordinal, access_role) VALUES ('miscLink', 'SQL Query', '', 'L', 'org.moonwave.ischool.wicket.page.admin.sql.SqlQuery', null, 3, null);
INSERT INTO ischool_menu (menu_group, menu_item, parent_menu, menu_location, target_class, panel_name, ordinal, access_role) VALUES ('miscLink', 'SQL Command', '', 'L', 'org.moonwave.ischool.wicket.page.GenericPage', 'sqlCommand', 4, null);
INSERT INTO ischool_menu (menu_group, menu_item, parent_menu, menu_location, target_class, panel_name, ordinal, access_role) VALUES ('miscLink', 'Groovy', '', 'L', 'org.moonwave.ischool.wicket.page.admin.groovy.GroovyConsole', null, 5, null);
INSERT INTO ischool_menu (menu_group, menu_item, parent_menu, menu_location, target_class, panel_name, ordinal, access_role) VALUES ('miscLink', 'JBoss JMX', '', 'L', 'org.moonwave.ischool.wicket.page.admin.jboss.JbossJmx', null, 6, null);
INSERT INTO ischool_menu (menu_group, menu_item, parent_menu, menu_location, target_class, panel_name, ordinal, access_role) VALUES ('miscLink', 'Log Viewer', '', 'L', 'org.moonwave.ischool.wicket.page.admin.jboss.LogViewer', null, 7, null);
INSERT INTO ischool_menu (menu_group, menu_item, parent_menu, menu_location, target_class, panel_name, ordinal, access_role) VALUES ('miscLink', 'SQL Query', '', 'L', 'org.moonwave.ischool.wicket.page.admin.userrole.UserRoleConfig', null, 8, null);
INSERT INTO ischool_menu (menu_group, menu_item, parent_menu, menu_location, target_class, panel_name, ordinal, access_role) VALUES ('miscLink', 'Launch DConfig Editor', '', 'L', '', null, 9, null);
INSERT INTO ischool_menu (menu_group, menu_item, parent_menu, menu_location, target_class, panel_name, ordinal, access_role) VALUES ('miscLink', 'Reload DConfig Data', '', 'L', 'org.moonwave.ischool.wicket.page.GenericPage', 'reloadDConfigData', 10, null);
INSERT INTO ischool_menu (menu_group, menu_item, parent_menu, menu_location, target_class, panel_name, ordinal, access_role) VALUES ('miscLink', 'Reload Ischool Cache', '', 'L', 'org.moonwave.ischool.wicket.page.GenericPage', 'reloadIschoolCache', 11, null);
INSERT INTO ischool_menu (menu_group, menu_item, parent_menu, menu_location, target_class, panel_name, ordinal, access_role, active) VALUES ('miscLink', 'Data Import', '', 'L', 'org.moonwave.ischool.wicket.page.admin.imports.DataImport', null, 12, null, false);
INSERT INTO ischool_menu (menu_group, menu_item, parent_menu, menu_location, target_class, panel_name, ordinal, access_role, active) VALUES ('miscLink', 'File Upload', '', 'L', 'org.apache.wicket.examples.upload.FileUploadEx', null, 13, null, false);
INSERT INTO ischool_menu (menu_group, menu_item, parent_menu, menu_location, target_class, panel_name, ordinal, access_role, active) VALUES ('miscLink', 'Test', '', 'L', 'org.apache.wicket.examples.captcha.Test', null, 14, null, true);

COMMIT;

START TRANSACTION;
INSERT INTO ischool_role (id, alias, name) VALUES (1, 'student', 'Student');
INSERT INTO ischool_role (id, alias, name) VALUES (5, 'teacher', 'School Teacher');
INSERT INTO ischool_role (id, alias, name) VALUES (10, 'staff', 'School Staff');
INSERT INTO ischool_role (id, alias, name) VALUES (15, 'board', 'Board Member');
INSERT INTO ischool_role (id, alias, name) VALUES (18, 'teacherLead', 'Teacher Lead');
INSERT INTO ischool_role (id, alias, name) VALUES (20, 'director', 'Board Director');
INSERT INTO ischool_role (id, alias, name) VALUES (25, 'chairman', 'Board Chairman');
INSERT INTO ischool_role (id, alias, name) VALUES (30, 'vp', 'School Vice Principal');
INSERT INTO ischool_role (id, alias, name) VALUES (31, 'vpr', 'Vice Principal, Registration');
INSERT INTO ischool_role (id, alias, name) VALUES (35, 'principal', 'School Principal');
INSERT INTO ischool_role (id, alias, name) VALUES (40, 'president', 'School President');
INSERT INTO ischool_role (id, alias, name) VALUES (45, 'cashier', 'Cashier');
INSERT INTO ischool_role (id, alias, name) VALUES (979, 'accountMember', 'Account Member');
INSERT INTO ischool_role (id, alias, name) VALUES (989, 'accountHolder', 'Account Holder');
INSERT INTO ischool_role (id, alias, name) VALUES (999, 'superuser', 'Super User');
COMMIT;

START TRANSACTION;
INSERT INTO ischool_communication_type (id, alias, name) VALUES (11, 'H-ph', 'Home phone');
INSERT INTO ischool_communication_type (id, alias, name) VALUES (12, 'W-ph', 'Work phone');
INSERT INTO ischool_communication_type (id, alias, name) VALUES (13, 'M-ph', 'Mobile phone');
INSERT INTO ischool_communication_type (id, alias, name) VALUES (14, 'H-fx', 'Home fax');
INSERT INTO ischool_communication_type (id, alias, name) VALUES (15, 'W-fx', 'Work fax');
INSERT INTO ischool_communication_type (id, alias, name) VALUES (16, 'O-ph', 'Other Phone');

INSERT INTO ischool_communication_type (id, alias, name) VALUES (31, 'H-em', 'Home email');
INSERT INTO ischool_communication_type (id, alias, name) VALUES (32, 'W-em', 'Work email');
INSERT INTO ischool_communication_type (id, alias, name) VALUES (33, 'O-em', 'Other email');

INSERT INTO ischool_communication_type (id, alias, name) VALUES (51, 'H-url', 'Home url');
INSERT INTO ischool_communication_type (id, alias, name) VALUES (52, 'W-url', 'Work url');
INSERT INTO ischool_communication_type (id, alias, name) VALUES (53, 'HPurl', 'Home Page url');
INSERT INTO ischool_communication_type (id, alias, name) VALUES (54, 'B-url', 'Blog url');
INSERT INTO ischool_communication_type (id, alias, name) VALUES (55, 'O-url', 'Other url');
COMMIT;

START TRANSACTION;
INSERT INTO ischool_course_type (id, name) VALUES ( 1, 'Chinese Core');
INSERT INTO ischool_course_type (id, name) VALUES ( 5, 'Chinese Advanced'); -- AP Chinese, SAT Chinese, Chinese Culture
INSERT INTO ischool_course_type (id, name) VALUES (10, 'Chinese Misc'); -- Biliguage, Preschool
INSERT INTO ischool_course_type (id, name) VALUES (15, 'Math Core');
INSERT INTO ischool_course_type (id, name) VALUES (20, 'Math Advanced');
INSERT INTO ischool_course_type (id, name) VALUES (25, 'Math Misc');
INSERT INTO ischool_course_type (id, name) VALUES (30, 'Extracurricular');
COMMIT;

START TRANSACTION;
-- debit or paid if payment submitted
INSERT INTO ischool_payment_type (id, name, comment) VALUES ( 1, 'One-Click Payment', 'Payment include App Fee, Late Fee, Tuition, etc.');
INSERT INTO ischool_payment_type (id, name) VALUES ( 5, 'Application Fee');
INSERT INTO ischool_payment_type (id, name) VALUES (10, 'Tuition');
INSERT INTO ischool_payment_type (id, name) VALUES (15, 'Late Fee');
INSERT INTO ischool_payment_type (id, name) VALUES (20, 'Bounced Check');
INSERT INTO ischool_payment_type (id, name) VALUES (25, 'Shipping & Handling');
INSERT INTO ischool_payment_type (id, name) VALUES (45, 'Tax');
INSERT INTO ischool_payment_type (id, name) VALUES (50, 'Other Fee');
-- credit
INSERT INTO ischool_payment_type (id, name) VALUES (101, 'Credit');
INSERT INTO ischool_payment_type (id, name) VALUES (105, 'Discount');
INSERT INTO ischool_payment_type (id, name) VALUES (110, 'Promotion');
INSERT INTO ischool_payment_type (id, name) VALUES (115, 'Refund');
COMMIT;

START TRANSACTION;
INSERT INTO ischool_duty_type (id, alias, name, time_sign_up_allowed) VALUES (-1, 'none', 'None', '12:00 PM=1, 01:00 PM=1, 02:00 PM=1, 03:00 PM=1, 04:00 PM=1, 05:00 PM=1');
INSERT INTO ischool_duty_type (id, alias, name, time_sign_up_allowed) VALUES (1, 'bell', 'Ring Bell', '12:00 PM=2, 01:00 PM=2, 02:00 PM=2, 03:00 PM=2, 04:00 PM=2');
INSERT INTO ischool_duty_type (id, alias, name, time_sign_up_allowed) VALUES (5, 'clean', 'Clean Classroom', '04:00 PM=4, 05:00 PM=4');
INSERT INTO ischool_duty_type (id, alias, name, time_sign_up_allowed) VALUES (10, 'lead', 'Parent Duty Lead', '12:00 PM=2');
INSERT INTO ischool_duty_type (id, alias, name, time_sign_up_allowed) VALUES (15, 'sec', 'Public Safety', '12:00 PM=2, 01:00 PM=2, 02:00 PM=2, 03:00 PM=2, 04:00 PM=2');
INSERT INTO ischool_duty_type (id, alias, name, time_sign_up_allowed, parent_duty) VALUES (20, 'ta', 'Teaching Assistant', null, FALSE);
INSERT INTO ischool_duty_type (id, alias, name, time_sign_up_allowed, parent_duty) VALUES (25, 'reg', 'Registration', null, FALSE);
INSERT INTO ischool_duty_type (id, alias, name, time_sign_up_allowed, parent_duty) VALUES (30, 'booksale', 'Textbook Sale', null, FALSE);
INSERT INTO ischool_duty_type (id, alias, name, time_sign_up_allowed, parent_duty) VALUES (99, 'other', 'Other', null, FALSE);

COMMIT;

START TRANSACTION;
INSERT INTO ischool_user (login_id, password, first_name, last_name, account_holder) VALUES ('admin', 'A0A7CAFDF64B1970759719646DFDB69A', 'Admin', '', true); -- add super user
INSERT INTO ischool_user_role (user_id, role_id) VALUES ((select id from ischool_user where login_id = 'admin'), 999); -- superuser
COMMIT;

--hope: '8C728E685DDDE9F7FBBC452155E29639'
--hope1^: 'A0A7CAFDF64B1970759719646DFDB69A'
--------------------------------------------------------------------------------
--                      dconfig-1.2.sql
--------------------------------------------------------------------------------

-- ============================================================================
-- GNU Lesser General Public License
-- ============================================================================
--
-- DConfig - Free Dynamic Configuration Toolkit
-- Copyright (C) 2006 - 2008 Jonathan Luo
--
-- This library is free software; you can redistribute it and/or
-- modify it under the terms of the GNU Lesser General Public
-- License as published by the Free Software Foundation; either
-- version 2.1 of the License, or (at your option) any later version.
--
-- This library is distributed in the hope that it will be useful,
-- but WITHOUT ANY WARRANTY; without even the implied warranty of
-- MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
-- Lesser General Public License for more details.
--
-- You should have received a copy of the GNU Lesser General Public
-- License along with this library; if not, write to the Free Software
-- Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307, USA.
--
--

--------------------------------------------------------------------------------
-- The scripts below has been executed on PostgresSQL v.7.4.13, v.8.1.5
--------------------------------------------------------------------------------
-- Sign on as a user with create table, sequence, etc. privileges
-- For example, postgres

-- uncomment the folloeing if you want to drop existing tables, sequences
drop table if exists dconfig_attribute;
drop table if exists dconfig_system;
drop table if exists dconfig_key;
drop table if exists dconfig_datatype;
drop SEQUENCE if exists dconfig_key_seq;
drop SEQUENCE if exists dconfig_attribute_seq;

--------------------------------------------------------------------------------
-- 1. Create table dconfig_datatype (Required)

CREATE TABLE dconfig_datatype (
    alias VARCHAR(10) NOT NULL,
    data_type_name VARCHAR(20) NOT NULL,
    PRIMARY KEY  (alias)
);

--------------------------------------------------------------------------------
-- 2. Create table dconfig_key (Required)

CREATE SEQUENCE dconfig_key_seq;
CREATE TABLE     dconfig_key      (
    id            INTEGER PRIMARY KEY DEFAULT nextval('dconfig_key_seq') NOT NULL,
    key_name      VARCHAR(500) NOT NULL,
    inherited     CHAR(1) DEFAULT 'N',
    date_created  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    date_modified TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE  ( key_name )
);

--------------------------------------------------------------------------------
-- 3. Create table dconfig_attribute (Required)

CREATE SEQUENCE dconfig_attribute_seq;
CREATE TABLE dconfig_attribute (
    id              INTEGER PRIMARY KEY DEFAULT nextval('dconfig_attribute_seq') NOT NULL,
    key_id          INTEGER NOT NULL REFERENCES dconfig_key(id),
    attribute_name  VARCHAR(100) NOT NULL,
    data_type_alias VARCHAR(10) DEFAULT NULL REFERENCES dconfig_datatype(alias),
    reference       CHAR(1) DEFAULT 'N',
    attribute_value VARCHAR(1000) DEFAULT NULL,
    comments        VARCHAR(1000) DEFAULT NULL,
    date_created    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_modified   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (key_id,attribute_name)
);

--------------------------------------------------------------------------------
-- 4. Create table dconfig_system (Required)
CREATE TABLE     dconfig_system      (
    system_name VARCHAR(100) NOT NULL,
    data_type_alias VARCHAR(10) DEFAULT NULL REFERENCES dconfig_datatype(alias),
    system_value VARCHAR(500),
    comments VARCHAR(500) NULL,
    date_created  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_modified TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY  (system_name)
);

--------------------------------------------------------------------------------
-- 5. Create data type data (Required)

START TRANSACTION;
insert into dconfig_datatype (alias, data_type_name) values ('bin', 'binary');
insert into dconfig_datatype (alias, data_type_name) values ('bool', 'Boolean');
insert into dconfig_datatype (alias, data_type_name) values ('boolar', 'Boolean Array');
insert into dconfig_datatype (alias, data_type_name) values ('int', 'Integer');
insert into dconfig_datatype (alias, data_type_name) values ('intar', 'Integer Array');
insert into dconfig_datatype (alias, data_type_name) values ('long', 'Long');
insert into dconfig_datatype (alias, data_type_name) values ('longar', 'Long Array');
insert into dconfig_datatype (alias, data_type_name) values ('str', 'String');
insert into dconfig_datatype (alias, data_type_name) values ('strar', 'String Array');
insert into dconfig_datatype (alias, data_type_name) values ('float', 'Float');
insert into dconfig_datatype (alias, data_type_name) values ('floatar', 'Float Array');
insert into dconfig_datatype (alias, data_type_name) values ('double', 'Double');
insert into dconfig_datatype (alias, data_type_name) values ('doublear', 'Double Array');
insert into dconfig_datatype (alias, data_type_name) values ('dt', 'Datetime');
insert into dconfig_datatype (alias, data_type_name) values ('dtar', 'Datetime array');
COMMIT;

-- 6. Create system data (Required)
insert into dconfig_system (system_name, data_type_alias, system_value, comments) values ('db.schema.version', 'str', '1.2', 'current db schema version');
insert into dconfig_system (system_name, data_type_alias, system_value, comments) values ('db.datachange.timestamp', 'dt', '2007-01-01 00:00:00', 'a new time stamp should be updated if any data changes (insert, update, delete) occurred in dconfig_key, dconfig_attribute tables');

--------------------------------------------------------------------------------
-- 7. Create a user account 'dcfg' (Optional)
-- You can use existing user account, replace dcfg with the actual user account.
-- and GRANT proper permissions to this account
--
-- CREATE USER dcfg WITH LOGIN PASSWORD 'dconfig';
-- Grant SELECT, INSERT, UPDATE, DELETE, EXECUTE ON TABLE dconfig_key to dcfg;
-- Grant SELECT, INSERT, UPDATE, DELETE ON TABLE dconfig_attribute to dcfg;
-- Grant SELECT, INSERT, UPDATE, DELETE ON TABLE dconfig_datatype to dcfg;
-- Grant EXECUTE sequence dconfig_attribute_seq to dcfg;

-- Note:

--------------------------------------------------------------------------------
--                      dconfig-1.3.sql
--------------------------------------------------------------------------------

-- Update db.schema.version value
update dconfig_system set system_value = '1.3' where system_name = 'db.schema.version';

--------------------------------------------------------------------------------
-- Add new action types to dconfig_datatype
insert into dconfig_datatype (alias, data_type_name) values ('op', 'Operation');
insert into dconfig_datatype (alias, data_type_name) values ('opgrp', 'Operation Group');



--------------------------------------------------------------------------------
--                      dconfig-1.3-ischool.sql
--------------------------------------------------------------------------------
START TRANSACTION;
insert into dconfig_key (key_name) values ('config.setup');
insert into dconfig_key (key_name) values ('config.application');
insert into dconfig_key (key_name) values ('config.ischool');
insert into dconfig_key (key_name) values ('config.ischool.app');
insert into dconfig_key (key_name) values ('config.ischool.regRules');
COMMIT;

START TRANSACTION;
insert into dconfig_attribute (key_id, attribute_name, data_type_alias, attribute_value, comments)
    values ((select id from dconfig_key where key_name = 'config.setup'), 'tableBgColor', 'str',
    '#664848', 'Table background color');
insert into dconfig_attribute (key_id, attribute_name, data_type_alias, attribute_value, comments)
    values ((select id from dconfig_key where key_name = 'config.setup'), 'columnCaptionBgColor', 'str',
    '#23c1d5', 'Table column caption background color');
insert into dconfig_attribute (key_id, attribute_name, data_type_alias, attribute_value, comments)
    values ((select id from dconfig_key where key_name = 'config.setup'), 'oddRowBgColor', 'str',
    '#f0f7f7', 'Table odd row background color');
insert into dconfig_attribute (key_id, attribute_name, data_type_alias, attribute_value, comments)
    values ((select id from dconfig_key where key_name = 'config.setup'), 'evenRowBgColor', 'str',
    '#e3ebc2', 'Table even row background color, or use #f7f7f0');

insert into dconfig_attribute (key_id, attribute_name, data_type_alias, attribute_value, comments)
    values ((select id from dconfig_key where key_name = 'config.setup'), 'rowsPerPage', 'int',
    '12', 'Displays 12 rows per page');

insert into dconfig_attribute (key_id, attribute_name, data_type_alias, attribute_value, comments)
    values ((select id from dconfig_key where key_name = 'config.application'), 'deploymentMode', 'strar',
'development&^#;qa&^#;production', '');
insert into dconfig_attribute (key_id, attribute_name, data_type_alias, attribute_value, comments)
    values ((select id from dconfig_key where key_name = 'config.application'), 'deployment', 'str',
'development', 'develop / qa / production');
COMMIT;

--------------------------------------------------------------------------------
--                 Create user and grant access permissions
--------------------------------------------------------------------------------
DROP USER IF EXISTS ischool;
CREATE USER ischool WITH LOGIN ENCRYPTED PASSWORD 'ischool';

Grant SELECT, INSERT, UPDATE, DELETE ON TABLE ischool_account to ischool;
Grant SELECT, INSERT, UPDATE, DELETE ON TABLE ischool_address to ischool;
Grant SELECT, INSERT, UPDATE, DELETE ON TABLE ischool_calendar to ischool;
Grant SELECT, INSERT, UPDATE, DELETE ON TABLE ischool_communication_type to ischool;
Grant SELECT, INSERT, UPDATE, DELETE ON TABLE ischool_course_type to ischool;
Grant SELECT, INSERT, UPDATE, DELETE ON TABLE ischool_course to ischool;
Grant SELECT, INSERT, UPDATE, DELETE ON TABLE ischool_course_reg to ischool;
Grant SELECT, INSERT, UPDATE, DELETE ON TABLE ischool_course_reg_log to ischool;
Grant SELECT, INSERT, UPDATE, DELETE ON TABLE ischool_email to ischool;
Grant SELECT, INSERT, UPDATE, DELETE ON TABLE ischool_email_log to ischool;
Grant SELECT, INSERT, UPDATE, DELETE ON TABLE ischool_news to ischool;
Grant SELECT, INSERT, UPDATE, DELETE ON TABLE ischool_lost_n_found to ischool;
Grant SELECT, INSERT, UPDATE, DELETE ON TABLE ischool_payment_type to ischool;
Grant SELECT, INSERT, UPDATE, DELETE ON TABLE ischool_payment to ischool;
Grant SELECT, INSERT, UPDATE, DELETE ON TABLE ischool_phone to ischool;
Grant SELECT, INSERT, UPDATE, DELETE ON TABLE ischool_role to ischool;
Grant SELECT, INSERT, UPDATE, DELETE ON TABLE ischool_setup to ischool;
Grant SELECT, INSERT, UPDATE, DELETE ON TABLE ischool_system_info to ischool;
Grant SELECT, INSERT, UPDATE, DELETE ON TABLE ischool_teacher to ischool;
Grant SELECT, INSERT, UPDATE, DELETE ON TABLE ischool_url to ischool;
Grant SELECT, INSERT, UPDATE, DELETE ON TABLE ischool_user to ischool;
Grant SELECT, INSERT, UPDATE, DELETE ON TABLE ischool_user_role to ischool;
Grant SELECT, INSERT, UPDATE, DELETE ON TABLE ischool_generic_page to ischool;
Grant SELECT, INSERT, UPDATE, DELETE ON TABLE ischool_vote to ischool;
Grant SELECT, INSERT, UPDATE, DELETE ON TABLE ischool_vote_result to ischool;
Grant SELECT, INSERT, UPDATE, DELETE ON TABLE ischool_duty_type to ischool;
Grant SELECT, INSERT, UPDATE, DELETE ON TABLE ischool_duty to ischool;
Grant SELECT, INSERT, UPDATE, DELETE ON TABLE ischool_volunteer_service to ischool;
Grant SELECT, INSERT, UPDATE, DELETE ON TABLE ischool_contact_us_category to ischool;
Grant SELECT, INSERT, UPDATE, DELETE ON TABLE ischool_menu to ischool;

Grant SELECT, UPDATE ON SEQUENCE ischool_account_seq to ischool;
Grant SELECT, UPDATE ON SEQUENCE ischool_address_seq to ischool;
Grant SELECT, UPDATE ON SEQUENCE ischool_calendar_seq to ischool;
Grant SELECT, UPDATE ON SEQUENCE ischool_course_reg_seq to ischool;
Grant SELECT, UPDATE ON SEQUENCE ischool_course_reg_log_seq to ischool;
Grant SELECT, UPDATE ON SEQUENCE ischool_course_seq to ischool;
Grant SELECT, UPDATE ON SEQUENCE ischool_email_seq to ischool;
Grant SELECT, UPDATE ON SEQUENCE ischool_email_log_seq to ischool;
Grant SELECT, UPDATE ON SEQUENCE ischool_news_seq to ischool;
Grant SELECT, UPDATE ON SEQUENCE ischool_lost_n_found_seq to ischool;
Grant SELECT, UPDATE ON SEQUENCE ischool_payment_seq to ischool;
Grant SELECT, UPDATE ON SEQUENCE ischool_phone_seq to ischool;
Grant SELECT, UPDATE ON SEQUENCE ischool_setup_seq to ischool;
Grant SELECT, UPDATE ON SEQUENCE ischool_system_info_seq to ischool;
Grant SELECT, UPDATE ON SEQUENCE ischool_teacher_seq to ischool;
Grant SELECT, UPDATE ON SEQUENCE ischool_url_seq to ischool;
Grant SELECT, UPDATE ON SEQUENCE ischool_user_role_seq to ischool;
Grant SELECT, UPDATE ON SEQUENCE ischool_user_seq to ischool;
Grant SELECT, UPDATE ON SEQUENCE ischool_generic_page_seq to ischool;
Grant SELECT, UPDATE ON SEQUENCE ischool_vote_seq to ischool;
Grant SELECT, UPDATE ON SEQUENCE ischool_vote_result_seq to ischool;
Grant SELECT, UPDATE ON SEQUENCE ischool_duty_seq to ischool;
Grant SELECT, UPDATE ON SEQUENCE ischool_volunteer_service_seq to ischool;
Grant SELECT, UPDATE ON SEQUENCE ischool_contact_us_category_seq to ischool;
Grant SELECT, UPDATE ON SEQUENCE ischool_menu_seq to ischool;

Grant SELECT, INSERT, UPDATE, DELETE ON TABLE dconfig_system to ischool;
Grant SELECT, INSERT, UPDATE, DELETE ON TABLE dconfig_key to ischool;
Grant SELECT, INSERT, UPDATE, DELETE ON TABLE dconfig_attribute to ischool;
Grant SELECT, INSERT, UPDATE, DELETE ON TABLE dconfig_datatype to ischool;

Grant SELECT, UPDATE ON SEQUENCE dconfig_key_seq to ischool;
Grant SELECT, UPDATE ON SEQUENCE dconfig_attribute_seq to ischool;
