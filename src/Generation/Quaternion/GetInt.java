package Generation.Quaternion;

import Generation.Function;

import java.io.IOException;
import java.io.OutputStreamWriter;

public class GetInt extends Quaternion {
    String assigned;
    public GetInt(String assigned) {
        this.assigned = assigned;
        kind = IRKind.GETINT;
    }

    @Override
    public void print(OutputStreamWriter writer) throws IOException {
        writer.append("getInt ").append(assigned).append("\n");
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
    public void assembly(OutputStreamWriter writer, Function function) throws IOException {
        if(assigned.charAt(0) == '$'){
            writer.append("move ").append(assigned).append(", $v0");
        } else {
            writer.append("sw $v0, ").append(assigned.substring(2)).append("($sp)").append("\n");
        }
    }
}
