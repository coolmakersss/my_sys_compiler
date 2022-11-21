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

    @Override
    public void buildIR(BuildIRCtx ctx, BuildIRRet ret) {
        if (ctx.afterFuncDef) {
            ctx.afterFuncDef = false;
        } else {
            Symbol.getSymbol().startBlock();
        }
        for (Node child:children){
            child.buildIR(ctx, ret);
        }
        Symbol.getSymbol().endBlock();
    }
}
