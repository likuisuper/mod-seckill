package com.cxylk.biz;

import com.cxylk.domain.SeckillGoodsDTO;
import com.cxylk.po.OrderInfo;
import com.cxylk.po.SeckillUser;

import java.awt.image.BufferedImage;
import java.util.List;

/**
 * @Classname SeckillService
 * @Description 秒杀service
 * @Author likui
 * @Date 2021/1/29 13:40
 **/
public interface SeckillService {
    /**
     * 秒杀实现，包括减库存，下订单，写入订单，这是一个原子操作
     * @param user user对象
     * @param goodsDTO 商品
     * @return 订单
     */
    OrderInfo seckill(SeckillUser user, SeckillGoodsDTO goodsDTO);

    /**
     * 获取秒杀结果
     * @param userId
     * @param goodsId
     * @return -1：失败，0：处理中，orderId：秒杀成功
     */
    long getSeckillResult(long userId, long goodsId);

    /**
     * 重置数据方便测试
     * @param goodsList
     */
    void reset(List<SeckillGoodsDTO> goodsList);

    /**
     * 生成秒杀路径
     * @param user
     * @param goodsId
     * @return
     */
    String createPath(SeckillUser user, long goodsId);

    /**
     * 后端校验path
     * @param user
     * @param goodsId
     * @param path
     * @return
     */
    boolean checkPath(SeckillUser user, long goodsId, String path);

    /**
     * 生成图形验证码
     * @param user
     * @param goodsId
     * @return
     */
    BufferedImage createVerifyCode(SeckillUser user, long goodsId);

    /**
     * 验证图形验证码
     * @param user
     * @param goodsId
     * @param verifyCode
     * @return
     */
    boolean checkVerifyCode(SeckillUser user, long goodsId, int verifyCode);
}
