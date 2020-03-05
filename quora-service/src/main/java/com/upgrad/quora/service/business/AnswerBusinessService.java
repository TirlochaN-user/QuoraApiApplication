package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.AnswerDao;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AnswerBusinessService {
    @Autowired
    private AnswerDao answerDao;

    @Transactional
    public void createAnswer(AnswerEntity answer)
    {
        answerDao.createAnser(answer);
    }

    public void checkOwner(String answerId, UserAuthTokenEntity userAuthTokenEntity) throws AuthorizationFailedException, AnswerNotFoundException {
        AnswerEntity answer = answerDao.getAnswerByUuid(answerId);
        if(answer==null)
            throw new AnswerNotFoundException("ANS-001","Entered answer uuid does not exist");
        else if(answer.getUser()!=userAuthTokenEntity.getUser())
        {
            throw new AuthorizationFailedException("ATHR-003","Only the answer owner can edit the answer");
        }
    }

    @Transactional
    public AnswerEntity editAnswerContent(String answerUuid,String answerContent)
    {
        AnswerEntity answer = answerDao.getAnswerByUuid(answerUuid);
        answer.setAns(answerContent);
        answerDao.editAnswerContent(answer);
        return answer;
    }
}
