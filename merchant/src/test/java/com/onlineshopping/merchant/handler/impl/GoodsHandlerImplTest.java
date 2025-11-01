package com.onlineshopping.merchant.handler.impl;

import com.onlineshopping.common.model.domain.merchant.Goods;
import com.onlineshopping.common.model.vo.GoodsVO;
import com.onlineshopping.merchant.handler.GoodsHandler;
import com.onlineshopping.merchant.repository.GoodsRepository;
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
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @description Test Cases of Goods Handler Implementation
 * @author henryzheng
 * @date  2025/10/28
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class GoodsHandlerImplTest {

    @InjectMocks
    GoodsHandler goodsHandler = new GoodsHandlerImpl();

    @Mock
    GoodsRepository goodsRepository;

    @Test
    public void test_inbound_new_goods() {
        ArgumentCaptor<Goods> goodsArgumentCaptor = ArgumentCaptor.forClass(Goods.class);

        Mockito.when(goodsRepository.findByMerchantIdAndSku(201, "Shoe201")).thenReturn(List.of());

        GoodsVO goodsVO = new GoodsVO();
        goodsVO.setMerchantId(201);
        goodsVO.setSku("Shoe201");
        goodsVO.setQuantity(100);
        goodsHandler.inbound(goodsVO);

        Mockito.verify(goodsRepository).save(goodsArgumentCaptor.capture());

        Goods goods = goodsArgumentCaptor.getValue();
        assertThat("Goods quantity <> 1100", goods.getQuantity(), CoreMatchers.is(100));
        assertThat("Goods quantity <> 1100", goods.getCreateTime(), CoreMatchers.notNullValue());

        assertThat("Goods must be stored with quantity = 100", goods.getQuantity(), CoreMatchers.is(100));
        assertThat("Goods must be stored with created time not null", goods.getCreateTime(), CoreMatchers.notNullValue());
    }

    @Test
    public void test_inbound_increase_quantity() {
        ArgumentCaptor<Goods> goodsArgumentCaptor = ArgumentCaptor.forClass(Goods.class);

        List<Goods> goodsList = List.of(new Goods(201, 201, "Shoe201", 1000, new BigDecimal("100"), new Date(), null));
        Mockito.when(goodsRepository.findByMerchantIdAndSku(201, "Shoe201")).thenReturn(goodsList);

        GoodsVO goodsVO = new GoodsVO();
        goodsVO.setMerchantId(201);
        goodsVO.setSku("Shoe201");
        goodsVO.setQuantity(100);
        goodsHandler.inbound(goodsVO);

        Mockito.verify(goodsRepository).save(goodsArgumentCaptor.capture());

        Goods goods = goodsArgumentCaptor.getValue();
        assertThat("Goods quantity <> 1100", goods.getQuantity(), CoreMatchers.is(1100));
    }

    @Test
    public void test_outbound_goods_not_found() {
        GoodsHandler goodsHandlerMock = Mockito.mock(GoodsHandler.class);

        Mockito.when(goodsRepository.findById(202)).thenReturn(null);

        GoodsVO goodsVO = new GoodsVO();
        goodsVO.setMerchantId(202);
        goodsVO.setSku("Shoe202");
        goodsVO.setQuantity(100);
        Mockito.doThrow(new RuntimeException()).when(goodsHandlerMock).outbound(goodsVO);
    }

    @Test
    public void test_outbound_decrease_quantity() {
        ArgumentCaptor<Goods> goodsArgumentCaptor = ArgumentCaptor.forClass(Goods.class);

        List<Goods> goodsList = List.of(new Goods(202, 202, "Shoe202", 1000, new BigDecimal("100"), new Date(), null));
        Mockito.when(goodsRepository.findByMerchantIdAndSku(202, "Shoe202")).thenReturn(goodsList);

        GoodsVO goodsVO = new GoodsVO();
        goodsVO.setMerchantId(202);
        goodsVO.setSku("Shoe202");
        goodsVO.setQuantity(100);
        goodsHandler.outbound(goodsVO);

        Mockito.verify(goodsRepository).save(goodsArgumentCaptor.capture());

        Goods goods = goodsArgumentCaptor.getValue();
        assertThat("Goods quantity <> 900", goods.getQuantity(), CoreMatchers.is(900));
    }

    @Test
    public void test_findByMerchantIdAndSku_return_null() {
        Mockito.when(goodsRepository.findByMerchantIdAndSku(203, "Shoe203")).thenReturn(null);

        Goods goods = goodsHandler.findByMerchantIdAndSku(203, "Shoe203");

        assertThat("No goods found", goods, CoreMatchers.nullValue());
    }

    @Test
    public void test_findByMerchantIdAndSku_return_one_goods() {
        List<Goods> goodsList = List.of(new Goods(203, 203, "Shoe203", 1000, new BigDecimal("100"), new Date(), null));
        Mockito.when(goodsRepository.findByMerchantIdAndSku(203, "Shoe203")).thenReturn(goodsList);

        Goods goods = goodsHandler.findByMerchantIdAndSku(203, "Shoe203");

        assertThat("One goods found with quantity = 1000", goods.getQuantity(), CoreMatchers.is(1000));
    }

}