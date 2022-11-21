package TreeNodes;

import Generation.BuildIRCtx;
import Generation.BuildIRRet;
import Lexer.SyntaxKind;
import Parser.ErrorCheckCtx;
import Parser.ErrorCheckRet;
import Parser.Errorkind;
import Tools.Pair;

import java.util.ArrayList;

public class ExpNode extends Node {
    private String res = "";

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

    @Override
    public void buildIR(BuildIRCtx ctx, BuildIRRet ret) {
        children.get(0).buildIR(ctx, ret);
        res = ret.res;
        ret.res = res;
    }
}
