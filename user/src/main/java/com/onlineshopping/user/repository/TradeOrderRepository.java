package com.onlineshopping.user.repository;

import com.onlineshopping.common.model.domain.user.TradeOrder;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @description Trade Order Repository
 * @author henryzheng
 * @date  2025/10/28
 */
public interface TradeOrderRepository extends JpaRepository<TradeOrder, Integer> {

}
