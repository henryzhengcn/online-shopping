package com.onlineshopping.user.handler;

import com.onlineshopping.common.model.vo.UserAccountVO;

/**
 * @description User Account Handler
 * @author henryzheng
 * @date  2025/10/28
 */
public interface UserAccountHandler {

    /**
     * 用户账户充值，如果用户账户不存在，则会自动创建账户，否则会累计账户金额。
     *
     * @param userAccountVO Swagger接口已列明必须传输字段
     * @return 用户账户充值成功，则返回true, 否则返回false。
     */
    boolean deposit(UserAccountVO userAccountVO);

    /**
     * 用户账户支付，会进行用户账户余额检测，在生产系统中可通过Redis分布式锁或数据库乐观锁保证多线程情况下的数据一致性。
     *
     * @param userAccountVO Swagger接口已列明必须传输字段
     * @return 用户账户支付成功，则返回true, 否则返回false。
     */
    boolean pay(UserAccountVO userAccountVO);

}
