package xzx.majia.community.community_xzx.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import xzx.majia.community.community_xzx.dto.QuestionDto;
import xzx.majia.community.community_xzx.mapper.QuestionMapper;
import xzx.majia.community.community_xzx.mapper.UserMapper;
import xzx.majia.community.community_xzx.model.Question;
import xzx.majia.community.community_xzx.model.User;
import xzx.majia.community.community_xzx.service.QuestionService;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {
    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionService questionService;

    @GetMapping("/publish/{id}")
    public String edit(@PathVariable(name = "id") Integer id,
                       Model model){
        QuestionDto question = questionService.getById(id);
        model.addAttribute("title",question.getTitle());
        model.addAttribute("description",question.getDescription());
        model.addAttribute("tag",question.getTag());
        model.addAttribute("id",question.getId());
        return "publish";
    }

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/publish")
    public String publish(){
        return "publish";
    }

    @PostMapping("/publish")
    public String doPublish(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("tag") String tag,
            @RequestParam("id") Integer id,
            HttpServletRequest request,
            Model model
    ){

        model.addAttribute("title",title);
        model.addAttribute("description",description);
        model.addAttribute("tag",tag);
        if ( title == null || title == ""){
            model.addAttribute("error","标题不能为空");
            return "publish";
        }
        if ( description == null || description == ""){
            model.addAttribute("error","描述不能为空");
            return "publish";
        }
        if ( tag == null || tag == ""){
            model.addAttribute("error","标签不能为空");
            return "publish";
        }



//        User user = null;
////        Cookie[] cookies =request.getCookies();
////        if (cookies !=null && cookies.length !=0){
////            for (Cookie cookie:cookies){
////                if ("token".equals(cookie.getName())){
////                    String token = cookie.getValue();
////                    user = userMapper.findByToken(token);
////                    if (user != null) {
////                        request.getSession().setAttribute("user",user);
////                    }
////                    break;
////                }
////            }
////        }
        User user = (User) request.getSession().getAttribute("user");
        if (user == null){
            model.addAttribute("error","用户未登录");
            return "publish";
        }
        Question question = new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        question.setCreator(user.getId());
/*        question.setGmtCreate(System.currentTimeMillis());
        question.setGmtModified(question.getGmtCreate());*/
        question.setId(id);
        questionService.createOrUpdate(question);
     /*   questionMapper.create(question);*/
        return "redirect:/";
    }
}
