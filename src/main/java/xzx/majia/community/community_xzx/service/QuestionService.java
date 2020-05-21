package xzx.majia.community.community_xzx.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xzx.majia.community.community_xzx.dto.PaginationDto;
import xzx.majia.community.community_xzx.dto.QuestionDto;
import xzx.majia.community.community_xzx.mapper.QuestionMapper;
import xzx.majia.community.community_xzx.mapper.UserMapper;
import xzx.majia.community.community_xzx.model.Question;
import xzx.majia.community.community_xzx.model.User;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {
    @Autowired(required = false)
    private UserMapper userMapper;

    @Autowired
    private QuestionMapper questionMapper;
    public PaginationDto list(Integer page, Integer size) {

        PaginationDto paginationDto = new PaginationDto();
        Integer totalCount = questionMapper.count();
        paginationDto.setPagination(totalCount,size,page);
        if (page < 1){
            page =1;
        }
        if (page > paginationDto.getTotalPage()){
            page =paginationDto.getTotalPage();
        }

        //size*(page-1)
        Integer offset = size*(page-1);
        List<Question> questions =questionMapper.list(offset,size);
        List<QuestionDto> questionDtoList = new ArrayList<>();

        for (Question question : questions) {
            User user =  userMapper.findById(question.getCreator());
            QuestionDto  questionDto = new QuestionDto();
            BeanUtils.copyProperties(question,questionDto);
            questionDto.setUser(user);
            questionDtoList.add(questionDto);
        }
        paginationDto.setQuestions(questionDtoList);

        return paginationDto;
    }
}
