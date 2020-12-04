# Database

## setting

- DB 구축하기(**처음 한 번만 실행**)

  1. 기존 Maria-db  삭제
  2. docker run –-name maria-db -p 3306:3306 –e MYSQL_ROOT_PASSWORD=ssafy –d mariadb	(cmd 창에서 실행)

  3. docker exec –it maria-db mysql –u root -p

  4. 비밀번호 (ssafy) 입력후 create database common_pjt;

  5. use common_pjt;

  

## table 

#### 1. user

유저 정보를 담고 있는 테이블 

```
user_email : 사용자 이메일(PK, Not, Null)
user_name : 사용자 이름 or 닉네임 (Not Null)
password : 비밀번호 (Not Null)
phone_number : 핸드폰 번호 (정규식 처리)
profile_img : 프로필사진(url)
introduction : 자기소개 
age : 나이
gender : 성별 (남자:M, 여자:W)
```



### 2. post 

포스트 정보를 담고 있는 테이블

```
post_id : 포스트 고유  번호(PK, AI)
title : 블로그 제목
user_email : 작성자 이메일 (FK)
created_at : 글 생성일자
updated_at : 글 수정일자
contents : 글 내용 
username : 아이디 혹은 이름
```



### 3. like

포스트 좋아요를 기록하는 테이블

```
post_id : 포스트 고유 번호(FK)
user_email : 사용자 이메일(FK)
username : 아이디 혹은 이름

복합키(post_id, user_email)
```



### 4. hashtag

해쉬태그를 관리하는 테이블

```
post_id : 포스트 고유 번호(FK)
word : 해쉬태그에 등록할 키워드
```



### 5. comments

포스트 댓글을 관리하는 테이블

```
comments_id : 댓글 고유 번호(PK)
user_email : 사용자 이메일(FK)
post_id : 포스트 고유 번호 (FK)
contents : 댓글 내용
created_at : 댓글 생성일자
updated_at : 댓글 수정일자 
username : 아이디 혹은 이름

```



### 6. posttemp 

포스트 임시저장 정보를 담고 있는 테이블 : post_table과 동일

```
post_id : 포스트 고유  번호(PK, AI)
title : 블로그 제목
user_email : 작성자 이메일 (FK)
created_at : 글 생성일자
updated_at : 글 수정일자
contents : 글 내용 
username : 아이디 혹은 이름

```



### 다음을 실행

