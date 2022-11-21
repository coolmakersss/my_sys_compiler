package Parser;

import Generation.ControlFlowGraphBuilder;
import Generation.Quaternion.Div;
import Generation.Quaternion.Minu;
import Generation.Quaternion.Mod;
import Generation.Quaternion.Mult;
import Generation.Quaternion.Plus;
import Lexer.SyntaxKind;
import Tools.Pair;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Symbol {
    public HashMap<String, FuncSymbol> funcs;
    public HashMap<String, Stack<LValSymbol>> vars;
    public Stack<HashSet<String>> symbolStack;
    public HashMap<String, LValSymbol> globalVal;
    public HashSet<Pair<String, LValSymbol>> constVal;
    public HashMap<String, String> constStr;
    int cnt;
    static int strcnt;

    private Symbol() {
        cnt = 0;
        strcnt = 0;
        funcs = new HashMap<>();
        vars = new HashMap<>();
        symbolStack = new Stack<>();
        globalVal = new HashMap<>();
        constVal = new HashSet<>();
        constStr = new HashMap<>();
    }
    private final static Symbol symbol = new Symbol();
    public static Symbol getSymbol(){
        return symbol;
    }

    public boolean addFunc(ArrayList<Integer> params,String name, boolean isVoid) {
        if(funcs.containsKey(name)){
            return false;
        } else {
            funcs.put(name, new FuncSymbol(name,params,isVoid));
            return true;
        }
    }

    public int addVar(boolean isConst, ArrayList<Integer> dimension, String name, ArrayList<Integer> initVal) {
        HashSet<String> cur = symbolStack.peek();
        if (cur.contains(name)) {
            return 0;
        }
        cnt++;
        int ret = cnt;
        LValSymbol lValSymbol = new LValSymbol(isConst, symbolStack.size() == 1, ret, dimension, initVal);
        if (symbolStack.size() == 1) {
            globalVal.put(generateIRVar(name,ret),lValSymbol);
        }
        cur.add(name);
        if (!vars.containsKey(name)) {
            vars.put(name, new Stack<>());
        }
        vars.get(name).push(lValSymbol);
        return ret;
    }
    public int addVar(boolean isConst, ArrayList<Integer> dimension, String name) {
        ArrayList<Integer> tmp = new ArrayList<>();
        return addVar(isConst, dimension, name, tmp);
    }

    public LValSymbol gerVar(String name) {
        if (!vars.containsKey(name)) {
            return null;
        }
        return vars.get(name).peek();
    }

    public FuncSymbol getFunc(String name) {
        if (!funcs.containsKey(name)) {
            return null;
        }
        return funcs.get(name);
    }

    public void startBlock() {
        symbolStack.push(new HashSet<>());
    }

    public void endBlock() {
        HashSet<String> cur = symbolStack.peek();
        for(String i:cur) {
            if(vars.get(i).peek().isConst()){
                int id = vars.get(i).peek().getId();
                constVal.add(Pair.of(generateIRVar(i,id),vars.get(i).peek()));
            }
            vars.get(i).pop();
            if(vars.get(i).isEmpty()){
                vars.remove(i);
            }
        }
        symbolStack.pop();
    }

    public void clear() {
        cnt = 0;
        funcs = new HashMap<>();
        vars = new HashMap<>();
        symbolStack = new Stack<>();
        globalVal = new HashMap<>();
        constVal = new HashSet<>();
        constStr = new HashMap<>();
    }

    public int getLayer() {
        return symbolStack.size();
    }


    public static String generateStr() {
        strcnt++;
        return "str_" + strcnt;
    }

    public static String generateIRVar(String name, int id) {System.out.println("var_" + name + "_" + id);
        return "var_" + name + "_" + id;
    }

    public static String generateIRFunc(String name) {
        return "func_" + name;
    }

    public static boolean exprResIsNumber(String res) {
        Pattern pattern = Pattern.compile("[+-]*[0-9]+");
        Matcher matcher = pattern.matcher(res);
        return matcher.matches();
    }
    public static int calculate(int a, int b, SyntaxKind op) {
        switch (op) {
            case PLUS:
                return a+b;
            case MINU:
                return a-b;
            case MULT:
                return a*b;
            case DIV:
                return a/b;
            case MOD:
                return a%b;
            default:
                System.out.println("calaulate op error!");
                return 0;
        }
    }
    public static void getRes(String assigned, String a, String b, SyntaxKind op) {
        switch (op) {
            case PLUS:
                ControlFlowGraphBuilder.getCFGB().insert(new Plus(assigned,a,b));
                return;
            case MINU:
                ControlFlowGraphBuilder.getCFGB().insert(new Minu(assigned,a,b));
                return;
            case MULT:
                ControlFlowGraphBuilder.getCFGB().insert(new Mult(assigned,a,b));
                return;
            case DIV:
                ControlFlowGraphBuilder.getCFGB().insert(new Div(assigned,a,b));
                return;
            case MOD:
                ControlFlowGraphBuilder.getCFGB().insert(new Mod(assigned,a,b));
                return;
            default:
                System.out.println("calaulate op error!");
                return;
        }
    }


    public void printGlobalVar(OutputStreamWriter writer) throws IOException {
        if(IR){
            for (String i : globalVal.keySet()) {
                writer.append(i).append(" ");
                globalVal.get(i).print(writer);
                writer.append("\n");
            }
            for (Pair<String, LValSymbol> i : constVal) {
                writer.append(i.first).append(" ");
                i.second.print(writer);
                writer.append("\n");
            }
            for (String i : constStr.keySet()) {
                writer.append(i).append(" \"").append(constStr.get(i)).append("\"").append("\n");
            }
        } else {
            for (String i : globalVal.keySet()) {
                writer.append(i).append(" ");
                globalVal.get(i).print(writer);
                writer.append("\n");
            }
            for (Pair<String, LValSymbol> i : constVal) {
                writer.append(i.first).append(" ");
                i.second.print(writer);
                writer.append("\n");
            }
            for (String i : constStr.keySet()) {
                writer.append(i).append(" : .asciiz \"").append(constStr.get(i)).append("\"").append("\n");
            }
        }
    }

    public static final boolean MyOPT = false,IR = false;

    public void addConstStr(String label, String str) {
        constStr.put(label,str);
    }

    public void addFunc(String name) {
        ArrayList<Integer> tmp = new ArrayList<>();
        addFunc(tmp,name,false);
    }

}
