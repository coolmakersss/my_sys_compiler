package TreeNodes;

import Lexer.SyntaxKind;
import Parser.ErrorCheckCtx;
import Parser.ErrorCheckRet;
import Parser.Errorkind;
import Tools.Pair;

import java.util.ArrayList;

public class StmtNode extends Node {

    @Override
    public void checkError(ArrayList<Pair<Errorkind, Integer>> errorlist, ErrorCheckCtx ctx, ErrorCheckRet ret) {
        for (Node child : children) {
            if(child.getKind() == SyntaxKind.LVAL){
                ctx.isLVal = true;
            }
            child.checkError(errorlist, ctx, ret);
            ctx.isLVal = false;
            ret.isReturn = child.getKind() == SyntaxKind.RETURN_STMT;
        }
    }
}
