package com.hxnidc.qiu_ly.utils;

import android.util.Log;

import com.orhanobut.logger.Logger;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * <pre>
 *     author: yrg
 *     time  : 2016/8/16
 *     desc  : 字符串相关工具类
 * </pre>
 */
public class StringUtils {

    private StringUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 判断字符串是否为null或长度为0
     *
     * @param s 待校验字符串
     * @return {@code true}: 空<br> {@code false}: 不为空
     */
    public static boolean isEmpty(CharSequence s) {
        return s == null || s.length() == 0;
    }

    /**
     * 判断字符串是否为null或全为空格
     *
     * @param s 待校验字符串
     * @return {@code true}: null或全空格<br> {@code false}: 不为null且不全空格
     */
    public static boolean isSpace(String s) {
        return (s == null || s.trim().length() == 0);
    }

    /**
     * 判断两字符串是否相等
     *
     * @param a 待校验字符串a
     * @param b 待校验字符串b
     * @return {@code true}: 相等<br>{@code false}: 不相等
     */
    public static boolean equals(CharSequence a, CharSequence b) {
        if (a == b) return true;
        int length;
        if (a != null && b != null && (length = a.length()) == b.length()) {
            if (a instanceof String && b instanceof String) {
                return a.equals(b);
            } else {
                for (int i = 0; i < length; i++) {
                    if (a.charAt(i) != b.charAt(i)) return false;
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 判断两字符串忽略大小写是否相等
     *
     * @param a 待校验字符串a
     * @param b 待校验字符串b
     * @return {@code true}: 相等<br>{@code false}: 不相等
     */
    public static boolean equalsIgnoreCase(String a, String b) {
        return (a == b) || (b != null) && (a.length() == b.length()) && a.regionMatches(true, 0, b, 0, b.length());
    }

    /**
     * null转为长度为0的字符串
     *
     * @param s 待转字符串
     * @return s为null转为长度为0字符串，否则不改变
     */
    public static String null2Length0(String s) {
        return s == null ? "" : s;
    }

    /**
     * 返回字符串长度
     *
     * @param s 字符串
     * @return null返回0，其他返回自身长度
     */
    public static int length(CharSequence s) {
        return s == null ? 0 : s.length();
    }

    /**
     * 首字母大写
     *
     * @param s 待转字符串
     * @return 首字母大写字符串
     */
    public static String upperFirstLetter(String s) {
        if (isEmpty(s) || !Character.isLowerCase(s.charAt(0))) return s;
        return String.valueOf((char) (s.charAt(0) - 32)) + s.substring(1);
    }

    public static boolean isBlank(String str) {

        return (str == null || str.trim().length() == 0);
    }

    /**
     * 首字母小写
     *
     * @param s 待转字符串
     * @return 首字母小写字符串
     */
    public static String lowerFirstLetter(String s) {
        if (isEmpty(s) || !Character.isUpperCase(s.charAt(0))) {
            return s;
        }
        return String.valueOf((char) (s.charAt(0) + 32)) + s.substring(1);
    }

    /**
     * 反转字符串
     *
     * @param s 待反转字符串
     * @return 反转字符串
     */
    public static String reverse(String s) {
        int len = length(s);
        if (len <= 1) return s;
        int mid = len >> 1;
        char[] chars = s.toCharArray();
        char c;
        for (int i = 0; i < mid; ++i) {
            c = chars[i];
            chars[i] = chars[len - i - 1];
            chars[len - i - 1] = c;
        }
        return new String(chars);
    }

    /**
     * 转化为半角字符
     *
     * @param s 待转字符串
     * @return 半角字符串
     */
    public static String toDBC(String s) {
        if (isEmpty(s)) {
            return s;
        }
        char[] chars = s.toCharArray();
        for (int i = 0, len = chars.length; i < len; i++) {
            if (chars[i] == 12288) {
                chars[i] = ' ';
            } else if (65281 <= chars[i] && chars[i] <= 65374) {
                chars[i] = (char) (chars[i] - 65248);
            } else {
                chars[i] = chars[i];
            }
        }
        return new String(chars);
    }

    public static double StringToDouble(String s) {

        try {
            double d = Double.parseDouble(s.replace(",", ""));
            return d;

        } catch (Exception e) {
            e.getMessage();
        }
        return 0;

    }


    public static int StringToIntValue(String s) {

        try {
            return Integer.parseInt(s.replace(",", ""));
        } catch (Exception e) {
            return -1;
        }

    }

    public static String DoubleToStr(double d) {

        try {

            BigDecimal b = new BigDecimal(d);
            double f1 = b.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
            return f1 + "";

        } catch (Exception e) {
            e.getMessage();
        }
        return null;

    }

    /**
     * 转化为全角字符
     *
     * @param s 待转字符串
     * @return 全角字符串
     */
    public static String toSBC(String s) {
        if (isEmpty(s)) {
            return s;
        }
        char[] chars = s.toCharArray();
        for (int i = 0, len = chars.length; i < len; i++) {
            if (chars[i] == ' ') {
                chars[i] = (char) 12288;
            } else if (33 <= chars[i] && chars[i] <= 126) {
                chars[i] = (char) (chars[i] + 65248);
            } else {
                chars[i] = chars[i];
            }
        }
        return new String(chars);
    }

    //StringToDouble StringToInt  DoubleToString DoubleToInt

/*
    public static String doubleToStr(String string) {
        try {
            DecimalFormat f = new DecimalFormat("#,###");
            return f.format(string);
        } catch (Exception e) {

        }
        return "";
    }
*/

    public static String formatting(Double value) {
        try {
            DecimalFormat f = new DecimalFormat("#,###");
            return f.format(value);
        } catch (Exception e) {

        }
        return "";
    }

    public static int StringTOInt(String string) {
        try {
            return Integer.parseInt(string);
        } catch (Exception e) {
            Log.e("=====", e.getMessage());
        }

        return -1;
    }

    public static String formatRate(double value) {
        try {
            //if (value < 0) value *= -1;
            return String.format("%,.2f", value * 100)+"%";
        } catch (Exception e) {
            return "";
        }
    }

    public static String format(double value) {
        try {
            //if (value < 0) value *= -1;
            return String.format("%,.0f", value);

        } catch (Exception e) {
            return "";
        }
    }

    public static String format(Integer value) {
        try {
            //if (value < 0) value *= -1;
            return String.format("%,d", value);
        } catch (Exception e) {
            return value.toString();
        }
    }


    public static String format(int value) {
        try {
            //if (value < 0) value *= -1;
            return String.format("%,d", value);
        } catch (Exception e) {
            return "";
        }
    }

    public static String formatAbs(double value) {
        try {
            if (value < 0) value *= -1;
            return String.format("%,.0f", value);

        } catch (Exception e) {
            return "";
        }
    }

    public static String formatAbs(Integer value) {
        try {
            if (value < 0) value *= -1;
            return String.format("%,d", value);
        } catch (Exception e) {
            return value.toString();
        }
    }


    public static String formatAbs(int value) {
        try {
            if (value < 0) value *= -1;
            return String.format("%,d", value);
        } catch (Exception e) {
            return "";
        }
    }

    public static boolean isBigOrSmall(double value) {
        try {
            if (value < 0) return true;
            else return false;
        } catch (Exception e) {
            return false;
        }
    }

    public static double removeRow(double value) {
        try {
            String string = Double.toString(value);
            if (string.contains("-"))
                return Double.parseDouble(string.replaceAll("[-]", ""));
            else return value;
        } catch (Exception e) {
            Logger.d(e.getMessage());
        }
        return value;
    }

    public static String replaceAlls(String value) {
        try {
            if (!value.contains(",")) return value;
            else return value.replaceAll(",", "");
        } catch (Exception e) {
            return value;
        }
    }

    public static String Number2Hangle(int lngNumber) {
        boolean UseDecimal = false;
        String Sign = "";
        int i = 0;
        int Level = 0;

        String[] NumberChar = new String[]{"", "일", "이", "삼", "사", "오", "육", "칠", "팔", "구"};
        String[] LevelChar = new String[]{"", "십", "백", "천"};
        String[] DecimalChar = new String[]{"", "만", "억 ", "조 ", "경 "};

        String strValue = lngNumber+"";
        String NumToKorea = Sign;
        UseDecimal = false;

        for (i = 0; i < strValue.length(); i++) {
            Level = strValue.length() - i;
            //Console.WriteLine("Level : " + Level);

            // 0 이 아니면 숫자에 대한 한글이 붙어야 한다.
            if (strValue.substring(i, i+1).equals("0") == false) {
                UseDecimal = true;

                // 천단위 숫자이면
                if (((Level - 1) % 4) == 0) {
                    NumToKorea = NumToKorea + NumberChar[Integer.parseInt(strValue.substring(i, i+1))] + DecimalChar[(Level - 1) / 4];
                    UseDecimal = false;
                } else // 천단위가 아니라면
                {
                    if (strValue.substring(i, i+1).equals("1")) {
                        NumToKorea = NumToKorea + LevelChar[(Level - 1) % 4];
                    } else {
                        NumToKorea = NumToKorea + NumberChar[Integer.parseInt(strValue.substring(i, i+1))] + LevelChar[(Level - 1) % 4];
                    }
                }
            } else // 0 이라면
            {
                // 만단위라면 만단위 한글이 붙어야 한다.
                if ((Level % 4 == 0) && UseDecimal) {
                    NumToKorea = NumToKorea + DecimalChar[Level / 4];
                    UseDecimal = false;
                }
            }
        }
        return NumToKorea;
    }

    /*public static String DoubleToInt(String string) {
        try {
            StringBuffer newPrice = new StringBuffer();
            newPrice.append(string);
            int i = newPrice.length();
            if (i > 3) {
                for (int j = i - 3; j > 0; j = j - 3) {
                    newPrice.insert(j, ",");
                }
                return newPrice.toString();
            } else
                return string;

        } catch (Exception e) {

        }
        return string;
    }*/
}