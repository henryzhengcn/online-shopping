package com.onlineshopping.user.service.impl;

import com.onlineshopping.common.model.domain.merchant.Goods;
import com.onlineshopping.common.model.domain.user.TradeOrder;
import com.onlineshopping.common.model.vo.GoodsVO;
import com.onlineshopping.common.model.vo.MerchantAccountVO;
import com.onlineshopping.common.model.vo.UserAccountVO;
import com.onlineshopping.merchant.handler.GoodsHandler;
import com.onlineshopping.merchant.handler.MerchantAccountHandler;
import com.onlineshopping.user.handler.UserAccountHandler;
import com.onlineshopping.user.repository.TradeOrderRepository;
import com.onlineshopping.user.service.TradingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @description Trading Service Implementation
 * @author henryzheng
 * @date  2025/10/28
 */
@Service
public class TradingServiceImpl implements TradingService {

    @Autowired
    private UserAccountHandler userAccountHandler;

    @Autowired
    private MerchantAccountHandler merchantAccountHandler;

    @Autowired
    private GoodsHandler goodsHandler;

    @Autowired
    private TradeOrderRepository tradeOrderRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean checkout(Integer userId, Integer merchantId, String merchantName, String sku, Integer quantity,
                            String currency) {
        // 用户结账购买商品必须存在
        Goods existGoods = goodsHandler.findByMerchantIdAndSku(merchantId, sku);
        if (existGoods == null) {
            throw new RuntimeException(String.format("Checkout failed due to sku not found, merchantId=%s, sku=%s",
                    merchantId, sku));
        }

        // 计算下单商品购买金额
        BigDecimal checkoutAmount = new BigDecimal(quantity * existGoods.getPrice().doubleValue());

        // 从用户账户扣除本次购买金额
        UserAccountVO userAccountVO = new UserAccountVO();
        userAccountVO.setId(userId);
        userAccountVO.setCurrency(currency);
        userAccountVO.setAmount(checkoutAmount);
        userAccountHandler.pay(userAccountVO);

        // 为商户账户增加本次购买金额
        MerchantAccountVO merchantAccountVO = new MerchantAccountVO();
        merchantAccountVO.setId(merchantId);
        merchantAccountVO.setMerchantName(merchantName);
        merchantAccountVO.setCurrency(currency);
        merchantAccountVO.setAmount(checkoutAmount);
        merchantAccountHandler.deposit(merchantAccountVO);

        // 从商户商品库存扣减本次购买数量
        GoodsVO goodsVO = new GoodsVO();
        goodsVO.setMerchantId(merchantId);
        goodsVO.setSku(sku);
        goodsVO.setQuantity(quantity);
        goodsHandler.outbound(goodsVO);

        // 写入用户订单成交记录，保存必要的信息
        Date dealDatetime = new Date();
        TradeOrder tradeOrder = new TradeOrder();
        tradeOrder.setUserId(userId);
        tradeOrder.setMerchantId(merchantId);
        tradeOrder.setSku(sku);
        tradeOrder.setDealDatetime(dealDatetime);
        tradeOrder.setQuantity(quantity);
        tradeOrder.setPrice(existGoods.getPrice());
        tradeOrder.setAmount(checkoutAmount);
        tradeOrder.setCurrency(currency);
        tradeOrder.setCreateTime(dealDatetime);
        tradeOrderRepository.save(tradeOrder);

        return true;
    }

}
