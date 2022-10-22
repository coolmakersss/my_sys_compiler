package TreeNodes;

import Parser.ErrorCheckCtx;
import Parser.ErrorCheckRet;
import Parser.Errorkind;
import Tools.Pair;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class BreakStmtNode extends Node {

    @Override
    public void print(OutputStreamWriter writer) throws IOException {
        for (Node child : children) {
            child.print(writer);
        }
    }

    @Override
    public void checkError(ArrayList<Pair<Errorkind, Integer>> errorlist, ErrorCheckCtx ctx, ErrorCheckRet ret) {
        for (Node i : children) {
            i.checkError(errorlist, ctx, ret);
        }
        if (ctx.loopNum == 0) {
            errorlist.add(Pair.of(Errorkind.BREAK_CONTINUE_OUT_LOOP, children.get(0).getStartLine()));
        }
    }
}
