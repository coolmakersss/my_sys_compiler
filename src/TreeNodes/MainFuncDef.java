package TreeNodes;

import Lexer.SyntaxKind;
import Parser.ErrorCheckCtx;
import Parser.ErrorCheckRet;
import Parser.Errorkind;
import Parser.Symbol;
import Tools.Pair;

import java.util.ArrayList;

public class MainFuncDef extends Node {

    @Override
    public void checkError(ArrayList<Pair<Errorkind, Integer>> errorlist, ErrorCheckCtx ctx, ErrorCheckRet ret) {
        String name = "";
        int line = -1;
        boolean inVoid = false;
        ArrayList<Integer> params = new ArrayList<>();
        for(Node child: children) {
            if (child.getKind() == SyntaxKind.IDENFR) {
                name = ((TokenNode) child).getContent();
                line = child.getFinishLine();
            } else if (child.getKind() == SyntaxKind.LPARENT) {
                Symbol.getSymbol().startBlock();
            } else if (child.getKind() == SyntaxKind.BLOCK) {
                if (!Symbol.getSymbol().addFunc(params, name, inVoid)) {
                    errorlist.add(Pair.of(Errorkind.REDEFINE_IDENT, line));
                }
                ctx.inVoidFunc = inVoid;
                ctx.afterFuncDef = true;
            }
            ErrorCheckRet ret1 = new ErrorCheckRet();
            child.checkError(errorlist, ctx, ret1);
            if (child.getKind() == SyntaxKind.FUNC_TYPE) {
                inVoid = ret1.isVoid;
            } else if (child.getKind() == SyntaxKind.FUNC_F_PARAMS) {
                params = ret1.args;
            } else if (child.getKind() == SyntaxKind.BLOCK) {
                ret.isReturn = ret1.isReturn;
            }
        }
        if(!ret.isReturn && !inVoid) {
            errorlist.add(Pair.of(Errorkind.INT_FUNC_RETURN_LACKED,finishLine));
        }
        ctx.inVoidFunc = false;
    }
}
