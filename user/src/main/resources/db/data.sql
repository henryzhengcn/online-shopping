insert into user_account (id, user_name, currency, balance, create_time, update_time)
values(101, 'Henry Zheng', 'CNY', 10000, now(), null);

insert into merchant_account (id, merchant_name, currency, balance, create_time, update_time)
values(201, 'Zara', 'CNY', 0, now(), null);

insert into goods (id, merchant_id, sku, quantity, price, create_time, update_time)
values(301, 201, 'Shoe001', 100, 100.5, now(), null);
