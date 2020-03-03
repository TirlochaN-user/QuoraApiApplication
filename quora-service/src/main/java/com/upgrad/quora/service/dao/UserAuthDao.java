package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.time.ZonedDateTime;

@Repository
public class UserAuthDao {
    @PersistenceContext
    EntityManager em;
    public UserAuthTokenEntity getAccessToken(String accessToken)
    {
        try{
            return em.createNamedQuery("tokenByAccessToken", UserAuthTokenEntity.class).setParameter("accessToken",accessToken).getSingleResult();
        }
        catch(NoResultException nre)
        {
            return null;
        }
    }
    public void updateLogoutAt(UserAuthTokenEntity userAuthTokenEntity)
    {
        userAuthTokenEntity.setLogoutAt(ZonedDateTime.now());
        em.merge(userAuthTokenEntity);

    }


}
