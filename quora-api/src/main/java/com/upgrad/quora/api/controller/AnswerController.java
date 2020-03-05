package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.AnswerRequest;
import com.upgrad.quora.api.model.AnswerResponse;
import com.upgrad.quora.service.business.AnswerBusinessService;
import com.upgrad.quora.service.business.AuthenticationService;
import com.upgrad.quora.service.business.QuestionBusinessService;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.SignOutRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class AnswerController {
    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private QuestionBusinessService questionBusinessService;

    @Autowired
    private AnswerBusinessService answerBusinessService;

    @RequestMapping(method = RequestMethod.POST,path="/question/{questionId}/answer/create",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerResponse> createAnswer(@RequestHeader("access-token") String accessToken, @PathVariable("questionId") String questionId,AnswerRequest answerRequest) throws AuthorizationFailedException, SignOutRestrictedException, InvalidQuestionException {
        UserAuthTokenEntity userAuthTokenEntity=authenticationService.getAccessToken(accessToken,8);
        QuestionEntity question=questionBusinessService.getQuestionByQuestionId(questionId);
        AnswerEntity answer=new AnswerEntity();
        answer.setAns(answerRequest.getAnswer());
        answer.setQuestion(question);
        answer.setUser(userAuthTokenEntity.getUser());
        answer.setUuid(UUID.randomUUID().toString());
        answer.setDate(ZonedDateTime.now());
        answerBusinessService.createAnswer(answer);
        AnswerResponse answerResponse=new AnswerResponse();
        answerResponse.setId(answer.getUuid());
        answerResponse.setStatus("ANSWER CREATED");

        return new ResponseEntity<AnswerResponse>(answerResponse, HttpStatus.CREATED);
    }
}
