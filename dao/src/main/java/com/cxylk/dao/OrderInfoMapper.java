package com.cxylk.dao;

import com.cxylk.po.OrderInfo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderInfoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(OrderInfo record);

    int insertSelective(OrderInfo record);

    OrderInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrderInfo record);

    int updateByPrimaryKey(OrderInfo record);

    /**
     * 重置订单
     */
    @Delete("delete from order_info")
    void deleteOrders();

    /**
     * 重置秒杀订单
     */
    @Delete("delete from seckill_order")
    void deleteSeckillOrders();
}