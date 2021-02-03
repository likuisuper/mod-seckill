package com.cxylk.controller;

import com.cxylk.biz.SeckillGoodsService;
import com.cxylk.biz.SeckillUserService;
import com.cxylk.domain.SeckillGoodsDTO;
import com.cxylk.exception.BizException;
import com.cxylk.po.SeckillUser;
import com.cxylk.service.RedisService;
import com.cxylk.service.impl.GoodsKey;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.context.webflux.SpringWebFluxContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Classname SeckillGoodsController
 * @Description 秒杀商品接口
 * @Author likui
 * @Date 2021/1/27 13:07
 **/
@Api(value = "SeckillGoodsController", tags = "秒杀商品")
@Controller
@RequestMapping("/goods")
public class SeckillGoodsController {
    @Autowired
    private RedisService redisService;

    @Autowired
    private SeckillGoodsService seckillGoodsService;

    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;

    /**
     * 未优化前：QPS:1116,5000*10
     * 页面缓存
     * 注意：返回类型为text/html，一定要加上responseBody
     */
    @ApiOperation(value = "商品列表")
    @GetMapping(value = "/to_list",produces = "text/html")
    @ResponseBody
    public String goodsList(HttpServletRequest request, HttpServletResponse response,Model model, SeckillUser seckillUser) throws BizException {
        model.addAttribute("user", seckillUser);
        //第一步就先取缓存，没有再去数据库查，否则压测的时候还是mysql负载高于redis
        String html=redisService.get(GoodsKey.getGoodsList,"",String.class);
        //缓存中存在就直接返回
        if(!StringUtils.isEmpty(html)){
            return html;
        }
        List<SeckillGoodsDTO> goodsList = seckillGoodsService.getGoodsList();
        model.addAttribute("goodsList", goodsList);
        //手动渲染
        WebContext context=new WebContext(request,response,request.getServletContext(),
                request.getLocale(),model.asMap());
        html=thymeleafViewResolver.getTemplateEngine().process("goods_list",context);
        if(!StringUtils.isEmpty(html)){
            //保存到缓存中
            redisService.set(GoodsKey.getGoodsList,"",html);
        }
        return html;
    }

    /**
     * url缓存，和页面缓存区别不大，多了一个参数
     */
    @ApiOperation(value = "商品详情")
    @GetMapping(value = "/to_detail/{goodsId}",produces = "text/html")
    @ResponseBody
    public String goodsDetail(@PathVariable("goodsId") long goodsId, Model model, SeckillUser seckillUser,
                              HttpServletRequest request,HttpServletResponse response) throws BizException {
        model.addAttribute("user", seckillUser);
        SeckillGoodsDTO goodsDetail = seckillGoodsService.getGoodsDetail(goodsId);
        model.addAttribute("goods", goodsDetail);
        //秒杀开始时间(ms)
        long startTime = goodsDetail.getStartDate().getTime();
        //秒杀结束时间(ms)
        long endTime = goodsDetail.getEndDate().getTime();
        //获取当前时间
        long nowTime = System.currentTimeMillis();
        //维护一个秒杀状态
        int seckillStatus;
        //维护一个秒杀倒计时
        int remainSeconds;
        //如果当前时间<秒杀开始时间，说明秒杀还未开始
        if (nowTime < startTime) {
            //计算还有多久开始秒杀，转换为秒
            seckillStatus = 0;
            remainSeconds = (int) ((startTime - nowTime) / 1000);
        } else if (nowTime > endTime) {//秒杀结束
            seckillStatus = 2;
            remainSeconds = -1;
        } else {//秒杀进行中
            seckillStatus = 1;
            remainSeconds = 0;
        }
        //将秒杀状态和倒计时返回前端页面
        model.addAttribute("remainSeconds", remainSeconds);
        model.addAttribute("seckillStatus", seckillStatus);
        String html=redisService.get(GoodsKey.getGoodsDetail,""+goodsId,String.class);
        //缓存中存在直接返回
        if(!StringUtils.isEmpty(html)){
            return html;
        }
        WebContext context=new WebContext(request,response,request.getServletContext(),request.getLocale(),model.asMap());
        //手动渲染
        html=thymeleafViewResolver.getTemplateEngine().process("goods_detail",context);
        if(!StringUtils.isEmpty(html)){
            //设置到缓存
            redisService.set(GoodsKey.getGoodsDetail,""+goodsId,html);
        }
        return html;
    }
}
