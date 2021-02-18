package com.cxylk;

import com.cxylk.po.SeckillUser;
import lombok.Data;

/**
 * @Classname SeckillMessage
 * @Description 生产者发送秒杀信息
 * @Author likui
 * @Date 2021/2/16 10:41
 **/
@Data
public class SeckillMessage {
    private SeckillUser user;

    private long goodsId;
}
