package TreeNodes;

import Generation.BuildIRCtx;
import Generation.BuildIRRet;
import Lexer.SyntaxKind;

import java.util.ArrayList;

public class InitValNode extends Node {

    @Override
    public void buildIR(BuildIRCtx ctx, BuildIRRet ret) {
        ArrayList<String> init = new ArrayList<>();
        for(Node child:children) {
            child.buildIR(ctx, ret);
            if(child.getKind() == SyntaxKind.EXP){
                init.add(ret.res);
            } else if(child.getKind() == SyntaxKind.INIT_VAL) {
                init.addAll(ret.init);
            }
        }
        ret.init = init;
    }
}
