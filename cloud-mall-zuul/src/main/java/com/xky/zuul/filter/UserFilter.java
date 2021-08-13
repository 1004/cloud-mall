package com.xky.zuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.xky.mall.common.common.Constants;
import com.xky.mall.user.model.pojo.User;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author xiekongying
 * @version 1.0
 * @date 2021/8/12 2:35 下午
 * 普通用户登录校验
 */
@Component
public class UserFilter extends ZuulFilter {
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
        if (requestURI.contains("images") || requestURI.contains("pay")){
            return false;
        }
        if (requestURI.contains("cart") || requestURI.contains("order")){
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
            HttpServletResponse response = currentContext.getResponse();
            HttpServletResponseWrapper responseWrapper = new HttpServletResponseWrapper(response);
            responseWrapper.setCharacterEncoding("UTF-8");
            responseWrapper.setContentType("application/json;charset=utf-8");
            PrintWriter writer = null;
            try {
                writer = responseWrapper.getWriter();
                writer.write("{\n" +
                        "    \"code\": 1006,\n" +
                        "    \"msg\": \"用户未登录或无权限\",\n" +
                        "    \"data\": null\n" +
                        "}");
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                writer.flush();
                writer.close();
            }
//            currentContext.setSendZuulResponse(false);
            currentContext.setResponse(responseWrapper);
//            currentContext.setResponseBody("{\n" +
//                    "    \"code\": 1006,\n" +
//                    "    \"msg\": \"用户未登录或无权限\",\n" +
//                    "    \"data\": null\n" +
//                    "}");
            currentContext.setResponseStatusCode(200);
        }
        return null;
    }
}
