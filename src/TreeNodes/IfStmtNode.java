package TreeNodes;

import Generation.BasicBlock;
import Generation.BuildIRCtx;
import Generation.BuildIRRet;
import Generation.ControlFlowGraphBuilder;
import Generation.Quaternion.JumpIfFalse;
import Lexer.SyntaxKind;

import java.io.IOException;
import java.io.OutputStreamWriter;

public class IfStmtNode extends Node {

    @Override
    public void print(OutputStreamWriter writer) throws IOException {
        for (Node child : children) {
            child.print(writer);
        }
    }

    @Override
    public void buildIR(BuildIRCtx ctx, BuildIRRet ret) {
        BasicBlock cfg3 = ctx.elseFinalBlock;
        boolean needJumpelse = ctx.needJumpElse;
        BasicBlock tmp = ctx.trueBlock;

        BasicBlock trueCFG = ControlFlowGraphBuilder.getCFGB().newBasicBlock();
        ctx.trueBlock = trueCFG;
        children.get(2).buildIR(ctx, ret);
        boolean hasElse = false;
        for(Node child:children){
            if(child.getKind() == SyntaxKind.ELSETK){
                hasElse = true;
                break;
            }
        }
        if(hasElse) {
            BasicBlock elseCFG = ControlFlowGraphBuilder.getCFGB().newBasicBlock();
            BasicBlock finalCFG = ControlFlowGraphBuilder.getCFGB().newBasicBlock();
            ControlFlowGraphBuilder.getCFGB().insert(new JumpIfFalse(elseCFG, ret.res));
            ControlFlowGraphBuilder.getCFGB().changeCur(trueCFG);
            ctx.elseFinalBlock = finalCFG;
            ctx.needJumpElse = true;
            children.get(4).buildIR(ctx, ret);
            ControlFlowGraphBuilder.getCFGB().changeCur(elseCFG);
            ctx.needJumpElse = false;
            children.get(6).buildIR(ctx, ret);
            ControlFlowGraphBuilder.getCFGB().changeCur(finalCFG);
        } else {
            BasicBlock finalCFG = ControlFlowGraphBuilder.getCFGB().newBasicBlock();
            ControlFlowGraphBuilder.getCFGB().insert(new JumpIfFalse(finalCFG, ret.res));
            ControlFlowGraphBuilder.getCFGB().changeCur(trueCFG);
            ctx.needJumpElse = false;
            children.get(4).buildIR(ctx, ret);
            ControlFlowGraphBuilder.getCFGB().changeCur(finalCFG);
        }

        ctx.elseFinalBlock = cfg3;
        ctx.trueBlock = tmp;
        ctx.needJumpElse = needJumpelse;
    }
}
