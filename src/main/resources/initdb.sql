drop table if exists tb_comment;
drop table if exists tb_file;
drop table if exists tb_board;
drop table if exists tb_category;

CREATE TABLE tb_board (
    boardIdx BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    categoryIdx INT NOT NULL,
    title varchar(200) NOT NULL,
    writer varchar(10) NOT NULL,
    content varchar(2000) NOT NULL,
    password varchar(16) NOT NULL,
    hit INT NOT NULL,
    regDate datetime NOT NULL,
    modDate datetime NULL
);

CREATE TABLE tb_comment (
    commentIdx BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    writer varchar(10) NOT NULL,
    password varchar(16) NOT NULL,
    content varchar(2000) NOT NULL,
    regDate datetime NOT NULL,
    boardIdx BIGINT NOT NULL
);

CREATE TABLE tb_file (
    fileIdx BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    savedName varchar(255) NOT NULL,
    originalName varchar(255) NOT NULL,
    fileSize INT NOT NULL,
    boardIdx BIGINT NOT NULL
);

CREATE TABLE tb_category (
    categoryIdx INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL
);

ALTER TABLE tb_board ADD CONSTRAINT FK_tb_category_TO_tb_board_1 FOREIGN KEY (categoryIdx)
    REFERENCES tb_category (categoryIdx);

ALTER TABLE tb_comment ADD CONSTRAINT FK_tb_board_TO_tb_comment_1 FOREIGN KEY (boardIdx)
    REFERENCES tb_board (boardIdx);

ALTER TABLE tb_file ADD CONSTRAINT FK_tb_board_TO_tb_file_1 FOREIGN KEY (boardIdx)
    REFERENCES tb_board (boardIdx);

INSERT INTO tb_category (code, name)VALUES
    ('EB001', 'HTML'),
    ('EB002', 'CSS'),
    ('EB003', 'JAVASCRIPT'),
    ('EB004', 'SPRING'),
    ('EB005', 'JPA'),
    ('EB006', 'PYTHON'),
    ('EB007', 'JAVA'),
    ('EB008', 'REACT'),
    ('EB009', 'ANGULAR'),
    ('EB010', 'DATABASE');

