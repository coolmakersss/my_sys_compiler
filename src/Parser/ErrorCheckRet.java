package Parser;

import java.util.ArrayList;

public class ErrorCheckRet {
    public int val;
    public int dimension; // defined in params, calculated in expression
    public boolean isConst, isReturn, isVoid;
    public ArrayList<Integer> args = new ArrayList<>(); // dimensions of each param and argument

    public ErrorCheckRet() {
        val = 0;
        dimension = 0;
        isConst = false;
        isReturn = false;
        isVoid = false;
    }
}
