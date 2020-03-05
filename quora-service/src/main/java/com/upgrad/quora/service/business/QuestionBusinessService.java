package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class QuestionBusinessService {
    @Autowired
    private QuestionDao questionDao;

    @Transactional
    public QuestionEntity createQuestion(QuestionEntity question)
    {
        QuestionEntity createdQuestion=questionDao.createQuestion(question);
        return createdQuestion;
    }

    public List<QuestionEntity> getAllQuestions()
    {
        List<QuestionEntity> allQuestion = questionDao.getAllQuestions();
        return allQuestion;
    }

    public List<QuestionEntity> getAllQuestionsByUser(UserEntity userEntity)
    {
        List<QuestionEntity> allQuestion = questionDao.getAllQuestionsByUser(userEntity.getId());
        return allQuestion;
    }

    public QuestionEntity getQuestionByQuestionId(String questionUuid) throws InvalidQuestionException {
        return questionDao.getQuestionByQuestionId(questionUuid);
    }


    @Transactional
    public QuestionEntity editQuestionContent(String questionUuid,String content) throws InvalidQuestionException {
        QuestionEntity questionEntity=questionDao.getQuestionByQuestionId(questionUuid);
        questionEntity.setContent(content);
        return questionDao.updateQuestion(questionEntity);
    }

    @Transactional
    public void deleteQuestion(String questionUuid) throws InvalidQuestionException {
        QuestionEntity questionEntity=questionDao.getQuestionByQuestionId(questionUuid);
        questionDao.deleteQuestion(questionEntity);

    }

}
