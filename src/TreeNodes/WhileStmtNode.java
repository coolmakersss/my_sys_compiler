package TreeNodes;

import Parser.ErrorCheckCtx;
import Parser.ErrorCheckRet;
import Parser.Errorkind;
import Tools.Pair;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class WhileStmtNode extends Node {

    @Override
    public void print(OutputStreamWriter writer) throws IOException {
        for (Node child : children) {
            child.print(writer);
        }
    }

    @Override
    public void checkError(ArrayList<Pair<Errorkind, Integer>> errorlist, ErrorCheckCtx ctx, ErrorCheckRet ret) {
        ctx.loopNum++;
        for (Node child : children) {
            child.checkError(errorlist, ctx, ret);
        }
        ctx.loopNum--;
    }
}
