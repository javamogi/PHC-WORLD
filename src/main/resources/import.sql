insert into USER(ID, EMAIL, PASSWORD, NAME, AUTHORITY, CREATE_DATE, PROFILE_IMAGE) values('1', 'test@test.test', 'test', '테스트', 'ROLE_ADMIN', CURRENT_TIMESTAMP(), 'blank-profile-picture.png');
insert into USER(ID, EMAIL, PASSWORD, NAME, AUTHORITY, CREATE_DATE, PROFILE_IMAGE) values('2', 'test2@test.test', 'test2', '테스트2', 'ROLE_USER', CURRENT_TIMESTAMP(), 'blank-profile-picture.png');

insert into EMAIL_AUTH(ID, EMAIL, AUTH_KEY, CONFIRM) values('1', 'test@test.test', '1234', 'Y');
insert into EMAIL_AUTH(ID, EMAIL, AUTH_KEY, CONFIRM) values('2', 'test2@test.test', '5678', 'Y');

insert into FREE_BOARD(ID, WRITER_ID, TITLE, CONTENTS, COUNT, CREATE_DATE, ICON, BADGE) values('1', '1', 'test', 'test', 0, '2018-07-16 15:55:20.879', '', '');
insert into FREE_BOARD(ID, WRITER_ID, TITLE, CONTENTS, COUNT, CREATE_DATE, ICON, BADGE) values('2', '1', 'test2', 'test2', 0,  '2018-07-17 15:55:20.879', '', '');
insert into FREE_BOARD(ID, WRITER_ID, TITLE, CONTENTS, COUNT, CREATE_DATE, ICON, BADGE) values('3', '1', 'test3', 'test3', 0,  CURRENT_TIMESTAMP(), '', '');
insert into FREE_BOARD(ID, WRITER_ID, TITLE, CONTENTS, COUNT, CREATE_DATE, ICON, BADGE) values('4', '2', 'testtest', 'testtest', 0,  CURRENT_TIMESTAMP(), '', '');

insert into TIMELINE(ID, TYPE, ICON, FREE_BOARD_ID, FREE_BOARD_ANSWER_ID, USER_ID, SAVE_DATE) values('1', 'free board', 'list-alt', '1', null, '1', '2018-07-16 15:55:20.879');
insert into TIMELINE(ID, TYPE, ICON, FREE_BOARD_ID, FREE_BOARD_ANSWER_ID, USER_ID, SAVE_DATE) values('2', 'free board', 'list-alt', '2', null, '1', '2018-07-17 15:55:20.879');
insert into TIMELINE(ID, TYPE, ICON, FREE_BOARD_ID, FREE_BOARD_ANSWER_ID, USER_ID, SAVE_DATE) values('3', 'free board', 'list-alt', '3', null, '1', CURRENT_TIMESTAMP());
insert into TIMELINE(ID, TYPE, ICON, FREE_BOARD_ID, FREE_BOARD_ANSWER_ID, USER_ID, SAVE_DATE) values('4', 'free board', 'list-alt', '4', null, '2', CURRENT_TIMESTAMP());

update FREE_BOARD set TIMELINE_ID = '1' where ID = '1';
update FREE_BOARD set TIMELINE_ID = '2' where ID = '2';
update FREE_BOARD set TIMELINE_ID = '3' where ID = '3';
update FREE_BOARD set TIMELINE_ID = '4' where ID = '4';