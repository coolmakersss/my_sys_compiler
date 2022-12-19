package TreeNodes;

import Generation.BuildIRCtx;
import Generation.BuildIRRet;
import Generation.ControlFlowGraphBuilder;
import Generation.Quaternion.Assign;
import Generation.Quaternion.JumpIfFalse;
import Generation.Quaternion.Land;
import Generation.Quaternion.Lor;
import Lexer.SyntaxKind;
import Parser.Symbol;

import java.io.IOException;
import java.io.OutputStreamWriter;

import static Lexer.SyntaxKind.getPrint;
import static Lexer.SyntaxKind.isParserToken;

public class LandExpNode extends Node {

    @Override
    public void print(OutputStreamWriter writer) throws IOException {
        for (Node child : children) {
            child.print(writer);
            if(isParserToken(child.kind))
                writer.append('<').append(getPrint(kind)).append('>').append("\n");
        }
        //writer.append('<').append(getPrint(kind)).append('>').append("\n");

    }

    /*
    @Override
    public void buildIR(BuildIRCtx ctx, BuildIRRet ret) {
        String tmp = ControlFlowGraphBuilder.getCFGB().tmpVar();
        BuildIRRet ret1 = new BuildIRRet();
        children.get(0).buildIR(ctx, ret1);
        //短路求值
        if(children.size()==1){
            ret.res = ret1.res;
            return;
        }
        if (Symbol.exprResIsNumber(ret1.res) && Integer.parseInt(ret1.res) == 0) {
            ret.res = "0";
            return;
        }
        ControlFlowGraphBuilder.getCFGB().insert(new Assign(tmp, ret1.res));
        if (children.size() != 1) {
            for (int i = 1; i < children.size(); i++) {
                ret1 = new BuildIRRet();
                children.get(i).buildIR(ctx, ret1);
                if (children.get(i).getKind() == SyntaxKind.EQ_EXP) {
                    if (Symbol.exprResIsNumber(ret1.res)) {
                        if (Integer.parseInt(ret1.res) == 0) {
                            ret.res = "0";
                            return;
                        }
                    } else {
                        ControlFlowGraphBuilder.getCFGB().insert(new Land(tmp, tmp, ret1.res));
                    }
                }
            }
        }
        ret.res = tmp;
    }*/

    @Override
    public void buildIR(BuildIRCtx ctx, BuildIRRet ret) {
        String tmp = ControlFlowGraphBuilder.getCFGB().tmpVar();
        for(int i=0;i<children.size();i+=2){
            BuildIRRet ret1 = new BuildIRRet();
            children.get(i).buildIR(ctx, ret1);
            if(children.size()==1){
                ret.res = ret1.res;
                return;
            }
            if (Symbol.exprResIsNumber(ret1.res) && Integer.parseInt(ret1.res) == 0) {
                ret.res = "0";
                return;
            }
            ControlFlowGraphBuilder.getCFGB().insert(new JumpIfFalse(ctx.lorBlock,ret1.res));
        }
        ret.res = "1";
    }
}
