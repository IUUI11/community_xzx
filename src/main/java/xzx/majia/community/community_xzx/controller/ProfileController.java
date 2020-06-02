package xzx.majia.community.community_xzx.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import xzx.majia.community.community_xzx.dto.PaginationDto;
import xzx.majia.community.community_xzx.mapper.UserMapper;
import xzx.majia.community.community_xzx.model.User;
import xzx.majia.community.community_xzx.service.QuestionService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class ProfileController {
    @Autowired(required = false)
    private UserMapper userMapper;

    @Autowired
    private QuestionService questionService;
    @GetMapping("/profile/{action}")
    public String profile(HttpServletRequest request,
                          @PathVariable(name = "action") String action,
                          Model model,
                          @RequestParam(name="page",defaultValue="1") Integer page,
                          @RequestParam(name="size",defaultValue="5") Integer size) {
        Cookie[] cookies =  request.getCookies();//这个cookie一直取不到值 登录后的用户名显示 靠的是session不是这里的cookie
//        User user =new User();
//        if (cookies != null){
//            System.out.println("cookie 不是空");
//            for (Cookie cookie:cookies){
//                if ("token".equals(cookie.getName())){
//                    String token = cookie.getValue();
//                    user = userMapper.findByToken(token);
//                    if (user != null) {
//                        request.getSession().setAttribute("user",user);
//                    }
//                    break;
//                }
//            }
//        }
//        if (user == null){
//            return "redirect:/";
//        }
        User user = (User) request.getSession().getAttribute("user");
        if ("questions".equals(action)){
            model.addAttribute("section","questions");
            model.addAttribute("sectionName","我的提问");
        }else if ("replies".equals(action)){
            model.addAttribute("section","replies");
            model.addAttribute("sectionName","最新回复");
        }
        PaginationDto paginationDto = questionService.list(user.getId(), page, size);
        model.addAttribute("pagination",paginationDto);
        return "profile";

    }
}
