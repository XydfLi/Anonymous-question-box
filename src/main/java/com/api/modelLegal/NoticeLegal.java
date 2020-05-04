package com.api.modelLegal;

/**
 * @author 李星源
 * @version 1.0
 * @date 2020/03/21
 */
public class NoticeLegal {

    public static boolean contentLegal(String content){
        if (content==null||content=="")
            return true;
        int len=content.length();
        if (len<0||len>30000)
            return false;
        return true;
    }

}
