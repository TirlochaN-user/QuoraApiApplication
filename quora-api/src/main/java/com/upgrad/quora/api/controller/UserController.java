package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.SigninResponse;
import com.upgrad.quora.api.model.SignoutResponse;
import com.upgrad.quora.api.model.SignupUserRequest;
import com.upgrad.quora.api.model.SignupUserResponse;
import com.upgrad.quora.service.business.AuthenticationService;
import com.upgrad.quora.service.business.SignupBusinessService;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.SignOutRestrictedException;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class UserController {
    @Autowired
    private SignupBusinessService signupBusinessService;

    @Autowired
    private AuthenticationService authenticationService;

    @RequestMapping(method = RequestMethod.POST,path = "/users/signup",produces = MediaType.APPLICATION_JSON_UTF8_VALUE,consumes=MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SignupUserResponse> signup(SignupUserRequest signupUserRequest) throws SignUpRestrictedException {
        UserEntity user=new UserEntity();
        user.setUuid(UUID.randomUUID().toString());
        user.setAboutme(signupUserRequest.getAboutMe());
        user.setContactNumber(signupUserRequest.getContactNumber());
        user.setCountry(signupUserRequest.getCountry());
        user.setDob(signupUserRequest.getDob());
        user.setEmail(signupUserRequest.getEmailAddress());
        user.setFname(signupUserRequest.getFirstName());
        user.setLname(signupUserRequest.getLastName());
        user.setPassword(signupUserRequest.getPassword());
        user.setUsername(signupUserRequest.getUserName());
        user.setRole("nonadmin");

        final UserEntity createdEntity=signupBusinessService.signup(user);
        SignupUserResponse signupUserResponse= new SignupUserResponse().id(createdEntity.getUuid()).status("registered");
        return new ResponseEntity<SignupUserResponse>(signupUserResponse, HttpStatus.CREATED);

    }

    @RequestMapping(method = RequestMethod.POST, path = "/user/signin", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SigninResponse> login(@RequestHeader("authorization") final String authorization) throws AuthenticationFailedException {
        byte[] decode = Base64.getDecoder().decode(authorization.split("Basic ")[1]);
        String decodedText = new String(decode);
        String[] decodedArray = decodedText.split(":");

        UserAuthTokenEntity userAuthToken = authenticationService.authenticate(decodedArray[0], decodedArray[1]);
        UserEntity user = userAuthToken.getUser();

        SigninResponse signinResponse = new SigninResponse().id(user.getUuid()).message("SIGNED IN SUCCESSFULLY");

        HttpHeaders headers = new HttpHeaders();
        headers.add("access-token", userAuthToken.getAccessToken());
        return new ResponseEntity<SigninResponse>(signinResponse,headers, HttpStatus.OK);
    }

    @RequestMapping(method=RequestMethod.POST,path="/user/signout",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SignoutResponse> signout(@RequestHeader("access-token") final String accestoken) throws SignOutRestrictedException, AuthorizationFailedException {
        UserAuthTokenEntity userAuthToken=authenticationService.getAccessToken(accestoken,1);
        String userUuid=userAuthToken.getUser().getUuid();
        SignoutResponse signoutResponse=new SignoutResponse();
        signoutResponse.id(userUuid).message("SIGNED OUT SUCCESSFULLY");
        return new ResponseEntity<SignoutResponse>(signoutResponse,HttpStatus.OK);
    }



}
