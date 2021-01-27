package com.cxylk.dao;

import com.cxylk.po.OrderInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderInfoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(OrderInfo record);

    int insertSelective(OrderInfo record);

    OrderInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrderInfo record);

    int updateByPrimaryKey(OrderInfo record);
}