```sql
create table usertable(
   useremail varchar(30) primary key not null,
   username varchar(10) not null,
   password varchar(30) not null,
   phonenumber varchar(20),
   profileimg varchar(50),
   introduction varchar(300),
   age int,
   gender char(2) 
);


create table posttable(
   postid int primary key not null AUTO_INCREMENT,
   title varchar(200) not null,
   useremail varchar(30),
   createdat date,
   updatedat date,
   contents varchar(10000),
   username varchar(10),
 
   foreign key(useremail) references usertable(useremail)

);


create table liketable (
   postid int ,
   useremail varchar(30),
    
    primary key(postid, useremail),
    foreign key(postid) references posttable(postid),
    foreign key(useremail) references usertable(useremail)
);

create table hashtagtable(
   postid int,
   word varchar(30),
    
    primary key(postid, word),
    foreign key(postid) references posttable(postid)
);


create table commentstable(
   commentsid int primary key AUTO_INCREMENT,
   useremail varchar(30), 
   postid int,
    contents varchar(300),
    createdat date,
    updatedat date,
    username varchar(10),   

    foreign key(postid) references posttable(postid)
);

create table posttemptable(
   postid int primary key not null AUTO_INCREMENT,
   title varchar(200) not null,
   useremail varchar(30),
   createdat date,
   updatedat date,
   contents varchar(10000),
   username varchar(10),   

    foreign key(useremail) references usertable(useremail)

);


insert into usertable (useremail , username , password, phonenumber , profileimg , introduction , age , gender) values ("a@naver.com", "sangwonseo","1234","010-2053-1733","a.png","hellowolrd!",27,"M");
insert into usertable (useremail , username , password, phonenumber , profileimg , introduction , age , gender) values ("b@naver.com", "sangwon2","1234","010-2053-1733","b.png","hellowolrd!",27,"M");
insert into usertable (useremail , username , password, phonenumber , profileimg , introduction , age , gender) values ("c@naver.com", "sangwon3","1234","010-2053-1733","c.png","hellowolrd!",27,"M");
insert into usertable (useremail , username , password, phonenumber , profileimg , introduction , age , gender) values ("d@naver.com", "sangwon4","1234","010-2053-1733","d.png","hellowolrd!",27,"M");

insert into posttable (postid , title , useremail, createdat , updatedat , contents, username ) values (0,"sangwonTestData1","a@naver.com",now(),null,"hellowolrd!","sangwon");
insert into posttable (postid , title , useremail, createdat , updatedat , contents, username ) values (0,"sangwonTestData2","b@naver.com",now(),null,"hellowolrd!","sumin");
insert into posttable (postid , title , useremail, createdat , updatedat , contents, username ) values (0,"sangwonTestData3","c@naver.com",now(),null,"hellowolrd!","dongwoo");
insert into posttable (postid , title , useremail, createdat , updatedat , contents, username ) values (0,"sangwonTestData4","a@naver.com",now(),null,"hellowolrd!","sangwon");

insert into liketable (postid , useremail) values (1,"a@naver.com");
insert into liketable (postid , useremail) values (2,"b@naver.com");
insert into liketable (postid , useremail) values (2,"c@naver.com");
insert into liketable (postid , useremail) values (3,"a@naver.com");
insert into liketable (postid , useremail) values (3,"b@naver.com");
insert into liketable (postid , useremail) values (3,"c@naver.com");

insert into liketable (postid , useremail) values (4,"a@naver.com");
insert into liketable (postid , useremail) values (4,"b@naver.com");
insert into liketable (postid , useremail) values (4,"c@naver.com");
insert into liketable (postid , useremail) values (4,"d@naver.com");


insert into hashtagtable  (postid , word) values (1,"JAVA");
insert into hashtagtable  (postid , word) values (1,"C++");
insert into hashtagtable  (postid , word)values (1,"C");
insert into hashtagtable  (postid , word)values (1,"DATA");

insert into hashtagtable  (postid , word)values (2,"JAVA");
insert into hashtagtable  (postid , word)values (2,"C++");
insert into hashtagtable  (postid , word)values (2,"C#");
insert into hashtagtable  (postid , word)values (2,"PYTHON");

insert into hashtagtable  (postid , word)values (3,"JAVA");
insert into hashtagtable  (postid , word)values (3,"C++");
insert into hashtagtable  (postid , word)values (4,"C++");
insert into hashtagtable  (postid , word)values (4,"JAVA");

insert into commentstable (commentsid, useremail,postid,contents,createdat,updatedat, username) values (0,"a@naver.com",1,"one",now(),null,"sangwon");
insert into commentstable (commentsid, useremail,postid,contents,createdat,updatedat, username) values (0,"b@naver.com",2,"two",now(),null,"sumin");
insert into commentstable (commentsid, useremail,postid,contents,createdat,updatedat, username) values (0,"c@naver.com",2,"three",now(),null,"sumin");
insert into commentstable (commentsid, useremail,postid,contents,createdat,updatedat, username) values (0,"d@naver.com",3,"four",now(),null,"dongwoo");
insert into commentstable (commentsid, useremail,postid,contents,createdat,updatedat, username) values (0,"a@naver.com",4,"five",now(),null,"hyemin");

insert into posttemptable (postid , title , useremail, createdat , updatedat , contents, username ) values (0,"sangwonTestData1","a@naver.com",now(),null,"hellowolrd!","sangwon");
insert into posttemptable (postid , title , useremail, createdat , updatedat , contents, username ) values (0,"sangwonTestData2","b@naver.com",now(),null,"hellowolrd!","sumin");


```