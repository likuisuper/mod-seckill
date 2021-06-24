package com.cxylk.biz;

import com.cxylk.domain.SeckillGoodsDTO;
import com.cxylk.po.OrderInfo;
import com.cxylk.po.SeckillOrder;
import com.cxylk.po.SeckillUser;
import com.cxylk.service.RedisService;
import com.cxylk.service.impl.SeckillKey;
import com.cxylk.util.MD5Util;
import com.cxylk.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Random;

/**
 * @Classname SeckillServiceImpl
 * @Description 秒杀serviceImpl
 * @Author likui
 * @Date 2021/1/29 13:46
 **/
@Service
public class SeckillServiceImpl implements SeckillService {
    @Autowired
    private SeckillGoodsService seckillGoodsService;

    @Autowired
    private OrderInfoService orderInfoService;

    @Autowired
    private SeckillOrderService seckillOrderService;

    @Autowired
    private RedisService redisService;

    /**
     * 操作数数组，为了避免分母出现0的情况，就不做除法操作了
     */
    private static char[] ops=new char[]{'+','-','*'};

    @Transactional
    @Override
    public OrderInfo seckill(SeckillUser user, SeckillGoodsDTO goodsDTO) {
        //减库存
        boolean success = seckillGoodsService.reduceGoods(goodsDTO);
        //减库存成功才生成订单
        if (success) {
            //下订单(包含订单和秒杀订单)
            return orderInfoService.createOrder(user, goodsDTO);
        } else {
            //秒杀失败，做一个标记
            setGoodsOver(goodsDTO.getId());
            return null;
        }
    }


    @Override
    public long getSeckillResult(long userId, long goodsId) {
        SeckillOrder seckillOrder = seckillOrderService.getOrderByUserIdGoodsId(userId, goodsId);
        if (seckillOrder != null) {
            //秒杀成功，返回订单id
            return seckillOrder.getOrderId();
        } else {
            boolean isOver = getGoodsOver(goodsId);
            if (isOver) {
                return -1;//说明已经卖完
            } else {
                return 0;//轮询
            }
        }
    }

    @Override
    public void reset(List<SeckillGoodsDTO> goodsList) {
        seckillGoodsService.resetStock(goodsList);
        orderInfoService.deleteOrders();
    }

    @Override
    public String createPath(SeckillUser user, long goodsId) {
        if (user == null || goodsId <= 0) {
            return null;
        }
        String md5 = MD5Util.md5(UUIDUtil.uuid() + "123456");
        redisService.set(SeckillKey.getSeckillPath, "" + user.getId() + "_" + goodsId, md5);
        return md5;
    }

    @Override
    public boolean checkPath(SeckillUser user, long goodsId, String path) {
        if (user == null || path == null || goodsId <= 0) {
            return false;
        }
        String oldPath = redisService.get(SeckillKey.getSeckillPath, "" + user.getId() + "_" + goodsId, String.class);
        return path.equals(oldPath);
    }

    @Override
    public BufferedImage createVerifyCode(SeckillUser user, long goodsId) {
        if (user == null || goodsId <= 0) {
            return null;
        }
        int width = 95;
        int height = 32;
        //create the image
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        // set the background color
        g.setColor(new Color(0xDCDCDC));
        g.fillRect(0, 0, width, height);
        // draw the border
        g.setColor(Color.black);
        g.drawRect(0, 0, width - 1, height - 1);
        // create a random instance to generate the codes
        Random rdm = new Random();
        // make some confusion
        for (int i = 0; i < 50; i++) {
            int x = rdm.nextInt(width);
            int y = rdm.nextInt(height);
            g.drawOval(x, y, 0, 0);
        }
        // generate a random code
        String verifyCode = generateVerifyCode(rdm);
        g.setColor(new Color(0, 100, 0));
        g.setFont(new Font("Candara", Font.BOLD, 24));
        g.drawString(verifyCode, 8, 24);
        g.dispose();
        //计算表达式
        int rnd = calc(verifyCode);
        //把验证码存到redis中
        redisService.set(SeckillKey.getSeckillVerifyCode, user.getId()+","+goodsId, rnd);
        //输出图片
        return image;
    }

    @Override
    public boolean checkVerifyCode(SeckillUser user, long goodsId, int verifyCode) {
        if(user==null||goodsId<=0){
            return false;
        }
        Integer oldVerifyCode = redisService.get(SeckillKey.getSeckillVerifyCode, user.getId() + "," + goodsId, Integer.class);
        if(oldVerifyCode==null||verifyCode-oldVerifyCode!=0){
            return false;
        }
        //安全起见，验证成功后删除验证码
        redisService.delete(SeckillKey.getSeckillVerifyCode,user.getId()+","+goodsId);
        return true;
    }

    /**
     * 计算表达式结果
     * @param exp
     * @return
     */
    private static int calc(String exp) {
        ScriptEngineManager manager=new ScriptEngineManager();
        ScriptEngine engineByName = manager.getEngineByName("JavaScript");
        try {
            return (int) engineByName.eval(exp);
        } catch (ScriptException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 生成验证码表达式
     * @param rdm
     * @return
     */
    private static String generateVerifyCode(Random rdm) {
        int num1=rdm.nextInt(10);
        int num2=rdm.nextInt(10);
        int num3=rdm.nextInt(10);
        int num4=rdm.nextInt(10);
        char op1=ops[rdm.nextInt(3)];
        char op2=ops[rdm.nextInt(3)];
        char op3=ops[rdm.nextInt(3)];
        return ""+num1+op1+num2+op2+num3+op3+num4;
    }

    private void setGoodsOver(Long goodsId) {
        redisService.set(SeckillKey.isGoodsOver, "" + goodsId, true);
    }

    private boolean getGoodsOver(long goodsId) {
        return redisService.exists(SeckillKey.isGoodsOver, "" + goodsId);
    }
}
