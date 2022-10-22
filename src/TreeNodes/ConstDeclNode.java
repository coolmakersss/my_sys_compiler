package TreeNodes;

import Parser.ErrorCheckCtx;
import Parser.ErrorCheckRet;
import Parser.Errorkind;
import Tools.Pair;

import java.util.ArrayList;

public class ConstDeclNode extends Node {

    @Override
    public void checkError(ArrayList<Pair<Errorkind, Integer>> errorlist, ErrorCheckCtx ctx, ErrorCheckRet ret) {
        ctx.isConst = true;
        for (Node child : children) {
            child.checkError(errorlist, ctx, ret);
        }
        ctx.isConst = false;
    }
}
