package com.api.controller;

import com.api.dao.AnswerBoxDaoImpl;
import com.api.dao.BaseCRUD;
import com.api.dao.QuestionBoxDaoImpl;
import com.api.jwt.JwtUtil;
import com.api.model.*;
import com.api.service.AnswerBoxService;
import com.api.service.AnswerBoxServiceImpl;
import io.jsonwebtoken.Claims;
import lombok.extern.java.Log;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author 李星源
 * @version 1.0
 * @date 2020/03/19
 */
@Log
@RolesAllowed({"admin","ordi"})//允许管理员、普通用户访问
@Path("/answerBox")
public class AnswerBoxController {

    private AnswerBoxService answerBoxService=new AnswerBoxServiceImpl();

    @POST
    @Path("/answer/{questionId}/{status}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResponseMes createAnswer(Account account,
                                    @PathParam("questionId") int questionId,
                                    @PathParam("status") int status,
                                    @Context HttpHeaders headers){
        Claims claims= JwtUtil.parseJWT(headers.getHeaderString("jwt"));
        String accountName=(String)claims.get("accountName");

        String answerAccount=account.getAccountName();
        answerBoxService.createAnswerLegal(answerAccount,questionId,status,accountName);
        AnswerBox answerBox=new AnswerBox(questionId,answerAccount,"未回答",new Date(),1,2);
        answerBoxService.createAnswer(answerBox,status,accountName);
        return ResponseMes.success(0);
    }

    @PUT
    @Path("/answer/{questionId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResponseMes answerQuestion(@Context HttpHeaders headers,
                                      @PathParam("questionId") int questionId,
                                      AnswerBox answerBox){
        Claims claims= JwtUtil.parseJWT(headers.getHeaderString("jwt"));
        String accountName=(String)claims.get("accountName");

        String content=answerBox.getAnswerContent();
        answerBoxService.answerQuestionLegal(content,questionId,accountName);
        answerBoxService.answerQuestion(content,questionId,accountName);
        return ResponseMes.success(0);
    }

    @PUT
    @Path("/answer/{questionId}/{deleted}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResponseMes updateDelete(@Context HttpHeaders headers,
                                      @PathParam("questionId") int questionId,
                                      @PathParam("deleted") int deleted){
        Claims claims= JwtUtil.parseJWT(headers.getHeaderString("jwt"));
        String accountName=(String)claims.get("accountName");

        answerBoxService.updateDeleteLegal(questionId,accountName);
        answerBoxService.updateDelete(deleted,questionId,accountName);
        return ResponseMes.success(0);
    }

    @GET
    @Path("/answer/{questionId}")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseMes readAnswer(@PathParam("questionId") int questionId,@Context HttpHeaders headers){
        Claims claims=JwtUtil.parseJWT(headers.getHeaderString("jwt"));
        String accountName=(String)claims.get("accountName");

        answerBoxService.readAnswerLegal(questionId,accountName);
        List<AnswerBox> answerBoxList=answerBoxService.readAnswer(questionId);
        List<AnswerBox> ans=new ArrayList<>();
        for (int i=0;i<answerBoxList.size();i++){
            if ((answerBoxList.get(i).getStatus()==2)&&(answerBoxList.get(i).getDeleted()==2))
                ans.add(answerBoxList.get(i));
        }
        return ResponseMes.success(new AnswerList(ans.size(),ans));
    }

    @GET
    @Path("/answer/Y")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseMes readAnswerY(@Context HttpHeaders headers){
        Claims claims=JwtUtil.parseJWT(headers.getHeaderString("jwt"));
        String accountName=(String)claims.get("accountName");

        AnswerBoxDaoImpl answerBoxDao=new AnswerBoxDaoImpl();
        List<AnswerBox> answerBoxList=answerBoxDao.AccountNameReadAll(new Object[]{accountName});
        List<AnswerBox> ans=new ArrayList<>();
        for (int i=0;i<answerBoxList.size();i++){
            if ((answerBoxList.get(i).getStatus()==2)&&(answerBoxList.get(i).getDeleted()==2))
                ans.add(answerBoxList.get(i));
        }
        return ResponseMes.success(new AnswerList(ans.size(),ans));
    }

    @GET
    @Path("/answer/N")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseMes readAnswerN(@Context HttpHeaders headers){
        Claims claims=JwtUtil.parseJWT(headers.getHeaderString("jwt"));
        String accountName=(String)claims.get("accountName");

        AnswerBoxDaoImpl answerBoxDao=new AnswerBoxDaoImpl();
        BaseCRUD<QuestionBox> questionBoxBaseCRUD=new QuestionBoxDaoImpl();
        List<AnswerBox> answerBoxList=answerBoxDao.AccountNameReadAll(new Object[]{accountName});
        List<QuestionBox> ans=new ArrayList<>();
        for (int i=0;i<answerBoxList.size();i++){
            if ((answerBoxList.get(i).getStatus()==1)&&(answerBoxList.get(i).getDeleted()==2)) {
                QuestionBox questionBox=questionBoxBaseCRUD.readByKey(new Object[]{answerBoxList.get(i).getQuestionId()});
                ans.add(questionBox);
            }
        }
        return ResponseMes.success(new QuestionList(ans.size(),ans));
    }

    @GET
    @Path("/answer/D")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseMes readAnswerD(@Context HttpHeaders headers){
        Claims claims=JwtUtil.parseJWT(headers.getHeaderString("jwt"));
        String accountName=(String)claims.get("accountName");

        AnswerBoxDaoImpl answerBoxDao=new AnswerBoxDaoImpl();
        List<AnswerBox> answerBoxList=answerBoxDao.AccountNameReadAll(new Object[]{accountName});
        List<AnswerBox> ans=new ArrayList<>();
        for (int i=0;i<answerBoxList.size();i++){
            if (answerBoxList.get(i).getDeleted()==1)
                ans.add(answerBoxList.get(i));
        }
        return ResponseMes.success(new AnswerList(ans.size(),ans));
    }
}
