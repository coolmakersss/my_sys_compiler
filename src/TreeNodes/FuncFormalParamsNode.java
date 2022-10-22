package TreeNodes;

import Lexer.SyntaxKind;
import Parser.ErrorCheckCtx;
import Parser.ErrorCheckRet;
import Parser.Errorkind;
import Tools.Pair;

import java.util.ArrayList;

public class FuncFormalParamsNode extends Node {
    @Override
    public void checkError(ArrayList<Pair<Errorkind, Integer>> errorlist, ErrorCheckCtx ctx, ErrorCheckRet ret) {
        for (Node child : children) {
            ErrorCheckRet ret1 = new ErrorCheckRet();
            child.checkError(errorlist, ctx, ret1);
            if(child.getKind() == SyntaxKind.FUNC_F_PARAM){
                ret.args.add(ret1.dimension);
            }
        }
    }
}
