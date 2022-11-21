package Generation.Quaternion;

import Generation.BasicBlock;
import Generation.Function;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashSet;

public class Quaternion {
    protected IRKind kind;

    public Quaternion next;
    public Quaternion last;
    public BasicBlock belong;

    public String getDefine(){
        return null;
    }
    public void print(OutputStreamWriter writer) throws IOException {}

    public void assembly(OutputStreamWriter writer, Function function) throws IOException {}


    public void setDefine(String s) {}

    public HashSet<String> getUse() {return new HashSet<>();}

    public void setUse(String k, String s) {}
}
