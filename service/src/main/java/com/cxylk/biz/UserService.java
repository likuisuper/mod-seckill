package com.cxylk.biz;

import com.cxylk.po.User;

/**
 * @Classname UserService
 * @Description UserService
 * @Author likui
 * @Date 2021/1/9 16:15
 **/
public interface UserService {

    User getUserById(Integer id);

    /**
     * 验证事务
     * @return
     */
    boolean insertTx();
}
