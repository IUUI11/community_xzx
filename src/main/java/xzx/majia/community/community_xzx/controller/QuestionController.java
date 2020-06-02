package xzx.majia.community.community_xzx.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import xzx.majia.community.community_xzx.dto.QuestionDto;
import xzx.majia.community.community_xzx.service.QuestionService;

@Controller
public class QuestionController {

    @Autowired(required = false)
    private QuestionService questionService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id") Integer id,
                            Model model){

        QuestionDto questionDto =  questionService.getById(id);
        model.addAttribute("question",questionDto);
        return "question";
    }
}
