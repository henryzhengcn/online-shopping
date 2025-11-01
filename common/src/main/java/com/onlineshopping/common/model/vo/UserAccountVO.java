package com.onlineshopping.common.model.vo;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.onlineshopping.common.model.domain.user.UserAccount;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.math.BigDecimal;

/**
 * @description User Account Value Object
 * @author henryzheng
 * @date  2025/10/28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAccountVO extends UserAccount {

    @Schema(description = "amount")
    @NonNull
    private BigDecimal amount;

    public UserAccount to() {
        UserAccount userAccount = new UserAccount();
        CopyOptions copyOptions = CopyOptions.create().ignoreNullValue();
        BeanUtil.copyProperties(this, userAccount, copyOptions);
        return userAccount;
    }

    public static UserAccountVO from(UserAccount entity) {
        UserAccountVO userAccountVO = new UserAccountVO();
        CopyOptions copyOptions = CopyOptions.create().ignoreNullValue();
        BeanUtil.copyProperties(entity, userAccountVO, copyOptions);
        return userAccountVO;
    }

}
