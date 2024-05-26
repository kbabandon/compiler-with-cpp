package utils;

import vars.Finals;
import vars.Vars;

/**
 * @author jxf
 * @create 2024-05-25 10:58
 */
public class LexUtils {
    //var
    private static String[] keywords = Vars.keywords;
    private static String[] operate = Vars.operate;
    private static char[] delimiter = Vars.delimiter;
    //const isOP()
    private static String op = Finals.op;

    // 0-9
    public static boolean isDigit(char c){
        return c >= '0' && c <= '9';
    }
    // 是否为字母或下划线
    public static boolean isLetter(char c){
        return c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z' || c == '_';
    }

    public static boolean isKeyword(String s){
        for(int i = 0; i < 22; ++i)
            if (s == keywords[i])
                return true;
        return false;
    }

    public static boolean isOperator(String s){
        for(int i = 0; i < 28; ++i)
            if (s == operate[i])
                return true;
        return false;
    }

    public static boolean isDelimiter(char c){
        for(int i = 0; i < 15; ++i)
            if (c == delimiter[i])
                return true;
        return false;
    }

    public static boolean isOP(char c){
//        boolean flag = false;
        for(char temp: op.toCharArray()){
            if (temp == c)
                return true;
        }
        return false;
    }
}
