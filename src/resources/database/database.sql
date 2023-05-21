CREATE DATABASE admin;

USE admin;
show databases;



CREATE TABLE users (
                      ID int primary key auto_increment,
                      USERNAME VARCHAR(20) NOT NULL,
                      PASSWORD VARCHAR(60) NOT NULL
);




CREATE TABLE  addBook(
Bookid varchar(200) not null,
title varchar(200) not null,
author varchar(200) not null,
publisher varchar(200) not null,
quantity int not null,
isAvail boolean default true,
primary key(Bookid));

CREATE TABLE  addMember(
Memberid varchar(200) not null,
name varchar(200) not null,
email varchar(200) not null ,
phone varchar(200)not null ,
gender enum('female','male') not null,
primary key(Memberid));

CREATE TABLE  issuedBooks(
bookID varchar(200) not null,
memberID varchar(200) not null,
issueTime timestamp default CURRENT_TIMESTAMP,
renew_count integer default 0,
primary key(bookID,memberID),
foreign key(bookID) references addBook(Bookid),
foreign key(Memberid) references addMember(Memberid));


-- INSERT INTO userAccount(firstName,lastName,username,password)
-- VALUES ('Enis','Shallci','admin','12345');
--
-- INSERT INTO userAccount(firstName,lastName,username,password)
-- VALUES ('Anjeza','Sfishta','Anjeza','12345');

INSERT INTO addBook(Bookid, title, author, publisher, quantity, isAvail)
VALUES ('b1', 'Book One', 'Author One', 'Publisher One', 5, true);

INSERT INTO addBook(Bookid, title, author, publisher, quantity, isAvail)
VALUES ('b2', 'Book Two', 'Author Two', 'Publisher Two', 10, true);

INSERT INTO addMember(Memberid, name, email, phone, gender)
VALUES ('1', 'John Doe', 'johndoe@example.com', '+1234567890', 'male');

INSERT INTO addMember(Memberid, name, email, phone, gender)
VALUES ('2', 'Jane Doe', 'janedoe@example.com', '+0987654321', 'female');

INSERT INTO issuedBooks(bookID, memberID)
VALUES ('b1', '1');

INSERT INTO issuedBooks(bookID, memberID)
VALUES ('b2', '2');


select * from issuedBooks;


