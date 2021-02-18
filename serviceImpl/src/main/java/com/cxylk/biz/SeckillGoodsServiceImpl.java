package com.cxylk.biz;

import com.cxylk.dao.GoodsMapper;
import com.cxylk.domain.SeckillGoodsDTO;
import com.cxylk.po.SeckillGoods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Classname SeckillGoodsServiceImpl
 * @Description SeckillGoodsServiceImpl
 * @Author likui
 * @Date 2021/1/27 17:25
 **/
@Service
public class SeckillGoodsServiceImpl implements SeckillGoodsService{
    @Autowired
    private GoodsMapper goodsMapper;

    @Override
    public List<SeckillGoodsDTO> getGoodsList() {
        return goodsMapper.listGoods();
    }

    @Override
    public SeckillGoodsDTO getGoodsDetail(long goodsId) {
        return goodsMapper.getGoodsDetail(goodsId);
    }

    @Override
    public boolean reduceGoods(SeckillGoodsDTO goodsDTO) {
        SeckillGoods seckillGoods=new SeckillGoods();
        seckillGoods.setGoodsId(goodsDTO.getId());
        int result = goodsMapper.reduceGoods(goodsDTO);
        return result>0;
    }
}
