package Parser;

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
}
