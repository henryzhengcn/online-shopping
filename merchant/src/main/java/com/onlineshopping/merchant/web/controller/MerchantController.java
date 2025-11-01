package com.onlineshopping.merchant.web.controller;

import com.onlineshopping.common.model.response.ResponseResult;
import com.onlineshopping.common.model.vo.MerchantAccountVO;
import com.onlineshopping.merchant.handler.MerchantAccountHandler;
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
 * @description Merchant Controller
 * @author henryzheng
 * @date  2025/10/28
 */
@Tag(name = "Merchant Module API")
@RestController
@RequestMapping("/merchant")
public class MerchantController {

    @Autowired
    private MerchantAccountHandler merchantAccountHandler;

    @Operation(summary = "商家账户充值", description="商家账户充值，如果商家账户不存在，则会自动创建账户，否则会累计账户金额。（此接口会被系统内部自动调用）")
    @Parameters({
            @Parameter(name = "id", description = "商家帐户ID", in = ParameterIn.QUERY, style = ParameterStyle.FORM, required = true),
            @Parameter(name = "currency", description = "充值币种", in = ParameterIn.QUERY, style = ParameterStyle.FORM, required = true),
            @Parameter(name = "amount", description = "充值金额", in = ParameterIn.QUERY, style = ParameterStyle.FORM, required = true),
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "商家账户充值成功"),
            @ApiResponse(responseCode = "400", description = "请求参数无效"),
    })
    @PostMapping("/deposit")
    public ResponseResult<String> deposit(
            @RequestParam(name = "id", required = true, defaultValue = "") Integer id,
            @RequestParam(name = "currency", required = true, defaultValue = "") String currency,
            @RequestParam(name = "amount", required = true, defaultValue = "") String amountStr
    ) {
        try {
            if (id == null || id <= 0) {
                return ResponseResult.fail("商家帐户ID格式无效");
            }
            if (!StringUtils.hasText(currency)) {
                return ResponseResult.fail("充值币种格式无效");
            }
            BigDecimal amount = new BigDecimal(amountStr);
            if (amount == null || amount.doubleValue() <= 0) {
                return ResponseResult.fail("充值金额不正确");
            }

            MerchantAccountVO merchantAccountVO = new MerchantAccountVO();
            merchantAccountVO.setId(id);
            merchantAccountVO.setCurrency(currency);
            merchantAccountVO.setAmount(amount);
            merchantAccountHandler.deposit(merchantAccountVO);

            return ResponseResult.ok();
        } catch (Exception e) {
            return ResponseResult.fail(e.getMessage());
        }
    }

}
