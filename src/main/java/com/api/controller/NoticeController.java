package com.api.controller;

import com.api.jwt.JwtUtil;
import com.api.model.Notice;
import com.api.model.NoticeList;
import com.api.model.ResponseMes;
import com.api.service.NoticeService;
import com.api.service.NoticeServiceImpl;
import io.jsonwebtoken.Claims;
import lombok.extern.java.Log;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.Date;
import java.util.List;

/**
 * @author 李星源
 * @version 1.0
 * @date 2020/03/21
 */
@Log
@RolesAllowed({"admin","ordi","unact","ban"})//只允许管理员、普通用户、未激活用户、封禁用户访问
@Path("/notice")
public class NoticeController {
    NoticeService noticeService=new NoticeServiceImpl();

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"admin"})//只允许管理员访问
    public ResponseMes createNotice(Notice notice){
        noticeService.createNoticeLegal(notice);
        notice.setReleaseTime(new Date());
        if (notice.getStatus()==4)
            notice.setAccountName(null);
        noticeService.createNotice(notice);
        return ResponseMes.success(0);
    }

    @PUT
    @Path("/{noticeId}/{status}")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseMes updateStatus(@PathParam("noticeId") int noticeId,
                                    @PathParam("status") int status,
                                    @Context HttpHeaders headers){
        Claims claims= JwtUtil.parseJWT(headers.getHeaderString("jwt"));
        String accountName=(String)claims.get("accountName");

        noticeService.updateStatusLegal(noticeId,status,accountName);
        noticeService.updateStatus(noticeId,status);
        return ResponseMes.success(0);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseMes getNotices(@Context HttpHeaders headers){
        Claims claims= JwtUtil.parseJWT(headers.getHeaderString("jwt"));
        String accountName=(String)claims.get("accountName");

        List<Notice> noticeList=noticeService.getNotices(accountName);
        return ResponseMes.success(new NoticeList(noticeList.size(),noticeList));
    }
}
