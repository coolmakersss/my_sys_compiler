package TreeNodes;

import Lexer.SyntaxKind;
import Parser.ErrorCheckCtx;
import Parser.ErrorCheckRet;
import Parser.Errorkind;
import Tools.Pair;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class ReturnStmtNode extends Node {

    @Override
    public void print(OutputStreamWriter writer) throws IOException {
        for (Node child : children) {
            child.print(writer);
        }
    }

    @Override
    public void checkError(ArrayList<Pair<Errorkind, Integer>> errorlist, ErrorCheckCtx ctx, ErrorCheckRet ret) {
        int line = -1;
        for (Node child : children) {
            child.checkError(errorlist, ctx, ret);
            if(child.getKind() == SyntaxKind.RETURNTK){
                line = child.getStartLine();
            } else if(child.getKind() == SyntaxKind.EXP && ctx.inVoidFunc) {
                errorlist.add(Pair.of(Errorkind.VOID_FUNC_RETURN_INTEGER,line));
            }
        }
    }
}
