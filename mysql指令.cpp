mysql -u root -p
123456

create database AnonymousQuestionBox;

use AnonymousQuestionBox;

create table Account(
accountName char(30) not null,
password char(40) not null,
identity tinyint not null default 3,
mailbox char(40) unique,
avatar char(45) default "1.jpg",
questionBoxStatus tinyint not null default 0,
primary key(accountName)
)engine=innodb default charset=utf8;

describe Account;

insert into Account(accountName,password,identity,mailbox,avatar,questionBoxStatus) values('admin','1f06ba75d893fee79cb138162b80348f',1,'xydflxy@sina.com','1.jpg',1);

select * from Account;

create table QuestionBox(
questionId int unsigned not null auto_increment,
accountName char(30) not null,
questionContent text,
questionTime datetime not null,
primary key(questionId),
foreign key(accountName) references Account(accountName) on delete cascade on update cascade
)engine=innodb default charset=utf8;

describe QuestionBox;

insert into QuestionBox(questionId,accountName,questionContent,questionTime,deleted) values(1,'admin','ø’Ã·Œ ','2000-01-01 00:00:00',0);

select * from QuestionBox;

create table AnswerBox(
questionId int unsigned not null,
accountName char(30) not null,
answerContent text,
answerTime datetime not null,
status tinyint not null default 1,
deleted tinyint not null default 2,
primary key(questionId,accountName),
foreign key(questionId) references QuestionBox(questionId) on delete cascade on update cascade,
foreign key(accountName) references Account(accountName) on delete cascade on update cascade
)engine=innodb default charset=utf8;

describe AnswerBox;

create table Action(
accountName char(30) not null,
blacklist tinyint not null default 0,
tipOff tinyint not null default 2,
questionId int unsigned not null,
primary key(accountName,questionId),
foreign key(accountName) references Account(accountName) on delete cascade on update cascade,
foreign key(questionId) references QuestionBox(questionId) on delete cascade on update cascade
)engine=innodb default charset=utf8;

describe Action;

insert into Action(actioner,peopleActioned,blacklist,tipOff) values('admin','admin',0,1);

select * from Action;

create table IPs(
accountName char(30) not null,
IP char(25) not null,
count smallint unsigned not null default 1,
primary key(accountName,IP),
foreign key(accountName) references Account(accountName) on delete cascade on update cascade
)engine=innodb default charset=utf8;

describe IPs;

create table Notice(
noticeId int unsigned not null auto_increment,
accountName char(30) not null,
content text,
releaseTime datetime not null,
status tinyint not null default 1,
primary key(noticeId),
foreign key(accountName) references Account(accountName) on delete cascade on update cascade
)engine=innodb default charset=utf8;

describe Notice;











