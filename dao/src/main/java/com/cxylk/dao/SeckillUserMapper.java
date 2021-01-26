package com.cxylk.dao;

import com.cxylk.po.SeckillUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SeckillUserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SeckillUser record);

    int insertSelective(SeckillUser record);

    SeckillUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SeckillUser record);

    int updateByPrimaryKey(SeckillUser record);
}