package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.QuestionEntity;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class QuestionDao {
    @PersistenceContext
    EntityManager em;

    public QuestionEntity createQuestion(QuestionEntity question)
    {
        em.persist(question);
        return question;
    }
}
