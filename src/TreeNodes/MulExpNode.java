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
import java.util.Stack;

import static Lexer.SyntaxKind.getPrint;
import static Lexer.SyntaxKind.isParserToken;

public class MulExpNode extends Node {

    @Override
    public void print(OutputStreamWriter writer) throws IOException {
        for (Node child : children) {
            child.print(writer);
            if(isParserToken(child.kind))
                writer.append('<').append(getPrint(kind)).append('>').append("\n");
        }
        //writer.append('<').append(getPrint(kind)).append('>').append("\n");
    }

//    @Override
//    public void checkError(ArrayList<Pair<Errorkind, Integer>> errorlist, ErrorCheckCtx ctx, ErrorCheckRet ret) {
//        for(Node child: children){
//            ErrorCheckRet ret1 = new ErrorCheckRet();
//            child.checkError(errorlist, ctx, ret1);
//            if(ret1.isConst){
//                ret.isConst = true;
//            }
//            ret.dimension = ret1.dimension;
//        }
//    }


    @Override
    public void buildIR(BuildIRCtx ctx, BuildIRRet ret) {
        Stack<String> all = new Stack<>();
        Stack<SyntaxKind> ops = new Stack<>();
        for(int i = children.size()-1;i>=0;i--){
            BuildIRRet ret1 = new BuildIRRet();
            children.get(i).buildIR(ctx,ret1);
            if(children.get(i).getKind() == SyntaxKind.MULT || children.get(i).getKind() == SyntaxKind.DIV
                    || children.get(i).getKind() == SyntaxKind.MOD) {
                ops.push(children.get(i).getKind());
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
    }
}
