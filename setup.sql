create user 'newuser'@'localhost' identified by 'secret';

create database "shortened_urls";

create table urls 
   (url   char(100),
    shorturl    char(100),
    primary key (url)
);

grant all privileges on "shortened_urls.urls" to 'newuser'@'localhost';