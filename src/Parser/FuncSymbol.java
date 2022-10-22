package Parser;

import java.util.ArrayList;
import java.util.Objects;

import Parser.*;

public class FuncSymbol {
    private final String name;
    private final ArrayList<Integer> params;// dimension of each param
    private final boolean isVoid;

    public FuncSymbol(String name,ArrayList<Integer> params,boolean isVoid) {
        this.isVoid=isVoid;
        this.name = name;
        this.params = params;
    }
    public Errorkind matchParams(ArrayList<Integer> in){
        if(in.size()!=params.size()){
            return Errorkind.PARAMS_NUM_UNMATCHED;
        }
        //System.out.println(in);System.out.println(params);
        for(int i=0;i<in.size();i++){
            if(!Objects.equals(in.get(i), params.get(i))){
                //System.out.println(in);System.out.println(params);
                return Errorkind.PARAM_TYPE_UNMATCHED;
            }
        }
        return null;
    }

    public boolean isVoid() {
        return isVoid;
    }

    public String getName() {
        return name;
    }
}
