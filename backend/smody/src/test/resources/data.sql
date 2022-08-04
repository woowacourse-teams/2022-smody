insert into member (email, nickname, picture)
values ('iawbg13@gmail.com', '조조그린',
        'https://lh3.googleusercontent.com/a-/AFdZucp2Jil0TsQ_Edr7hFi7RGfyJK48yeffjHgCVI3JNw=s96-c');
insert into member (email, nickname, picture)
values ('ldk980130@gmail.com', '더즈',
        'https://lh3.googleusercontent.com/a-/AFdZuco9OnuGCM1lskeFuuYNyHXTZ7YVZTw5L1kOfGWapQ=s96-c-rg-br100');
insert into member (email, nickname, picture)
values ('diddmlvkf@gmail.com', '토닉',
        'https://lh3.googleusercontent.com/a/AItbvmm1hUW1WalreKj7mQ54AJeTx7xTpkLHQ91Dc6BC=s576-p-rw-no-mo');
insert into member (email, nickname, picture)
values ('bcc101106@gmail.com', '알파',
        'https://lh3.googleusercontent.com/a/AItbvmmloBfQkCwgxHvdDFPtuqIkAbMKWjk_NHd7xXjq=s192-c-br100-rg-mo');

insert into challenge (name)
values ('스모디 방문하기');
insert into challenge (name)
values ('미라클 모닝');
insert into challenge (name)
values ('오늘의 운동');
insert into challenge (name)
values ('알고리즘 풀기');
insert into challenge (name)
values ('JPA 공부');

insert into cycle (challenge_id, member_id, progress, start_time)
values (1, 1, 'NOTHING', '2022-01-01 00:00:00');
