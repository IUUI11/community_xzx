package xzx.majia.community.community_xzx.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xzx.majia.community.community_xzx.dto.QuestionDto;
import xzx.majia.community.community_xzx.mapper.QuestionMapper;
import xzx.majia.community.community_xzx.mapper.UserMapper;
import xzx.majia.community.community_xzx.model.Question;
import xzx.majia.community.community_xzx.model.User;

import java.util.List;

@Service
public class QuestionService {
    @Autowired(required = false)
    private UserMapper userMapper;

    @Autowired
    private QuestionMapper questionMapper;
    public List<QuestionDto> list() {
        List<Question> questions =questionMapper.list();
        for (Question question : questions) {
           User user =  userMapper.findById(question.getCreator());
            QuestionDto  questionDto = new QuestionDto();
            BeanUtils.copyProperties(question,questionDto);
        }
        return null;
    }
}
