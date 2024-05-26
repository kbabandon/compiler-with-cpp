package vars;

import entity.Token;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * @author jxf
 * @create 2024-05-25 11:26
 */
public class Vars {
    /**
     * LexUtils中使用的变量
     */
    public static String[] keywords = new String[22];
    public static String[] operate = new String[28];
    public static char[] delimiter = new char[15];
    /**
     * 其他全局变量
     */
    public static Map<String, Integer> categoryCode = new HashMap<>();  // 种别码表
    public static String cat[] = { "id", "int", "double", "operator", "delimiter", "keyword", "char", "string", "comment", "space" };
    public static int pos, len;  // 当前字符位置和长度
    public static String code, tempToken;  // 当前识别的字符串
    public static Vector<Token> tokenList;  // 存储识别出的token
}
