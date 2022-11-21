package Generation.Quaternion;

import Generation.Function;

import java.io.IOException;
import java.io.OutputStreamWriter;

public class PrintString extends Quaternion {
    String str;
    public PrintString(String label) {
        kind = IRKind.PRINT_STR;
        str = label;
    }

    @Override
    public void print(OutputStreamWriter writer) throws IOException {
        writer.append("printString ").append(str).append("\n");
    }

    @Override
    public void assembly(OutputStreamWriter writer, Function function) throws IOException {
        writer.append("la $a0, ").append(str).append("\n");
        writer.append("li $v0, 4\n");
        writer.append("syscall\n");
    }
}
