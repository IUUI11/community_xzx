package xzx.majia.community.community_xzx.service;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xzx.majia.community.community_xzx.dto.PaginationDto;
import xzx.majia.community.community_xzx.dto.QuestionDto;
import xzx.majia.community.community_xzx.exception.CustomizeErrorCode;
import xzx.majia.community.community_xzx.exception.CustomizeException;
import xzx.majia.community.community_xzx.mapper.QuestionMapper;
import xzx.majia.community.community_xzx.mapper.UserMapper;
import xzx.majia.community.community_xzx.model.Question;
import xzx.majia.community.community_xzx.model.QuestionExample;
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
        Integer totalPage;
        Integer totalCount = (int)questionMapper.countByExample(new QuestionExample());

        if (totalCount % size ==0){
            totalPage = totalCount / size;
        }else {
            totalPage = totalCount / size +1;
        }

        if (page < 1){
            page =1;
        }
        if (page > totalPage){
            page =totalPage;
        }

        paginationDto.setPagination(totalPage,page);
        //size*(page-1)
        Integer offset = size*(page-1);
        /*List<Question> questions = questionMapper.list(offset,size);*/ // 没有用mybatis整合前的方法

        List<Question> questions = questionMapper.selectByExampleWithRowbounds(new QuestionExample(), new RowBounds(offset, size));
        List<QuestionDto> questionDtoList = new ArrayList<>();

        for (Question question : questions) {
            User user =  userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDto  questionDto = new QuestionDto();
            BeanUtils.copyProperties(question,questionDto);
            questionDto.setUser(user);
            questionDtoList.add(questionDto);
        }
        paginationDto.setQuestions(questionDtoList);

        return paginationDto;
    }

    public PaginationDto list(Integer userId, Integer page, Integer size) {

        PaginationDto paginationDto = new PaginationDto();
        Integer totalPage;
      /*  Integer totalCount = questionMapper.countByUserId(userId);*/ //没有用mybatis整合前的写法
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria()
                .andCreatorEqualTo(userId);
        Integer totalCount = (int) questionMapper.countByExample(questionExample);
        if (totalCount % size ==0){
            totalPage = totalCount / size;
        }else {
            totalPage = totalCount / size +1;
        }

        if (page < 1){
            page =1;
        }
        if (page > totalPage){
            page =totalPage;
        }

        paginationDto.setPagination(totalPage,page);
        //size*(page-1)

        Integer offset = size*(page-1);
        if ( page == 0){
            offset = -1*page;
        }
        /*List<Question> questions =questionMapper.listByUserId(userId,offset,size);*/ //没有用mybatis整合前的写法
        QuestionExample example =  new QuestionExample();
        example.createCriteria()
                .andCreatorEqualTo(userId);
        List<Question> questions = questionMapper.selectByExampleWithRowbounds(example, new RowBounds(offset, size));
        List<QuestionDto> questionDtoList = new ArrayList<>();

        for (Question question : questions) {
            User user =  userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDto  questionDto = new QuestionDto();
            BeanUtils.copyProperties(question,questionDto);
            questionDto.setUser(user);
            questionDtoList.add(questionDto);
        }
        paginationDto.setQuestions(questionDtoList);

        return paginationDto;
    }

    public QuestionDto getById(Integer id) {
        Question question  = questionMapper.selectByPrimaryKey(id);
        if (question == null){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        QuestionDto questionDto = new QuestionDto();
        BeanUtils.copyProperties(question,questionDto);
        User user =  userMapper.selectByPrimaryKey(question.getCreator());
        questionDto.setUser(user);
        return questionDto;
    }

    public void createOrUpdate(Question question) {
            if (question.getId() == null){
                //创建新的问题
                question.setGmtCreate(System.currentTimeMillis());
                question.setGmtModified(question.getGmtCreate());
                questionMapper.insert(question);
            }else {
                //更新
                /*question.setGmtModified(question.getGmtCreate());*/ //没有用mybatis整合前的写法
                Question updateQuestion = new Question();
                updateQuestion.setGmtModified(System.currentTimeMillis());
                updateQuestion.setTitle(question.getTitle());
                updateQuestion.setDescription(question.getDescription());
                updateQuestion.setTag(question.getTag());
                QuestionExample example = new QuestionExample();
                example.createCriteria()
                        .andIdEqualTo(question.getId());
                int updated =  questionMapper.updateByExampleSelective(updateQuestion, example);
                if (updated !=1){
                    throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
                }
            }
    }
}
