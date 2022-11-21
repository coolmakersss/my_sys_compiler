package TreeNodes;

import Generation.BuildIRCtx;
import Generation.BuildIRRet;
import Generation.ControlFlowGraphBuilder;
import Generation.Quaternion.Negative;
import Generation.Quaternion.NotLogic;

public class UnaryOpNode extends Node {

    @Override
    public void buildIR(BuildIRCtx ctx, BuildIRRet ret) {System.out.println(children.size());
        if(ctx.isConst){
            int val = Integer.parseInt(ctx.res);
            switch (children.get(0).getKind()){
                case NOT:
                    val = val==0?1:0;
                    break;
                case PLUS:
                    break;
                case MINU:
                    val = -val;
                    break;
            }
            ctx.res = String.valueOf(val);
        } else {
            String tmp = ControlFlowGraphBuilder.getCFGB().tmpVar();
            switch (children.get(0).getKind()){
                case NOT:
                    ControlFlowGraphBuilder.getCFGB().insert(new NotLogic(tmp, ctx.res));
                    ctx.res = tmp;
                    break;
                case PLUS:
                    break;
                case MINU:
                    ControlFlowGraphBuilder.getCFGB().insert(new Negative(tmp, ctx.res));
                    ctx.res = tmp;
                    break;
            }
        }

    }
}
