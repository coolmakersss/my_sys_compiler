package TreeNodes;

import Generation.BuildIRCtx;
import Generation.BuildIRRet;
import Generation.ControlFlowGraphBuilder;
import Generation.Quaternion.LoadPointer;
import Generation.Quaternion.LoadWord;
import Lexer.SyntaxKind;
import Parser.ErrorCheckCtx;
import Parser.ErrorCheckRet;
import Parser.Errorkind;
import Parser.LValSymbol;
import Parser.Symbol;
import Tools.Pair;

import java.util.ArrayList;

public class LvalNode extends Node {
    String res;
    @Override
    public void checkError(ArrayList<Pair<Errorkind, Integer>> errorlist, ErrorCheckCtx ctx, ErrorCheckRet ret) {
        TokenNode child0 = null;
        int cnt = 0;
        for (Node child : children) {
            if(child.getKind() == SyntaxKind.IDENFR) {
                child0 = (TokenNode)child;
                if (Symbol.getSymbol().gerVar(child0.getContent()) == null) {
                    errorlist.add(Pair.of(Errorkind.UNDEFINED_IDENT, child0.getFinishLine()));
                    return;
                } else if (ctx.isLVal && Symbol.getSymbol().gerVar(child0.getContent()).isConst) {
                    errorlist.add(Pair.of(Errorkind.CONSTANT_ASSIGNED, child0.getFinishLine()));
                    return;
                }
                ctx.isLVal = false;
            }
            if(child.getKind() ==SyntaxKind.EXP) cnt++;
            child.checkError(errorlist, ctx, ret);
        }
        if(child0!=null){
            ret.dimension = Symbol.getSymbol().gerVar(child0.getContent()).dimension.size() - cnt;
        }

    }

    @Override
    public void buildIR(BuildIRCtx ctx, BuildIRRet ret) {
        LValSymbol lValSymbol = null;
        String name = "";
        ArrayList<String> indices = new ArrayList<>();
        boolean lVal = ctx.isLVal;
        ctx.isLVal = false;
        for(Node child:children){
            if(child.getKind() == SyntaxKind.IDENFR) {
                name = ((TokenNode) child).getContent();
                lValSymbol = Symbol.getSymbol().gerVar(name);
            }
            child.buildIR(ctx, ret);
            if(child.getKind() == SyntaxKind.EXP){
                indices.add(ret.res);
            }
        }
        boolean flag = true;
        for(String i:indices) {
            if(!Symbol.exprResIsNumber(i)){
                flag = false;
            }
        }
        if((lValSymbol.isConst() && flag) || ctx.isGlobal) {
            ArrayList<Integer> dimension = new ArrayList<>();
            for(String i:indices) {
                dimension.add(Integer.parseInt(i));
            }
            res = String.valueOf(lValSymbol.getVal(dimension));
            ret.res = res;System.out.println(ctx.isGlobal);
            return;
        }
        String offset = null;
        if(!indices.isEmpty()){
            offset = lValSymbol.findOffset(indices);
        } else if(lValSymbol.isGlobal()){
            offset = "0";
        }
        if(lVal){
            res = Symbol.generateIRVar(name,lValSymbol.getId());
            ret.res = res;
            ret.param = offset;
            ret.isArray = offset!=null;
            return;
        }
        if(indices.isEmpty() && !lValSymbol.isGlobal()){
            res = Symbol.generateIRVar(name,lValSymbol.getId());
            ret.res = res;
        } else {
            res = ControlFlowGraphBuilder.getCFGB().tmpVar();
            String varName = Symbol.generateIRVar(name,lValSymbol.getId());
            if(indices.size() == lValSymbol.dimension.size()){
                ControlFlowGraphBuilder.getCFGB().insert(new LoadWord(res,varName,offset));
            } else {
                ControlFlowGraphBuilder.getCFGB().insert(new LoadPointer(res,varName,offset));
            }
            ret.res = res;
        }
    }
}
