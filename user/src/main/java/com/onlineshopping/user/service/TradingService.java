package com.onlineshopping.user.service;

/**
 * @description Trading Service
 * @author henryzheng
 * @date  2025/10/28
 */
public interface TradingService {

    /**
     * 用户购买结帐，会计算用户购买的商品数量和金额，并扣除用户账户金额、增加商户账户金额、扣减商品库存数量、以及写入用户订单成交记录等。
     * 此接口通过事务来保证扣除用户账户金额、增加商户账户金额、扣减商品库存数量、写入用户订单成交记录等业务操作的数据一致性、整体成功或失败，
     * 在生产系统中可通过分布式事务消息、或最终一致性来保证同构/异构跨系统数据的一致性。
     *
     * @param userId 用户账户ID
     * @param merchantId 商家账户ID
     * @param merchantName 商家账户名称
     * @param sku 商品SKU
     * @param quantity 购买数量
     * @param currency 货币币种
     * @return 用户购买结帐成功，则返回true, 否则返回false。
     */
    boolean checkout(Integer userId, Integer merchantId, String merchantName, String sku, Integer quantity, String currency);

}
