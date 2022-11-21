package TreeNodes;

import Generation.BuildIRCtx;
import Generation.BuildIRRet;
import Generation.ControlFlowGraphBuilder;
import Generation.Quaternion.Assign;
import Generation.Quaternion.FuncCall;
import Generation.Quaternion.GetReturn;
import Generation.Quaternion.PushParam;
import Lexer.SyntaxKind;
import Parser.ErrorCheckCtx;
import Parser.ErrorCheckRet;
import Parser.Errorkind;
import Parser.FuncSymbol;
import Parser.Symbol;
import Tools.Pair;

import java.util.ArrayList;

public class UnaryExpNode extends Node {
    private String res;

    @Override
    public void checkError(ArrayList<Pair<Errorkind, Integer>> errorlist, ErrorCheckCtx ctx, ErrorCheckRet ret) {
        if(children.get(0).getKind() == SyntaxKind.IDENFR && children.get(1).getKind() == SyntaxKind.LPARENT){
            FuncSymbol funcSymbol = null;
            boolean flag = false;
            int line = -1;
            for (Node child : children) {
                if (child.getKind() == SyntaxKind.IDENFR) {
                        funcSymbol = Symbol.getSymbol().getFunc(((TokenNode)child).getContent());
                    if (funcSymbol == null) {
                        errorlist.add(Pair.of(Errorkind.UNDEFINED_IDENT, child.getFinishLine()));
                    } else if (funcSymbol.isVoid()) {
                        ret.dimension = -1;
                    }
                }
                ErrorCheckRet ret1 = new ErrorCheckRet();
                child.checkError(errorlist, ctx, ret1);
                if (child.getKind() == SyntaxKind.IDENFR) {
                    line = child.getFinishLine();
                } else if (funcSymbol != null &&
                        (child.getKind() == SyntaxKind.FUNC_R_PARAMS || (!flag && child.getKind() == SyntaxKind.RPARENT))) {
                    flag = true;
                    Errorkind kind = funcSymbol.matchParams(ret1.args);
                    //System.out.println(ret1.args);
                    if (kind == Errorkind.PARAMS_NUM_UNMATCHED) {
                        errorlist.add(Pair.of(Errorkind.PARAMS_NUM_UNMATCHED, line));
                    } else if (kind == Errorkind.PARAM_TYPE_UNMATCHED) {
                        errorlist.add(Pair.of(Errorkind.PARAM_TYPE_UNMATCHED, line));
                    }
                }
            }
        } else {
            for(Node child: children){
                ErrorCheckRet ret1 = new ErrorCheckRet();
                child.checkError(errorlist, ctx, ret1);
                if(ret1.isConst){
                    ret.isConst = true;
                }
                ret.dimension = ret1.dimension;
                ret.val = ret1.val;
            }
        }
    }

    @Override
    public void buildIR(BuildIRCtx ctx, BuildIRRet ret) {
        //???
        if(ctx.isLVal) {
            children.get(0).buildIR(ctx, ret);
            res = ret.res;
            return;
        }
        if(children.get(0).getKind() == SyntaxKind.IDENFR) {
            String name = null;
            FuncSymbol funcSymbol = null;
            ArrayList<String> args = new ArrayList<>();
            for(Node child:children){
                if(child.getKind() == SyntaxKind.IDENFR){
                    name = ((TokenNode) child).getContent();
                    funcSymbol = Symbol.getSymbol().getFunc(name);
                }
                child.buildIR(ctx, ret);
                if(child.getKind() == SyntaxKind.FUNC_R_PARAMS){
                    args = ret.args;
                }
            }
            if(funcSymbol.isVoid()) res = "-";
            else res = ControlFlowGraphBuilder.getCFGB().tmpVar();
            String irFunc = Symbol.generateIRFunc(name);
            for (int i = 0; i < args.size(); i++) {
                ControlFlowGraphBuilder.getCFGB().insert(new PushParam(args.get(i), i));
            }
            ControlFlowGraphBuilder.getCFGB().insert(new FuncCall(irFunc));
            if(!funcSymbol.isVoid()){
                ControlFlowGraphBuilder.getCFGB().insert(new GetReturn(res));
            }
            ret.res =res;
        } else {
            for (Node child:children){
                if(child.getKind() == SyntaxKind.PRIMARY_EXP || child.getKind() == SyntaxKind.UNARY_EXP){
                    child.buildIR(ctx, ret);
                    res = ret.res;
                }
            }
        }
        ctx.isConst = Symbol.exprResIsNumber(ret.res);
        if(ctx.isConst || children.get(0).getKind() != SyntaxKind.UNARY_OP){
            res = ret.res;
        } else {
            res = ControlFlowGraphBuilder.getCFGB().tmpVar();System.out.println(res+ret.res);
            ControlFlowGraphBuilder.getCFGB().insert(new Assign(res,ret.res));
        }
        if(children.get(0).getKind() == SyntaxKind.UNARY_OP){
            ctx.res = res;
            children.get(0).buildIR(ctx, ret);
            res = ctx.res;
        }
        ctx.isConst = false;
        ret.res = res;
    }
}
