package com.onlineshopping.merchant.handler.impl;

import com.onlineshopping.common.model.domain.merchant.MerchantAccount;
import com.onlineshopping.common.model.vo.MerchantAccountVO;
import com.onlineshopping.merchant.handler.MerchantAccountHandler;
import com.onlineshopping.merchant.repository.MerchantAccountRepository;
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @description Test Cases of Merchant Account Handler Implementation
 * @author henryzheng
 * @date  2025/10/28
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MerchantAccountHandlerImplTest {

    @InjectMocks
    MerchantAccountHandler merchantAccountHandler = new MerchantAccountHandlerImpl();

    @Mock
    MerchantAccountRepository merchantAccountRepository;

    @Test
    public void test_deposit_open_account() {
        ArgumentCaptor<MerchantAccount> merchantAccountArgumentCaptor = ArgumentCaptor.forClass(MerchantAccount.class);

        Mockito.when(merchantAccountRepository.findById(201)).thenReturn(Optional.empty());

        MerchantAccountVO merchantAccountVO = new MerchantAccountVO();
        merchantAccountVO.setId(201);
        merchantAccountVO.setMerchantName("Zara");
        merchantAccountVO.setCurrency("CNY");
        merchantAccountVO.setAmount(new BigDecimal("500"));
        merchantAccountHandler.deposit(merchantAccountVO);

        Mockito.verify(merchantAccountRepository).save(merchantAccountArgumentCaptor.capture());

        MerchantAccount merchantAccount = merchantAccountArgumentCaptor.getValue();
        assertThat("A new merchant account must be opened and balance = 500", merchantAccount.getBalance(), CoreMatchers.is(new BigDecimal("500")));
        assertThat("A new merchant account must be opened with created time not null", merchantAccount.getCreateTime(), CoreMatchers.notNullValue());
    }

    @Test
    public void test_deposit_increase_balance() {
        ArgumentCaptor<MerchantAccount> merchantAccountArgumentCaptor = ArgumentCaptor.forClass(MerchantAccount.class);

        Optional<MerchantAccount> merchantAccountOptional = Optional.of(new MerchantAccount(201, "Zara", "CNY", new BigDecimal("1000"), new Date(), null));
        Mockito.when(merchantAccountRepository.findById(201)).thenReturn(merchantAccountOptional);

        MerchantAccountVO merchantAccountVO = new MerchantAccountVO();
        merchantAccountVO.setId(201);
        merchantAccountVO.setMerchantName("Zara");
        merchantAccountVO.setCurrency("CNY");
        merchantAccountVO.setAmount(new BigDecimal("500"));
        merchantAccountHandler.deposit(merchantAccountVO);

        Mockito.verify(merchantAccountRepository).save(merchantAccountArgumentCaptor.capture());

        MerchantAccount merchantAccount = merchantAccountArgumentCaptor.getValue();
        assertThat("Merchant account balance <> 1500", merchantAccount.getBalance(), CoreMatchers.is(new BigDecimal("1500")));
    }

}