package com.onlineshopping.common.model.vo;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.onlineshopping.common.model.domain.merchant.MerchantAccount;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.math.BigDecimal;

/**
 * @description Merchant Account Value Object
 * @author henryzheng
 * @date  2025/10/28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MerchantAccountVO extends MerchantAccount {

    @Schema(description = "amount")
    @NonNull
    private BigDecimal amount;

    public MerchantAccount to() {
        MerchantAccount merchantAccount = new MerchantAccount();
        CopyOptions copyOptions = CopyOptions.create().ignoreNullValue();
        BeanUtil.copyProperties(this, merchantAccount, copyOptions);
        return merchantAccount;
    }

    public static MerchantAccountVO from(MerchantAccount entity) {
        MerchantAccountVO merchantAccountVO = new MerchantAccountVO();
        CopyOptions copyOptions = CopyOptions.create().ignoreNullValue();
        BeanUtil.copyProperties(entity, merchantAccountVO, copyOptions);
        return merchantAccountVO;
    }

}
