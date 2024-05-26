import entity.Token;
import entity.Type;
import utils.LexUtils;
import vars.Finals;
import vars.Vars;

import java.io.FileReader;
import java.util.Map;
import java.util.Scanner;
import java.util.Vector;

/**
 * @author jxf
 * @create 2024-05-25 10:54
 */
public class lextotal {
    //var main
    private static int pos = Vars.pos;//相当于文件指针
    //occurs in all methods except for readFile() and peek()
    private static int len = Vars.len;
    private static Vector<Token> tokenList = Vars.tokenList; //结果列表
    /* var init()中init */
    // the follwing three vars is only changed by methods of LexUtils Class
    private static String[]  keywords = Vars.keywords;
    private static String[]  operate = Vars.operate;
    private static char[]  delimiter = Vars.delimiter;
    // init() and read_next()
    private static Map<String, Integer> categoryCode = Vars.categoryCode;  // 种别码表
    //var read_next()
    private static String code = Vars.code;//读入的代码文件存放变量
    private static String tempToken = Vars.tempToken;//单行代码
    public static String cat[] = Vars.cat;

    //const
    //const - main()
    private static final int _EOF_ = Finals._EOF_; // in main() and read_next()
    private static final int _ERROR_ = Finals._ERROR_; //in main() and read_next() and judge()
    //const - init()
    private static final String CategoryFileName = Finals.CategoryFileName;
    private static final String CodeFileName = Finals.CodeFileName;

