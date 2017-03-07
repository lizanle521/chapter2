// test 代码会根据就近原则来取配置文件
TRUNCATE customer;
insert into customer(name,contact,telephone,email,remark) values("customer1","lizanle","15074962645","491823689@qq.com","111");
insert into customer(name,contact,telephone,email,remark) values("customer2","rose","15774962645","231823689@qq.com","121");
