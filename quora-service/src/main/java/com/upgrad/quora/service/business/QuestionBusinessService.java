package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.entity.QuestionEntity;
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

    @Transactional
    public QuestionEntity editQuestionContent(String questionUuid,String content) throws InvalidQuestionException {
        QuestionEntity questionEntity=questionDao.getQuestionByQuestionId(questionUuid);
        questionEntity.setContent(content);
        return questionDao.updateQuestion(questionEntity);
    }
}
