package com.onlineshopping.merchant.job;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.onlineshopping.common.model.domain.merchant.MerchantAccount;
import com.onlineshopping.common.model.domain.user.TradeOrder;
import com.onlineshopping.merchant.repository.MerchantAccountRepository;
import com.onlineshopping.merchant.repository.TradeOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @description Merchant Settlement Task
 * @author henryzheng
 * @date  2025/10/28
 */
@Component
public class MerchantSettlementTask {

    @Autowired
    TradeOrderRepository tradeOrderRepository;

    @Autowired
    MerchantAccountRepository merchantAccountRepository;

    /**
     * 商家结算批处理任务，每日0点执行，会计算当日商家发生的所有销售金额、并与商家账户余额进行比对，并给出比对结果。
     */
    @Scheduled(cron = "0 * * * * *")
    public void dailySettlement() {
        System.out.println("###################################### 商家每日结算开始 ###################################### ");
        System.out.println("====================================== 商家每日结算结果 ====================================== ");

        Date now = new Date();
        List<TradeOrder> tradeOrders = tradeOrderRepository.findByDealDateTime(DateUtil.beginOfDay(now), DateUtil.endOfDay(now));
        if (CollectionUtil.isEmpty(tradeOrders)) {
            System.out.printf("结算日期: %s 无订单成交记录，不需结算。\n", DateUtil.format(now, "yyyy-MM-dd"));
        }

        Map<Integer, Double> tradeAmountGroupByMerchant = tradeOrders.stream()
                .collect(Collectors.groupingBy(TradeOrder::getMerchantId,
                        Collectors.summingDouble(order -> order.getAmount().doubleValue())));
        tradeAmountGroupByMerchant.forEach((merchantId, tradeAmount) -> {
            Optional<MerchantAccount> merchantAccount = merchantAccountRepository.findById(merchantId);
            if (!merchantAccount.isPresent()) {
                System.out.printf("结算日期: %s 商家ID: %s 销售金额: %s 【当日商家发生销售金额，但商家账户不存在，请核对】\n",
                        DateUtil.format(now, "yyyy-MM-dd"), merchantId, tradeAmount);
            } else {
                Double merchantBalance = merchantAccount.get().getBalance().doubleValue();
                Double diff = tradeAmount - merchantBalance;
                if (diff == 0) {
                    System.out.printf("结算日期: %s 商家ID: %s 销售金额: %s 商家余额: %s 【商家销售金额和商家账户余额结算一致】\n",
                            DateUtil.format(now, "yyyy-MM-dd"), merchantId, tradeAmount, merchantBalance);
                } else {
                    System.out.printf("结算日期: %s 商家ID: %s 销售金额: %s 商家余额: %s 【商家销售金额和商家账户余额结算不一致】\n",
                            DateUtil.format(now, "yyyy-MM-dd"), merchantId, tradeAmount, merchantBalance);
                }
            }
        });

        System.out.println("###################################### 商家每日结算结束 ###################################### ");
    }

}
