package xzx.majia.community.community_xzx.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import xzx.majia.community.community_xzx.mapper.UserMapper;
import xzx.majia.community.community_xzx.model.User;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {
    @Autowired(required = false)
    private UserMapper userMapper;
    @GetMapping("/index")
    public String Index(HttpServletRequest request){
        Cookie[] cookies =  request.getCookies();//这个cookie一直取不到值 登录后的用户名显示 靠的是session不是这里的cookie
        if (cookies != null){
            System.out.println("cookie 不是空");
            for (Cookie cookie:cookies){
                if ("token".equals(cookie.getName())){
                    String token = cookie.getValue();
                    User user = userMapper.findByToken(token);
                    if (user != null) {
                        request.getSession().setAttribute("user",user);
                    }
                    break;
                }
            }
        }

        return "index";
    }
}
