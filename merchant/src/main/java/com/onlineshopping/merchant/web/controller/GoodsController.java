package com.onlineshopping.merchant.web.controller;

import com.onlineshopping.common.model.response.ResponseResult;
import com.onlineshopping.common.model.vo.GoodsVO;
import com.onlineshopping.merchant.handler.GoodsHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.enums.ParameterStyle;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * @description Goods Controller
 * @author henryzheng
 * @date  2025/10/28
 */
@Tag(name = "Merchant Module API")
@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private GoodsHandler goodsHandler;

    @Operation(summary = "商家商品入库", description="商家商品入库，需指定所属商家、商品SKU、入库数量、商品价格等必须信息。")
    @Parameters({
            @Parameter(name = "merchantId", description = "商家帐户ID", in = ParameterIn.QUERY, style = ParameterStyle.FORM, required = true),
            @Parameter(name = "sku", description = "商品SKU", in = ParameterIn.QUERY, style = ParameterStyle.FORM, required = true),
            @Parameter(name = "quantity", description = "入库数量", in = ParameterIn.QUERY, style = ParameterStyle.FORM, required = true),
            @Parameter(name = "price", description = "商品价格", in = ParameterIn.QUERY, style = ParameterStyle.FORM, required = true),
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "商家商品入库成功"),
            @ApiResponse(responseCode = "400", description = "请求参数无效"),
    })
    @PostMapping("/inbound")
    public ResponseResult<String> inbound(
            @RequestParam(name = "merchantId", required = true, defaultValue = "") Integer merchantId,
            @RequestParam(name = "sku", required = true, defaultValue = "") String sku,
            @RequestParam(name = "quantity", required = true, defaultValue = "0") Integer quantity,
            @RequestParam(name = "price", required = true, defaultValue = "0") String priceStr
    ) {
        try {
            if (merchantId == null || merchantId <= 0) {
                return ResponseResult.fail("商家帐户ID格式无效");
            }
            if (!StringUtils.hasText(sku)) {
                return ResponseResult.fail("商品SKU不能为空");
            }
            if (quantity == null || quantity <= 0) {
                return ResponseResult.fail("商品入库数量无效");
            }
            BigDecimal price = new BigDecimal(priceStr);
            if (price == null || price.doubleValue() <= 0) {
                return ResponseResult.fail("商品入库价格无效");
            }

            GoodsVO goodsVO = new GoodsVO();
            goodsVO.setMerchantId(merchantId);
            goodsVO.setSku(sku);
            goodsVO.setQuantity(quantity);
            goodsVO.setPrice(price);
            goodsHandler.inbound(goodsVO);

            return ResponseResult.ok();
        } catch (Exception e) {
            return ResponseResult.fail(e.getMessage());
        }
    }

}
