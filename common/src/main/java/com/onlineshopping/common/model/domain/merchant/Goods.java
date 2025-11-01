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
 * @description Goods Entity
 * @author henryzheng
 * @date  2025/10/28
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Goods implements Serializable {

    @Schema(description = "goods id")
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Schema(description = "merchant id")
    @NonNull
    private Integer merchantId;

    @Schema(description = "sku")
    @NonNull
    private String sku;

    @Schema(description = "quantity")
    @NonNull
    private Integer quantity;

    @Schema(description = "price")
    @NonNull
    private BigDecimal price;

    @Schema(description = "create time")
    private Date createTime;

    @Schema(description = "update time")
    private Date updateTime;

}
