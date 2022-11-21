package Generation.Quaternion;

import Generation.Function;
import Parser.Symbol;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.Objects;

public class StoreWord extends Quaternion {
    private String integer;
    private String pointer;
    private String offset;
    public StoreWord(String integer, String pointer, String offset) {
        kind = IRKind.SW;
        this.integer = integer;
        this.pointer = pointer;
        this.offset = offset;
    }

    @Override
    public void print(OutputStreamWriter writer) throws IOException {
        writer.append("storeWord ").append(integer).append(" ").append(pointer).append(" ").append(offset).append("\n");
    }

    @Override
    public HashSet<String> getUse() {
        HashSet<String> use = new HashSet<>();
        use.add(integer);
        use.add(pointer);
        use.add(offset);
        return use;
    }
    public void setUse(String k, String s) {
        if(Objects.equals(integer, k)) integer = s;
        else if(Objects.equals(pointer, k)) pointer = s;
        else if(Objects.equals(offset, k)) offset = s;
    }

    @Override
    public void assembly(OutputStreamWriter writer, Function function) throws IOException {
        if(pointer.charAt(0) == 'a') {
            writer.append("addi $27，$sp, ").append(pointer.substring(5)).append("\n");
            pointer = "$27";
        } else if(pointer.charAt(0) == 'v') {
            writer.append("la $27, ").append(pointer).append("\n");
            pointer = "$27";
        } else if(pointer.charAt(0) == 's') {
            writer.append("lw $27, ").append(pointer.substring(2)).append("($sp)").append("\n");
            pointer = "$27";
        }

        if(Symbol.exprResIsNumber(offset)){
            offset = String.valueOf(Integer.parseInt(offset)*4);
        } else {
            if(offset.charAt(0) != '$') {
                writer.append("lw $28, ").append(offset.substring(2)).append("($sp)").append("\n");
                offset = "$28";
            }
            writer.append("sll $28").append(offset).append(", 2").append("\n");
            writer.append("add $27, $28, ").append(pointer).append("\n");
            pointer = "$27";
            offset = "0";
        }
        if(integer.charAt(0) == '$') {
            writer.append("sw ").append(integer).append(", ").append(offset).append("(").append(pointer).append(")").append("\n");
        } else {
            if(integer.charAt(0) == 's') {
                writer.append("lw $28, ").append(integer.substring(2)).append("($sp)").append("\n");
            } else {
                writer.append("li $28, ").append(integer).append("\n");
            }
            writer.append("sw $28, ").append(offset).append("(").append(pointer).append(")").append("\n");

        }
    }
}
