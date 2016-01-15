-- TINYINT          1 byte      -128 ~ 127, 0 ~ 255
-- SMALLINT         2 bytes     -32768 ~ 32767, 0 ~ 65536
-- MEDIUMINT        3 bytes     -8388608 ~ 8388607, 0 ~ 16777215
-- INT, INTEGER     4 bytes     -2147483648 ~ 2147483647, 0 ~ 4294967295
-- BIGINT           8 bytes     -9223372036854775808 ~ 9223372036854775807, 0 ~ 18446744073709551615

START TRANSACTION;
INSERT INTO role (alias, name) values ('student', 'Student');
INSERT INTO role (alias, name) values ('tutor', 'Tutor');
INSERT INTO role (alias, name) values ('teacher', 'Teacher');
INSERT INTO role (alias, name) values ('supervisor', 'Supervisor');
INSERT INTO role (alias, name) values ('admin', 'Administrator');
COMMIT;

-- https://students.asu.edu/academic-calendar#spring16
START TRANSACTION;
INSERT INTO semester (alias, name, start_date) values ('F2015', 'Fall 2015', '2015-08-17'); -- 08-20
INSERT INTO semester (alias, name, start_date) values ('S2016', 'Spring 2016', '2016-01-11');
INSERT INTO semester (alias, name, start_date) values ('M2016', 'Summer 2016', '2016-05-16');
INSERT INTO semester (alias, name, start_date) values ('F2016', 'Fall 2016', '2016-08-15'); -- 08-18
COMMIT;

START TRANSACTION;
INSERT INTO week (week) values ('Week 1');
INSERT INTO week (week) values ('Week 2');
INSERT INTO week (week) values ('Week 3');
INSERT INTO week (week) values ('Week 4');
INSERT INTO week (week) values ('Week 5');
INSERT INTO week (week) values ('Week 6');
INSERT INTO week (week) values ('Week 7');
INSERT INTO week (week) values ('Week 8');
INSERT INTO week (week) values ('Week 9');
INSERT INTO week (week) values ('Week 10');
INSERT INTO week (week) values ('Week 11');
INSERT INTO week (week) values ('Week 12');
INSERT INTO week (week) values ('Week 13');
INSERT INTO week (week) values ('Week 14');
INSERT INTO week (week) values ('Week 15');
INSERT INTO week (week) values ('Week 16');
COMMIT;

START TRANSACTION;
INSERT INTO semester_week (semester, week, start_day) values ('F2015', 'Week 1',  '2015-08-16');
INSERT INTO semester_week (semester, week, start_day) values ('F2015', 'Week 2',  '2015-08-23');
INSERT INTO semester_week (semester, week, start_day) values ('F2015', 'Week 3',  '2015-08-30');
INSERT INTO semester_week (semester, week, start_day) values ('F2015', 'Week 4',  '2015-09-06');
INSERT INTO semester_week (semester, week, start_day) values ('F2015', 'Week 5',  '2015-09-13');
INSERT INTO semester_week (semester, week, start_day) values ('F2015', 'Week 6',  '2015-09-20');
INSERT INTO semester_week (semester, week, start_day) values ('F2015', 'Week 7',  '2015-09-27');
INSERT INTO semester_week (semester, week, start_day) values ('F2015', 'Week 8',  '2015-10-04');
INSERT INTO semester_week (semester, week, start_day) values ('F2015', 'Week 9',  '2015-10-11');
INSERT INTO semester_week (semester, week, start_day) values ('F2015', 'Week 10', '2015-10-18');
INSERT INTO semester_week (semester, week, start_day) values ('F2015', 'Week 11', '2015-10-25');
INSERT INTO semester_week (semester, week, start_day) values ('F2015', 'Week 12', '2015-11-01');
INSERT INTO semester_week (semester, week, start_day) values ('F2015', 'Week 13', '2015-11-08');
INSERT INTO semester_week (semester, week, start_day) values ('F2015', 'Week 14', '2015-11-15');
INSERT INTO semester_week (semester, week, start_day) values ('F2015', 'Week 15', '2015-11-22');
INSERT INTO semester_week (semester, week, start_day) values ('F2015', 'Week 16', '2015-11-29');
COMMIT;

START TRANSACTION;
INSERT INTO tutor_group (alias, name, ordinal) values ('Listening & Oral 1', 'Listening & Oral 1 Tutoring Group', 1);
INSERT INTO tutor_group (alias, name, ordinal) values ('Listening & Oral 2', 'Listening & Oral 2 Tutoring Group', 5);
INSERT INTO tutor_group (alias, name, ordinal) values ('Grammar', 'Grammar Tutoring Group', 10);
INSERT INTO tutor_group (alias, name, ordinal) values ('HSK', 'HSK Tutoring Group', 15);
INSERT INTO tutor_group (alias, name, ordinal) values ('Capstone Preparation', 'Capstone Preparation Tutoring Group', 20);
INSERT INTO tutor_group (alias, name, ordinal) values ('Pronunciation & Fluency', 'Pronunciation & Fluency Tutoring Group', 25);
INSERT INTO tutor_group (alias, name, ordinal) values ('Reading', 'Reading Tutoring Group', 30);
INSERT INTO tutor_group (alias, name, ordinal) values ('Reading & Oral', 'Reading & Oral Tutoring Group', 35);
INSERT INTO tutor_group (alias, name, ordinal) values ('Writing', 'Writing Tutoring Group', 40);
INSERT INTO tutor_group (alias, name, ordinal) values ('Practice', 'Practice Tutoring Group', 45);
COMMIT;



