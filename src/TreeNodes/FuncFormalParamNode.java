package TreeNodes;

import Generation.BuildIRCtx;
import Generation.BuildIRRet;
import Lexer.SyntaxKind;
import Parser.ErrorCheckCtx;
import Parser.ErrorCheckRet;
import Parser.Errorkind;
import Parser.Symbol;
import Tools.Pair;

import java.util.ArrayList;

public class FuncFormalParamNode extends Node {
    @Override
    public void checkError(ArrayList<Pair<Errorkind, Integer>> errorlist, ErrorCheckCtx ctx, ErrorCheckRet ret) {
        String name = "";
        int line = -1;
        ArrayList<Integer> dimension = new ArrayList<>();
        boolean flag = false;
        for (Node child : children) {
            if (child.getKind() == SyntaxKind.IDENFR) {
                name = ((TokenNode)child).getContent();
                line = child.getFinishLine();
            }
            ErrorCheckRet ret1 = new ErrorCheckRet();
            child.checkError(errorlist, ctx, ret1);
            if (flag) {
                flag = false;
                dimension.add(ret1.val);
            }
            if (child.getKind() == SyntaxKind.LBRACK) {
                flag = true;
            }
        }
        ret.dimension = dimension.size();
        if (Symbol.getSymbol().addVar(false, dimension, name) == 0) {
            errorlist.add(Pair.of(Errorkind.REDEFINE_IDENT, line));
        }

    }

    @Override
    public void buildIR(BuildIRCtx ctx, BuildIRRet ret) {
        String name = "";
        ArrayList<Integer> dimension = new ArrayList<>();
        int val = 0;
        ret.clear();
        for(Node child:children) {
            if(child.getKind() == SyntaxKind.IDENFR){
                name = ((TokenNode)child).getContent();
            }
            child.buildIR(ctx, ret);
            if(child.getKind() == SyntaxKind.RBRACK){
                dimension.add(val);
            } else if(child.getKind() == SyntaxKind.CONST_EXP) {
                if(Symbol.exprResIsNumber(ret.res)){
                    val = Integer.parseInt(ret.res);
                } else {
                    val = 0;
                }
            }
        }
        int id = Symbol.getSymbol().addVar(false,dimension,name);
        ret.param = Symbol.generateIRVar(name, id);
    }
}
