package com.onlineshopping.merchant.repository;

import com.onlineshopping.common.model.domain.merchant.Goods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description Goods Repository
 * @author henryzheng
 * @date  2025/10/28
 */
@Repository
public interface GoodsRepository extends JpaRepository<Goods, Integer> {

    /**
     * 根据商家ID、商品SKU查询商家商品。
     *
     * @param merchantId 商家ID
     * @param sku 商品SKU
     * @return 商家商品信息列表
     */
    List<Goods> findByMerchantIdAndSku(Integer merchantId, String sku);

}
