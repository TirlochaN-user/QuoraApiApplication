package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class UserDao {

    @PersistenceContext
    private EntityManager em;

    public UserEntity getUserByUsername(String username) {
        try {
            return em.createNamedQuery("userByUsername", UserEntity.class).setParameter("username", username).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public UserEntity getUserById(String uuid) {
        try {
            return em.createNamedQuery("userById", UserEntity.class).setParameter("uuid",uuid).getSingleResult();
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

    public UserAuthTokenEntity createAuthToken(final UserAuthTokenEntity userAuthTokenEntity) {
        em.persist(userAuthTokenEntity);
        return userAuthTokenEntity;
    }

    public void updateUser(final UserEntity updatedUserEntity) {
        em.merge(updatedUserEntity);
    }

    public void deleteUser(Integer id)
    {
        UserEntity user=em.find(UserEntity.class,id);
        em.remove(user);
    }

}
