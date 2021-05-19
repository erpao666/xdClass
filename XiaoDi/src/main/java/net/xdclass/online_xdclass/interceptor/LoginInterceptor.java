package net.xdclass.online_xdclass.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import net.xdclass.online_xdclass.utils.JWTUtils;
import net.xdclass.online_xdclass.utils.JsonData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class LoginInterceptor implements HandlerInterceptor {

    /**
     * 进入到controller之前的方法
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            String accesToken = request.getHeader("token");
            if (accesToken == null){
                accesToken = request.getParameter("token");
            }
            if (StringUtils.isNoneBlank(accesToken)){
                Claims claims = JWTUtils.checkJWT(accesToken);  // Claims可获取用户的基本信息
                if (claims == null){
                    // 告诉用户登录过期，需要重新登录
                    sendJsonMessage(response, JsonData.buildError("登录过期，请重新登录"));
                    return false;
                }
                Integer id = (Integer)claims.get("id");
                String name = (String)claims.get("name");

                request.setAttribute("user_id",id);
                request.setAttribute("name",name);

                return true;
            }
        } catch (Exception e){

        }

        // 登录失败

        return false;
    }

    /**
     * 相应数据给前端
     * @param response
     * @param obj
     */
    public static void sendJsonMessage(HttpServletResponse response, Object obj){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            response.setContentType("application/json; charset=UTF-8");
            PrintWriter writer = response.getWriter();
            writer.print(objectMapper.writeValueAsString(obj));
            writer.close();
            response.flushBuffer();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
