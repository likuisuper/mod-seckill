package com.cxylk.biz;

import com.cxylk.constant.ConstantField;
import com.cxylk.dao.SeckillUserMapper;
import com.cxylk.dto.SeckillUserDTO;
import com.cxylk.exception.BizException;
import com.cxylk.po.SeckillUser;
import com.cxylk.response.ResultCode;
import com.cxylk.service.RedisService;
import com.cxylk.service.impl.SeckillUserKey;
import com.cxylk.util.ComUtil;
import com.cxylk.util.MD5Util;
import com.cxylk.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * @Classname SeckillUserServiceImpl
 * @Description 秒杀用户serviceImpl
 * @Author likui
 * @Date 2021/1/25 13:51
 **/
@Service
public class SeckillUserServiceImpl implements SeckillUserService {
    @Autowired
    private SeckillUserMapper seckillUserMapper;

    @Autowired
    private RedisService redisService;

    @Override
    public SeckillUser getById(long id) {
        //缓存中获取，不为空直接返回
        SeckillUser seckillUser = redisService.get(SeckillUserKey.getById, "" + id, SeckillUser.class);
        if (seckillUser != null) {
            return seckillUser;
        }
        //为空则从数据库获取
        seckillUser = seckillUserMapper.selectByPrimaryKey(id);
        if (seckillUser != null) {
            //设置到缓存中
            redisService.set(SeckillUserKey.getById, "" + id, seckillUser);
        }
        return seckillUser;
    }

    @Override
    public boolean updatePassword(String token, long id, String newPassWord) throws BizException {
        SeckillUser seckillUser = getById(id);
        if (seckillUser == null) {
            throw new BizException(ResultCode.MOBILE_NOT_EXIST);
        }
        //为什么这里要新建一个对象更新，因为更新字段越多，产生的bingLog越多。为了提高效率，修改哪个字段就更新哪个字段
        //更新数据库
        SeckillUser newUser = new SeckillUser();
        newUser.setId(id);
        newUser.setPassword(MD5Util.formPassToDB(newPassWord, seckillUser.getSalt()));
        seckillUserMapper.updateByPrimaryKey(newUser);
        //处理缓存，数据更新了，一定要更新缓存，防止缓存不一致
        //先删除与该id关联的key
        redisService.delete(SeckillUserKey.getById, "" + id);
        //再删除token,但是删除token后就不能登录了，所以不是删除而是更新
        //设置更新后的密码
        seckillUser.setPassword(newUser.getPassword());
        redisService.set(SeckillUserKey.token, token, seckillUser);
        return true;
    }

    @Override
    public String login(HttpServletResponse response, SeckillUserDTO seckillUserDTO) throws BizException {
        if (ComUtil.isEmpty(seckillUserDTO)) {
            throw new BizException(ResultCode.SERVER_ERROR);
        }
        String mobile = seckillUserDTO.getMobile();
        //表单输入的密码
        String formPass = seckillUserDTO.getPassword();
        SeckillUser seckillUser = this.getById(Long.parseLong(mobile));
        //判断手机号是否存在
        if (ComUtil.isEmpty(seckillUser)) {
            throw new BizException(ResultCode.MOBILE_NOT_EXIST);
        }
        //数据库中的密码
        String dbPassword = seckillUser.getPassword();
        //获取盐
        String salt = seckillUser.getSalt();
        String password = MD5Util.formPassToDB(formPass, salt);
        if (!password.equals(dbPassword)) {
            throw new BizException(ResultCode.PASSWORD_ERROR);
        }
        //设置cookie
        //生成一个UUID作为token，只需要生成一次token即可
        String token = UUIDUtil.uuid();
        addCookie(response, token, seckillUser);
        return token;
    }

    @Override
    public SeckillUser getByToken(HttpServletResponse response, String token) {
        if (ComUtil.isEmpty(token)) {
            return null;
        }
        //先根据token获取用户信息
        SeckillUser seckillUser = redisService.get(SeckillUserKey.token, token, SeckillUser.class);
        //延长有效期
        //有效期=最后一次操作+过期时间
        //用户不存在就不需要这个操作，存在的话只需要更新即可
        if (seckillUser != null) {
            addCookie(response, token, seckillUser);
        }
        return seckillUser;
    }

    private void addCookie(HttpServletResponse response, String token, SeckillUser seckillUser) {
        //将token做为key，seckillUser作为value存入redis，并且生成一个SeckillUserKey来标识
        redisService.set(SeckillUserKey.token, token, seckillUser);
        //设置name为"token",value为生成的token，添加进token
        Cookie cookie = new Cookie(ConstantField.COOKIE_NAME_TOKEN, token);
        //过期时间设置为和分布式session一致
        cookie.setMaxAge(SeckillUserKey.token.expireSeconds());
        //设置为网站根目录
        cookie.setPath("/");
        //将cookie添加进response
        response.addCookie(cookie);
    }
}
