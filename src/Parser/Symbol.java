package Parser;

import Tools.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;

public class Symbol {
    public HashMap<String, FuncSymbol> funcs;
    public HashMap<String, Stack<LValSymbol>> vars;
    public Stack<HashSet<String>> symbolStack;
    public HashMap<String, LValSymbol> globalVal;
    public HashSet<Pair<String, LValSymbol>> constVal;
    public HashMap<String, String> constStr;
    int cnt;

    private Symbol() {
        cnt = 0;
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
            globalVal.put("_"+name+ret,lValSymbol);
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
            vars.get(i).pop();
            if(vars.get(i).isEmpty()){
                vars.remove(i);
            }
        }
        symbolStack.pop();
    }


}
