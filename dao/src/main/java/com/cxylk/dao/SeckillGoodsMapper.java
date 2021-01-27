package com.cxylk.dao;

import com.cxylk.po.SeckillGoods;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SeckillGoodsMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SeckillGoods record);

    int insertSelective(SeckillGoods record);

    SeckillGoods selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SeckillGoods record);

    int updateByPrimaryKey(SeckillGoods record);
}