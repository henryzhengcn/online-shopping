# Online Shopping工程说明

## 1. 使用到的技术框架
- Springboot
- H2内存数据库
- Spring Data JPA
- Spring DOC OpenAPI 3/Swagger
- RESTful
- JUint
- Hamcrest

## 2. 如何运行系统
### 2.1 前置依赖环境
- JDK17, 用于提供Java运行时环境
- Git，用于从Github上下载源代码
- IntelliJ IDEA，用于浏览源代码和运行系统、测试用例等

### 2.2 运行Online Shopping系统
- 从[github/online-shopping](https://github.com/henryzhengcn/online-shopping)下载源代码到本地目录
- 使用IntelliJ IDEA打开下载的Online Shopping工程
- 打开工程源代码OnlineShoppingUserApplication SpringBoot启动文件，点击绿色三角按钮即可运行系统
  - ![launch_system](https://github.com/henryzhengcn/online-shopping/blob/master/doc/images/2_2_1_launch_system.png)
- 通过 [用户模块/用户账户充值](http://localhost:8080/onlineshopping/swagger-ui/index.html#/User%20Module%20API/deposit) 为用户开设帐户并充值金额。详细的接口功能和字段说明请参考Swagger文档，下同。
  - ![user_deposit_1](https://github.com/henryzhengcn/online-shopping/blob/master/doc/images/2_2_2_user_deposit_1.png)
  - ![user_deposit_2](https://github.com/henryzhengcn/online-shopping/blob/master/doc/images/2_2_2_user_deposit_2.png)
- 通过 [商家模块/商家商品入库](http://localhost:8080/onlineshopping/swagger-ui/index.html#/Merchant%20Module%20API/inbound) 对商家商品进行入库。
  - ![goods_inbound_1](https://github.com/henryzhengcn/online-shopping/blob/master/doc/images/2_2_3_goods_inbound_1.png)
  - ![goods_inbound_2](https://github.com/henryzhengcn/online-shopping/blob/master/doc/images/2_2_3_goods_inbound_2.png)
- 通过 [用户模块/用户购买结帐](http://localhost:8080/onlineshopping/swagger-ui/index.html#/User%20Module%20API/checkout) 为用户购买指定商品，并扣除用户账户金额、增加商户账户金额、扣减商品库存数量、以及写入用户订单成交记录等。
  - 用户账户初始余额为1000 CNY，商家账户初始余额为0 CNY，商家商品库存初始数量为500。
  - 第1次用户购买商品数量1件、购买商品金额100.50 CNY，用户账户余额为899.50 CNY，商家账户余额为100.50 CNY，商家商品库存数量为499。 
    - ![user_checkout_1_1](https://github.com/henryzhengcn/online-shopping/blob/master/doc/images/2_2_4_user_checkout_1_1.png)
    - ![user_checkout_1_2](https://github.com/henryzhengcn/online-shopping/blob/master/doc/images/2_2_4_user_checkout_1_2.png)
  - 第2次用户购买商品数量2件、购买商品金额201.00 CNY，用户账户余额为698.50 CNY，商家账户余额为301.50 CNY，商家商品库存数量为497。
    - ![user_checkout_2_1](https://github.com/henryzhengcn/online-shopping/blob/master/doc/images/2_2_4_user_checkout_2_1.png)
    - ![user_checkout_2_2](https://github.com/henryzhengcn/online-shopping/blob/master/doc/images/2_2_4_user_checkout_2_2.png)
  - 第3次用户购买商品数量3件、购买商品金额301.50 CNY，用户账户余额为397.00 CNY，商家账户余额为603.00 CNY，商家商品库存数量为494。
    - ![user_checkout_3_1](https://github.com/henryzhengcn/online-shopping/blob/master/doc/images/2_2_4_user_checkout_3_1.png)
    - ![user_checkout_3_2](https://github.com/henryzhengcn/online-shopping/blob/master/doc/images/2_2_4_user_checkout_3_2.png)
  - 第4/5/6次用户购买商品数量1件、购买商品金额100.50 CNY，用户账户余额为95.50 CNY，商家账户余额为904.50 CNY，商家商品库存数量为491。
    - ![user_checkout_4_1](https://github.com/henryzhengcn/online-shopping/blob/master/doc/images/2_2_4_user_checkout_4_1.png)
  - 第7次用户购买商品数量1件、购买商品金额100.5 CNY，系统提示用户账户余额不足，用户购买失败。
    - ![user_checkout_5_1](https://github.com/henryzhengcn/online-shopping/blob/master/doc/images/2_2_4_user_checkout_5_1.png)
- 系统自动运行商家结算批处理任务([MerchantSettlementTask.dailySettlement](https://github.com/henryzhengcn/online-shopping/blob/master/merchant/src/master/java/com/onlineshopping/merchant/job/MerchantSettlementTask.java))，每日0点执行（为调试方便每分钟执行），会计算当日商家发生的所有销售金额、并与商家账户余额进行比对，并给出比对结果。
  - ![merchant_settlement](https://github.com/henryzhengcn/online-shopping/blob/master/doc/images/2_2_5_merchant_settlement.png)

### 2.3 运行Online Shopping测试用例
- 打开工程源代码test/java下的测试用例（例如[UserAccountHandlerImplTest](https://github.com/henryzhengcn/online-shopping/blob/master/user/src/test/java/com/onlineshopping/user/handler/impl/UserAccountHandlerImplTest.java)），点击绿色三角按钮即可运行所有测试用例
  - ![testcases](https://github.com/henryzhengcn/online-shopping/blob/master/doc/images/2_3_testcases.png)

### 3. 相关资源
- 源代码位置：https://github.com/henryzhengcn/online-shopping
- Online Shopping系统Swagger API文档地址：http://localhost:8080/onlineshopping/swagger-ui/index.html
  - ![swagger](https://github.com/henryzhengcn/online-shopping/blob/master/doc/images/3_1_swagger.png)
- Online Shopping系统数据库后台地址：http://localhost:8080/onlineshopping/h2/login.do
  - 数据库JDBC URL: jdbc:h2:~/onlineshopping;AUTO_SERVER=TRUE
  - 账号：root
  - 密码：123456
  - ![database_1](https://github.com/henryzhengcn/online-shopping/blob/master/doc/images/3_2_database_1.png)
  - ![database_2](https://github.com/henryzhengcn/online-shopping/blob/master/doc/images/3_2_database_2.png)
- 数据库初始化Schema位于：resources/db，系统会自动执行数据库初始化，不需手工执行
