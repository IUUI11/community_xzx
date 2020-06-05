package xzx.majia.community.community_xzx.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import xzx.majia.community.community_xzx.dto.PaginationDto;
import xzx.majia.community.community_xzx.mapper.UserMapper;
import xzx.majia.community.community_xzx.model.User;
import xzx.majia.community.community_xzx.model.UserExample;
import xzx.majia.community.community_xzx.service.QuestionService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController {
    @Autowired(required = false)
    private UserMapper userMapper;

    @Autowired
    private QuestionService questionService;

    @GetMapping("/")
    public String Index(HttpServletRequest request,
                        Model model,
                        @RequestParam(name="page",defaultValue="1") Integer page,
                        @RequestParam(name="size",defaultValue="5") Integer size){
        Cookie[] cookies =  request.getCookies();//这个cookie一直取不到值 登录后的用户名显示 靠的是session不是这里的cookie
        if (cookies != null){
            System.out.println("cookie 不是空");
            for (Cookie cookie:cookies){
                if ("token".equals(cookie.getName())){
                    String token = cookie.getValue();
                    UserExample userExample = new UserExample();
                    userExample.createCriteria()
                            .andTokenEqualTo(token);
                    List<User> users = userMapper.selectByExample(userExample);
                    /*User user = userMapper.findByToken(token);*/ //集成mybatis之前用的方法
                    if (users.size() != 0) {
                        request.getSession().setAttribute("user",users.get(0));
                    }
                    break;
                }
            }
        }

        PaginationDto pagination = questionService.list(page,size);
        model.addAttribute("pagination",pagination);
        return "index";
    }
}
