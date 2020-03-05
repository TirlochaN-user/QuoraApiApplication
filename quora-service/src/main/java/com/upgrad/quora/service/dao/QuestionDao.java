package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class QuestionDao {
    @PersistenceContext
    EntityManager em;

    public QuestionEntity createQuestion(QuestionEntity question)
    {
        em.persist(question);
        return question;
    }

    public List<QuestionEntity> getAllQuestions()
    {
        return em.createNamedQuery("getAllQuestions",QuestionEntity.class).getResultList();
    }

    public List<QuestionEntity> getAllQuestionsByUser(Integer userid)
    {
        return em.createNamedQuery("questionByUserid",QuestionEntity.class).setParameter("userid",userid).getResultList();
    }

    public QuestionEntity getQuestionByQuestionId(String questionUuid) throws InvalidQuestionException {
        try{
            return em.createNamedQuery("questionByUuid",QuestionEntity.class).setParameter("uuid",questionUuid).getSingleResult();
        }
        catch(NoResultException nre) {
                throw new InvalidQuestionException("QUES-001", "Entered question uuid does not exist");
        }
    }

    public QuestionEntity updateQuestion(QuestionEntity questionEntity) {
        em.merge(questionEntity);
        return questionEntity;
    }

    public void deleteQuestion(QuestionEntity question)
    {
        QuestionEntity questionEntity = em.find(QuestionEntity.class,question.getId());
        em.remove(questionEntity);
    }
}
