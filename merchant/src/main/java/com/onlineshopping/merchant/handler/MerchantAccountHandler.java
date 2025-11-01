package com.onlineshopping.merchant.handler;

import com.onlineshopping.common.model.vo.MerchantAccountVO;

/**
 * @description Merchant Account Handler
 * @author henryzheng
 * @date  2025/10/28
 */
public interface MerchantAccountHandler {

    /**
     * 商家账户入账 ，如果商家账户不存在，则会自动创建账户，否则会累计账户金额。
     *
     * @param merchantAccountVO Swagger接口已列明必须传输字段
     * @return 商家账户入账成功，则返回true, 否则返回false。
     */
    boolean deposit(MerchantAccountVO merchantAccountVO);
    
}
