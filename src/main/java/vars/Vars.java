package vars;

import entity.Production;
import entity.Token;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * @author jxf
 *
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
    public static String[] cat = { "id", "int", "double", "operator", "delimiter", "keyword", "char", "string", "comment", "space" };
    public static int pos=0, len=0;  // 当前字符位置和长度
    public static String code = "";
    public static String tempToken = "";  // 当前识别的字符串
    public static Vector<Token> tokenList = new Vector<>();  // 存储识别出的token


    /**
     * 语法分析
     */
    public static char[] analyeStack = new char[20]; /*分析栈*/
    public static char[] restStack = new char[20]; /*剩余栈*/
    //变量区
    //简单变量
    public static int top=0;
    public static int ridx=0;
    public static int len2=0; //len为输入串长度
    //复合变量
    public static Production e, t, g, g1, s, s1, f, f1; /* 产生式结构体变量 */
    public static Production[][]  C = new Production[10][10];

}
