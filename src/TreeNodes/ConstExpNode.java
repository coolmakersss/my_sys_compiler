package TreeNodes;

import Generation.BuildIRCtx;
import Generation.BuildIRRet;

public class ConstExpNode extends Node {
    private String res = "";

    @Override
    public void buildIR(BuildIRCtx ctx, BuildIRRet ret) {
        children.get(0).buildIR(ctx, ret);
        res = ret.res;
        ret.res = res;
    }
}
