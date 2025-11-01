package com.onlineshopping.merchant.repository;

import com.onlineshopping.common.model.domain.user.TradeOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @description Trade Order Repository
 * @author henryzheng
 * @date  2025/10/28
 */
@Repository("tradeOrderQueryRepository")
public interface TradeOrderRepository extends JpaRepository<TradeOrder, Integer> {

    @Query(value = "select * from trade_order where deal_datetime between ?1 and ?2", nativeQuery = true)
    List<TradeOrder> findByDealDateTime(Date beginDateTime, Date endDateTime);

}
