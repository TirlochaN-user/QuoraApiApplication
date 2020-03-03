package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.UserDeleteResponse;
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
public class AdminController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserCommonBusinessService userCommonBusinessService;

    @RequestMapping(method = RequestMethod.DELETE,path="/admin/user/{userId}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UserDeleteResponse> deleteUser(@RequestHeader("access-token") String access_token, @PathVariable("userId") String uuid) throws AuthorizationFailedException, SignOutRestrictedException, UserNotFoundException {
        UserAuthTokenEntity userAuthTokenEntity=authenticationService.getAccessToken(access_token,2);
        UserEntity user=userCommonBusinessService.getUserByUuid(uuid,2);
        userCommonBusinessService.deleteUser(user.getUuid());
        UserDeleteResponse userDeleteResponse=new UserDeleteResponse();
        userDeleteResponse.id(user.getUuid()).status("USER SUCCESSFULLY DELETED");
        return new ResponseEntity<UserDeleteResponse>(userDeleteResponse, HttpStatus.OK);



    }

}
