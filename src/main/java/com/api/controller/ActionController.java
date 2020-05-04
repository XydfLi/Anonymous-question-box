package com.api.controller;

import com.api.jwt.JwtUtil;
import com.api.model.*;
import com.api.service.ActionService;
import com.api.service.ActionServiceImpl;
import io.jsonwebtoken.Claims;
import lombok.extern.java.Log;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * @author 李星源
 * @version 1.0
 * @date 2020/03/19
 */
@Log
@RolesAllowed({"admin","ordi"})//只允许管理员、普通用户访问
@Path("/action")
public class ActionController {
    private ActionService actionService=new ActionServiceImpl();

    @POST
    @Path("/black/{questionId}")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseMes blackAccount(@PathParam("questionId") int questionId,@Context HttpHeaders headers){
        Claims claims= JwtUtil.parseJWT(headers.getHeaderString("jwt"));
        String accountName=(String)claims.get("accountName");

        actionService.blackAccountLegal(questionId,accountName);
        Action action=new Action(accountName,true,2,questionId);
        actionService.blackAccount(action);
        return ResponseMes.success(0);
    }

    @PUT
    @Path("/black/{questionId}")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseMes NotBlackAccount(@PathParam("questionId") int questionId,@Context HttpHeaders headers){
        Claims claims= JwtUtil.parseJWT(headers.getHeaderString("jwt"));
        String accountName=(String)claims.get("accountName");

        actionService.NotBlackAccountLegal(questionId,accountName);
        actionService.NotBlackAccount(questionId,accountName);
        return ResponseMes.success(0);
    }

    @POST
    @Path("/tipOff/{questionId}")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseMes tipOffAccount(@PathParam("questionId") int questionId,@Context HttpHeaders headers){
        Claims claims= JwtUtil.parseJWT(headers.getHeaderString("jwt"));
        String accountName=(String)claims.get("accountName");

        actionService.tipOffAccountLegal(questionId);
        Action action=new Action(accountName,false,1,questionId);
        actionService.tipOffAccount(action);
        return ResponseMes.success(0);
    }

    @GET
    @Path("/black")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseMes getBlackList(@Context HttpHeaders headers){
        Claims claims= JwtUtil.parseJWT(headers.getHeaderString("jwt"));
        String accountName=(String)claims.get("accountName");

        List<QuestionBox> questionBoxList=actionService.getBlackList(accountName);
        return ResponseMes.success(new QuestionList(questionBoxList.size(),questionBoxList));
    }

    @GET
    @Path("/tipOff")
    @RolesAllowed({"admin"})//只允许管理员访问
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseMes getBlackList(){
        List<QuestionBox> questionBoxList=actionService.getTipOffList();
        return ResponseMes.success(new QuestionList(questionBoxList.size(),questionBoxList));
    }

    @PUT
    @Path("/ban/{questionId}/{success}")
    @RolesAllowed({"admin"})//只允许管理员访问
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResponseMes banAccount(@PathParam("questionId") int questionId,
                                  QuestionBox questionBox,
                                  @PathParam("success") int success){
        String reason=questionBox.getQuestionContent();
        actionService.banAccountLegal(questionId,reason,success);
        actionService.banAccount(questionId, success,reason);
        return ResponseMes.success(0);
    }

    @POST
    @Path("/ban/{id}/{ma}")
    @RolesAllowed({"admin"})//只允许管理员访问
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseMes removeBan(@PathParam("id") String id,
                                 @PathParam("ma") int ma){
        String accountName=actionService.removeBanLegal(id,ma);
        actionService.removeBan(accountName);
        return ResponseMes.success(0);
    }
}
