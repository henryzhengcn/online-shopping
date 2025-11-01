package com.onlineshopping.common.model.vo;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.onlineshopping.common.model.domain.merchant.Goods;
import com.onlineshopping.common.model.domain.user.UserAccount;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * @description Goods Value Object
 * @author henryzheng
 * @date  2025/10/28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsVO extends Goods {

    @Schema(description = "merchantName")
    @NonNull
    private String merchantName;

    public Goods to() {
        Goods goods = new Goods();
        CopyOptions copyOptions = CopyOptions.create().ignoreNullValue();
        BeanUtil.copyProperties(this, goods, copyOptions);
        return goods;
    }

    public static GoodsVO from(UserAccount entity) {
        GoodsVO goodsVO = new GoodsVO();
        CopyOptions copyOptions = CopyOptions.create().ignoreNullValue();
        BeanUtil.copyProperties(entity, goodsVO, copyOptions);
        return goodsVO;
    }

}
