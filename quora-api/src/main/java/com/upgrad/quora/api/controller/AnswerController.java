package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.*;
import com.upgrad.quora.service.business.AnswerBusinessService;
import com.upgrad.quora.service.business.AuthenticationService;
import com.upgrad.quora.service.business.QuestionBusinessService;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.SignOutRestrictedException;
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

    @RequestMapping(method = RequestMethod.PUT,path = "/answer/edit/{answerId}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerEditResponse> editQuestion(@RequestHeader("access-token") String accessToken, @PathVariable("answerId") String answerId, AnswerEditRequest answerEditRequest) throws AuthorizationFailedException, SignOutRestrictedException, AnswerNotFoundException {
        UserAuthTokenEntity userAuthTokenEntity=authenticationService.getAccessToken(accessToken,9);
        answerBusinessService.checkOwner(answerId,userAuthTokenEntity,1);
        AnswerEntity answerEntity=answerBusinessService.editAnswerContent(answerId,answerEditRequest.getContent());
        AnswerEditResponse answerEditResponse=new AnswerEditResponse();
        answerEditResponse.id(answerEntity.getUuid()).status("ANSWER EDITED");

        return new ResponseEntity<AnswerEditResponse>(answerEditResponse,HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE,path = "/answer/delete/{answerId}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerDeleteResponse> deleteAnswer(@RequestHeader("access-token") String accessToken, String answerId) throws AuthorizationFailedException, SignOutRestrictedException, AnswerNotFoundException {
        UserAuthTokenEntity userAuthTokenEntity=authenticationService.getAccessToken(accessToken,10);
        answerBusinessService.checkOwner(answerId,userAuthTokenEntity,2);
        AnswerEntity answer=answerBusinessService.getAnswerByUuid(answerId);
        answerBusinessService.deleteAnswer(answer);
        AnswerDeleteResponse answerDeleteResponse=new AnswerDeleteResponse();
        answerDeleteResponse.id(answerId).status("ANSWER DELETED");

        return new ResponseEntity<AnswerDeleteResponse>(answerDeleteResponse,HttpStatus.OK);
    }
    
    @RequestMapping(method = RequestMethod.GET,path="answer/all/{questionId}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<AnswerDetailsResponse>> getAllAnswersToQuestion(@RequestHeader("access-token") String accessToken,@PathVariable("questionId") String questionId) throws AuthorizationFailedException, SignOutRestrictedException, InvalidQuestionException {
        UserAuthTokenEntity userAuthTokenEntity=authenticationService.getAccessToken(accessToken,11);
        QuestionEntity question=questionBusinessService.getQuestionByQuestionId(questionId);
        List<AnswerEntity> answerEntityList=answerBusinessService.getAnswerByQuestion(question);
        List<AnswerDetailsResponse> answerDetailsResponses=new ArrayList<AnswerDetailsResponse>();
        for(int i=0;i<answerEntityList.size();i++)
        {
            answerDetailsResponses.add(new AnswerDetailsResponse().answerContent(answerEntityList.get(i).getAns()).
                    questionContent(answerEntityList.get(i).getQuestion().getContent()).id(answerEntityList.get(i).getUuid()));
        }
        return new ResponseEntity<List<AnswerDetailsResponse>>(answerDetailsResponses,HttpStatus.OK);



    }
}
