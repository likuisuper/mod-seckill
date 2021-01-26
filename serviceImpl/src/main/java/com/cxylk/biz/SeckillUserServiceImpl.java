package com.cxylk.biz;

import com.cxylk.dao.SeckillUserMapper;
import com.cxylk.dto.SeckillUserDTO;
import com.cxylk.exception.BizException;
import com.cxylk.po.SeckillUser;
import com.cxylk.response.ResultCode;
import com.cxylk.util.ComUtil;
import com.cxylk.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Classname SeckillUserServiceImpl
 * @Description TODO
 * @Author likui
 * @Date 2021/1/25 13:51
 **/
@Service
public class SeckillUserServiceImpl implements SeckillUserService{
    @Autowired
    private SeckillUserMapper seckillUserMapper;

    @Override
    public SeckillUser getById(long id) {
        return seckillUserMapper.selectByPrimaryKey(id);
    }

    @Override
    public boolean login(SeckillUserDTO seckillUserDTO) throws BizException {
        if(ComUtil.isEmpty(seckillUserDTO)){
            throw new BizException(ResultCode.SERVER_ERROR);
        }
        String mobile=seckillUserDTO.getMobile();
        //表单输入的密码
        String formPass=seckillUserDTO.getPassword();
        SeckillUser seckillUser=this.getById(Long.parseLong(mobile));
        //判断手机号是否存在
        if(ComUtil.isEmpty(seckillUser)){
            throw new BizException(ResultCode.MOBILE_NOT_EXIST);
        }
        //数据库中的密码
        String dbPassword = seckillUser.getPassword();
        //获取盐
        String salt = seckillUser.getSalt();
        String password=MD5Util.formPassToDB(formPass,salt);
        if(!password.equals(dbPassword)){
            throw new BizException(ResultCode.PASSWORD_ERROR);
        }
        return true;
    }
}
