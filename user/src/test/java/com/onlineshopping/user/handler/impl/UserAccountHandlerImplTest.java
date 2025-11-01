package com.onlineshopping.user.handler.impl;

import com.onlineshopping.common.model.domain.user.UserAccount;
import com.onlineshopping.common.model.vo.UserAccountVO;
import com.onlineshopping.user.handler.UserAccountHandler;
import com.onlineshopping.user.repository.UserAccountRepository;
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
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @description Test Cases of User Account Handler Implementation
 * @author henryzheng
 * @date  2025/10/28
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserAccountHandlerImplTest {

    @InjectMocks
    UserAccountHandler userAccountHandler = new UserAccountHandlerImpl();

    @Mock
    UserAccountRepository userAccountRepository;

    @Test
    public void test_deposit_open_account() {
        ArgumentCaptor<UserAccount> userAccountArgumentCaptor = ArgumentCaptor.forClass(UserAccount.class);

        Mockito.when(userAccountRepository.findById(101)).thenReturn(Optional.empty());

        UserAccountVO userAccountVO = new UserAccountVO();
        userAccountVO.setId(101);
        userAccountVO.setUserName("HenryZheng");
        userAccountVO.setCurrency("CNY");
        userAccountVO.setAmount(new BigDecimal("500"));
        userAccountHandler.deposit(userAccountVO);

        Mockito.verify(userAccountRepository).save(userAccountArgumentCaptor.capture());

        UserAccount userAccount = userAccountArgumentCaptor.getValue();
        assertThat("A new user account must be opened and balance = 500", userAccount.getBalance(), CoreMatchers.is(new BigDecimal("500")));
        assertThat("A new user account must be opened with created time not null", userAccount.getCreateTime(), CoreMatchers.notNullValue());
    }

    @Test
    public void test_deposit_increase_balance() {
        ArgumentCaptor<UserAccount> userAccountArgumentCaptor = ArgumentCaptor.forClass(UserAccount.class);

        Optional<UserAccount> userAccountOptional = Optional.of(new UserAccount(101, "HenryZheng", "CNY", new BigDecimal("1000"), new Date(), null));
        Mockito.when(userAccountRepository.findById(101)).thenReturn(userAccountOptional);

        UserAccountVO userAccountVO = new UserAccountVO();
        userAccountVO.setId(101);
        userAccountVO.setUserName("HenryZheng");
        userAccountVO.setCurrency("CNY");
        userAccountVO.setAmount(new BigDecimal("500"));
        userAccountHandler.deposit(userAccountVO);

        Mockito.verify(userAccountRepository).save(userAccountArgumentCaptor.capture());

        UserAccount userAccount = userAccountArgumentCaptor.getValue();
        assertThat("User account balance <> 1500", userAccount.getBalance(), CoreMatchers.is(new BigDecimal("1500")));
    }

    @Test
    public void test_pay_account_not_found() {
        UserAccountHandler userAccountHandlerMock = Mockito.mock(UserAccountHandler.class);

        Mockito.when(userAccountRepository.findById(102)).thenReturn(null);

        UserAccountVO userAccountVO = new UserAccountVO();
        userAccountVO.setId(102);
        userAccountVO.setUserName("HenryZheng");
        userAccountVO.setCurrency("CNY");
        userAccountVO.setAmount(new BigDecimal("500"));
        Mockito.doThrow(new RuntimeException()).when(userAccountHandlerMock).pay(userAccountVO);
    }

    @Test
    public void test_pay_decrease_balance() {
        ArgumentCaptor<UserAccount> userAccountArgumentCaptor = ArgumentCaptor.forClass(UserAccount.class);

        Optional<UserAccount> userAccountOptional = Optional.of(new UserAccount(102, "HenryZheng", "CNY", new BigDecimal("1000"), new Date(), null));
        Mockito.when(userAccountRepository.findById(102)).thenReturn(userAccountOptional);

        UserAccountVO userAccountVO = new UserAccountVO();
        userAccountVO.setId(102);
        userAccountVO.setUserName("HenryZheng");
        userAccountVO.setCurrency("CNY");
        userAccountVO.setAmount(new BigDecimal("200"));
        userAccountHandler.pay(userAccountVO);

        Mockito.verify(userAccountRepository).save(userAccountArgumentCaptor.capture());

        UserAccount userAccount = userAccountArgumentCaptor.getValue();
        assertThat("User account balance <> 800", userAccount.getBalance(), CoreMatchers.is(new BigDecimal("800")));
    }

}