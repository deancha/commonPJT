create table usertable
(
   useremail varchar(30) primary key not null,
   username varchar(20) not null,
   password varchar(30) not null,
   phonenumber varchar(20),
   profileimg varchar(100),
   introduction varchar(300),
   age int,
   gender char(2) ,
   isadmin char(2),
   issocial char(2)
);
Alter table usertable add column issocial varchar
(2);

create table posttable
(
   postid int primary key not null
   AUTO_INCREMENT,
   title varchar
   (200) not null,
   useremail varchar
   (30),
   createdat datetime,
   updatedat datetime,
   contents varchar
   (10000),
   username varchar
   (20) not null,
 
   foreign key
   (useremail) references usertable
   (useremail)

);


   create table liketable
   (
      postid int ,
      useremail varchar(30),
      username varchar(20) not null,
      primary key(postid, useremail),
      foreign key(postid) references posttable(postid),
      foreign key(useremail) references usertable(useremail)
   );

   create table hashtagtable
   (
      postid int,
      word varchar(30),

      primary key(postid, word),
      foreign key(postid) references posttable(postid)
   );


   create table commentstable(
   commentsid int primary key AUTO_INCREMENT,
   useremail varchar
   (30), 
   postid int,
    contents varchar
   (300),
    createdat datetime,
    updatedat datetime,
    username varchar
   (20),   

    foreign key
   (postid) references posttable
   (postid)
);

   create table posttemptable
   (
      postid int primary key not null
      AUTO_INCREMENT,
   title varchar
      (200) not null,
   useremail varchar
      (30),
   createdat datetime, 
   updatedat datetime, 
   contents varchar
      (10000),
   username varchar
      (20),   

    foreign key
      (useremail) references usertable
      (useremail)

);
      create table typotable
      (
         word varchar(20),
         searchingdate datetime,
         wordcnt int,
         primary key ( word )
      );


      create table studentstable
      (
         username varchar(20) ,
         useremail varchar(30) ,
         curriculumlargescale varchar(50),
         curriculummediumscale varchar(50),
         curriculumsmallscale varchar(50),
         isfinish varchar(10),
          primary key (useremail, curriculumlargescale, curriculummediumscale, curriculumsmallscale),
          foreign key(useremail) references usertable(useremail),
         foreign key(curriculumlargescale, curriculummediumscale, curriculumsmallscale) references curriculumtable(curriculumlargescale, curriculummediumscale, curriculumsmallscale)
     
      )ENGINE=InnoDB DEFAULT CHARSET=utf8;


      create table curriculumtable
      (
         curriculumlargescale varchar(50),
         curriculummediumscale varchar(50),
         curriculumsmallscale varchar(50),
         nodeid int,
         parentid varchar(200),
         link varchar(200),
         primary key (curriculumlargescale, curriculummediumscale, curriculumsmallscale)
      );


      create table viewposttable
      (
         postid int not null,
         viewdate datetime,
         useremail varchar(30),
         username varchar(20),
         primary key ( postid , viewdate),
         foreign key(postid) references posttable(postid),
         foreign key(useremail) references usertable(useremail)
      );

      create table unloginviewposttable
      (
         postid int not null ,
         views int ,
         primary key (postid),
         foreign key(postid) references posttable(postid)
      );

      create table visitortable
      (
         `date` datetime not null primary key,
         visitors int

      )