create database studentsapi;

use studentsapi;

insert into student_tbl(id, first_name, last_name, address, contact, dob, email, nationality, result1, result2) values(1, "Imam", "Biplob", "Mohakhali, Dhaka", "+8801521559190", "1999-08-15", "imam@gmail.com", "Bangladeshi", "A+", "A+");

update student_tbl set email = "nafisha@gmail.com" where id = 3;

delete from student_tbl where id = 4;

select * from student_tbl;
