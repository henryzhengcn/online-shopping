package com.onlineshopping.common.model.domain.merchant;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @description Merchant Account Entity
 * @author henryzheng
 * @date  2025/10/28
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class MerchantAccount implements Serializable {

    @Schema(description = "merchant account id")
    @Id
    // 为了方便通过Restful接口生成商品和商家之间的映射关系，不自动生成ID。
//    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Schema(description = "merchant name")
    @NonNull
    private String merchantName;

    @Schema(description = "currency")
    @NonNull
    private String currency;

    @Schema(description = "balance")
    @NonNull
    private BigDecimal balance;

    @Schema(description = "create time")
    private Date createTime;

    @Schema(description = "update time")
    private Date updateTime;

}
