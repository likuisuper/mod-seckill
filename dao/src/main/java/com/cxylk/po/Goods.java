package com.cxylk.po;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Goods implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商品ID")
    private Long id;

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "商品标题")
    private String goodsTitle;

    @ApiModelProperty(value = "商品的图片")
    private String goodsImg;

    @ApiModelProperty(value = "商品单价")
    private BigDecimal goodsPrice;

    @ApiModelProperty(value = "商品库存，-1表示没有限制")
    private Integer goodsStock;

    @ApiModelProperty(value = "商品的详情介绍")
    private String goodsDetail;

}