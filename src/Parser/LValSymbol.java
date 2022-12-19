package Parser;

import Generation.ControlFlowGraphBuilder;
import Generation.Quaternion.Assign;
import Generation.Quaternion.Mult;
import Generation.Quaternion.Plus;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class LValSymbol {
    public boolean isConst;
    public boolean isGlobal;
    public int id;
    public ArrayList<Integer> dimension; // upper limit of each dimension
    public ArrayList<Integer> initVal;

    public LValSymbol(boolean isConst,boolean isGlobal,int id,ArrayList<Integer> dimension,ArrayList<Integer> initVal) {
        this.isConst = isConst;
        this.isGlobal = isGlobal;
        this.id = id;
        this.dimension = dimension;
        this.initVal = initVal;
        System.out.println(initVal);
    }

    public int getId() {
        return id;
    }

    public boolean isConst() {
        return isConst;
    }

    public boolean isGlobal() {
        return isGlobal;
    }

    public int getDimension() {
        return dimension.size();
    }

    public void print(OutputStreamWriter writer) throws IOException {
        int size = 1;
        for (Integer i : dimension) {
            size *= i;
        }
        if (Symbol.IR) {
            writer.append(String.valueOf(size)).append(" ");
            for (Integer i : initVal) {
                writer.append(String.valueOf(i)).append(" ");
            }
        } else {
            writer.append(" : .word ");
            if (initVal.isEmpty()) {
                writer.append(" 0:").append(String.valueOf(size));
            } else {
                for (Integer i : initVal) {
                    writer.append(String.valueOf(i)).append(" ");
                }
            }
        }
    }

    public int getOffset(ArrayList<Integer>  indices) {
        if (indices.isEmpty()) {
            return 0;
        }
        int base = 1;
        int ret = 0;
        for (int i = dimension.size() - 1; i >= indices.size(); i--) {
            base *= dimension.get(i);
        }
        for (int i = indices.size() - 1; i > 0; i--) {
            ret += indices.get(i) * base;
            base *= dimension.get(i);
        }
        ret = ret+indices.get(0)*base;
        return ret;
    }


    public int getVal(ArrayList<Integer> dimension) {
        int offset = getOffset(dimension);
        if(initVal.isEmpty()){
            return 0;
        } else {
            if(offset >= initVal.size()){
                System.out.println("array out of index!!!");
                return 0;
            }
        }
        return initVal.get(offset);
    }

    public String findOffset(ArrayList<String> indices) {
        System.out.println(dimension.size());
        System.out.println(indices.size());
        String ret = ControlFlowGraphBuilder.getCFGB().tmpVar();
        String base = ControlFlowGraphBuilder.getCFGB().tmpVar();
        String tmp;
        ControlFlowGraphBuilder.getCFGB().insert(new Assign(base, "1"));
        for(int i=dimension.size()-1;i>= indices.size();i--){
            tmp = String.valueOf(dimension.get(i));
            ControlFlowGraphBuilder.getCFGB().insert(new Mult(base, base, tmp));
        }
        ControlFlowGraphBuilder.getCFGB().insert(new Assign(ret, "0"));
        for(int i = indices.size()-1;i>=0;i--){
            tmp = ControlFlowGraphBuilder.getCFGB().tmpVar();
            ControlFlowGraphBuilder.getCFGB().insert(new Mult(tmp, indices.get(i), base));
            ControlFlowGraphBuilder.getCFGB().insert(new Plus(ret, ret, tmp));
            tmp = String.valueOf(dimension.get(i));
            ControlFlowGraphBuilder.getCFGB().insert(new Mult(base ,base, tmp));

        }
        return ret;
    }

}