    public static void main(String[] args) {
        init();
        try {
            while (pos < len){
                int flag = read_next();
                if(flag == _EOF_){
                    break;
                }
                else if(flag == _ERROR_){
                    tokenList.add(new Token(_ERROR_, "ERROR!", "ERROR"));
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        //输出结果,tokenList-token结果列表
        for(Token token:tokenList){
            System.out.println(token);
        }
    }

    private static Vector<String> readFile(String fileName) {
        Vector<String> res = new Vector<>();
        try {
            Scanner scanner;
            scanner = new Scanner(new FileReader(fileName));
            String temp;
            while (scanner.hasNextLine()){
                temp = scanner.nextLine();
                res.add(temp);
            }
            return res;
        }catch (Exception e){
            e.printStackTrace();
            return res;
        }
    }

    private static void init() {
        //1.读取符号文件
        Vector<String> res = readFile(CategoryFileName);
        for (int i = 0; i < 22; i++) {
//            String temp = res.get(i);
            keywords[i] = res.get(i);
            categoryCode.put(res.get(i), i+1);
        }
        for (int i = 0; i < 28; i++) {
            operate[i] = res.get(i+22);
            categoryCode.put(res.get(i+22), i+1+22);
        }
        for (int i = 0; i < 15; i++) {
            //更改:字符串和字符转换
            delimiter[i] = res.get(i+50).charAt(0);
            categoryCode.put(res.get(i+50), i+1+50);
        }

        //2.读取测试文件
        res = readFile(CodeFileName);
        for (int i = 0; i < res.size(); i++) {
            code += res.get(i)+'\n';
        }
        len = code.length();
        System.out.println("len = " + len);
    }

    /**
     * 3.词法分析-核心区
     * 调用链 read_next -> judge -> peek,only调用
     */
    private static int read_next() {
        //code:读取文件得到的测试代码
        //1.第一次测试
        int type = judge(code.charAt(pos));
        //2.循环测试直到结束，连续字符读取
        while(pos < len && type == Type._SPACE_.getValue()) {
            ++pos;
            type = judge(code.charAt(pos));
        }//result识别到一个有效的token,包括_ERROR
        //3.
        if(pos >= len) return _EOF_;
        ++pos;
//        System.out.println("pos = " + pos);
        if(type == _ERROR_) return _ERROR_;
        if(type == Type._INT_.getValue()) {
            // cout << "int: " << tempToken << endl;
            tokenList.add(new Token(66, tempToken, cat[Type._INT_.getValue()]));
            return Type._INT_.getValue();
        }
        if(type == Type._DOUBLE_.getValue()) {
            // cout << "double: " << tempToken << endl;
            tokenList.add(new Token(67, tempToken, cat[Type._DOUBLE_.getValue()]));
            return Type._DOUBLE_.getValue();
        }
        if(type == Type._ID_.getValue()) {  // 标识符
            // cout << "id: " << tempToken << endl;
            tokenList.add(new Token(70, tempToken, cat[Type._ID_.getValue()]));
            return Type._ID_.getValue();
        }
        if(type == Type._OPERATOR_.getValue() || type == Type._KEYWORD_.getValue() || type == Type._DELIMITER_.getValue()) {
            tokenList.add(new Token(categoryCode.get(tempToken), tempToken, cat[type]));
            return type;
        }
        return _ERROR_;
    }

    private static int judge(char ch) {
        //ch 当前字符 nextChar 下一个字符
        //pos tempToken
        if(ch == '\n' || ch == ' ') return Type._SPACE_.getValue();
        //1.数字常量
        if(LexUtils.isDigit(ch)) {
            char nextChar = peek();
            //小数分支
            if(ch == '0' && nextChar == '.') { // 0.多少
                //先移动pos,让pos指向'.'
                ++pos;
                if(!LexUtils.isDigit(peek()))   // .后面不是数字
                    return _ERROR_;
                tempToken = "0.";
                //当型循环，结束时pos指向最后一个数字
                while(LexUtils.isDigit(peek())) { //先判断后执行
                    tempToken += peek();
                    ++pos;
                }
                return Type._DOUBLE_.getValue();    // 终止状态8
            }
            //update-逻辑需完善
            else if(ch == '0' && !LexUtils.isDigit(nextChar)) { // 不是数字也不是.，说明是单纯的一个0
                tempToken = "0";
                return Type._INT_.getValue();   // 终止状态5
            }
            //最后判断整数
            else if(ch != '0') {
                // digit1
                //1.小数点前面的数字
                //ch -> string
                tempToken = ch+"";
                //同上
                while(LexUtils.isDigit(peek())) {
                    tempToken += peek();
                    ++pos;
                }
                //2.小数点后面的数字
                nextChar = peek();
                if(nextChar == '.') {
                    tempToken += nextChar;
                    ++pos;
                    nextChar = peek();
                    if(LexUtils.isDigit(nextChar)) {
                        tempToken += peek();
                        ++pos;
                        while(LexUtils.isDigit(peek())) {
                            tempToken += peek();
                            ++pos;
                        }
                        return Type._DOUBLE_.getValue();    // 终止状态8
                    }
                    else return _ERROR_;
                }
                else return Type._INT_.getValue();    // 终止状态6
            }
            else {    // 0+数字的情况进入本分支
                ++pos;
                return _ERROR_;         // ERROR
            }
        }
        //2.标识符或关键字,isLetter have '_'
        if(LexUtils.isLetter(ch)) {
            tempToken = ch + "";
            char nextChar = peek();
            //上面是另一种形式
            while( LexUtils.isLetter(nextChar) || LexUtils.isDigit(nextChar) ) { // 标识符~
                tempToken += nextChar;
                ++pos;
                nextChar = peek();
            }
            return LexUtils.isKeyword(tempToken) ? Type._KEYWORD_.getValue() : Type._ID_.getValue();
        }
        //3.字符串常量
        if(ch == '\"') {
            tokenList.add(new Token(54, "\"", cat[Type._DELIMITER_.getValue()]));
            tempToken = "";
            char nextChar = peek();
            while(nextChar != '\"') {
                tempToken += nextChar;
                ++pos;
                nextChar = peek();
            }
            tokenList.add(new Token(69, tempToken, cat[Type._STRING_.getValue()]));//包含了tempToken为空的情况
            tokenList.add(new Token(54, "\"", cat[Type._DELIMITER_.getValue()]));
            pos += 2;//因为下一个是'\"'已处理
            return Type._STRING_.getValue();
        }
        //4.字符常量
        if(ch == '\'') {
            tempToken = "";
            //因为字符常量只有一个字符，所以没有while循环
            ++pos;
            char nextChar = peek();
            if(nextChar == '\'') {
                tokenList.add(new Token(53, "\'", cat[Type._DELIMITER_.getValue()]));
                //...
                tempToken += code.charAt(pos);
                tokenList.add(new Token(68, tempToken, cat[Type._CHAR_.getValue()]));
                tokenList.add(new Token(53, "\'", cat[Type._DELIMITER_.getValue()]));
                ++pos;
                return Type._CHAR_.getValue();
            }
            else if(code.charAt(pos) == '\'') {//空字符常量
                tokenList.add(new Token(53, "\'", cat[Type._DELIMITER_.getValue()]));
                tokenList.add(new Token(68, tempToken, cat[Type._CHAR_.getValue()]));  // 空字符串
                tokenList.add(new  Token(53, "\'", cat[Type._DELIMITER_.getValue()]));
                return Type._CHAR_.getValue();
            } else {//code.charAt(pos) is not '\''
                while(pos < len && nextChar != '\'') { //一错到底
                    ++pos;
                    nextChar = peek();
                }
                ++pos;
                return _ERROR_;
            }
        }
        //5.多行注释和单行注释
        if(ch == '/') {
            //多行注释进入,忽略/*界符
            if(peek() == '*') {
                ++pos;
                char nextChar = peek();//第一个*
                ++pos;//指向第一个*
                tempToken = "";
                while(pos < len) {
                    if(nextChar == '*' && peek() == '/') { //*/结束否则，其他都当作注释
                        tokenList.add(new Token(55, "/*", cat[Type._DELIMITER_.getValue()]));
                        tokenList.add(new Token(71, tempToken, cat[Type._COMMENT_.getValue()]));
                        tokenList.add(new Token(56, "*/", cat[Type._DELIMITER_.getValue()]));
                        ++pos;
                        ++pos;//将移动到*/结束的开头第一个字符
                        return Type._COMMENT_.getValue();
                    } else {
                        tempToken += nextChar; //第一个*？？？
                        nextChar = peek();
                        ++pos;
                    }
                }
                return _ERROR_;
            }
        }
        //6.运算符
        if(LexUtils.isOP(ch)) {   // op运算符
            tempToken = "";
            tempToken += ch;
            char nextChar = peek();
            if(LexUtils.isOP(nextChar)) {
                if(LexUtils.isOperator(tempToken + nextChar)) {
                    tempToken += nextChar;
                    ++pos;
                    return Type._OPERATOR_.getValue();      // 15
                } else return Type._OPERATOR_.getValue();   // 14
            } else return Type._OPERATOR_.getValue();       // 14
        }
        //7.界符，界符只有一个字符
        if(LexUtils.isDelimiter(ch)) {
            tempToken = "";
            tempToken += ch;
            return Type._DELIMITER_.getValue();
        }
        return _ERROR_;
    }


    /**
     * 预读取code变量的方法(只读)
     * @var pos len
     * @return '\0'
     */
    private static char peek() {
        if(pos+1 < len) return code.charAt(pos+1);
        else return '\0';
    }

}
