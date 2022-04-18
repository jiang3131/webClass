package mycontroller.filters;


import com.alibaba.fastjson.JSONObject;
import mycontroller.User;
import mycontroller.service.UserTokenService;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 核心api拦截器
 */
@WebFilter(urlPatterns = "/api/face")
public class ApiFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //拦截req，判断是否有token
        HttpServletRequest request=(HttpServletRequest)servletRequest;
        request.setCharacterEncoding("utf8");
        String url=request.getRequestURL().toString();
        //如果是登录或者验证码相关的资源，直接放行，不需要登录令牌。
        if(url.indexOf("/api/login")>=0 || url.indexOf("validate-code")>=0){
            filterChain.doFilter(servletRequest,servletResponse);
            return;
        }
        String token=request.getParameter("token");
        if(token!=null){
            //对token进行校验
            UserTokenService userTokenService=new UserTokenService();
            User u=userTokenService.getUser(token);
            if(u!=null){
                //放行
                filterChain.doFilter(servletRequest,servletResponse);
            }else{
                HttpServletResponse response=(HttpServletResponse) servletResponse;
                JSONObject error=new JSONObject();
                error.put("error","令牌错误，身份信息不合法");
                response.setStatus(401);
                response.setContentType("application/json;charset=utf8");
                response.getWriter().write(error.toJSONString());
            }
        }else{
            HttpServletResponse response=(HttpServletResponse) servletResponse;
            JSONObject error=new JSONObject();
            error.put("error","请登录认证");
            response.setStatus(401);
            response.setContentType("application/json;charset=utf8");
            response.getWriter().write(error.toJSONString());
        }
    }

    @Override
    public void destroy() {

    }
}
