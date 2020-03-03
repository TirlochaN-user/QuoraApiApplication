package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.QuestionRequest;
import com.upgrad.quora.api.model.QuestionResponse;
import com.upgrad.quora.service.business.AuthenticationService;
import com.upgrad.quora.service.business.QuestionBusinessService;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.SignOutRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;
import java.util.UUID;

@RestController
public class QuestionController {
    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private QuestionBusinessService questionBusinessService;

    @RequestMapping(method = RequestMethod.POST,path="/question/create",produces = MediaType.APPLICATION_JSON_UTF8_VALUE,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionResponse> createQuestion(@RequestHeader("access-token") String access_token, QuestionRequest questionRequest) throws AuthorizationFailedException, SignOutRestrictedException {
        UserAuthTokenEntity authTokenEntity=authenticationService.getAccessToken(access_token,3);
        QuestionEntity question=new QuestionEntity();
        question.setContent(questionRequest.getContent());
        question.setDate(ZonedDateTime.now());
        question.setUuid(UUID.randomUUID().toString());
        question.setUser(authTokenEntity.getUser());
        QuestionEntity createdQuestion=questionBusinessService.createQuestion(question);
        QuestionResponse questionResponse=new QuestionResponse();
        questionResponse.id(question.getUuid()).status("QUESTION CREATED");

        return new ResponseEntity<QuestionResponse>(questionResponse, HttpStatus.CREATED);

    }
}
