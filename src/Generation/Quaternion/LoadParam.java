package Generation.Quaternion;

import Generation.Function;

import java.io.IOException;
import java.io.OutputStreamWriter;

public class LoadParam extends Quaternion {
    String integer;
    int num;
    public LoadParam(String s, int num) {
        kind = IRKind.LOAD_PARAM;
        this.integer = s;
        this.num = num;
    }

    @Override
    public void print(OutputStreamWriter writer) throws IOException {
        writer.append("pop ").append(integer).append(" ").append(String.valueOf(num)).append("\n");
    }

    @Override
    public String getDefine() {
        return integer;
    }

    @Override
    public void setDefine(String s) {
        integer = s;
    }

    @Override
    public void assembly(OutputStreamWriter writer, Function function) throws IOException {
        if(num < 4){
            if(integer.charAt(0) == '$'){
                writer.append("move ").append(integer).append(", $a").append(String.valueOf(num)).append("\n");
            } else {
                writer.append("sw $a").append(String.valueOf(num)).append(", ").append(integer.substring(2)).append("($sp)").append("\n");
            }
        } else {
            if(integer.charAt(0) == '$'){
                writer.append("lw ").append(integer).append(", ").append(String.valueOf(function.getMemory()-4*(num-3))).append("($sp)").append("\n");
            } else {
                writer.append("lw $27, ").append(String.valueOf(function.getMemory()-4*(num-3))).append("($sp)").append("\n");
                writer.append("sw $27, ").append(integer.substring(2)).append("($sp)").append("\n");
            }
        }
    }
}
