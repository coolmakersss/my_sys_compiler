package TreeNodes;

import Lexer.SyntaxKind;
import Parser.ErrorCheckCtx;
import Parser.ErrorCheckRet;
import Parser.Errorkind;
import Parser.Symbol;
import Tools.Pair;

import java.util.ArrayList;

public class BlockNode extends Node {

    @Override
    public void checkError(ArrayList<Pair<Errorkind, Integer>> errorlist, ErrorCheckCtx ctx, ErrorCheckRet ret) {
        if (ctx.afterFuncDef) {
            ctx.afterFuncDef = false;
        } else {
            Symbol.getSymbol().startBlock();
        }
        boolean flag = false;
        for (Node child : children) {
            if (child.getKind() == SyntaxKind.STMT) {
                flag = true;
            }
            child.checkError(errorlist, ctx, ret);
        }
        if (!flag) {
            ret.isReturn = false;
        }
        Symbol.getSymbol().endBlock();
    }
}
