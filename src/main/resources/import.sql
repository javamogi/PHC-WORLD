insert into users(ID, EMAIL, PASSWORD, NAME, AUTHORITY, CREATE_DATE, PROFILE_IMAGE) values('1', 'test@test.test', 'test', '테스트', 'ROLE_ADMIN', CURRENT_TIMESTAMP(), 'blank-profile-picture.png');
insert into users(ID, EMAIL, PASSWORD, NAME, AUTHORITY, CREATE_DATE, PROFILE_IMAGE) values('2', 'test2@test.test', 'test2', '테스트2', 'ROLE_USER', CURRENT_TIMESTAMP(), 'blank-profile-picture.png');
insert into users(ID, EMAIL, PASSWORD, NAME, AUTHORITY, CREATE_DATE, PROFILE_IMAGE) values('3', 'test3@test.test', 'test3', '테스트3', 'ROLE_USER', CURRENT_TIMESTAMP(), 'blank-profile-picture.png');

insert into email_auth(ID, EMAIL, AUTH_KEY, AUTHENTICATE) values('1', 'test@test.test', '1234', true);
insert into email_auth(ID, EMAIL, AUTH_KEY, AUTHENTICATE) values('2', 'test2@test.test', '5678', true);

insert into free_board(ID, WRITER_ID, TITLE, CONTENTS, COUNT, CREATE_DATE, UPDATE_DATE, ICON, BADGE) values('1', '1', 'test', 'test', 0, '2018-07-16 15:55:20.879', '2018-07-16 15:55:20.879', '', '');
insert into free_board(ID, WRITER_ID, TITLE, CONTENTS, COUNT, CREATE_DATE, UPDATE_DATE, ICON, BADGE) values('2', '1', 'test2', 'test2', 0, '2018-07-17 15:55:20.879', '2018-07-17 15:55:20.879', '', '');
insert into free_board(ID, WRITER_ID, TITLE, CONTENTS, COUNT, CREATE_DATE, UPDATE_DATE, ICON, BADGE) values('3', '1', 'test3', 'test3', 0, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), '', '');
insert into free_board(ID, WRITER_ID, TITLE, CONTENTS, COUNT, CREATE_DATE, UPDATE_DATE, ICON, BADGE) values('4', '2', 'testtest', 'testtest', 0,  CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), '', '');

-- insert into timeline(ID, TYPE, ICON, FREE_BOARD_ID, USER_ID, SAVE_DATE) values('1', 'free board', 'list-alt', '1', '1', '2018-07-16 15:55:20.879');
-- insert into timeline(ID, TYPE, ICON, FREE_BOARD_ID, USER_ID, SAVE_DATE) values('2', 'free board', 'list-alt', '2', '1', '2018-07-17 15:55:20.879');
-- insert into timeline(ID, TYPE, ICON, FREE_BOARD_ID, USER_ID, SAVE_DATE) values('3', 'free board', 'list-alt', '3', '1', CURRENT_TIMESTAMP());
-- insert into timeline(ID, TYPE, ICON, FREE_BOARD_ID, USER_ID, SAVE_DATE) values('4', 'free board', 'list-alt', '4', '2', CURRENT_TIMESTAMP());

insert into timeline(ID, SAVE_TYPE, POST_ID, REDIRECT_ID, USER_ID, SAVE_DATE) values('1', 'FREE_BOARD', '1', '1', '1', '2018-07-16 15:55:20.879');
insert into timeline(ID, SAVE_TYPE, POST_ID, REDIRECT_ID, USER_ID, SAVE_DATE) values('2', 'FREE_BOARD', '2', '2', '1', '2018-07-16 15:55:20.879');
insert into timeline(ID, SAVE_TYPE, POST_ID, REDIRECT_ID, USER_ID, SAVE_DATE) values('3', 'FREE_BOARD', '3', '3', '1', '2018-07-16 15:55:20.879');
insert into timeline(ID, SAVE_TYPE, POST_ID, REDIRECT_ID, USER_ID, SAVE_DATE) values('4', 'FREE_BOARD', '4', '4', '2', '2018-07-16 15:55:20.879');
