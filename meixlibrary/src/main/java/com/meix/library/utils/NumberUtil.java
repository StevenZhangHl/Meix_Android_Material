package com.meix.library.utils;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * author steven
 * date 2021/12/15
 * description 数值格式化工具
 */
public class NumberUtil {
    /**
     * 保留2位小数且带千位符
     *
     * @param number
     * @return
     */
    public static String formatNumber(double number) {
        DecimalFormat df = new DecimalFormat("###,###.##");
        NumberFormat nf = NumberFormat.getInstance();
        String result = "";
        try {
            result = df.format((nf.parse(String.valueOf(number))));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (!result.contains(".")) {
            result = result + ".00";
        }
        return result;
    }

    /**
     * 带千位符但是不保留小数
     *
     * @param number
     * @return
     */
    public static String formatNumberNoPoint(double number) {
        String result = "";
        DecimalFormat df = new DecimalFormat("###,###");
        NumberFormat nf = NumberFormat.getInstance();
        try {
            result = df.format((nf.parse(String.valueOf(number))));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }

    public static float formatFloatPoint(float number,int scale) {
        BigDecimal b = new BigDecimal(number);
        float f = b.setScale(scale, BigDecimal.ROUND_HALF_UP).floatValue();
        return f;
    }

    public static int formatInteger(float number){
        BigDecimal b = new BigDecimal(number);
        int value = b.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
        return value;
    }

    /**
     * 字符串是否是字母
     *
     * @param s
     * @return
     */
    public static boolean isAlphabetic(String s) {
        Pattern p = Pattern.compile("[a-zA-Z]{1,}");
        Matcher m = p.matcher(s);
        return m.matches();
    }

    /**
     * 字符串是否是数字
     *
     * @param s
     * @return
     */
    public static boolean isNumber(String s) {
        Pattern p = Pattern.compile("-?[0-9]+(\\\\\\\\.[0-9]+)?");
        Matcher m = p.matcher(s);
        return m.matches();
    }

    /**
     * 字符串是否是字母和数字
     *
     * @param s
     * @return
     */
    public static boolean isAlphaNumeric(String s) {
        Pattern p = Pattern.compile("[0-9a-zA-Z]{1,}");
        Matcher m = p.matcher(s);
        return m.matches();
    }

    /**
     * 数字转中文数字
     *
     * @param d
     * @return
     */
    public static String getChineseNum(int d) {
        String[] str = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
        String ss[] = new String[]{"", "十", "百", "千", "万", "十", "百", "千", "亿"};
        String s = String.valueOf(d);
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < s.length(); i++) {
            String index = String.valueOf(s.charAt(i));
            sb = sb.append(str[Integer.parseInt(index)]);
        }
        String sss = String.valueOf(sb);
        int i = 0;
        for (int j = sss.length(); j > 0; j--) {
            sb = sb.insert(j, ss[i++]);
        }
        return sb.toString().trim();
    }
}
