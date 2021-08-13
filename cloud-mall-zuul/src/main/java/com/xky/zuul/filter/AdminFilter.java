package com.xky.zuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.xky.mall.common.common.Constants;
import com.xky.mall.user.model.pojo.User;
import com.xky.zuul.feign.UserFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;

/**
 * @author xiekongying
 * @version 1.0
 * @date 2021/8/12 2:35 下午
 * 后台用户登录校验
 */
@Component
public class AdminFilter extends ZuulFilter {
    @Autowired
    private UserFeignClient userFeignClient;
    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        //是否校验，根据路由校验
        RequestContext currentContext = RequestContext.getCurrentContext();
        String requestURI = currentContext.getRequest().getRequestURI();
        if (requestURI.contains("/admin/login")){
            return false;
        }
        if (requestURI.contains("admin")){
            return true;
        }
        return false;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpSession session = currentContext.getRequest().getSession();
        User user = (User) session.getAttribute(Constants.USER_LOGIN_CACHE_KEY);
        if (user == null){
            currentContext.setSendZuulResponse(false);
            currentContext.setResponseBody("{\n" +
                    "    \"code\": 1006,\n" +
                    "    \"msg\": \"用户未登录或无权限\",\n" +
                    "    \"data\": null\n" +
                    "}");
            currentContext.setResponseStatusCode(200);
            return null;
        }
        //判断是否为管理员
        boolean isAdmin = userFeignClient.checkAdminRole(user);
        if (!isAdmin){
            currentContext.setSendZuulResponse(false);
            currentContext.setResponseBody("{\n" +
                    "    \"code\": 1006,\n" +
                    "    \"msg\": \"NO_Permission\",\n" +
                    "    \"data\": null\n" +
                    "}");
            currentContext.setResponseStatusCode(200);
            return null;
        }
        return null;
    }
}
