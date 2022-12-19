package Generation.Quaternion;

import Generation.Function;
import Parser.Symbol;

import java.io.IOException;
import java.io.OutputStreamWriter;

public class Land extends Binary {
    public Land(String tmp, String a, String b) {
        kind = IRKind.AND_BIN;
        this.assigned = tmp;
        this.integer1 = a;
        this.integer2 = b;
    }

    @Override
    public void print(OutputStreamWriter writer) throws IOException {
        writer.append("land ").append(assigned).append(" ").append(integer1).append(" ").append(integer2).append("\n");
    }

    @Override
    public void assembly(OutputStreamWriter writer, Function function) throws IOException {
        if(Symbol.exprResIsNumber(integer1)){
            writer.append("li $27, ").append(integer1).append("\n");
            writer.append("sne $27, $27, 0").append("\n");
            integer1 = "$27";
        } else if(integer1.charAt(0) != '$') {
            writer.append("lw $27 ").append(integer1.substring(2)).append("($sp)").append("\n");
            writer.append("sne $27, $27, 0").append("\n");
            integer1 = "$27";
        }
        if(Symbol.exprResIsNumber(integer2)){
            writer.append("li $28, ").append(integer2).append("\n");
            writer.append("sne $28, $28, 0").append("\n");
            integer2 = "$28";
        } else if(integer2.charAt(0) != '$'){
            writer.append("lw $28, ").append(integer2.substring(2)).append("($sp)").append("\n");
            writer.append("sne $28, $28, 0").append("\n");
            integer2 = "$28";
        }
        if(assigned.charAt(0) == '$'){
            writer.append("and ").append(assigned).append(", ").append(integer1).append(", ").append(integer2).append("\n");
        } else {
            writer.append("and $27, ").append(integer1).append(", ").append(integer2).append("\n");
            writer.append("sw $27, ").append(assigned.substring(2)).append("($sp)").append("\n");
        }
    }
}
