package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.SignupUserRequest;
import com.upgrad.quora.api.model.SignupUserResponse;
import com.upgrad.quora.service.business.SignupBusinessService;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/")
public class UserController {
    @Autowired
    private SignupBusinessService signupBusinessService;

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
}
