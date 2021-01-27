package com.cxylk.dao;

import com.cxylk.po.SeckillOrder;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SeckillOrderMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SeckillOrder record);

    int insertSelective(SeckillOrder record);

    SeckillOrder selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SeckillOrder record);

    int updateByPrimaryKey(SeckillOrder record);
}