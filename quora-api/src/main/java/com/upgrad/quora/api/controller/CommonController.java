package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.UserDetailsResponse;
import com.upgrad.quora.service.business.AuthenticationService;
import com.upgrad.quora.service.business.UserCommonBusinessService;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.SignOutRestrictedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class CommonController {
    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserCommonBusinessService userCommonBusinessService;

    @RequestMapping(method = RequestMethod.POST,path="/userprofile/{userid}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UserDetailsResponse> getUserDetails(@PathVariable("userid") final String userUuid,
                                                              @RequestHeader("access-token") final String access_token) throws AuthorizationFailedException, SignOutRestrictedException, UserNotFoundException {
        UserAuthTokenEntity userAuthTokenEntity=authenticationService.getAccessToken(access_token,2);
        UserEntity user=userCommonBusinessService.getUserByUuid(userUuid,1);
        UserDetailsResponse userDetails=new UserDetailsResponse();
        userDetails.setUserName(user.getUsername());
        userDetails.setFirstName(user.getFname());
        userDetails.setLastName(user.getLname());
        userDetails.setAboutMe(user.getAboutme());
        userDetails.setContactNumber(user.getContactNumber());
        userDetails.setCountry(user.getCountry());
        userDetails.setDob(user.getDob());
        userDetails.setEmailAddress(user.getEmail());
        return new ResponseEntity<UserDetailsResponse>(userDetails,HttpStatus.OK);
    }
}
