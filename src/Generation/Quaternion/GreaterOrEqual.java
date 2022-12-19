package Generation.Quaternion;

import Generation.Function;
import Parser.Symbol;

import java.io.IOException;
import java.io.OutputStreamWriter;

public class GreaterOrEqual extends Binary {
    public GreaterOrEqual(String tmp, String tmp1, String res) {
        kind = IRKind.GT_BIN;
        this.assigned = tmp;
        this.integer1 = tmp1;
        this.integer2 = res;
    }

    @Override
    public void print(OutputStreamWriter writer) throws IOException {
        writer.append("greaterOrEqual ").append(assigned).append(" ").append(integer1).append(" ").append(integer2).append("\n");
    }

    @Override
    public void assembly(OutputStreamWriter writer, Function function) throws IOException {
        if(Symbol.exprResIsNumber(integer1)){
            writer.append("li $27, ").append(integer1).append("\n");
            integer1 = "$27";
        } else if(integer1.charAt(0) != '$') {
            writer.append("lw $27 ").append(integer1.substring(2)).append("($sp)").append("\n");
            integer1 = "$27";
        }
        if(!Symbol.exprResIsNumber(integer2) && integer2.charAt(0) != '$'){
            writer.append("lw $28, ").append(integer2.substring(2)).append("($sp)").append("\n");
            integer2 = "$28";
        }
        if(assigned.charAt(0) == '$'){
            writer.append("sge ").append(assigned).append(", ").append(integer1).append(", ").append(integer2).append("\n");
        } else {
            writer.append("sge $27, ").append(integer1).append(", ").append(integer2).append("\n");
            writer.append("sw $27, ").append(assigned.substring(2)).append("($sp)").append("\n");
        }
    }
}
