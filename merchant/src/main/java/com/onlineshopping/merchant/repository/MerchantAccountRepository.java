package com.onlineshopping.merchant.repository;

import com.onlineshopping.common.model.domain.merchant.MerchantAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @description Merchant Account Repository
 * @author henryzheng
 * @date  2025/10/28
 */
@Repository
public interface MerchantAccountRepository extends JpaRepository<MerchantAccount, Integer> {

}
