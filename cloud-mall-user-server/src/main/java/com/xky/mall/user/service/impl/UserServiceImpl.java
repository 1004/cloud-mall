package com.xky.mall.user.service.impl;

import com.xky.mall.common.exception.MallException;
import com.xky.mall.common.exception.MallExceptionEnum;
import com.xky.mall.common.utils.Md5Util;
import com.xky.mall.user.model.dao.UserMapper;
import com.xky.mall.user.model.pojo.User;
import com.xky.mall.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author xiekongying
 * @version 1.0
 * @date 2021/7/19 11:53 上午
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired  //按照名称注入，名称不能乱写
    private UserMapper userMapper;

    @Override
    public User selectByPrimaryKey(Integer id) {
        return userMapper.selectByPrimaryKey(id);
    }

    /**
     * 注册
     *
     * @param userName
     * @param password
     */
    @Override
    public void regist(String userName, String password) throws MallException {
        //不能重名
        User user = userMapper.selectUserByName(userName);
        if (user != null) {
            throw new MallException(MallExceptionEnum.USER_NAME_REPEAT);
        }
        User newUser = new User();
        newUser.setUsername(userName);
        newUser.setPassword(Md5Util.md5(password));
        int count = userMapper.insertSelective(newUser);
        if (count == 0) {
            throw new MallException(MallExceptionEnum.USER_REGIST_FAILED);
        }
    }

    /**
     * 普通用户登录
     * @param userName
     * @param password
     * @return
     * @throws MallException
     */
    @Override
    public User login(String userName,String password) throws MallException {
        //针对密码的碰撞
        User user = userMapper.selectUserByLogin(userName, Md5Util.md5(password));
        if (user == null){
            throw new MallException(MallExceptionEnum.USER_LOGIN_PWD);
        }
        return user;
    }

    /**
     * 更新签名
     * @param user
     */
    @Override
    public void updataSig(User user) throws MallException {
        int count = userMapper.updateByPrimaryKeySelective(user);
        if(count>1){
            throw new MallException(MallExceptionEnum.USER_UPDATA_SIG_F);
        }
    }

    /**
     * 用户角色
     * @param user
     * @return
     */
    @Override
    public boolean checkAdminRole(User user){
        return user != null && user.getRole()==2;
    }
}
