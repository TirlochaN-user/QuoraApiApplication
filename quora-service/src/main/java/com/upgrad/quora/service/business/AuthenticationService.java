package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserAuthDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
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
            else if (op == 2||op==3||op==4) {
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

}
