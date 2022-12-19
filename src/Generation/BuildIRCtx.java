package Generation;
import TreeNodes.CondNode;
import TreeNodes.ExpNode;

public class BuildIRCtx {
    public boolean isConst, afterFuncDef, isLVal, isGlobal, needJumpElse, stmtAfterLoop;
    public String res;
    public CondNode condition;
    public BasicBlock continueBlock, breakBlock, elseFinalBlock;
    public BasicBlock trueBlock,lorBlock; //短路求值
    public BuildIRCtx() {
        isConst = false;
        afterFuncDef = false;
        isLVal = false;
        isGlobal = false;
        needJumpElse = false;
        stmtAfterLoop = false;
    }
}
