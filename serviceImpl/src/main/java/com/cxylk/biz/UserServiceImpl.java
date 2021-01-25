package com.cxylk.biz;

import com.cxylk.dao.UserMapper;
import com.cxylk.po.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Classname UserServiceImpl
 * @Description TODO
 * @Author likui
 * @Date 2021/1/10 14:21
 **/
@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserMapper userMapper;


    @Override
    public User getUserById(Integer id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Transactional
    @Override
    public boolean insertTx() {
        User user1=new User();
        user1.setId(4);
        user1.setName("222");
        //先插入一条id为4的记录，可以插入成功
        userMapper.insertTX(user1);
        User user2=new User();
        user2.setId(1);
        user2.setName("111");
        //再插入一条记录为1的记录，因为数据库中已经存在id为1的记录，由于加了
        //Transactional注解，所以该操作不会插入数据，即4这条记录不会插入数据库
        userMapper.insertTX(user2);
        return true;
    }

}
