package Generation.Quaternion;

import Generation.Function;
import Parser.Symbol;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashSet;

public class PrintInteger extends Quaternion {
    String integer;
    public PrintInteger(String s) {
        kind = IRKind.PRINT_INT;
        integer = s;
    }

    @Override
    public void print(OutputStreamWriter writer) throws IOException {
        writer.append("printInteger ").append(integer).append("\n");
    }

    @Override
    public HashSet<String> getUse() {
        HashSet<String> use = new HashSet<>();
        use.add(integer);
        return use;
    }

    @Override
    public void setUse(String k, String s) {
        integer = s;
    }

    @Override
    public void assembly(OutputStreamWriter writer, Function function) throws IOException {
        if(integer.charAt(0) == '$'){
            writer.append("move $a0, ").append(integer).append("\n");
        } else if(integer.charAt(0) == 's'){
            writer.append("lw $a0, ").append(integer.substring(2)).append("($sp)").append("\n");
        } else {
            writer.append("li $a0, ").append(integer).append("\n");
        }
        writer.append("li $v0, 1\n");
        writer.append("syscall\n");
    }
}
