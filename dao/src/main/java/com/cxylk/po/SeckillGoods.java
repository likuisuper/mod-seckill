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
public class SeckillGoods implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "秒杀的商品表")
    private Long id;

    @ApiModelProperty(value = "商品Id")
    private Long goodsId;

    @ApiModelProperty(value = "秒杀价")
    private BigDecimal seckillPrice;

    @ApiModelProperty(value = "库存数量")
    private Integer stockCount;

    @ApiModelProperty(value = "秒杀开始时间")
    private Date startDate;

    @ApiModelProperty(value = "秒杀结束时间")
    private Date endDate;

}