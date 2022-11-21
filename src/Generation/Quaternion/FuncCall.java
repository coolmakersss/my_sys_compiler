package Generation.Quaternion;

import Generation.Function;

import java.io.IOException;
import java.io.OutputStreamWriter;

public class FuncCall extends Quaternion {
    String name;
    public FuncCall(String irFunc) {
        kind = IRKind.FUNC_CALL;
        name = irFunc;
    }

    @Override
    public void print(OutputStreamWriter writer) throws IOException {
        writer.append("funcCall ").append(name).append("\n");
    }

    @Override
    public void assembly(OutputStreamWriter writer, Function function) throws IOException {
        writer.append("jal ").append(name).append("\n");
    }

}
