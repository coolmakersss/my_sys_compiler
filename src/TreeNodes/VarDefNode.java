package TreeNodes;

import Generation.BuildIRCtx;
import Generation.BuildIRRet;
import Generation.ControlFlowGraphBuilder;
import Generation.Quaternion.Assign;
import Generation.Quaternion.CreatePointer;
import Generation.Quaternion.StoreWord;
import Lexer.SyntaxKind;
import Parser.ErrorCheckCtx;
import Parser.ErrorCheckRet;
import Parser.Errorkind;
import Parser.Symbol;
import Tools.Pair;

import java.util.ArrayList;


public class VarDefNode extends Node {

    @Override // i
    public void checkError(ArrayList<Pair<Errorkind, Integer>> errorlist, ErrorCheckCtx ctx, ErrorCheckRet ret) {
        String name = "";
        int line = -1;
        ArrayList<Integer> limit = new ArrayList<>();
        for (Node child : children) {
            if(child.getKind() == SyntaxKind.IDENFR) {
                name = ((TokenNode)child).getContent();
                line = child.getFinishLine();
            }
            ErrorCheckRet ret1 = new ErrorCheckRet();
            child.checkError(errorlist, ctx, ret1);
            if(child.getKind() == SyntaxKind.CONST_EXP){
                limit.add(ret1.val);
            }
        }
        if(Symbol.getSymbol().addVar(ctx.isConst, limit, name) == 0) {
            errorlist.add(Pair.of(Errorkind.REDEFINE_IDENT, line));
        }
    }

    @Override
    public void buildIR(BuildIRCtx ctx, BuildIRRet ret) {
        boolean isConst = false;
        String name = "";
        ArrayList<Integer> dimension = new ArrayList<>();
        ArrayList<String> init = new ArrayList<>();
        ArrayList<Integer> initVal = new ArrayList<>();
        boolean flag = Symbol.getSymbol().getLayer() == 1;
        ret.clear();
        for (Node child : children) {
            if (child.getKind() == SyntaxKind.IDENFR) {
                name = ((TokenNode) child).getContent();
            }
            child.buildIR(ctx, ret);
            if (child.getKind() == SyntaxKind.CONST_EXP) {
                dimension.add(Integer.parseInt(ret.res));
            } else if (child.getKind() == SyntaxKind.INIT_VAL) {
                if(flag){
                    for (String j : ret.init) {
                        initVal.add(Integer.parseInt(j));
                    }
                } else {
                    init = ret.init;
                }
            }
        }
        int id = Symbol.getSymbol().addVar(isConst,dimension,name,initVal);
        if(flag) return;
        if(!dimension.isEmpty()){
            String pointer = Symbol.generateIRVar(name, id);
            int size = 1;
            for (int j:dimension){
                size*=j;
            }
            ControlFlowGraphBuilder.getCFGB().insert(new CreatePointer(pointer, size));
            if(!init.isEmpty()){
                for (int i = 0; i < size; i++) {
                    String offset = String.valueOf(i);
                    ControlFlowGraphBuilder.getCFGB().insert(new StoreWord(init.get(i), pointer, offset));
                }
            }

        } else {
            String var = Symbol.generateIRVar(name,id);
            if(!init.isEmpty()){
                ControlFlowGraphBuilder.getCFGB().insert(new Assign(var,init.get(0)));
            } else {
                String tmp = "20373560";
                ControlFlowGraphBuilder.getCFGB().insert(new Assign(var, tmp));
            }
        }
    }

}
