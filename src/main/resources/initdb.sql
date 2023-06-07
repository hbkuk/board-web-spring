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
    (1, 'Title 1', '테1', 'Content 1', 'password1!', 0, '2023-05-18 10:00:00', null),
    (2, 'Title 2', '테2', 'Content 2', 'password2!', 5, '2023-05-18 11:00:00', null),
    (3, 'Title 3', '테3', 'Content 3', 'password3!', 8, '2023-05-18 12:00:00', null),
    (4, 'Title 4', '테4', 'Content 4', 'password4!', 3, '2023-05-18 13:00:00', null),
    (5, 'Title 5', '테5', 'Content 5', 'password5!', 15, '2023-05-18 14:00:00', null),
    (6, 'Title 6', '테6', 'Content 6', 'password6!', 12, '2023-05-18 15:00:00', null),
    (7, 'Title 7', '테7', 'Content 7', 'password7!', 7, '2023-05-18 16:00:00', null),
    (8, 'Title 8', '테8', 'Content 8', 'password8!', 20, '2023-05-18 17:00:00', null),
    (9, 'Title 9', '테9', 'Content 9', 'password9!', 2, '2023-05-18 18:00:00', null),
    (10, 'Title 10', '테10', 'Content 10', 'password10!', 9, '2023-05-18 19:00:00', null);

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
