package TreeNodes;

import Generation.BasicBlock;
import Generation.BuildIRCtx;
import Generation.BuildIRRet;
import Generation.ControlFlowGraphBuilder;
import Generation.Quaternion.Assign;
import Generation.Quaternion.GetInt;
import Generation.Quaternion.Jump;
import Generation.Quaternion.JumpIfTrue;
import Generation.Quaternion.ReadInt;
import Generation.Quaternion.StoreWord;
import Lexer.SyntaxKind;
import Parser.ErrorCheckCtx;
import Parser.ErrorCheckRet;
import Parser.Errorkind;
import Tools.Pair;

import java.util.ArrayList;

public class StmtNode extends Node {

    @Override
    public void checkError(ArrayList<Pair<Errorkind, Integer>> errorlist, ErrorCheckCtx ctx, ErrorCheckRet ret) {
        for (Node child : children) {
            if(child.getKind() == SyntaxKind.LVAL){
                ctx.isLVal = true;
            }
            child.checkError(errorlist, ctx, ret);
            ctx.isLVal = false;
            ret.isReturn = child.getKind() == SyntaxKind.RETURN_STMT;
        }
    }

    @Override
    public void buildIR(BuildIRCtx ctx, BuildIRRet ret) {
        BasicBlock continueCFG = ctx.continueBlock;
        BasicBlock curBB = ControlFlowGraphBuilder.getCFGB().getCur();
        boolean stmtAfterloop = ctx.stmtAfterLoop;
        if(stmtAfterloop) {
            ctx.continueBlock = ControlFlowGraphBuilder.getCFGB().newBasicBlock();
        }
        boolean needJumpElse = ctx.needJumpElse;
        ctx.stmtAfterLoop = false;
        ctx.needJumpElse = false;
        if(children.get(0).getKind() == SyntaxKind.LVAL){
            ctx.isLVal = true;
            children.get(0).buildIR(ctx, ret);
            boolean isArray = ret.isArray;
            String lVal = ret.res;
            String offset = ret.param;
            ret.param = null;
            String integer = "";
            for(int i=1;i<children.size();i++){
                children.get(i).buildIR(ctx, ret);
                if(children.get(i).getKind() == SyntaxKind.EXP) {
                    integer = ret.res;
                } else if(children.get(i).getKind() == SyntaxKind.GETINTTK) {
                    integer = ControlFlowGraphBuilder.getCFGB().tmpVar();
                    ControlFlowGraphBuilder.getCFGB().insert(new ReadInt());
                    ControlFlowGraphBuilder.getCFGB().insert(new GetInt(integer));
                }
            }
            if(isArray){System.out.println("@@@@@@@@@@@!");
                ControlFlowGraphBuilder.getCFGB().insert(new StoreWord(integer,lVal,offset));
            } else {
                ControlFlowGraphBuilder.getCFGB().insert(new Assign(lVal,integer));
            }
        } else {
            for(Node child:children) {
                child.buildIR(ctx, ret);
            }
        }
        if(stmtAfterloop) {
            ControlFlowGraphBuilder.getCFGB().changeCur(ctx.continueBlock);
            ctx.condition.buildIR(ctx, ret);
            ControlFlowGraphBuilder.getCFGB().insert(new JumpIfTrue(curBB,ret.res));
        } else if(needJumpElse) {
            ControlFlowGraphBuilder.getCFGB().insert(new Jump(ctx.elseFinalBlock));
        }
        ctx.continueBlock = continueCFG;
    }
}
