package com.cxylk.controller;

import com.cxylk.MQSender;
import com.cxylk.SeckillMessage;
import com.cxylk.biz.SeckillGoodsService;
import com.cxylk.biz.SeckillOrderService;
import com.cxylk.biz.SeckillService;
import com.cxylk.domain.SeckillGoodsDTO;
import com.cxylk.exception.BizException;
import com.cxylk.po.OrderInfo;
import com.cxylk.po.SeckillOrder;
import com.cxylk.po.SeckillUser;
import com.cxylk.response.Response;
import com.cxylk.response.ResponseResult;
import com.cxylk.response.ResultCode;
import com.cxylk.service.RedisService;
import com.cxylk.service.impl.GoodsKey;
import com.cxylk.service.impl.OrderKey;
import com.cxylk.service.impl.SeckillKey;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Classname SeckillController
 * @Description 秒杀控制层
 * @Author likui
 * @Date 2021/1/29 10:48
 **/
@Api(value = "SeckillController", tags = "秒杀控制层")
@RestController
@RequestMapping("/seckill")
public class SeckillController implements InitializingBean {
    @Autowired
    private SeckillGoodsService seckillGoodsService;

    @Autowired
    private SeckillOrderService seckillOrderService;

    @Autowired
    private SeckillService seckillService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private MQSender sender;

    //本地内存标记
    private Map<Long, Boolean> localOverMap = new HashMap<>();

    /**
     * 实现InitializingBean接口，重写该方法，bean被实例化以后立马回调该方法。
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        //系统初始化，把商品库存数量加载到redis
        List<SeckillGoodsDTO> goodsList = seckillGoodsService.getGoodsList();
        if (goodsList == null) {
            return;
        }
        for (SeckillGoodsDTO goodsDTO : goodsList) {
            redisService.set(GoodsKey.getSeckillGoodsStock, "" + goodsDTO.getId(), goodsDTO.getStockCount());
            //系统初始化的时候标记该商品还没有结束秒杀
            localOverMap.put(goodsDTO.getId(), false);
        }

    }

    /**
     * 优化前：3000*10个请求，QPS:450
     * 优化后：redis+mq QPS:2271
     * get是幂等的，而post是不幂等的
     */
    @ApiOperation(value = "秒杀实现")
    @PostMapping("/{path}/do_seckill")
    public ResponseResult<Integer> doSeckill(@PathVariable("path") String path, @RequestParam("goodsId") long goodsId, SeckillUser user) throws BizException {
        //如果未登录则跳转到登录界面
        if (user == null) {
            return Response.makeErrRsp(ResultCode.SESSION_ERROR);
        }
        //验证path
        boolean check = seckillService.checkPath(user, goodsId, path);
        if (!check) {
            return Response.makeErrRsp(ResultCode.ILLEGAL_REQUEST);
        }
        //内存标记，减少redis访问
        Boolean over = localOverMap.get(goodsId);
        //如果已经结束秒杀，那么后面步骤就没有必要走了
        if (over) {
            return Response.makeErrRsp(ResultCode.SECKILL_OVER);
        }
        //预减库存
        long stock = redisService.decr(GoodsKey.getSeckillGoodsStock, "" + goodsId);
        if (stock < 0) {
            localOverMap.put(goodsId, true);//当这里设置为true表示已经结束，后面的请求在上面发现为true后就不再执行后面步骤
            return Response.makeErrRsp(ResultCode.SECKILL_OVER);
        }
        //判断是否已经秒杀到了
        SeckillOrder order = seckillOrderService.getOrderByUserIdGoodsId(user.getId(), goodsId);
        if (order != null) {
            return Response.makeErrRsp(ResultCode.REPEATE_SECKILL);
        }
        //入队
        SeckillMessage seckillMessage = new SeckillMessage();
        seckillMessage.setUser(user);
        seckillMessage.setGoodsId(goodsId);
        sender.sendSeckillMessage(seckillMessage);
        //返回0代表排队中,还没生成订单
        return Response.makeSuccessRsp(0);

//        SeckillGoodsDTO goodsDetail = seckillGoodsService.getGoodsDetail(goodsId);
//        //1.如果秒杀商品库存<=0秒杀失败
//        if (goodsDetail.getStockCount() <= 0) {
//            throw new BizException(ResultCode.SECKILL_OVER);
//        }
//        //2.库存有，判断是否存在重复秒杀
//        SeckillOrder seckillOrder = seckillOrderService.getOrderByUserIdGoodsId(user.getId(), goodsId);
//        if (seckillOrder != null) {
//            throw new BizException(ResultCode.REPEATE_SECKILL);
//        }
//        //3.到这里可以正常秒杀
//        //减库存、下订单、写入秒杀订单(原子操作，使用事务)
//        OrderInfo orderInfo = seckillService.seckill(user, goodsDetail);
//        return Response.makeSuccessRsp(orderInfo);
    }

