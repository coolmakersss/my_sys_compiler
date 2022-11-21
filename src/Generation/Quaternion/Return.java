package Generation.Quaternion;

import Generation.Function;
import Parser.Symbol;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.Objects;

public class Return extends Quaternion {
    private String integer;
    public Return(String s) {
        kind = IRKind.RETURN;
        integer = s;
    }

    @Override
    public void print(OutputStreamWriter writer) throws IOException {
        writer.append("return ").append(integer).append("\n");
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
        if(Objects.equals(integer, "-")){
            function.functionReturn(writer);
            return;
        }
        if(integer.charAt(0) == '$') {
            writer.append("move $v0, ").append(integer).append("\n");
        } else if(integer.charAt(0) == 's') {
            writer.append("lw $v0, ").append(integer.substring(2)).append("($sp)").append("\n");
        } else {
            writer.append("li $v0, ").append(integer).append("\n");
        }
        function.functionReturn(writer);
    }
}
