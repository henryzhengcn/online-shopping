package com.onlineshopping.user.handler.impl;

import com.onlineshopping.common.model.domain.user.UserAccount;
import com.onlineshopping.common.model.vo.UserAccountVO;
import com.onlineshopping.user.handler.UserAccountHandler;
import com.onlineshopping.user.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

/**
 * @description User Account Handler Implementation
 * @author henryzheng
 * @date  2025/10/28
 */
@Service
public class UserAccountHandlerImpl implements UserAccountHandler {

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean deposit(UserAccountVO userAccountVO) {
        UserAccount userAccountToSave = null;
        Date operationDate = new Date();
        Optional<UserAccount> existUserAccount = userAccountRepository.findById(userAccountVO.getId());
        if (!existUserAccount.isPresent()) {
            userAccountToSave = userAccountVO.to();
            userAccountToSave.setBalance(userAccountVO.getAmount());
            userAccountToSave.setCreateTime(operationDate);
        } else {
            userAccountToSave = existUserAccount.get();
            userAccountToSave.setBalance(userAccountToSave.getBalance().add(userAccountVO.getAmount()));
            userAccountToSave.setUpdateTime(operationDate);
        }

        userAccountRepository.save(userAccountToSave);

        return true;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean pay(UserAccountVO userAccountVO) {
        UserAccount existUserAccount = userAccountRepository.findById(userAccountVO.getId())
                .orElseThrow(() -> new RuntimeException("User account not found"));

        // 用户账户余额检测，在生产系统中可通过Redis分布式锁或数据库乐观锁保证多线程情况下的数据一致性。
        BigDecimal latestBalance = existUserAccount.getBalance().subtract(userAccountVO.getAmount());
        if (latestBalance.doubleValue() < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        existUserAccount.setBalance(latestBalance);
        existUserAccount.setUpdateTime(new Date());
        userAccountRepository.save(existUserAccount);

        return true;
    }

}
