package TreeNodes;

import Lexer.SyntaxKind;
import Parser.ErrorCheckCtx;
import Parser.ErrorCheckRet;
import Parser.Errorkind;
import Parser.FuncSymbol;
import Parser.Symbol;
import Tools.Pair;

import java.util.ArrayList;

public class UnaryExpNode extends Node {

    @Override
    public void checkError(ArrayList<Pair<Errorkind, Integer>> errorlist, ErrorCheckCtx ctx, ErrorCheckRet ret) {
        if(children.get(0).getKind() == SyntaxKind.IDENFR && children.get(1).getKind() == SyntaxKind.LPARENT){
            FuncSymbol funcSymbol = null;
            boolean flag = false;
            int line = -1;
            for (Node child : children) {
                if (child.getKind() == SyntaxKind.IDENFR) {
                        funcSymbol = Symbol.getSymbol().getFunc(((TokenNode)child).getContent());
                    if (funcSymbol == null) {
                        errorlist.add(Pair.of(Errorkind.UNDEFINED_IDENT, child.getFinishLine()));
                    } else if (funcSymbol.isVoid()) {
                        ret.dimension = -1;
                    }
                }
                ErrorCheckRet ret1 = new ErrorCheckRet();
                child.checkError(errorlist, ctx, ret1);
                if (child.getKind() == SyntaxKind.IDENFR) {
                    line = child.getFinishLine();
                } else if (funcSymbol != null &&
                        (child.getKind() == SyntaxKind.FUNC_R_PARAMS || (!flag && child.getKind() == SyntaxKind.RPARENT))) {
                    flag = true;
                    Errorkind kind = funcSymbol.matchParams(ret1.args);
                    //System.out.println(ret1.args);
                    if (kind == Errorkind.PARAMS_NUM_UNMATCHED) {
                        errorlist.add(Pair.of(Errorkind.PARAMS_NUM_UNMATCHED, line));
                    } else if (kind == Errorkind.PARAM_TYPE_UNMATCHED) {
                        errorlist.add(Pair.of(Errorkind.PARAM_TYPE_UNMATCHED, line));
                    }
                }
            }
        } else {
            for(Node child: children){
                ErrorCheckRet ret1 = new ErrorCheckRet();
                child.checkError(errorlist, ctx, ret1);
                if(ret1.isConst){
                    ret.isConst = true;
                }
                ret.dimension = ret1.dimension;
                ret.val = ret1.val;
            }
        }
    }
}
