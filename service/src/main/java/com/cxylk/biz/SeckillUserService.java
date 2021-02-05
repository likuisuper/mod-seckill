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
     * 根据id获取用户信息(对象级缓存)
     * @param id
     * @return
     */
    SeckillUser getById(long id);

    /**
     * 修改用户密码(对象级缓存)
     * @param token token值
     * @param id 用户id
     * @param newPassWord 更新的密码
     * @return
     */
    boolean updatePassword(String token,long id,String newPassWord) throws BizException;

    /**
     * 登录实现
     * @param response
     * @param seckillUserDTO
     * @return token
     */
    String login(HttpServletResponse response,SeckillUserDTO seckillUserDTO) throws BizException;


    /**
     * 根据token获取用户信息(对象级缓存)
     * @param token
     * @return
     */
    SeckillUser getByToken(HttpServletResponse response,String token) throws BizException;
}
