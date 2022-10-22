package Parser;

public class ErrorCheckCtx {
    public int loopNum;
    public boolean inVoidFunc, afterFuncDef, isConst, isLVal, isGlobal;

    public ErrorCheckCtx(){
        loopNum = 0;
        inVoidFunc = false;
        afterFuncDef =false;
        isConst = false;
        isLVal = false;
        isGlobal =false;
    }
}
