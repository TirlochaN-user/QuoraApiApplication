package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.AnswerEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class AnswerDao {
    @PersistenceContext
    EntityManager em;

    public void createAnser(AnswerEntity answer)
    {
        em.persist(answer);
    }

    public AnswerEntity getAnswerByUuid(String answerUuid)
    {
        AnswerEntity answer;
        try{
            answer=em.createNamedQuery("getAnswerByUuid",AnswerEntity.class).setParameter("uuid",answerUuid).getSingleResult();
        }
        catch(NoResultException nre) {
            return null;
        }
        return answer;
    }

    public void editAnswerContent(AnswerEntity answer)
    {
        em.merge(answer);
    }

    public void deleteAnswer(AnswerEntity answer)
    {
        AnswerEntity answerEntity = em.find(AnswerEntity.class, answer.getId());
        em.remove(answerEntity);

    }
}
