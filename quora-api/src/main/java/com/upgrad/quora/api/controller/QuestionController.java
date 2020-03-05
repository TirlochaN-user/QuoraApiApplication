package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.*;
import com.upgrad.quora.service.business.AuthenticationService;
import com.upgrad.quora.service.business.QuestionBusinessService;
import com.upgrad.quora.service.business.UserCommonBusinessService;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.SignOutRestrictedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
public class QuestionController {
    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private QuestionBusinessService questionBusinessService;

    @Autowired
    private UserCommonBusinessService userCommonBusinessService;

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

    @RequestMapping(method = RequestMethod.GET,path = "/question/all",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<QuestionDetailsResponse>> getAllQuestions(@RequestHeader("access-token") String accessToken) throws AuthorizationFailedException, SignOutRestrictedException {
        UserAuthTokenEntity userAuthTokenEntity=authenticationService.getAccessToken(accessToken,4);
        List<QuestionEntity> allQuestion = questionBusinessService.getAllQuestions();
        List<QuestionDetailsResponse> questionDetailsResponses=new ArrayList<QuestionDetailsResponse>();
        for(int i=0;i<allQuestion.size();i++)
        {
            questionDetailsResponses.add(new QuestionDetailsResponse().content(allQuestion.get(i).getContent())
                    .id(allQuestion.get(i).getUuid()));
        }
        return new ResponseEntity<List<QuestionDetailsResponse>>(questionDetailsResponses,HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT,path = "/question/edit/{questionId}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionEditResponse> editQuestion(@RequestHeader("access-token") String accessToken, @PathVariable("questionId") String questionId, QuestionEditRequest questionEditRequest) throws AuthorizationFailedException, SignOutRestrictedException, InvalidQuestionException {
        UserAuthTokenEntity userAuthTokenEntity=authenticationService.getAccessToken(accessToken,5);
        authenticationService.checkOwner(userAuthTokenEntity,questionId,1);
        QuestionEntity question=questionBusinessService.editQuestionContent(questionId,questionEditRequest.getContent());
        QuestionEditResponse questionEditResponse=new QuestionEditResponse();
        questionEditResponse.id(question.getUuid()).status("QUESTION EDITED");

        return new ResponseEntity<QuestionEditResponse>(questionEditResponse,HttpStatus.OK);
    }

    @RequestMapping(method=RequestMethod.DELETE,path="/question/delete/{questionId}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionDeleteResponse> deleteQuestion(@RequestHeader("access-token") String accessToken,@PathVariable("questionId") String questionId) throws AuthorizationFailedException, SignOutRestrictedException, InvalidQuestionException {
        UserAuthTokenEntity userAuthTokenEntity=authenticationService.getAccessToken(accessToken,6);
        authenticationService.checkOwner(userAuthTokenEntity,questionId,2);
        QuestionEntity question=questionBusinessService.getQuestionByQuestionId(questionId);
        questionBusinessService.deleteQuestion(question.getUuid());
        QuestionDeleteResponse questionDeleteResponse=new QuestionDeleteResponse();
        questionDeleteResponse.id(question.getUuid()).status("QUESTION DELETED");

        return new ResponseEntity<QuestionDeleteResponse>(questionDeleteResponse,HttpStatus.OK);

    }

    @RequestMapping(method = RequestMethod.GET,path = "question/all/{userId}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<QuestionDetailsResponse>> getAllQuestionsByUser(@RequestHeader("access-token") String accessToken,@PathVariable("userId") String userId) throws AuthorizationFailedException, SignOutRestrictedException, UserNotFoundException {
        UserAuthTokenEntity userAuthTokenEntity=authenticationService.getAccessToken(accessToken,7);
        UserEntity user=userCommonBusinessService.getUserByUuid(userId,3);
        List<QuestionEntity> allQuestions=questionBusinessService.getAllQuestionsByUser(user);
        List<QuestionDetailsResponse> questionDetailsResponses=new ArrayList<QuestionDetailsResponse>();
        for(int i=0;i<allQuestions.size();i++)
        {
            questionDetailsResponses.add(new QuestionDetailsResponse().content(allQuestions.get(i).getContent())
                    .id(allQuestions.get(i).getUuid()));
        }
        return new ResponseEntity<List<QuestionDetailsResponse>>(questionDetailsResponses,HttpStatus.OK);
    }


}
