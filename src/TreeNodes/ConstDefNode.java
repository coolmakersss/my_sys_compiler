package TreeNodes;

import Lexer.SyntaxKind;
import Parser.ErrorCheckCtx;
import Parser.ErrorCheckRet;
import Parser.Errorkind;
import Parser.Symbol;
import Tools.Pair;

import java.util.ArrayList;

public class ConstDefNode extends Node {

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
}
