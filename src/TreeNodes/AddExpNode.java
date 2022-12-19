package TreeNodes;

import Generation.BuildIRCtx;
import Generation.BuildIRRet;
import Generation.ControlFlowGraphBuilder;
import Lexer.SyntaxKind;
import Parser.ErrorCheckCtx;
import Parser.ErrorCheckRet;
import Parser.Errorkind;
import Parser.Symbol;
import Tools.Pair;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Stack;

import static Lexer.SyntaxKind.getPrint;
import static Lexer.SyntaxKind.isParserToken;

public class AddExpNode extends Node {

    @Override
    public void print(OutputStreamWriter writer) throws IOException {
        for (Node child : children) {
            child.print(writer);
            if(isParserToken(child.kind))
                writer.append('<').append(getPrint(kind)).append('>').append("\n");
        }
        //writer.append('<').append(getPrint(kind)).append('>').append("\n");

    }

    @Override
    public void checkError(ArrayList<Pair<Errorkind, Integer>> errorlist, ErrorCheckCtx ctx, ErrorCheckRet ret) {
        int max = -1;
        for(Node child: children){
            ErrorCheckRet ret1 = new ErrorCheckRet();
            child.checkError(errorlist, ctx, ret1);
            if(ret1.isConst) {
                ret.isConst = true;
            }
            if (max<ret1.dimension){
                max = ret1.dimension;
            }
        }
        ret.dimension = max;
    }

    /*@Override
    public void buildIR(BuildIRCtx ctx, BuildIRRet ret) {
        Stack<String> all = new Stack<>();
        Stack<SyntaxKind> ops = new Stack<>();
        for(int i = children.size()-1;i>=0;i--){
            BuildIRRet ret1 = new BuildIRRet();
            children.get(i).buildIR(ctx,ret1);
            if(children.get(i).getKind() == SyntaxKind.PLUS) {
                ops.push(SyntaxKind.PLUS);
            } else if(children.get(i).getKind() == SyntaxKind.MINU) {
                ops.push(SyntaxKind.MINU);
            } else {
                all.push(ret1.res);
            }
        }
        while(all.size()>1){
            String a = all.pop();
            SyntaxKind op = ops.pop();
            String b = all.pop();
            if(Symbol.exprResIsNumber(a) && Symbol.exprResIsNumber(b)){
                int x1 = Integer.parseInt(a);
                int x2 = Integer.parseInt(b);
                all.push(String.valueOf(Symbol.calculate(x1,x2,op)));
            } else {
                String tmp;
                tmp = ControlFlowGraphBuilder.getCFGB().tmpVar();
                Symbol.getRes(tmp,a,b,op);
                all.push(tmp);
            }
        }
        ret.res = all.pop();
    }*/

    @Override
    public void buildIR(BuildIRCtx ctx, BuildIRRet ret) {
        Stack<String> all_tmp = new Stack<>();
        Stack<SyntaxKind> ops_tmp = new Stack<>();
        Stack<String> all = new Stack<>();
        Stack<SyntaxKind> ops = new Stack<>();
        for (Node child : children) {
            BuildIRRet ret1 = new BuildIRRet();
            child.buildIR(ctx, ret1);
            if (child.getKind() == SyntaxKind.PLUS) {
                ops_tmp.push(SyntaxKind.PLUS);
            } else if (child.getKind() == SyntaxKind.MINU) {
                ops_tmp.push(SyntaxKind.MINU);
            } else {
                all_tmp.push(ret1.res);
            }
        }
        while (!all_tmp.isEmpty()) {
            all.push(all_tmp.pop());
        }
        while (!ops_tmp.isEmpty()) {
            ops.push(ops_tmp.pop());
        }

        while(all.size()>1){
            String a = all.pop();
            SyntaxKind op = ops.pop();
            String b = all.pop();
            if(Symbol.exprResIsNumber(a) && Symbol.exprResIsNumber(b)){
                int x1 = Integer.parseInt(a);
                int x2 = Integer.parseInt(b);
                all.push(String.valueOf(Symbol.calculate(x1,x2,op)));
            } else {
                String tmp;
                tmp = ControlFlowGraphBuilder.getCFGB().tmpVar();
                Symbol.getRes(tmp,a,b,op);
                all.push(tmp);
            }
        }
        ret.res = all.pop();
    }
}
