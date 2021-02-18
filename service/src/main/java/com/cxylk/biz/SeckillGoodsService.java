package com.cxylk.biz;

import com.cxylk.domain.SeckillGoodsDTO;

import java.util.List;

/**
 * @Classname SeckillGoodsService
 * @Description TODO
 * @Author likui
 * @Date 2021/1/27 17:24
 **/
public interface SeckillGoodsService {
    /**
     * 获取商品表和商品秒杀表中的属性
     * @return
     */
    List<SeckillGoodsDTO> getGoodsList();

    /**
     * 根据商品id获取商品详情
     * @param goodsId
     * @return
     */
    SeckillGoodsDTO getGoodsDetail(long goodsId);

    /**
     * 减库存
     * @param goodsDTO
     */
    boolean reduceGoods(SeckillGoodsDTO goodsDTO);
}
