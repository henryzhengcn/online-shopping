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
 * @description User Account Entity
 * @author henryzheng
 * @date  2025/10/28
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class UserAccount implements Serializable {

    @Schema(description = "user account id")
    @Id
    // 为了方便通过Restful接口生成用户和购买商品之间的映射关系，不自动生成ID。
//    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Schema(description = "user name")
    @NonNull
    private String userName;

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
