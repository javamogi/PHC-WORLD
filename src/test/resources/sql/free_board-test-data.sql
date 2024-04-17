-- insert into users(ID, EMAIL, PASSWORD, NAME, AUTHORITY, CREATE_DATE, PROFILE_IMAGE) values('1', 'test@test.test', '$2a$10$aWqY0MzLKnt.6bvFk4zhPu.HZDabDQttLC2uAupM1yq1p6cTSTjSi', '테스트', 'ROLE_ADMIN', CURRENT_TIMESTAMP(), 'blank-profile-picture.png');
--
-- insert into free_board(ID, WRITER_ID, TITLE, CONTENTS, COUNT, CREATE_DATE, UPDATE_DATE) values('1', '1', 'test', 'test', 0, '2018-07-16 15:55:20.879', '2018-07-16 15:55:20.879');
-- insert into free_board(ID, WRITER_ID, TITLE, CONTENTS, COUNT, CREATE_DATE, UPDATE_DATE) values('2', '1', 'test2', 'test2', 0, '2018-07-17 15:55:20.879', '2018-07-17 15:55:20.879');
-- insert into free_board(ID, WRITER_ID, TITLE, CONTENTS, COUNT, CREATE_DATE, UPDATE_DATE) values('3', '1', 'test3', 'test3', 0, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());
-- insert into free_board(ID, WRITER_ID, TITLE, CONTENTS, COUNT, CREATE_DATE, UPDATE_DATE) values('4', '2', 'testtest', 'testtest', 0,  CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

insert into free_board_answer(ID, WRITER_ID, FREE_BOARD_ID, CONTENTS, CREATE_DATE, UPDATE_DATE) values('1', '1', '1', 'test3', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());
insert into free_board_answer(ID, WRITER_ID, FREE_BOARD_ID, CONTENTS, CREATE_DATE, UPDATE_DATE) values('2', '1', '1', 'test2', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());
insert into free_board_answer(ID, WRITER_ID, FREE_BOARD_ID, CONTENTS, CREATE_DATE, UPDATE_DATE) values('3', '1', '1', 'test1', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());