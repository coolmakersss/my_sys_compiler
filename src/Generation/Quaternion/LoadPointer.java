package Generation.Quaternion;

import Generation.Function;
import Parser.Symbol;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.Objects;

public class LoadPointer extends Quaternion {
    String assigned;
    String pointer;
    String offset;

    public LoadPointer(String res, String varName, String offset) {
        assigned = res;
        pointer = varName;
        this.offset = offset;
    }

    @Override
    public void print(OutputStreamWriter writer) throws IOException {
        writer.append("loadPointer ").append(assigned).append(" ").append(pointer).append(" ").append(offset).append("\n");
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
        use.add(pointer);
        use.add(offset);
        return use;
    }

    @Override
    public void setUse(String k, String s) {
        if(Objects.equals(k, pointer)) pointer = s;
        if(Objects.equals(k, offset)) offset = s;
    }

    @Override
    public void assembly(OutputStreamWriter writer, Function function) throws IOException {
        if(pointer.charAt(0) == 'a') {
            writer.append("addi $27, $sp, ").append(pointer.substring(5)).append("\n");
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
            writer.append("sll $28, ").append(offset).append(", 2").append("\n");
            offset = "$28";
        }
        if(assigned.charAt(0) == '$') {
            writer.append("add ").append(assigned).append(", ").append(pointer).append(", ").append(offset).append("\n");
        } else {
            writer.append("add $28, ").append(pointer).append(", ").append(offset).append("\n");
            writer.append("sw $28, ").append(assigned.substring(2)).append("($sp)").append("\n");
        }

    }
}
