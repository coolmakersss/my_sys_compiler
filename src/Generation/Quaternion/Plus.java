package Generation.Quaternion;

import Generation.Function;
import Parser.Symbol;

import java.io.IOException;
import java.io.OutputStreamWriter;

public class Plus extends Binary {
    public Plus(String assigned, String a, String b) {
        kind = IRKind.PLUS_BIN;
        this.assigned = assigned;
        this.integer1 = a;
        this.integer2 = b;
    }

    @Override
    public void print(OutputStreamWriter writer) throws IOException {
        writer.append("plus ").append(assigned).append(" ").append(integer1).append(" ").append(integer2).append("\n");
    }

    @Override
    public void assembly(OutputStreamWriter writer, Function function) throws IOException {
        if(Symbol.exprResIsNumber(integer1)){
            writer.append("li $27, ").append(integer1).append("\n");
            integer1 = "$27";
        } else if(integer1.charAt(0) != '$'){
            writer.append("lw $27, ").append(integer1.substring(2)).append("($sp)").append("\n");
            integer1 = "$27";
        }
        if(integer2.charAt(0) == 's'){
            writer.append("lw $28, ").append(integer2.substring(2)).append("($sp)").append("\n");
            integer2 = "$28";
        }

        if(assigned.charAt(0) == '$'){
            writer.append("addu ").append(assigned).append(", ").append(integer1).append(", ").append(integer2).append("\n");
        } else {
            writer.append("addu $27, ").append(integer1).append(", ").append(integer2).append("\n");
            writer.append("sw $27, ").append(assigned.substring(2)).append("($sp)").append("\n");
        }
    }
}
