package TreeNodes;

import Lexer.SyntaxKind;
import Parser.ErrorCheckCtx;
import Parser.ErrorCheckRet;
import Parser.Errorkind;
import Tools.Pair;

import java.util.ArrayList;

public class ExpNode extends Node {

    @Override
    public void checkError(ArrayList<Pair<Errorkind, Integer>> errorlist, ErrorCheckCtx ctx, ErrorCheckRet ret) {
        for(Node child: children){
            ErrorCheckRet ret1 = new ErrorCheckRet();
            child.checkError(errorlist, ctx, ret1);
            if(ret1.isConst){
                ret.isConst = true;
            }
            ret.dimension = ret1.dimension;
        }
    }
}
