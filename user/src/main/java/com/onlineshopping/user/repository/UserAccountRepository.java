package com.onlineshopping.user.repository;

import com.onlineshopping.common.model.domain.user.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @description User Account Repository
 * @author henryzheng
 * @date  2025/10/28
 */
public interface UserAccountRepository extends JpaRepository<UserAccount, Integer> {

}
