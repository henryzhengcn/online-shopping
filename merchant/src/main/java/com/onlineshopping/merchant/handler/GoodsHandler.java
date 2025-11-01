package com.onlineshopping.merchant.handler;

import com.onlineshopping.common.model.domain.merchant.Goods;
import com.onlineshopping.common.model.vo.GoodsVO;

/**
 * @description Goods Handler
 * @author henryzheng
 * @date  2025/10/28
 */
public interface GoodsHandler {

    /**
     * 商家商品入库，需指定所属商家、商品SKU、入库数量、商品价格等必须信息。
     *
     * @param goodsVO Swagger接口已列明必须传输字段
     * @return 商家商品入库成功，则返回true, 否则返回false。
     */
    boolean inbound(GoodsVO goodsVO);

    /**
     * 商家商品出库，出库业务成功则扣减商家商品库存数量。
     *
     * @param goodsVO Swagger接口已列明必须传输字段
     * @return 商家商品出库成功，则返回true, 否则返回false。
     */
    boolean outbound(GoodsVO goodsVO);

    /**
     * 根据商家ID、商品SKU查询商家商品。
     *
     * @param merchantId 商家ID
     * @param sku 商品SKU
     * @return 商家商品信息
     */
    Goods findByMerchantIdAndSku(Integer merchantId, String sku);

}
