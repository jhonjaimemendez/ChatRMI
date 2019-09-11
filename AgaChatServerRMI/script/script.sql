-- Create the database
create database agachat;

use agachat;

-- user table
create table users (
  
   userid varchar(20) primary key,
   password varchar(20) not null

);

-- message table
create table messages (

   id INT primary key AUTO_INCREMENT,
   message varchar(2000) not null,
   datereceived datetime not null,
   sender char(1) not null,
   userid varchar(20) references users(userid)
  
);

insert into users values ("admin","admin");
insert into users values ("user1","user1");
insert into users values ("user2","user2");
insert into users values ("user3","user3");