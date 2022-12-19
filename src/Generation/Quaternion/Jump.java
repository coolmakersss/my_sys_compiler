package Generation.Quaternion;

import Generation.BasicBlock;
import Generation.Function;

import java.io.IOException;
import java.io.OutputStreamWriter;

public class Jump extends Quaternion {
    BasicBlock basicBlock;
    public Jump(BasicBlock continueBlock) {
        basicBlock = continueBlock;
    }

    @Override
    public void print(OutputStreamWriter writer) throws IOException {
        writer.append("jump ").append(basicBlock.getTag()).append("\n");
    }

    @Override
    public void assembly(OutputStreamWriter writer, Function function) throws IOException {
        writer.append("j ").append(basicBlock.getTag()).append("\n");
    }
}
