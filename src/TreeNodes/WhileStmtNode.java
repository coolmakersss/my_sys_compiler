package TreeNodes;

import Generation.BasicBlock;
import Generation.BuildIRCtx;
import Generation.BuildIRRet;
import Generation.ControlFlowGraphBuilder;
import Generation.Quaternion.JumpIfFalse;
import Generation.Quaternion.JumpIfTrue;
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

    @Override
    public void buildIR(BuildIRCtx ctx, BuildIRRet ret) {
        BasicBlock cfg2 = ctx.breakBlock;
        boolean inLoop = ctx.stmtAfterLoop;

        children.get(2).buildIR(ctx, ret);
        BasicBlock finalBB = ControlFlowGraphBuilder.getCFGB().newBasicBlock();
        CondNode condition = ctx.condition;
        ControlFlowGraphBuilder.getCFGB().insert(new JumpIfFalse(finalBB, ret.res));
        BasicBlock body = ControlFlowGraphBuilder.getCFGB().newBasicBlock();
        ControlFlowGraphBuilder.getCFGB().changeCur(body);
        ctx.breakBlock = finalBB;
        ctx.stmtAfterLoop = true;
        ctx.condition = (CondNode) children.get(2);
        children.get(4).buildIR(ctx, ret);

        ctx.breakBlock = cfg2;
        ctx.stmtAfterLoop = inLoop;
        ctx.condition = condition;
        ControlFlowGraphBuilder.getCFGB().changeCur(finalBB);
    }
}
