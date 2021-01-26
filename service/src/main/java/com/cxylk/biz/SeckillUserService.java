package com.cxylk.biz;

import com.cxylk.dto.SeckillUserDTO;
import com.cxylk.exception.BizException;
import com.cxylk.po.SeckillUser;

/**
 * @Classname SeckillUserService
 * @Description TODO
 * @Author likui
 * @Date 2021/1/25 13:46
 **/
public interface SeckillUserService {
    /**
     * 根据id获取用户信息
     * @param id
     * @return
     */
    SeckillUser getById(long id);

    /**
     * 登录实现
     * @param seckillUserDTO
     * @return
     */
    boolean login(SeckillUserDTO seckillUserDTO) throws BizException;
}
