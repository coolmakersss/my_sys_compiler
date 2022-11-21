package Parser;

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


    //??未支持数组
    public int getVal(ArrayList<Integer> dimension) {
        if(initVal.isEmpty()){
            return 0;
        }
        return initVal.get(0);
    }
}
