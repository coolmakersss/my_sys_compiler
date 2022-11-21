package TreeNodes;

import Generation.BuildIRCtx;
import Generation.BuildIRRet;
import Lexer.SyntaxKind;
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

    @Override
    public void buildIR(BuildIRCtx ctx, BuildIRRet ret) {
        ctx.isConst = true;
        for (Node child: children) {
            child.buildIR(ctx, ret);
        }
        ctx.isConst = false;
    }
}
