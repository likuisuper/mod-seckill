package com.cxylk.dao;

import com.cxylk.domain.SeckillGoodsDTO;
import com.cxylk.po.Goods;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GoodsMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Goods record);

    int insertSelective(Goods record);

    Goods selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Goods record);

    int updateByPrimaryKeyWithBLOBs(Goods record);

    int updateByPrimaryKey(Goods record);

    /**
     * 查询goods表中所有属性和seckill_goods中的价格，库存，开始时间和结束时间
     * @return
     */
    List<SeckillGoodsDTO> listGoods();

    /**
     * 根据id获取商品详情
     * @param goodsId
     * @return
     */
    SeckillGoodsDTO getGoodsDetail(long goodsId);

    /**
     * 减库存
     * @param goodsDTO
     */
    int reduceGoods(SeckillGoodsDTO goodsDTO);
}