package Generation.Quaternion;

import Generation.Function;
import Parser.Symbol;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashSet;

public class Assign extends Quaternion {
    private String assigned;
    private String integer;

    public Assign(String assigned , String integer) {
        kind = IRKind.ASSIGN;
        this.assigned = assigned;
        this.integer = integer;
    }

    @Override
    public void print(OutputStreamWriter writer) throws IOException {
        writer.append("assign ").append(assigned).append(" ").append(integer).append("\n");
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
        if(integer.charAt(0) == 's'){
            writer.append("lw $27, ").append(integer.substring(2)).append("($sp)").append("\n");
            integer = "$27";
        }
        if(assigned.charAt(0)=='$'){
            if(Symbol.exprResIsNumber(integer)){
                writer.append("li ").append(assigned).append(", ").append(integer).append("\n");
            } else {
                writer.append("move ").append(assigned).append(", ").append(integer).append("\n");
            }
        } else {
            if(Symbol.exprResIsNumber(integer)) {
                writer.append("li $27, ").append(integer).append("\n");
                integer = "$27";
            }
            writer.append("sw ").append(integer).append(", ").append(assigned.substring(2)).append("($sp)").append("\n");
        }
    }
}
