package Generation.Quaternion;

import Generation.Function;
import Parser.Symbol;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashSet;

public class Negative extends Quaternion {
    String assigned;
    String integer;
    public Negative(String tmp, String res) {
        kind = IRKind.NEGATE_UNI;
        assigned = tmp;
        integer = res;
    }

    @Override
    public void print(OutputStreamWriter writer) throws IOException {
        writer.append("negate ").append(assigned).append(" ").append(integer).append("\n");
    }

    @Override
    public String getDefine() {
        return assigned;
    }

    @Override
    public void setDefine(String s) {
        assigned = s;
    }

    @Override
    public HashSet<String> getUse() {
        HashSet<String> use = new HashSet<>();
        if(!Symbol.exprResIsNumber(integer)){
            use.add(integer);
        }
        return use;
    }

    @Override
    public void setUse(String k, String s) {
        integer = s;
    }

    @Override
    public void assembly(OutputStreamWriter writer, Function function) throws IOException {
        if(integer.charAt(0) != '$'){
            writer.append("lw $27, ").append(integer.substring(2)).append("($sp)").append("\n");
            integer = "$27";
        }
        if(assigned.charAt(0) == '$'){
            writer.append("sub ").append(assigned).append(", $0, ").append(integer).append("\n");
        } else {
            writer.append("sub $27, $0, ").append(integer).append("\n");
            writer.append("sw $27, ").append(assigned.substring(2)).append("($sp)").append("\n");
        }
    }
}
