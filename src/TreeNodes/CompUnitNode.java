package TreeNodes;

import Parser.*;
import Parser.ErrorCheckRet;
import Parser.Errorkind;
import Tools.Pair;

import java.util.ArrayList;


public class CompUnitNode extends Node{

    @Override
    public void checkError(ArrayList<Pair<Errorkind, Integer>> errorlist, ErrorCheckCtx ctx, ErrorCheckRet ret) {
        Symbol.getSymbol().startBlock();
        for (Node child : children) {
            child.checkError(errorlist, ctx, ret);
        }
        Symbol.getSymbol().endBlock();
    }
}
