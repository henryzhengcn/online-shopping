package com.onlineshopping.common.model.domain.user;

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
 * @description Trade Order Entity
 * @author henryzheng
 * @date  2025/10/28
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class TradeOrder implements Serializable {

    @Schema(description = "trader order id")
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Schema(description = "user id")
    @NonNull
    private Integer userId;

    @Schema(description = "merchant id")
    @NonNull
    private Integer merchantId;

    @Schema(description = "sku")
    @NonNull
    private String sku;

    @Schema(description = "deal datetime")
    @NonNull
    private Date dealDatetime;

    @Schema(description = "quantity")
    @NonNull
    private Integer quantity;

    @Schema(description = "price")
    @NonNull
    private BigDecimal price;

    @Schema(description = "amount")
    @NonNull
    private BigDecimal amount;

    @Schema(description = "currency")
    @NonNull
    private String currency;

    @Schema(description = "create time")
    private Date createTime;

    @Schema(description = "update time")
    private Date updateTime;

}
