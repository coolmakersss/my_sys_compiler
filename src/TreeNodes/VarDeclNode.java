package TreeNodes;

import Generation.BuildIRCtx;
import Generation.BuildIRRet;
import Lexer.SyntaxKind;

public class VarDeclNode extends Node {

    @Override
    public void buildIR(BuildIRCtx ctx, BuildIRRet ret) {
        for (Node child: children) {
            child.buildIR(ctx, ret);
        }
    }
}
