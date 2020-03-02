package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SignupBusinessService {

    @Autowired
    UserDao userDao;

    @Autowired
    private PasswordCryptographyProvider cryptographyProvider;

    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity signup(UserEntity user) throws SignUpRestrictedException{
        UserEntity createdUser;
        createdUser=userDao.getUserByUsername(user.getUsername());
        if(createdUser==null)
        {
            createdUser=userDao.getUserByEmail(user);
            if(createdUser==null)
            {
                String[] encrytpedPass=cryptographyProvider.encrypt(user.getPassword());
                user.setSalt(encrytpedPass[0]);
                user.setPassword(encrytpedPass[1]);
                UserEntity userCreated=userDao.createUser(user);
                return userCreated;
            }
            else
            {
                throw new SignUpRestrictedException("SGR-002","This user has already been registered, try with any other emailId");
            }
        }
        else
        {
            throw new SignUpRestrictedException("SGR-001","Try any other Username, this Username has already been taken");
        }
    }
}
