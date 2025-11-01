package com.onlineshopping.user.web.controller;

import com.onlineshopping.common.model.response.ResponseResult;
import com.onlineshopping.common.model.vo.UserAccountVO;
import com.onlineshopping.user.handler.UserAccountHandler;
import com.onlineshopping.user.service.TradingService;
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
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * @description User Controller
 * @author henryzheng
 * @date  2025/10/28
 */
@Tag(name = "User Module API")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserAccountHandler userAccountHandler;

    @Autowired
    private TradingService tradingService;

    @Operation(summary = "用户账户充值", description = "用户账户充值，如果用户账户不存在，则会自动创建账户，否则会累计账户金额。")
    @Parameters({
            @Parameter(name = "id", description = "用户帐户ID", in = ParameterIn.QUERY, style = ParameterStyle.FORM, required = true),
            @Parameter(name = "userName", description = "用户账户名称", in = ParameterIn.QUERY, style = ParameterStyle.FORM, required = true),
            @Parameter(name = "currency", description = "充值币种", in = ParameterIn.QUERY, style = ParameterStyle.FORM, required = true),
            @Parameter(name = "amount", description = "充值金额", in = ParameterIn.QUERY, style = ParameterStyle.FORM, required = true),
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "用户账户充值成功"),
            @ApiResponse(responseCode = "400", description = "请求参数无效"),
    })
    @PostMapping("/deposit")
    public ResponseResult<String> deposit(
            @RequestParam(name = "id", required = true, defaultValue = "") Integer id,
            @RequestParam(name = "userName", required = true, defaultValue = "") String userName,
            @RequestParam(name = "currency", required = true, defaultValue = "") String currency,
            @RequestParam(name = "amount", required = true, defaultValue = "") String amountStr
    ) {
        try {
            if (id == null || id <= 0) {
                return ResponseResult.fail("用户帐户ID格式无效");
            }
            if (!StringUtils.hasText(userName)) {
                return ResponseResult.fail("用户帐户名称不能为空");
            }
            if (!StringUtils.hasText(currency)) {
                return ResponseResult.fail("充值币种格式无效");
            }
            BigDecimal amount = new BigDecimal(amountStr);
            if (amount == null || amount.doubleValue() <= 0) {
                return ResponseResult.fail("充值金额不正确");
            }

            UserAccountVO userAccountVO = new UserAccountVO();
            userAccountVO.setId(id);
            userAccountVO.setUserName(userName);
            userAccountVO.setCurrency(currency);
            userAccountVO.setAmount(amount);
            userAccountHandler.deposit(userAccountVO);

            return ResponseResult.ok();
        } catch (Exception e) {
            return ResponseResult.fail(e.getMessage());
        }
    }

    @Operation(
            summary = "用户账户支付",
            description = "用户账户支付，会进行用户账户余额检测，在生产系统中可通过Redis分布式锁或数据库乐观锁保证多线程情况下的数据一致性。"
                    + "（此接口会被系统内部自动调用）")
    @Parameters({
            @Parameter(name = "id", description = "用户帐户ID", in = ParameterIn.QUERY, style = ParameterStyle.FORM, required = true),
            @Parameter(name = "currency", description = "支付币种", in = ParameterIn.QUERY, style = ParameterStyle.FORM, required = true),
            @Parameter(name = "amount", description = "支付金额", in = ParameterIn.QUERY, style = ParameterStyle.FORM, required = true),
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "用户账户支付成功"),
            @ApiResponse(responseCode = "400", description = "请求参数无效"),
    })
    @PostMapping("/pay")
    public ResponseResult<String> pay(
            @RequestParam(name = "id", required = true, defaultValue = "") Integer id,
            @RequestParam(name = "currency", required = true, defaultValue = "") String currency,
            @RequestParam(name = "amount", required = true, defaultValue = "") String amountStr
    ) {
        try {
            if (id == null || id <= 0) {
                return ResponseResult.fail("用户帐户ID格式无效");
            }
            if (!StringUtils.hasText(currency)) {
                return ResponseResult.fail("支付币种格式无效");
            }
            BigDecimal amount = new BigDecimal(amountStr);
            if (amount == null || amount.doubleValue() <= 0) {
                return ResponseResult.fail("支付金额不正确");
            }

            UserAccountVO userAccountVO = new UserAccountVO();
            userAccountVO.setId(id);
            userAccountVO.setCurrency(currency);
            userAccountVO.setAmount(amount);
            userAccountHandler.pay(userAccountVO);

            return ResponseResult.ok();
        } catch (Exception e) {
            return ResponseResult.fail(e.getMessage());
        }
    }

    @Operation(
            summary = "用户购买结账",
            description = "用户购买结帐，会计算用户购买的商品数量和金额，并扣除用户账户金额、增加商户账户金额、扣减商品库存数量、以及写入用户订单成交记录等。"
                    + "此接口通过事务来保证扣除用户账户金额、增加商户账户金额、扣减商品库存数量、写入用户订单成交记录等业务操作的数据一致性、整体成功或失败，"
                    + "在生产系统中可通过分布式事务消息、或最终一致性来保证同构/异构跨系统数据的一致性。"
    )
    @Parameters({
            @Parameter(name = "userId", description = "用户账户ID", in = ParameterIn.QUERY, style = ParameterStyle.FORM, required = true),
            @Parameter(name = "merchantId", description = "商家账户ID", in = ParameterIn.QUERY, style = ParameterStyle.FORM, required = true),
            @Parameter(name = "merchantName", description = "商家账户名称", in = ParameterIn.QUERY, style = ParameterStyle.FORM, required = true),
            @Parameter(name = "sku", description = "商品SKU", in = ParameterIn.QUERY, style = ParameterStyle.FORM, required = true),
            @Parameter(name = "quantity", description = "购买数量", in = ParameterIn.QUERY, style = ParameterStyle.FORM, required = true),
            @Parameter(name = "currency", description = "货币币种", in = ParameterIn.QUERY, style = ParameterStyle.FORM, required = true),
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "用户购买结账成功"),
            @ApiResponse(responseCode = "400", description = "请求参数无效"),
    })
    @PostMapping(value = "/checkout")
    public ResponseResult<Boolean> checkout(
            @RequestParam(name = "userId", required = true, defaultValue = "") Integer userId,
            @RequestParam(name = "merchantId", required = true, defaultValue = "") Integer merchantId,
            @RequestParam(name = "merchantName", required = true, defaultValue = "") String merchantName,
            @RequestParam(name = "sku", required = true, defaultValue = "") String sku,
            @RequestParam(name = "quantity", required = true, defaultValue = "") Integer quantity,
            @RequestParam(name = "currency", required = true, defaultValue = "") String currency
    ) {
        try {
            if (userId == null || userId <= 0) {
                return ResponseResult.fail("用户帐户ID格式无效");
            }
            if (merchantId == null || merchantId <= 0) {
                return ResponseResult.fail("商家账户ID格式无效");
            }
            if (!StringUtils.hasText(merchantName)) {
                return ResponseResult.fail("商家帐户名称不能为空");
            }
            if (!StringUtils.hasText(sku)) {
                return ResponseResult.fail("商品SKU不能为空");
            }
            if (quantity == null || quantity <= 0) {
                return ResponseResult.fail("购买数量不正确");
            }
            if (!StringUtils.hasText(currency)) {
                return ResponseResult.fail("货币币种不能为空");
            }

            return ResponseResult.ok(tradingService.checkout(userId, merchantId, merchantName, sku, quantity, currency));
        } catch (Exception e) {
            return ResponseResult.fail(e.getMessage());
        }
    }

}
