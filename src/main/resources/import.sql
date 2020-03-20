insert into USER(ID, EMAIL, PASSWORD, NAME, AUTHORITY, CREATE_DATE, PROFILE_IMAGE) values('1', 'test@test.test', 'test', '테스트', 'ROLE_ADMIN', CURRENT_TIMESTAMP(), 'blank-profile-picture.png');
insert into USER(ID, EMAIL, PASSWORD, NAME, AUTHORITY, CREATE_DATE, PROFILE_IMAGE) values('2', 'test2@test.test', 'test2', '테스트2', 'ROLE_USER', CURRENT_TIMESTAMP(), 'blank-profile-picture.png');

insert into EMAIL_AUTH(ID, EMAIL, AUTH_KEY) values('1', 'test@test.test', '1234');
insert into EMAIL_AUTH(ID, EMAIL, AUTH_KEY) values('2', 'test2@test.test', '5678');

insert into FREE_BOARD(ID, WRITER_ID, TITLE, CONTENTS, COUNT, CREATE_DATE, ICON, BADGE) values('1', '1', 'test', 'test', 0, '2018-07-16 15:55:20.879', '', '');
insert into FREE_BOARD(ID, WRITER_ID, TITLE, CONTENTS, COUNT, CREATE_DATE, ICON, BADGE) values('2', '1', 'test2', 'test2', 0,  '2018-07-17 15:55:20.879', '', '');
insert into FREE_BOARD(ID, WRITER_ID, TITLE, CONTENTS, COUNT, CREATE_DATE, ICON, BADGE) values('3', '1', 'test3', 'test3', 0,  CURRENT_TIMESTAMP(), '', '');
insert into FREE_BOARD(ID, WRITER_ID, TITLE, CONTENTS, COUNT, CREATE_DATE, ICON, BADGE) values('4', '2', 'testtest', 'testtest', 0,  CURRENT_TIMESTAMP(), '', '');

insert into TIMELINE(ID, TYPE, ICON, FREE_BOARD_ID, USER_ID, SAVE_DATE) values('1', 'free board', 'list-alt', '1', '1', '2018-07-16 15:55:20.879');
insert into TIMELINE(ID, TYPE, ICON, FREE_BOARD_ID, USER_ID, SAVE_DATE) values('2', 'free board', 'list-alt', '2', '1', '2018-07-17 15:55:20.879');
insert into TIMELINE(ID, TYPE, ICON, FREE_BOARD_ID, USER_ID, SAVE_DATE) values('3', 'free board', 'list-alt', '3', '1', CURRENT_TIMESTAMP());
insert into TIMELINE(ID, TYPE, ICON, FREE_BOARD_ID, USER_ID, SAVE_DATE) values('4', 'free board', 'list-alt', '4', '2', CURRENT_TIMESTAMP());
