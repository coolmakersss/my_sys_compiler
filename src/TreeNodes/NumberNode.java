package TreeNodes;

import Generation.BuildIRCtx;
import Generation.BuildIRRet;
import Lexer.SyntaxKind;
import Parser.ErrorCheckCtx;
import Parser.ErrorCheckRet;
import Parser.Errorkind;
import Tools.Pair;

import java.util.ArrayList;

public class NumberNode extends Node {


    @Override
    public void checkError(ArrayList<Pair<Errorkind, Integer>> errorlist, ErrorCheckCtx ctx, ErrorCheckRet ret) {
            String content;
            if (children.get(0).getKind() == SyntaxKind.INTCON) {
                content = ((TokenNode)children.get(0)).getContent();
                ret.val = Integer.parseInt(content);
                ret.isConst = true;
            }
    }

    @Override
    public void buildIR(BuildIRCtx ctx, BuildIRRet ret) {
        ret.res = ((TokenNode) children.get(0)).getContent();
    }
}
