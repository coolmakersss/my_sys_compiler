package Generation.Quaternion;

import Parser.Symbol;

import java.util.HashSet;
import java.util.Objects;

public class Binary extends Quaternion{
    String integer1;
    String integer2;
    String assigned;
    public Binary(){
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
        if(!Symbol.exprResIsNumber(integer1)){
            use.add(integer1);
        }
        if(!Symbol.exprResIsNumber(integer2)){
            use.add(integer2);
        }
        return use;
    }
    @Override
    public void setUse(String k, String s) {
        if(Objects.equals(integer1, k)) integer1= s;
        if(Objects.equals(integer2, k)) integer2 = s;
    }

}
