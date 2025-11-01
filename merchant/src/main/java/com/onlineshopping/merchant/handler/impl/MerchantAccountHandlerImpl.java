package com.onlineshopping.merchant.handler.impl;

import com.onlineshopping.common.model.domain.merchant.MerchantAccount;
import com.onlineshopping.common.model.vo.MerchantAccountVO;
import com.onlineshopping.merchant.repository.MerchantAccountRepository;
import com.onlineshopping.merchant.handler.MerchantAccountHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

/**
 * @description Merchant Account Handler Implementation
 * @author henryzheng
 * @date  2025/10/28
 */
@Service
public class MerchantAccountHandlerImpl implements MerchantAccountHandler {

    @Autowired
    private MerchantAccountRepository merchantAccountRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean deposit(MerchantAccountVO merchantAccountVO) {
        MerchantAccount merchantAccountToSave = null;
        Date operationDate = new Date();
        Optional<MerchantAccount> existMerchantAccount = merchantAccountRepository.findById(merchantAccountVO.getId());
        if (!existMerchantAccount.isPresent()) {
            merchantAccountToSave = merchantAccountVO.to();
            merchantAccountToSave.setBalance(merchantAccountVO.getAmount());
            merchantAccountToSave.setCreateTime(operationDate);
        } else {
            merchantAccountToSave = existMerchantAccount.get();
            merchantAccountToSave.setBalance(merchantAccountToSave.getBalance().add(merchantAccountVO.getAmount()));
            merchantAccountToSave.setUpdateTime(operationDate);
        }

        merchantAccountRepository.save(merchantAccountToSave);

        return true;
    }

}
