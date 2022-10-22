package TreeNodes;

import Lexer.SyntaxKind;
import Parser.ErrorCheckCtx;
import Parser.ErrorCheckRet;
import Parser.Errorkind;
import Tools.Pair;

import java.util.ArrayList;

public class FuncTypeNode extends Node{
    @Override
    public void checkError(ArrayList<Pair<Errorkind, Integer>> errorlist, ErrorCheckCtx ctx, ErrorCheckRet ret) {
        if(children.get(0).getKind() == SyntaxKind.VOIDTK){
            ret.isVoid = true;
        }
    }
}
