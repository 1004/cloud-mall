package com.xky.mall.user.controller;

import com.xky.mall.common.common.CommonResponse;
import com.xky.mall.common.common.Constants;
import com.xky.mall.common.exception.MallException;
import com.xky.mall.common.exception.MallExceptionEnum;
import com.xky.mall.user.model.pojo.User;
import com.xky.mall.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author xiekongying
 * @version 1.0
 * @date 2021/7/19 11:54 上午
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/select")
    public User selectUser(@RequestParam("id") int id, HttpServletRequest request) {
//        request.getSession()
        return userService.selectByPrimaryKey(id);
    }

    /**
     * 注册
     *
     * @param userName
     * @param password
     * @return
     */
    @PostMapping("/register")
    public CommonResponse<Object> register(@RequestParam("userName") String userName, @RequestParam("password") String password) throws MallException {
        if (StringUtils.isEmpty(userName)) {
            return CommonResponse.error(MallExceptionEnum.USER_NEED_NAME);
        }
        if (StringUtils.isEmpty(password)) {
            return CommonResponse.error(MallExceptionEnum.USER_NEED_PWD);
        }
        if (password.length() < 8) {
            return CommonResponse.error(MallExceptionEnum.USER_PWD_LENGTH);
        }
        userService.regist(userName, password);
        return CommonResponse.success();
    }

    /**
     * 普通用户登录
     *
     * @param userName
     * @param password
     * @return
     */
    @PostMapping("/login")
    public CommonResponse<User> login(@RequestParam("userName") String userName, @RequestParam("password") String password, HttpSession session) throws MallException {
        if (StringUtils.isEmpty(userName)) {
            return CommonResponse.error(MallExceptionEnum.USER_NEED_NAME);
        }
        if (StringUtils.isEmpty(password)) {
            return CommonResponse.error(MallExceptionEnum.USER_NEED_PWD);
        }

        User user = userService.login(userName, password);
        //登录成功后，对用户信息进行保存
        session.setAttribute(Constants.USER_LOGIN_CACHE_KEY, user);
        return CommonResponse.success(user);
    }

    /**
     * 更新签名
     *
     * @param sig
     * @param session
     * @return
     */
    @PostMapping("/updataSig")
    public CommonResponse<Object> updataSig(String sig, HttpSession session) throws MallException {
        Object attribute = session.getAttribute(Constants.USER_LOGIN_CACHE_KEY);
        if (attribute == null || !(attribute instanceof User)) {
            return CommonResponse.error(MallExceptionEnum.USER_LOGIN_NEED);
        }
        User currentUser = (User) attribute;
        User user = new User();
        user.setPersonalizedSignature(sig);
        user.setId(currentUser.getId());
        userService.updataSig(user);
        return CommonResponse.success();
    }

    /**
     * 退出登录
     *
     * @return
     */
    @GetMapping("/logout")
    public CommonResponse<Object> logout(HttpSession session) {
        session.removeAttribute(Constants.USER_LOGIN_CACHE_KEY);
        return CommonResponse.success();
    }

    /**
     * 管理员登录
     *
     * @param userName
     * @param password
     * @return
     */
    @PostMapping("/admin/login")
    public CommonResponse<User> Adminlogin(@RequestParam("userName") String userName, @RequestParam("password") String password, HttpSession session) throws MallException {
        if (StringUtils.isEmpty(userName)) {
            return CommonResponse.error(MallExceptionEnum.USER_NEED_NAME);
        }
        if (StringUtils.isEmpty(password)) {
            return CommonResponse.error(MallExceptionEnum.USER_NEED_PWD);
        }
        User user = userService.login(userName, password);
        if (userService.checkAdminRole(user)) {
            //登录成功后，对用户信息进行保存
            session.setAttribute(Constants.USER_LOGIN_CACHE_KEY, user);
            return CommonResponse.success(user);
        }
        return CommonResponse.error(MallExceptionEnum.USER_ADMIN_LOGIN_F);
    }


    @PostMapping("/checkRole")
    public boolean checkAdminRole(@RequestBody User user){
        return userService.checkAdminRole(user);
    }

    @GetMapping("/getUserByFeign")
    public User getUserByFeign(HttpSession session){
        Object attribute = session.getAttribute(Constants.USER_LOGIN_CACHE_KEY);
        return (User) attribute;
    }
}
