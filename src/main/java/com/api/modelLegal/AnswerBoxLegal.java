package com.api.modelLegal;

/**
 * @author 李星源
 * @version 1.0
 * @date 2020/03/19
 */
public class AnswerBoxLegal {

    public static boolean answerContentLegal(String answerContent){
        if ((answerContent==null)||(answerContent==""))
            return true;
        if ((answerContent.length()>30000)||(answerContent.length()<0))
            return false;
        return true;
    }
}
