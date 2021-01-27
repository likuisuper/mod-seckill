package com.cxylk.domain;

import com.cxylk.po.Goods;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Classname SeckillGoodsDTO
 * @Description 通过继承Goods从而将商品表和秒杀商品的属性放在一起
 * @Author likui
 * @Date 2021/1/27 17:06
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class SeckillGoodsDTO extends Goods {
    @ApiModelProperty(value = "秒杀价")
    private BigDecimal seckillPrice;

    @ApiModelProperty(value = "库存数量")
    private Integer stockCount;

    @ApiModelProperty(value = "秒杀开始时间")
    private Date startDate;

    @ApiModelProperty(value = "秒杀结束时间")
    private Date endDate;
}
