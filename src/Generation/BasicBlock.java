package Generation;

import Generation.Quaternion.PhiFunction;
import Generation.Quaternion.Quaternion;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class BasicBlock {
    private String tag;
    Quaternion head;
    Quaternion tail;
    HashSet<BasicBlock> predecessor;
    HashSet<BasicBlock> successor;
    HashSet<BasicBlock> dominator;
    BasicBlock immediateDominator;
    private ArrayList<BasicBlock> dominateChildren;
    HashMap<String, PhiFunction> phiNode;
    HashSet<String> activenessIn;
    HashSet<String> activenessOut;
    HashSet<String> variableDefine;
    HashSet<String> variableUse;
    int dominateSize;
    int dfsNumber;

    public BasicBlock nextBB;
    public BasicBlock lastBB;


    public BasicBlock(int id) {
        tag = "label_"+id;
        head = null;
        tail = null;
        dominateSize = 0;
        dfsNumber = 0;
        nextBB = null;
        lastBB = null;
        immediateDominator = null;
        predecessor = new HashSet<>();
        successor = new HashSet<>();

    }
    public void insert(Quaternion quaternion) {
        quaternion.belong = this;
        if (tail == null) {
            head = tail = quaternion;
        } else {
            quaternion.last = tail;
            tail.next = quaternion;
            tail = quaternion;
        }
    }

    public void print(OutputStreamWriter writer) throws IOException {
        writer.append(tag).append(" : ").append("\n");
        for(Quaternion i=head;i!=null;i=i.next){
            i.print(writer);
        }
    }

    public void assembly(OutputStreamWriter writer, Function function) throws IOException {
        writer.append(tag).append(":").append("\n");
        for (Quaternion i = head; i!=null; i = i.next) {
            writer.append("# ");
            i.print(writer);
            i.assembly(writer, function);
        }
    }

    public void rename(HashMap<String, Integer> variable2Register, HashMap<String, Integer> variableInMemory, HashMap<String, Integer> arrayInMemory) {
        for(Quaternion i = head;i!=null;i=i.next){
            String define = i.getDefine();
            if(define!=null) {
                if(variableInMemory.containsKey(define)){
                    i.setDefine("sp"+ variableInMemory.get(define));
                } else if(arrayInMemory.containsKey(define)) {
                    i.setDefine("array"+ arrayInMemory.get(define));
                }
            }

            HashSet<String> use = i.getUse();
            for(String k:use){
                if(variableInMemory.containsKey(k)){
                    i.setUse(k,"sp"+ variableInMemory.get(k));
                } else if(arrayInMemory.containsKey(k)) {
                    i.setUse(k,"array"+ arrayInMemory.get(k));
                }
            }
        }
    }

    public String getTag() {
        return tag;
    }
}
