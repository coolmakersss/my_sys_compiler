package TreeNodes;

import Generation.BuildIRCtx;
import Generation.BuildIRRet;
import Generation.ControlFlowGraphBuilder;
import Generation.Quaternion.Jump;
import Parser.ErrorCheckCtx;
import Parser.ErrorCheckRet;
import Parser.Errorkind;
import Tools.Pair;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class ContinueStmtNode extends Node {

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

    @Override
    public void buildIR(BuildIRCtx ctx, BuildIRRet ret) {
        ControlFlowGraphBuilder.getCFGB().insert(new Jump(ctx.continueBlock));
    }
}
