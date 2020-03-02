package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.UserEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class UserDao {

    @PersistenceContext
    private EntityManager em;

    public UserEntity getUserByUsername(UserEntity user) {
        try {
            return em.createNamedQuery("userByUsername", UserEntity.class).setParameter("username", user.getUsername()).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public UserEntity getUserByEmail(UserEntity user) {
        try {
            return em.createNamedQuery("userByEmail", UserEntity.class).setParameter("email", user.getEmail()).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public UserEntity createUser(UserEntity user)
    {
        em.persist(user);
        return user;
    }

}
