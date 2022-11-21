package Generation.Quaternion;

import Generation.Function;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;

public class PushParam extends Quaternion {
    String integer;
    int num;
    public PushParam(String param, int num) {
        kind = IRKind.PUSH_PARAM;
        this.integer = param;
        this.num = num;
    }

    @Override
    public void print(OutputStreamWriter writer) throws IOException {
        writer.append("push ").append(integer).append(" ").append(String.valueOf(num)).append("\n");
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
        if(num<4){
            if(integer.charAt(0) == '$'){
                writer.append("move $a").append(String.valueOf(num)).append(", ").append(integer).append("\n");
            } else if(integer.charAt(0) == 's'){
                writer.append("lw $a").append(String.valueOf(num)).append(", ").append(integer.substring(2)).append("($sp)").append("\n");
            } else if(integer.charAt(0) == 'a'){
                writer.append("addi $a").append(String.valueOf(num)).append(", $sp, ").append(integer.substring(5)).append("\n");
            } else {
                writer.append("li $a").append(String.valueOf(num)).append(", ").append(integer).append("\n");
            }
        } else {
            if(integer.charAt(0) == '$') {
                writer.append("sw ").append(integer).append(", ")
                        .append(String.valueOf(-4*(num-3))).append("($sp)").append("\n");
            } else {
                if(integer.charAt(0) == 's'){
                    writer.append("lw $27, ").append(integer.substring(2)).append("($sp)");
                } else if(integer.charAt(0) == 'a') {
                    writer.append("addi $27").append(", $sp, ").append(integer.substring(5));
                } else {
                    writer.append("li $27, ").append(integer).append("\n");
                }
                writer.append("sw $27, ").append(String.valueOf(-4*(num-3))).append("($sp)").append("\n");
            }
        }
    }
}
