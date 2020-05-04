package com.api.controller;

import com.api.dao.AccountDaoImpl;
import com.api.dao.AnswerBoxDaoImpl;
import com.api.dao.BaseCRUD;
import com.api.jwt.JwtUtil;
import com.api.model.*;
import com.api.service.QuestionBoxService;
import com.api.service.QuestionBoxServiceImpl;
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
 * @date 2020/03/18
 */
@Log
@RolesAllowed({"admin","ordi"})//只允许管理员、普通用户访问
@Path("/questionBox")
public class QuestionBoxController {

    private QuestionBoxService questionBoxService=new QuestionBoxServiceImpl();

    @POST
    @Path("/question")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResponseMes createQuestion(QuestionBox questionBox,@Context HttpHeaders headers){
        Claims claims=JwtUtil.parseJWT(headers.getHeaderString("jwt"));
        String accountName=(String)claims.get("accountName");

        questionBoxService.createQuestionLegal(questionBox);
        questionBox.setAccountName(accountName);
        questionBox.setQuestionTime(new Date());//设置当前时间
        return ResponseMes.success(questionBoxService.createQuestion(questionBox));
    }

    @PUT
    @Path("/question")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResponseMes updateQuestion(QuestionBox questionBox,@Context HttpHeaders headers){
        Claims claims=JwtUtil.parseJWT(headers.getHeaderString("jwt"));
        String accountName=(String)claims.get("accountName");

        questionBox.setAccountName(accountName);
        questionBoxService.updateQuestionLegal(questionBox);
        questionBoxService.updateQuestion(questionBox);
        return ResponseMes.success(0);
    }

    @GET
    @Path("/question/ans")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseMes readQuestionAns(@Context HttpHeaders headers){
        Claims claims=JwtUtil.parseJWT(headers.getHeaderString("jwt"));
        String accountName=(String)claims.get("accountName");

        List<QuestionBox> questionBoxList=questionBoxService.readQuestion(accountName);
        int len=questionBoxList.size();
        for (int i=0;i<len;i++)
            questionBoxList.get(i).setAccountName(null);//传输中设置提问人匿名
        BaseCRUD<AnswerBox> answerBoxBaseCRUD=new AnswerBoxDaoImpl();
        List<AnswerBox> answerBoxes;
        List<QuestionBox> ans=new ArrayList<>();
        for (int i=0;i<len;i++){
            answerBoxes=answerBoxBaseCRUD.readAll(new Object[]{questionBoxList.get(i).getQuestionId()});
            boolean flag=false;
            for (int j=0;j<answerBoxes.size();j++){
                if (answerBoxes.get(j).getStatus()==2)
                    flag=true;
            }
            if (flag)
                ans.add(questionBoxList.get(i));
        }
        return ResponseMes.success(new QuestionList(ans.size(),ans));
    }

    @GET
    @Path("/question/{questionId}")
    @RolesAllowed({"admin"})//只允许管理员访问
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseMes getQuestionAccount(@PathParam("questionId") int questionId){

        questionBoxService.readAccountLegal(questionId);
        return ResponseMes.success(questionBoxService.readAccount(questionId).getAccountName());
    }

    @GET
    @Path("/questionBoxState")
    @RolesAllowed({"admin","ordi"})//只允许管理员、普通用户访问
    public ResponseMes getQuestionBoxState(@Context HttpHeaders headers){
        Claims claims=JwtUtil.parseJWT(headers.getHeaderString("jwt"));
        String accountName=(String)claims.get("accountName");
        BaseCRUD<Account> accountBaseCRUD=new AccountDaoImpl();
        Account account=accountBaseCRUD.readByKey(new Object[]{accountName,"accountName"});
        if (account.isQuestionBoxStatus())
            return ResponseMes.success(1);
        else
            return ResponseMes.success(0);
    }

}
