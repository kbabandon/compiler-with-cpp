package utils;

import vars.Finals;
import vars.Vars;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;
import java.util.Vector;

/**
 * @author jxf
 * @create 2024-05-29 15:28
 */
public class GrammarUtils {
    //常量
    private static final String v1 = Finals.v1;
    private static final String v2 = Finals.v2;
    //变量
    private static int top = Vars.top;
    private static int ridx = Vars.ridx;
    private static int len = Vars.len2;

    private static char[] analyeStack = Vars.analyeStack;
    private static char[] restStack = Vars.restStack;
    /*输出分析栈和剩余栈*/
    public static void print(){
        for (int i = 0; i <= top + 1; i++) { /*输出分析栈*/
            System.out.print(analyeStack[i]);
        }
        System.out.print("\t\t");
        for (int i = 0; i < ridx; i++) { /*输出对齐符*/
            System.out.print(" ");
        }
        for (int i = ridx; i <= len; i++) { /*输出剩余串*/
            System.out.print(restStack[i]);
        }
        System.out.print("\t\t\t");
    }

    public static Vector<String> readFile(String fileName){
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
        } catch (Exception e) {
            e.printStackTrace();
            return res;
        }
    }

    public static boolean isTerminator(char c){ //判断是否是终结符
        for(int i = 0; i < v1.length(); ++i)
            if (c == v1.charAt(i))
                return true;
        return false;
    }


}
