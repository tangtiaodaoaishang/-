package com.example.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.pojo.User;
import com.example.service.IUserService;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Component
public class TokenUtils {

    private  static IUserService u;
   @Resource
    private IUserService u1;
    @PostConstruct
    public void setUserService(){
         u=u1;
    }
    //Jwt:token令牌生成类
    //利用token设置拦截器从而不能直接访问除登录/注册页面的其他页面信息
   public static String genToken(String userId,String sign){

      return JWT.create().withAudience(userId) // 将 user id 保存到 token 里面,作为载荷
               .withExpiresAt(DateUtil.offsetHour(new Date(),2)) //2小时后token过期
               .sign(Algorithm.HMAC256(sign)); // 以 password(sign) 作为 token 的密钥


   }
   //获取当前登录的用户信息
    //因为静态方法只能引用静态变量,所以IUserService对象需要赋值给静态的IUserService对象
   public static User getCurrentUser(){
       try {
           HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
           String token = request.getHeader("token");
           if (StrUtil.isNotBlank(token)) {
               String userId = JWT.decode(token).getAudience().get(0);
               return u.getById(Integer.valueOf(userId));
           }
       }catch (Exception e){
              return null;
            }
       return null;
    }
 }

