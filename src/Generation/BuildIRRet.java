package Generation;

import java.util.ArrayList;

public class BuildIRRet {

    public String res; // stored in exprNode
    public String param;
    public ArrayList<String> args;
    public ArrayList<String> init;
    public boolean isArray;
    public BuildIRRet(){
        isArray = false;
        args = new ArrayList<>();
        init = new ArrayList<>();
    }

    public void clear() {
        param = null;
        args.clear();
        init.clear();
    }
}
