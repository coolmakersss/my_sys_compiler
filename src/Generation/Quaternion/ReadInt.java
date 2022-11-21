package Generation.Quaternion;

import Generation.Function;

import java.io.IOException;
import java.io.OutputStreamWriter;

public class ReadInt extends Quaternion {
    public ReadInt(){
        kind = IRKind.READ_INT;
    }

    @Override
    public void print(OutputStreamWriter writer) throws IOException {
        writer.append("readInt").append("\n");
    }

    @Override
    public void assembly(OutputStreamWriter writer, Function function) throws IOException {
        writer.append("li $v0, 5\n");
        writer.append("syscall\n");
    }
}
