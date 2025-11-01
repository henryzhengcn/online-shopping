create table if not exists user_account (
  id int not null primary key auto_increment,
  user_name varchar(100) not null,
  currency varchar(50) not null,
  balance number(10,2) not null,
  create_time date,
  modify_time date
);

create table if not exists merchant_account (
  id int not null primary key auto_increment,
  merchant_name varchar(100) not null,
  currency varchar(50) not null,
  balance number(10,2) not null,
  create_time date,
  modify_time date
);

create table if not exists goods (
  id int not null primary key auto_increment,
  merchant_id int not null,
  sku varchar(100) not null,
  goods_name varchar(100) not null,
  quantity number(10) not null,
  price number(8,2) not null,
  create_time date,
  modify_time date
);

create table if not exists trade_order (
  id int not null primary key auto_increment,
  user_id int not null,
  merchant_id int not null,
  sku varchar(100) not null,
  deal_datetime timestamp not null,
  quantity number(10) not null,
  price number(8,2) not null,
  amount number(8,2) not null,
  currency varchar(50) not null,
  create_time date,
  modify_time date
);

