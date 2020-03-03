package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserCommonBusinessService {

    @Autowired
    private UserDao userDao;

    public UserEntity getUserByUuid(String uuid,int op) throws UserNotFoundException {
        //op=1 for user         op==2 for admin
        UserEntity user = userDao.getUserById(uuid);
        if(user==null)
        {
            if(op==1)
                throw new UserNotFoundException("USR-001","User with entered uuid does not exist");
            else
                throw new UserNotFoundException("USR-001","User with entered uuid to be deleted does not exist");
        }
        else
        {
            return user;
        }
    }

    @Transactional
    public void deleteUser(String Uuid)
    {
        UserEntity user=userDao.getUserById(Uuid);
        userDao.deleteUser(user.getId());

    }


}
