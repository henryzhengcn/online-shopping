package com.onlineshopping.merchant.handler.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.onlineshopping.common.model.domain.merchant.Goods;
import com.onlineshopping.common.model.vo.GoodsVO;
import com.onlineshopping.merchant.handler.MerchantAccountHandler;
import com.onlineshopping.merchant.repository.GoodsRepository;
import com.onlineshopping.merchant.handler.GoodsHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @description Goods Handler Implementation
 * @author henryzheng
 * @date  2025/10/28
 */
@Service
public class GoodsHandlerImpl implements GoodsHandler {

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private MerchantAccountHandler merchantAccountHandler;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean inbound(GoodsVO goodsVO) {
        Goods goodsToSave = null;
        Date operationDate = new Date();
        Goods existGoods = this.findByMerchantIdAndSku(goodsVO.getMerchantId(), goodsVO.getSku());
        if (existGoods == null) {
            goodsToSave = goodsVO.to();
            goodsToSave.setCreateTime(operationDate);
        } else {
            goodsToSave = existGoods;
            goodsToSave.setQuantity(existGoods.getQuantity() + goodsVO.getQuantity());
            goodsToSave.setUpdateTime(operationDate);
        }

        goodsRepository.save(goodsToSave);

        return true;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean outbound(GoodsVO goodsVO) {
        Goods existGoods = this.findByMerchantIdAndSku(goodsVO.getMerchantId(), goodsVO.getSku());
        if (existGoods == null) {
            throw new RuntimeException("Goods not found");
        }

        // 商品超卖检测，通过Redis分布式锁或数据库乐观锁保证多线程情况下的数据正确性。
        Integer latestQuantity = existGoods.getQuantity() - goodsVO.getQuantity();
        if (latestQuantity < 0) {
            throw new RuntimeException("Insufficient goods quantity");
        }

        existGoods.setQuantity(latestQuantity);
        existGoods.setUpdateTime(new Date());
        goodsRepository.save(existGoods);

        return true;
    }

    @Override
    public Goods findByMerchantIdAndSku(Integer merchantId, String sku) {
        List<Goods> goodsList = goodsRepository.findByMerchantIdAndSku(merchantId, sku);
        return CollectionUtil.isEmpty(goodsList) ? null : goodsList.get(0);
    }

}
