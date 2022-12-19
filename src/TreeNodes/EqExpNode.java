package TreeNodes;

import Generation.BuildIRCtx;
import Generation.BuildIRRet;
import Generation.ControlFlowGraphBuilder;
import Generation.Quaternion.Assign;
import Generation.Quaternion.Equal;
import Generation.Quaternion.GreaterOrEqual;
import Generation.Quaternion.Land;
import Generation.Quaternion.NotEqual;
import Lexer.SyntaxKind;
import Parser.Symbol;

import java.io.IOException;
import java.io.OutputStreamWriter;

import static Lexer.SyntaxKind.getPrint;
import static Lexer.SyntaxKind.isParserToken;

public class EqExpNode extends Node {

    @Override
    public void print(OutputStreamWriter writer) throws IOException {
        for (Node child : children) {
            child.print(writer);
            if (isParserToken(child.kind))
                writer.append('<').append(getPrint(kind)).append('>').append("\n");
        }
        //writer.append('<').append(getPrint(kind)).append('>').append("\n");

    }

    @Override
    public void buildIR(BuildIRCtx ctx, BuildIRRet ret) {
        BuildIRRet ret1 = new BuildIRRet();
        children.get(0).buildIR(ctx, ret1);
        if(children.size()==1){
            ret.res = ret1.res;
            return;
        }
        String tmp="";
        if(Symbol.exprResIsNumber(ret1.res)){
            tmp = ret1.res;
        } else {
            tmp = ControlFlowGraphBuilder.getCFGB().tmpVar();
            ControlFlowGraphBuilder.getCFGB().insert(new Assign(tmp, ret1.res));
        }
        if (children.size() != 1) {
            int flag = 0;
            for (int i = 1; i < children.size(); i++) {
                ret1 = new BuildIRRet();
                children.get(i).buildIR(ctx, ret1);
                if (children.get(i).getKind() == SyntaxKind.EQL) {
                    flag = 1;
                } else if (children.get(i).getKind() == SyntaxKind.NEQ) {
                    flag = 0;
                } else if (children.get(i).getKind() == SyntaxKind.REAL_EXP) {
                    if (flag == 1) {
                        if(Symbol.exprResIsNumber(tmp) && Symbol.exprResIsNumber(ret1.res)){
                            if(Integer.parseInt(tmp)==Integer.parseInt(ret1.res)){
                                tmp = "1";
                            } else {
                                tmp = "0";
                            }
                        } else {
                            if(Symbol.exprResIsNumber(tmp)){
                                String tmpf = ControlFlowGraphBuilder.getCFGB().tmpVar();
                                ControlFlowGraphBuilder.getCFGB().insert(new Equal(tmpf, tmp, ret1.res));
                                tmp = tmpf;
                            } else  ControlFlowGraphBuilder.getCFGB().insert(new Equal(tmp, tmp, ret1.res));
                        }
                    } else {
                        if(Symbol.exprResIsNumber(tmp) && Symbol.exprResIsNumber(ret1.res)){
                            if(Integer.parseInt(tmp)!=Integer.parseInt(ret1.res)){
                                tmp = "1";
                            } else {
                                tmp = "0";
                            }
                        } else {
                            if(Symbol.exprResIsNumber(tmp)){
                                String tmpf = ControlFlowGraphBuilder.getCFGB().tmpVar();
                                ControlFlowGraphBuilder.getCFGB().insert(new NotEqual(tmpf, tmp, ret1.res));
                                tmp = tmpf;
                            } else ControlFlowGraphBuilder.getCFGB().insert(new NotEqual(tmp, tmp, ret1.res));
                        }
                    }
                }
            }
        }
        ret.res = tmp;
    }
}
