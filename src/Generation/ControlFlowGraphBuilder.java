package Generation;

import Generation.Quaternion.Quaternion;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashSet;

public class ControlFlowGraphBuilder {
    public HashSet<Function> funcs;
    public Function curFunc;
    public BasicBlock curBasicBlock;
    public int basicBlockCnt;
    public int tmpCnt;
    private ControlFlowGraphBuilder(){
        funcs = new HashSet<>();
        curFunc = null;
        curBasicBlock = null;
        basicBlockCnt = 0;
        tmpCnt = 0;
    }
    private final static ControlFlowGraphBuilder cfgb = new ControlFlowGraphBuilder();
    public static ControlFlowGraphBuilder getCFGB(){
        return cfgb;
    }

    public void insert(Quaternion quaternion){
        curBasicBlock.insert(quaternion);
    }

    public void changeFunction(Function function) {
        curFunc = function;
        funcs.add(function);
    }

    public void changeCur(BasicBlock newBB) {
        if(newBB == curBasicBlock) return;
        curBasicBlock = newBB;
        curFunc.addBasicBlock(newBB);
    }

    public BasicBlock newBasicBlock() {
        basicBlockCnt++;
        return new BasicBlock(basicBlockCnt);
    }

    public BasicBlock getCur() {
        return curBasicBlock;
    }

    public void print(OutputStreamWriter writer) throws IOException {
        for(Function func :funcs) {
            func.print(writer);
        }
    }

    public void assembly(OutputStreamWriter writer) throws IOException {
        for(Function func :funcs) {
            func.assembly(writer);
        }
    }

    public String tmpVar() {
        tmpCnt++;
        return "tmp_"+tmpCnt;
    }

    public void allocateReg() {
        for(Function func :funcs) {
            func.allocateReg();
        }
    }
}
