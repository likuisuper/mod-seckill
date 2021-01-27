package com.cxylk.po;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OrderInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "商品ID")
    private Long goodsId;

    @ApiModelProperty(value = "收获地址ID")
    private Long deliveryAddrId;

    @ApiModelProperty(value = "冗余过来的商品名称")
    private String goodsName;

    @ApiModelProperty(value = "商品数量")
    private Integer goodsCount;

    @ApiModelProperty(value = "商品单价")
    private BigDecimal goodsPrice;

    @ApiModelProperty(value = "1pc，2android，3ios")
    private Byte orderChannel;

    @ApiModelProperty(value = "订单状态，0新建未支付，1已支付，2已发货，3已收货，4已退款，5已完成")
    private Byte status;

    @ApiModelProperty(value = "订单的创建时间")
    private Date createDate;

    @ApiModelProperty(value = "支付时间")
    private Date payDate;

}