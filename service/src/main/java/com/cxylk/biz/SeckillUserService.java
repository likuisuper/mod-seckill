package com.cxylk.biz;

import com.cxylk.dto.SeckillUserDTO;
import com.cxylk.exception.BizException;
import com.cxylk.po.SeckillUser;

import javax.servlet.http.HttpServletResponse;

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
     * @return token
     */
    String login(HttpServletResponse response,SeckillUserDTO seckillUserDTO) throws BizException;


    /**
     * 根据token获取用户信息
     * @param token
     * @return
     */
    SeckillUser getByToken(HttpServletResponse response,String token) throws BizException;
}
