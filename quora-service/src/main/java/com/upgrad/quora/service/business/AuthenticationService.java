package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.dao.UserAuthDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.SignOutRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.UUID;

@Service
public class AuthenticationService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordCryptographyProvider cryptographyProvider;

    @Autowired
    private UserAuthDao userAuthDao;

    @Autowired
    private QuestionDao questionDao;


    @Transactional(propagation = Propagation.REQUIRED)
    public UserAuthTokenEntity authenticate(final String username, final String password) throws AuthenticationFailedException {
        UserEntity userEntity = userDao.getUserByUsername(username);
        if (userEntity == null) {
            throw new AuthenticationFailedException("ATH-001", "This username does not exist");
        }

        final String encryptedPassword = cryptographyProvider.encrypt(password, userEntity.getSalt());
        if (encryptedPassword.equals(userEntity.getPassword())) {
            JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(encryptedPassword);
            UserAuthTokenEntity userAuthToken = new UserAuthTokenEntity();
            userAuthToken.setUser(userEntity);
            userAuthToken.setUuid(UUID.randomUUID().toString());
            final ZonedDateTime now = ZonedDateTime.now();
            final ZonedDateTime expiresAt = now.plusHours(8);
            userAuthToken.setAccessToken(jwtTokenProvider.generateToken(userEntity.getUuid(), now, expiresAt));
            userAuthToken.setLoginAt(now);
            userAuthToken.setExpiresAt(expiresAt);

            userDao.createAuthToken(userAuthToken);

            return userAuthToken;
        } else {
            throw new AuthenticationFailedException("ATH-002", "Password failed");
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public UserAuthTokenEntity getAccessToken(String accessToken,int op) throws SignOutRestrictedException,AuthorizationFailedException {
        //op=1 for /user/signout
        //op=2 for /userprofile/{id},/admin/user/{userId}
        UserAuthTokenEntity userAuthToken = userAuthDao.getAccessToken(accessToken);
        if (userAuthToken == null) {
            if (op == 1) {
                throw new SignOutRestrictedException("SGR-001", "User is not Signed in");
            }
            else{
                throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
            }
        }
        else if(userAuthToken.getLogoutAt()!=null)
        {
            if(op==1)
                throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to get user details");
            else if(op==2)
                throw new AuthorizationFailedException("ATHR-002","User is signed out.");
            else if(op==3)
                throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to post a question");
            else if(op==4)
                throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to get all questions");
            else if(op==5)
                throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to edit the question");
            else if(op==6)
                throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to delete a question");
            else if(op==7)
                throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to get all questions posted by a specific user");
            else if(op==8)
                throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to post an answer");
            else if(op==9)
                throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to edit an answer");
        }
        else {
            if(op==1)
                userAuthDao.updateLogoutAt(userAuthToken);
            if(op==2)
            {
                if(userAuthToken.getUser().getRole().equals("nonadmin"))
                    throw new AuthorizationFailedException("ATHR-003","Unauthorized Access, Entered user is not an admin");
            }
        }
        return userAuthToken;
    }

    public void checkOwner(UserAuthTokenEntity userAuthTokenEntity,String questionId,int op) throws AuthorizationFailedException, InvalidQuestionException {
        QuestionEntity question =questionDao.getQuestionByQuestionId(questionId);
        if(userAuthTokenEntity.getUser().getId()!=question.getUser().getId())
        {
            if(op==1)
                throw  new AuthorizationFailedException("ATHR-003","Only the question owner can change the question.");
            else if(op==2)
                throw  new AuthorizationFailedException("ATHR-003","Only the question owner or admin can delete the question.");
        }
    }

}
