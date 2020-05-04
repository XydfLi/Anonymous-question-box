package com.api.modelLegal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author 李星源
 * @version 1.0
 * @date 2020/03/18
 */
public class QuestionBoxLegal {

    private static final String MaxTime="2100-01-01 00:00:00";
    private static Date dataMax;

    static {
        try {
            dataMax=(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(MaxTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static boolean questionContentLegal(String questionContent){
        if ((questionContent==null)||(questionContent==""))
            return true;
        if ((questionContent.length()>30000)||(questionContent.length()<0))
            return false;
        return true;
    }

}
