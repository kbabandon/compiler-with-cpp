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
    //main
    private static int pos = Vars.pos;
    private static int len = Vars.len;
    private static Vector<Token> tokenList = Vars.tokenList;
    //init
    private static String[]  keywords = Vars.keywords;
    private static String[]  operate = Vars.operate;
    private static char[]  delimiter = Vars.delimiter;
    private static Map<String, Integer> categoryCode = Vars.categoryCode;  // 种别码表
    //read_next
    private static String code = Vars.code;
    private static String tempToken = Vars.tempToken;
    public static String cat[] = Vars.cat;
    //
    //const
    //const - main
    private static final int _EOF_ = Finals._EOF_;
    private static final int _ERROR_ = Finals._ERROR_;
    //const - init
    private static final String CategoryFileName = Finals.CategoryFileName;
    private static final String CodeFileName = Finals.CodeFileName;

    public static void main(String[] args) {
        init();
        while (pos < len){
            int flag = read_next();
            if(flag == _EOF_){
                break;
            }
            else if(flag == _ERROR_){
                tokenList.add(new Token(_ERROR_, "ERROR!", "ERROR"));
            }
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
            //字符串和字符转换
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


    private static int read_next() {
        //code读取文件得到的测试代码
        //第一次测试
        int type = judge(code.charAt(pos));
        //循环测试直到结束，连续字符读取
        while(pos < len && type == Type._SPACE_.getValue()) {
            ++pos;
            type = judge(code.charAt(pos));
        }
        if(pos >= len) return _EOF_;
        ++pos;
//    cout << "pos: " <<  pos << endl;
//    lineCount++;
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
        if(ch == '\n' || ch == ' ') return Type._SPACE_.getValue();
        if(LexUtils.isDigit(ch)) {
            char nextChar = peek();
            if(ch == '0' && nextChar == '.') { // 0.多少
                ++pos;
                if(!LexUtils.isDigit(peek()))   // .后面不是数字
                    return _ERROR_;
                tempToken = "0.";
                while(LexUtils.isDigit(peek())) {
                    tempToken += peek();
                    ++pos;
                }
                return Type._DOUBLE_.getValue();    // 8
            } else if(ch == '0' && !LexUtils.isDigit(nextChar)) { // 不是数字也不是.，说明是单纯的一个0
                tempToken = "0";
                return Type._INT_.getValue();   // 5
            } else if(ch != '0') {  // digit1
                //ch -> string
                tempToken = ch+"";
                while(LexUtils.isDigit(peek())) {
                    tempToken += peek();
                    ++pos;
                }
                //??
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
                        return Type._DOUBLE_.getValue();    // 8
                    } else return _ERROR_;
                } else return Type._INT_.getValue();    // 6
            } else {    // 0+数字
                ++pos;
                return _ERROR_;         // ERROR
            }
        }
        if(LexUtils.isLetter(ch)) {
            tempToken = ch + "";
            char nextChar = peek();
            while( LexUtils.isLetter(nextChar) || LexUtils.isDigit(nextChar) ) { // 标识符~
                tempToken += nextChar;
                ++pos;
                nextChar = peek();
            }
            return LexUtils.isKeyword(tempToken) ? Type._KEYWORD_.getValue() : Type._ID_.getValue();
        }
        if(ch == '\"') {
            tokenList.add(new Token(54, "\"", cat[Type._DELIMITER_.getValue()]));
            tempToken = "";
            char nextChar = peek();
            while(nextChar != '\"') {
                tempToken += nextChar;
                ++pos;
                nextChar = peek();
            }
            tokenList.add(new Token(69, tempToken, cat[Type._STRING_.getValue()]));
            tokenList.add(new Token(54, "\"", cat[Type._DELIMITER_.getValue()]));
            pos += 2;
            return Type._STRING_.getValue();
        }
        if(ch == '\'') {
            tempToken = "";
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
            } else if(code.charAt(pos) == '\'') {
                tokenList.add(new Token(53, "\'", cat[Type._DELIMITER_.getValue()]));
                tokenList.add(new Token(68, tempToken, cat[Type._CHAR_.getValue()]));  // 空字符串
                tokenList.add(new  Token(53, "\'", cat[Type._DELIMITER_.getValue()]));
                return Type._CHAR_.getValue();
            } else {
                while(pos < len && nextChar != '\'') {
                    ++pos;
                    nextChar = peek();
                }
                ++pos;
                return _ERROR_;
            }
        }
        if(ch == '/') {
            if(peek() == '*') {
                ++pos;
                char nextChar = peek();
                ++pos;
                tempToken = "";
                while(pos < len) {
                    if(nextChar == '*' && peek() == '/') {
                        tokenList.add(new Token(55, "/*", cat[Type._DELIMITER_.getValue()]));
                        tokenList.add(new Token(71, tempToken, cat[Type._COMMENT_.getValue()]));
                        tokenList.add(new Token(56, "*/", cat[Type._DELIMITER_.getValue()]));
                        ++pos;
                        ++pos;
                        return Type._COMMENT_.getValue();
                    } else {
                        tempToken += nextChar;
                        nextChar = peek();
                        ++pos;
                    }
                }
                return _ERROR_;
            }
        }

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
        if(LexUtils.isDelimiter(ch)) {
            tempToken = "";
            tempToken += ch;
            return Type._DELIMITER_.getValue();
        }
        return _ERROR_;
    }

    private static char peek() {
        return 0;
    }

}
