package com.tmall.pokemon.bulbasaur.schedule.util;

import org.apache.commons.lang.StringUtils;

/**
 * @author : yunche.ch ... (ว ˙o˙)ง
 * Date: 18/3/22
 * Time: 下午5:31
 */
public class CommentUtil {

    public static final String SUFFIX = ", \n";

    /**
     * 得到一个字符串的长度,显示的长度,一个汉字或日韩文长度为1,英文字符长度为0.5
     *
     * @param s 需要得到长度的字符串
     * @return int 得到的字符串长度
     */
    public static int getLength(String s) {
        double valueLength = 0;
        String chinese = "[\u4e00-\u9fa5]";
        // 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1
        for (int i = 0; i < s.length(); i++) {
            // 获取一个字符
            String temp = s.substring(i, i + 1);
            // 判断是否为中文字符
            if (temp.matches(chinese)) {
                // 中文字符长度为1
                valueLength += 1;
            } else {
                // 其他字符长度为0.5
                valueLength += 0.5;
            }
        }
        //进位取整
        return (int)Math.ceil(valueLength);
    }

    /**
     * 截取字符长度，区分中英文
     *
     * @param abc 字符串内容
     * @param len 截取长度
     * @return
     */
    public static String subStr(String abc, int len) {
        if (StringUtils.isEmpty(abc) || len <= 0) { return ""; }
        StringBuffer stringBuffer = new StringBuffer();
        int sum = 0;
        char[] chars = abc.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (sum >= (len * 3)) {
                break;
            }
            char bt = chars[i];
            if (bt > 64 && bt < 123) {
                stringBuffer.append(String.valueOf(bt));
                sum += 2;
            } else {
                stringBuffer.append(String.valueOf(bt));
                sum += 3;
            }
        }
        return stringBuffer.toString();
    }

}
