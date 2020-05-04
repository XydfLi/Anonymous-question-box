package com.api.VerifyCode;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * @author 李星源
 * @version 1.0
 * @date 2020/03/22
 */
public class VerifyCodeUtil {

    // 验证码字符集（去掉0、1、i、o、O）
    private static final char[] chars = {
            '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'm',
            'n', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

    // 验证码位数
    private static final int CODE_SIZE = 4;
    // 图片上干扰线数量
    private static final int LINES = 5;
    // 图片宽度
    private static final int IMAGE_WIDTH = 80;
    // 图片高度
    private static final int IMAGE_HEIGHT = 40;
    // 图片字体大小
    private static final int FONT_SIZE = 30;

    /**
     * 生成随机验证码及图片
     * Object[0]：验证码字符串；
     * Object[1]：验证码图片。
     */
    public static Object[] createImage() {
        StringBuffer sb = new StringBuffer();
        // 1.创建空白图片
        BufferedImage image = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);
        // 2.获取图片画笔
        Graphics graphic = image.getGraphics();
        // 3.设置画笔颜色
        graphic.setColor(Color.LIGHT_GRAY);
        // 4.绘制矩形背景
        graphic.fillRect(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
        // 5.画随机字符
        Random ran = new Random();
        for (int i = 0; i < CODE_SIZE; i++) {
            // 取随机字符索引
            int n = ran.nextInt(chars.length);
            // 设置随机颜色
            graphic.setColor(getRandomColor());
            // 设置字体大小
            graphic.setFont(new Font(null, Font.BOLD + Font.ITALIC, FONT_SIZE));
            // 画字符
            graphic.drawString(chars[n] + "", i * IMAGE_WIDTH / CODE_SIZE, IMAGE_HEIGHT * 2 / 3);
            // 记录字符
            sb.append(chars[n]);
        }
        // 6.画干扰线
        for (int i = 0; i < LINES; i++) {
            // 设置随机颜色
            graphic.setColor(getRandomColor());
            // 随机画线
            graphic.drawLine(ran.nextInt(IMAGE_WIDTH), ran.nextInt(IMAGE_HEIGHT),
                    ran.nextInt(IMAGE_WIDTH), ran.nextInt(IMAGE_HEIGHT));
        }
        // 7.返回验证码和图片
        return new Object[]{sb.toString(), image};
    }
    /**
     * 获取随机背景颜色(RGB)
     */
    private static Color getRandomColor() {
        Random ran = new Random();
        Color color = new Color(ran.nextInt(256), ran.nextInt(256), ran.nextInt(256));
        return color;
    }
}