INSERT INTO tb_board (categoryIdx, title, writer, content, password, hit, regDate, modDate) VALUES
    (1, 'Title 1', '테1', 'Content 1', 'password1!', 0, '2021-05-18 10:00:00', null),
    (2, 'Title 2', '테2', 'Content 2', 'password2!', 5, '2022-05-18 11:00:00', null),
    (3, 'Title 3', '테3', 'Content 3', 'password3!', 8, '2021-05-18 12:00:00', null),
    (4, 'Title 4', '테4', 'Content 4', 'password4!', 3, '2022-05-18 13:00:00', null),
    (5, 'Title 5', '테5', 'Content 5', 'password5!', 15, '2020-05-18 14:00:00', null),
    (6, 'Title 6', '테6', 'Content 6', 'password6!', 12, '2023-05-18 15:00:00', null),
    (7, 'Title 7', '테7', 'Content 7', 'password7!', 7, '2023-05-18 16:00:00', null),
    (8, 'Title 8', '테8', 'Content 8', 'password8!', 20, '2023-05-18 17:00:00', null),
    (9, 'Title 9', '테9', 'Content 9', 'password9!', 2, '2023-05-18 18:00:00', null),
    (10, 'Title 10', '테10', 'Content 10', 'password10!', 9, '2022-05-18 19:00:00', null),
    (1, 'Title 11', '테11', 'Content 11', 'password11!', 6, '2021-05-18 20:00:00', null),
    (1, 'Title 12', '테12', 'Content 12', 'password12!', 4, '2022-05-18 21:00:00', null),
    (1, 'Title 13', '테13', 'Content 13', 'password13!', 11, '2023-05-18 22:00:00', null),
    (1, 'Title 14', '테14', 'Content 14', 'password14!', 18, '2021-05-18 23:00:00', null),
    (1, 'Title 15', '테15', 'Content 15', 'password15!', 1, '2020-05-19 00:00:00', null),
    (1, 'Title 16', '테16', 'Content 16', 'password16!', 14, '2021-05-19 01:00:00', null),
    (1, 'Title 17', '테17', 'Content 17', 'password17!', 9, '2022-05-19 02:00:00', null),
    (2, 'Title 18', '테18', 'Content 18', 'password18!', 6, '2023-05-19 03:00:00', null),
    (2, 'Title 19', '테19', 'Content 19', 'password19!', 13, '2023-05-19 04:00:00', null),
    (2, 'Title 20', '테20', 'Content 20', 'password20!', 10, '2023-05-19 05:00:00', null),
    (2, 'Title 21', '테21', 'Content 21', 'password21!', 7, '2023-05-19 06:00:00', null),
    (2, 'Title 22', '테22', 'Content 22', 'password22!', 3, '2023-05-19 07:00:00', null),
    (2, 'Title 23', '테23', 'Content 23', 'password23!', 12, '2023-05-19 08:00:00', null),
    (2, 'Title 24', '테24', 'Content 24', 'password24!', 9, '2023-05-19 09:00:00', null),
    (2, 'Title 25', '테25', 'Content 25', 'password25!', 5, '2023-05-19 10:00:00', null),
    (2, 'Title 26', '테26', 'Content 26', 'password26!', 16, '2023-05-19 11:00:00', null),
    (2, 'Title 27', '테27', 'Content 27', 'password27!', 8, '2023-05-19 12:00:00', null),
    (2, 'Title 28', '테28', 'Content 28', 'password28!', 22, '2023-05-19 13:00:00', null),
    (2, 'Title 29', '테29', 'Content 29', 'password29!', 3, '2023-05-19 14:00:00', null),
    (3, 'Title 30', '테30', 'Content 30', 'password30!', 11, '2023-05-19 15:00:00', null),
    (3, 'Title 31', '테31', 'Content 31', 'password31!', 20, '2023-05-19 16:00:00', null),
    (3, 'Title 32', '테32', 'Content 32', 'password32!', 9, '2023-05-19 17:00:00', null),
    (3, 'Title 33', '테33', 'Content 33', 'password33!', 6, '2023-05-19 18:00:00', null),
    (3, 'Title 34', '테34', 'Content 34', 'password34!', 15, '2023-05-19 19:00:00', null),
    (3, 'Title 35', '테35', 'Content 35', 'password35!', 2, '2023-05-19 20:00:00', null),
    (3, 'Title 36', '테36', 'Content 36', 'password36!', 9, '2023-05-19 21:00:00', null),
    (3, 'Title 37', '테37', 'Content 37', 'password37!', 18, '2023-05-19 22:00:00', null),
    (3, 'Title 38', '테38', 'Content 38', 'password38!', 11, '2023-05-19 23:00:00', null),
    (3, 'Title 39', '테39', 'Content 39', 'password39!', 6, '2023-05-20 00:00:00', null),
    (4, 'Title 40', '테40', 'Content 40', 'password40!', 13, '2023-05-20 01:00:00', null),
    (4, 'Title 41', '테41', 'Content 41', 'password41!', 10, '2023-05-20 02:00:00', null),
    (4, 'Title 42', '테42', 'Content 42', 'password42!', 7, '2023-05-20 03:00:00', null),
    (4, 'Title 43', '테43', 'Content 43', 'password43!', 15, '2023-05-20 04:00:00', null),
    (4, 'Title 44', '테44', 'Content 44', 'password44!', 3, '2023-05-20 05:00:00', null),
    (4, 'Title 45', '테45', 'Content 45', 'password45!', 8, '2023-05-20 06:00:00', null),
    (4, 'Title 46', '테46', 'Content 46', 'password46!', 12, '2023-05-20 07:00:00', null),
    (4, 'Title 47', '테47', 'Content 47', 'password47!', 5, '2023-05-20 08:00:00', null),
    (4, 'Title 48', '테48', 'Content 48', 'password48!', 9, '2023-05-20 09:00:00', null),
    (4, 'Title 49', '테49', 'Content 49', 'password49!', 7, '2023-05-20 10:00:00', null),
    (5, 'Title 50', '테50', 'Content 50', 'password50!', 13, '2023-05-20 11:00:00', null),
    (5, 'Title 51', '테51', 'Content 51', 'password51!', 18, '2023-05-20 12:00:00', null),
    (5, 'Title 52', '테52', 'Content 52', 'password52!', 6, '2023-05-20 13:00:00', null),
    (5, 'Title 53', '테53', 'Content 53', 'password53!', 11, '2023-05-20 14:00:00', null),
    (5, 'Title 54', '테54', 'Content 54', 'password54!', 9, '2023-05-20 15:00:00', null),
    (5, 'Title 55', '테55', 'Content 55', 'password55!', 14, '2023-05-20 16:00:00', null),
    (5, 'Title 56', '테56', 'Content 56', 'password56!', 4, '2023-05-20 17:00:00', null),
    (5, 'Title 57', '테57', 'Content 57', 'password57!', 10, '2023-05-20 18:00:00', null),
    (5, 'Title 58', '테58', 'Content 58', 'password58!', 6, '2023-05-20 19:00:00', null),
    (5, 'Title 59', '테59', 'Content 59', 'password59!', 9, '2023-05-20 20:00:00', null),
    (6, 'Title 60', '테60', 'Content 60', 'password60!', 12, '2023-05-20 21:00:00', null);

INSERT INTO tb_comment (writer, password, content, regDate, boardIdx) VALUES
    ('테1', 'commentpass1!', 'Comment 1', '2023-05-18 10:30:00', 1),
    ('테2', 'commentpass2!', 'Comment 2', '2023-05-18 11:30:00', 2),
    ('테3', 'commentpass3!', 'Comment 3', '2023-05-18 12:30:00', 3),
    ('테4', 'commentpass4!', 'Comment 4', '2023-05-18 13:30:00', 4),
    ('테5', 'commentpass5!', 'Comment 5', '2023-05-18 14:30:00', 5),
    ('테6', 'commentpass6!', 'Comment 6', '2023-05-18 15:30:00', 6),
    ('테7', 'commentpass7!', 'Comment 7', '2023-05-18 16:30:00', 7),
    ('테8', 'commentpass8!', 'Comment 8', '2023-05-18 17:30:00', 8),
    ('테9', 'commentpass9!', 'Comment 9', '2023-05-18 18:30:00', 9),
    ('테10', 'commentpass10!', 'Comment 10', '2023-05-18 19:30:00', 10);

INSERT INTO tb_file (savedName, originalName, fileSize, boardIdx) VALUES
    ('file1.png', 'Image 1.png', 1024, 1),
    ('file2.png', 'Image 2.png', 2048, 2),
    ('file3.png', 'Image 3.png', 3072, 3),
    ('file4.png', 'Image 4.png', 4096, 4),
    ('file5.png', 'Image 5.png', 5120, 5),
    ('file6.png', 'Image 6.png', 6144, 6),
    ('file7.png', 'Image 7.png', 7168, 7),
    ('file8.png', 'Image 8.png', 8192, 8),
    ('file9.png', 'Image 9.png', 9216, 9),
    ('file10.png', 'Image 10.png', 10240, 10);
