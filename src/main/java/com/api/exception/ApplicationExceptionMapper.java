package com.api.exception;

import com.api.model.ResponseMes;
import lombok.extern.java.Log;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * 设置抛出的ApplicationException异常为ResponseMes对象
 *
 * @author 李星源
 * @version 1.0
 * @date 2020/03/11
 */
@Log
public class ApplicationExceptionMapper implements ExceptionMapper<ApplicationException> {

    /**
     * 用于将ApplicationException异常转换为ResponseMes对象
     *
     * @param e ApplicationException对象
     * @return ResponseMes对象
     */
    @Override
    public Response toResponse(ApplicationException e) {
        ResponseMes result=ResponseMes.failure(HttpServletResponse.SC_OK,e.getCode().getCode(),e.getCode().getValue(),0);
        return Response.ok(result, MediaType.APPLICATION_JSON).build();
    }

}
