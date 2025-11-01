package com.onlineshopping.user.service.impl;

import com.onlineshopping.common.model.domain.merchant.Goods;
import com.onlineshopping.common.model.domain.merchant.MerchantAccount;
import com.onlineshopping.common.model.domain.user.UserAccount;
import com.onlineshopping.merchant.repository.GoodsRepository;
import com.onlineshopping.merchant.repository.MerchantAccountRepository;
import com.onlineshopping.user.repository.UserAccountRepository;
import com.onlineshopping.user.service.TradingService;
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @description Test Cases of Trading Service Implementation
 * @author henryzheng
 * @date  2025/10/28
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TradingServiceImplTest {

    @Autowired
    TradingService tradingService;

    @SpyBean
    UserAccountRepository userAccountRepository;

    @SpyBean
    MerchantAccountRepository merchantAccountRepository;

    @SpyBean
    GoodsRepository goodsRepository;

    @Test
    public void test_checkout_sku_not_found() {
        TradingService tradingServiceMock = Mockito.mock(TradingService.class);

        Mockito.when(goodsRepository.findByMerchantIdAndSku(401, "Shoe401")).thenReturn(null);

        Integer userId = 301;
        Integer merchantId = 401;
        String merchantName = "Zara";
        String sku = "Shoe401";
        Integer quantity = 10;
        String currency = "CNY";
        Mockito.doThrow(new RuntimeException()).when(tradingServiceMock)
                .checkout(userId, merchantId, merchantName, sku, quantity, currency);
    }

    @Test
    public void test_checkout_decrease_user_balance_increase_merchant_balance_decrease_goods_quantity() {
        ArgumentCaptor<UserAccount> userAccountArgumentCaptor = ArgumentCaptor.forClass(UserAccount.class);
        ArgumentCaptor<MerchantAccount> merchantAccountArgumentCaptor = ArgumentCaptor.forClass(MerchantAccount.class);
        ArgumentCaptor<Goods> goodsArgumentCaptor = ArgumentCaptor.forClass(Goods.class);

        Optional<UserAccount> userAccountOptional = Optional.of(new UserAccount(302, "HenryZheng", "CNY", new BigDecimal("10000"), new Date(), null));
        Optional<MerchantAccount> merchantAccountOptional = Optional.of(new MerchantAccount(402, "Zara", "CNY", new BigDecimal("0"), new Date(), null));
        List<Goods> goodsList = List.of(new Goods(502, 402, "Shoe402", 100, new BigDecimal("100.5"), new Date(), null));
        Mockito.when(userAccountRepository.findById(302)).thenReturn(userAccountOptional);
        Mockito.when(merchantAccountRepository.findById(402)).thenReturn(merchantAccountOptional);
        Mockito.when(goodsRepository.findByMerchantIdAndSku(402, "Shoe402")).thenReturn(goodsList);

        tradingService.checkout(302, 402, "Zara", "Shoe402", 10, "CNY");

        Mockito.verify(userAccountRepository).save(userAccountArgumentCaptor.capture());
        Mockito.verify(merchantAccountRepository).save(merchantAccountArgumentCaptor.capture());
        Mockito.verify(goodsRepository).save(goodsArgumentCaptor.capture());

        UserAccount userAccount = userAccountArgumentCaptor.getValue();
        assertThat("User account balance must be from 10000 to 8995", userAccount.getBalance(), CoreMatchers.is(new BigDecimal("8995")));
        MerchantAccount merchantAccount = merchantAccountArgumentCaptor.getValue();
        assertThat("Merchant account balance must be from 0 to 1005", merchantAccount.getBalance(), CoreMatchers.is(new BigDecimal("1005")));
        Goods goods = goodsArgumentCaptor.getValue();
        assertThat("Goods quantity must be from 100 to 90", goods.getQuantity(), CoreMatchers.is(new Integer("90")));
    }

}