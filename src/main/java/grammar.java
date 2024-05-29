import entity.Production;
import utils.GrammarUtils;
import vars.Finals;
import vars.Vars;

import java.util.Vector;

/**
 * @author jxf
 * @create 2024-05-26 17:20
 */
public class grammar {

    //常量
    private static final String v1 = Finals.v1;
    private static final String v2 = Finals.v2;
    private static final String ExpFileName = Finals.ExpFileName;

    private static int top = Vars.top;
    private static int ridx = Vars.ridx;
    private static int len = Vars.len2;
    //双栈
    private static char[] analyeStack = Vars.analyeStack;
    private static char[] restStack = Vars.restStack;


    public static Production e, t, g, g1, s, s1, f, f1, o; /* 产生式结构体变量 */
    private static Production[][] C = Vars.C; /*预测分析表*/

    //初始化3个简单变量和resetStack集合
    private static void init(String exp){
        top = 0;
        ridx = 0;
        len = exp.length(); /*分析串长度*/
//        System.out.println("分析串长度:" + len);
        //压栈
        for (int i = 0; i < len; i++) {
            restStack[i] = exp.charAt(i);
            System.out.print(restStack[i]);
        }
//        System.out.println("---end");
    }

    //分析一个文法
    //调用init
    private static void analyze(String exp){
        //top栈指针, k临时变量
        //1.初始化
        init(exp);
        int k = 0;
        //analyeStack初始化
        analyeStack[top] = '#';
        analyeStack[++top] = 'E';/*'#','E'进栈*/
        System.out.println(" 步骤\t\t分析栈 \t\t剩余字符 \t\t所用产生式 ");

        //2.死循环
        while (true){
            char ch = restStack[ridx];
            char x = analyeStack[top--]; /*x为当前栈顶字符*/
            System.out.print(++k+"\t\t"); //输出的序号
            //1
            if(x == '#') {
                System.out.println("分析成功!AC!\n"); /* 接受 */
                return;
            }
            //2 根据x分支
            if (GrammarUtils.isTerminator(x)){
                if(x == ch){ //匹配上了
//                    GrammarUtils.print();
                    print();
                    System.out.println(ch + "匹配");
                    ch = restStack[++ridx]; /*下一个输入字符*/
                }
                /*出错处理*/
                else{
//                    GrammarUtils.print();
                    print();
                    System.out.println("分析出错,错误终结符为"+ch); /*输出出错终结符*/
                    return;
                }
            }
            //3 非终结符处理
            else{
                //first
                int m, n; //非终结符下标,终结符下标
                m = v2.indexOf(x); // m为-1则说明找不到该非终结符，出错
                n = v1.indexOf(ch); // n为-1则说明找不到该终结符，出错
                /*出错处理*/
                if(m == -1 || n == -1){
//                    GrammarUtils.print();
                    print();
                    System.out.println("分析出错,错误非终结符为" + x);
                    return;
                }
                //second
                //第m行第n列的产生式
                Production nowProduction = C[m][n]; /* 用来接受C[m][n] */
                if (nowProduction.getOrigin() != 'N'){
//                    GrammarUtils.print();
                    print();
                    System.out.println(nowProduction.getOrigin()+"->"+nowProduction.getArray()); /*输出产生式*/
                    for (int j = (nowProduction.getLength()-1); j >= 0; j--) {
                        analyeStack[++top] = nowProduction.getArray().charAt(j); /*产生式逆序入栈*/
                    }
                    if (analyeStack[top] == '^') {/*为空则不进栈*/
                        top--;
                    }
                }
                //出错处理
                else {
//                    GrammarUtils.print();
                    print();
                    System.out.println("分析出错,错误非终结符为" + x);
                    return;
                }
            }
        }
    }

    private static void initC(Production[][] productions){
        for (int i = 0; i < productions.length; i++) {
            for (int j = 0; j < productions[i].length; j++) {
//                productions[i][j] = o;
                productions[i][j] = new Production(o);
            }
        }
    }

    private static void printC(Production[][] productions){
        for (int i = 0; i < productions.length; i++) {
            for (int j = 0; j < productions[i].length; j++) {
//                productions[i][j] = o;
                System.out.println(productions[i][j]);
            }
        }
    }

    //调用analyze
    public static void main(String[] args) {
        //1.init
        /* 结构体变量 */
        e = new Production('E', "TG");
        t = new Production('T', "FS");
        g = new Production('G', "+TG");
        g1 = new Production('G', "^");
        s = new Production('S', "*FS");
        s1 = new Production('S', "^");
        f = new Production('F', "(E)");
        f1 = new Production('F', "i");
        o = new Production('N',"");

        /*
         * 填充分析表
         * C[][]存储分析表
         */
//        initC(C);
        C[0][0] = C[0][3] = e;
        C[1][1] = g;
        C[1][4] = C[1][5] = g1;
        C[2][0] = C[2][3] = t;
        C[3][2] = s;
        C[3][1] = C[3][4] = C[3][5] = s1;
        C[4][0] = f1; C[4][3] = f;
        //2.提示信息输出
        /*System.out.println("LL（1）分析程序分析程序，编制人：xxx xxx 计科xxxx班");
        System.out.println("提示:本程序只能对由'i','+','*','(',')'构成的以'#'结束的字符串进行分析，每行一个读入的字符串");
        System.out.println("读取的文件名为：" + ExpFileName);
        Vector<String> exps = GrammarUtils.readFile(ExpFileName);
        for(String exp:exps){
            System.out.println(exp);
        }
        int len = exps.size();
        System.out.println("表达式个数:"+len);*/
//        printC(C);
        Vector<String> exps = GrammarUtils.readFile(ExpFileName);
        for(String exp:exps){
            System.out.println(exp);
        }
        int len = exps.size();
        System.out.println("表达式个数:"+len);
        //3.关键信息输出
        for(int i = 0; i < len; i++) {
            String exp = exps.get(i);//Vector
            System.out.println("------------------待分析字符串"+(i+1)+"："+exp+"--------------------");

            boolean flag = true;
            for(int j = 0; j < exp.length(); j++) {
                if(!GrammarUtils.isTerminator(exp.charAt(j))) {
                    System.out.println("第"+(i+1)+"行输入的字符串不合法，请重新输入");
                    flag = false;
                    break;
                }
            }
            if(flag) {
                System.out.println("字符串"+(i+1)+"："+exp);
                analyze(exp);
            }
        }
        return ;
    }

    /*输出分析栈和剩余栈*/
    private static void print(){
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

}