    /**
     * 返回orderId表示秒杀成功
     * 返回 -1：秒杀失败
     * 返回 0：排队中，还没处理完
     */
    @ApiOperation(value = "获取秒杀结果")
    @GetMapping("/seckillResult")
    public ResponseResult<Long> getSeckillResult(@RequestParam("goodsId") long goodsId, SeckillUser user) throws BizException {
        //如果未登录则跳转到登录界面
        if (user == null) {
            return Response.makeErrRsp(ResultCode.SESSION_ERROR);
        }
        long result = seckillService.getSeckillResult(user.getId(), goodsId);
        return Response.makeSuccessRsp(result);
    }

    @ApiOperation(value = "生成秒杀地址，用于隐藏真实的秒杀地址")
    @GetMapping("/path")
    public ResponseResult<String> getSeckillPath(SeckillUser user, @RequestParam("goodsId") long goodsId,
                                                 @RequestParam("verifyCode") int verifyCode) throws BizException {
        //如果未登录则跳转到登录界面
        if (user == null) {
            return Response.makeErrRsp(ResultCode.SESSION_ERROR);
        }
        boolean check = seckillService.checkVerifyCode(user, goodsId, verifyCode);
        if (!check) {
            return Response.makeErrRsp(ResultCode.ILLEGAL_REQUEST);
        }
        String path = seckillService.createPath(user, goodsId);
        return Response.makeSuccessRsp(path);
    }

    @ApiOperation(value = "生成图形验证码")
    @GetMapping("/verifyCode")
    public ResponseResult<String> getVerifyCode(@RequestParam("goodsId") long goodsId, SeckillUser user, HttpServletResponse response) throws BizException {
        //如果未登录则跳转到登录界面
        if (user == null) {
            return Response.makeErrRsp(ResultCode.SESSION_ERROR);
        }
        BufferedImage bufferedImage = seckillService.createVerifyCode(user, goodsId);
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            ImageIO.write(bufferedImage, "JPG", outputStream);
            outputStream.flush();
            outputStream.close();
            //返回null,因为已经通过outPutStream输出了
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return Response.makeErrRsp(ResultCode.SESSION_ERROR);
        }
    }

    @ApiOperation(value = "重置redis和数据库中的数据")
    @RequestMapping(value = "/reset", method = RequestMethod.GET)
    public ResponseResult<Boolean> reset() {
        List<SeckillGoodsDTO> goodsList = seckillGoodsService.getGoodsList();
        for (SeckillGoodsDTO goods : goodsList) {
            goods.setStockCount(10);
            redisService.set(GoodsKey.getSeckillGoodsStock, "" + goods.getId(), 10);
            localOverMap.put(goods.getId(), false);
        }
        redisService.delete(OrderKey.getSeckillOrderKey);
        redisService.delete(SeckillKey.isGoodsOver);
        seckillService.reset(goodsList);
        return Response.makeSuccessRsp(true);
    }
}
