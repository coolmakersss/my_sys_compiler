package TreeNodes;

import Lexer.SyntaxKind;
import Parser.ErrorCheckCtx;
import Parser.ErrorCheckRet;
import Parser.Errorkind;
import Parser.Symbol;
import Tools.Pair;

import java.util.ArrayList;

public class LvalNode extends Node {
    @Override
    public void checkError(ArrayList<Pair<Errorkind, Integer>> errorlist, ErrorCheckCtx ctx, ErrorCheckRet ret) {
        TokenNode child0 = null;
        int cnt = 0;
        for (Node child : children) {
            if(child.getKind() == SyntaxKind.IDENFR) {
                child0 = (TokenNode)child;
                if (Symbol.getSymbol().gerVar(child0.getContent()) == null) {
                    errorlist.add(Pair.of(Errorkind.UNDEFINED_IDENT, child0.getFinishLine()));
                    return;
                } else if (ctx.isLVal && Symbol.getSymbol().gerVar(child0.getContent()).isConst) {
                    errorlist.add(Pair.of(Errorkind.CONSTANT_ASSIGNED, child0.getFinishLine()));
                    return;
                }
                ctx.isLVal = false;
            }
            if(child.getKind() ==SyntaxKind.EXP) cnt++;
            child.checkError(errorlist, ctx, ret);
        }
        if(child0!=null){
            ret.dimension = Symbol.getSymbol().gerVar(child0.getContent()).dimension.size() - cnt;
        }

    }
}
