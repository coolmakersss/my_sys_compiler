package Generation;

import Generation.Quaternion.Quaternion;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.HashSet;

public class Function {
    String tag;
    int memory;
    HashMap<String, Integer> variable2Register;
    HashMap<String, Integer> variableInMemory;
    HashMap<String, Integer> arrayInMemory;
    HashMap<String, Integer> variableWeight;
    BasicBlock head;
    BasicBlock tail;
    HashMap<String, Quaternion> defPlace;
    HashMap<String, HashSet<Quaternion>> usePlace;

    HashMap<Integer, Integer> registerInMemory;
    int paramNumber;

    public Function(String funcName, int size) {
        paramNumber = size;
        tag = funcName;
        memory = 0;
        head = null;
        tail = null;
        variable2Register = new HashMap<>();
        variableInMemory = new HashMap<>();
        registerInMemory = new HashMap<>();
    }

    public void addBasicBlock(BasicBlock newBB) {
        if(head == null){
            head = newBB;
            tail = newBB;
        } else {
            tail.nextBB = newBB;
            newBB.lastBB = tail;
            tail = newBB;
        }
    }

    public void print(OutputStreamWriter writer) throws IOException {
        writer.append(tag).append(" :").append("\n");
        for(BasicBlock i=head ; i!=null ; i = i.nextBB) {
            i.print(writer);
        }
    }

    public void assembly(OutputStreamWriter writer) throws IOException {
        for (BasicBlock i = head; i!=null; i = i.nextBB) {
            i.rename(variable2Register, variableInMemory, arrayInMemory);
        }
        writer.append(tag).append(":").append("\n");
        if(paramNumber>4) memory += paramNumber*4-16;
        writer.append("subi $sp, $sp, ").append(String.valueOf(memory)).append("\n");
        for(int i: registerInMemory.keySet()){
            writer.append("sw $").append(String.valueOf(i)).append(", ").append(String.valueOf(registerInMemory.get(i))).append("($sp)").append("\n");
        }
        for (BasicBlock i = head;i!=null;i=i.nextBB){
            i.assembly(writer,this);
        }
    }

    public void allocateReg() {
        for(BasicBlock i = head;i!=null;i=i.nextBB){
            for(Quaternion j = i.head;j!=null;j=j.next){
                String define = j.getDefine();
                if(define!=null) {System.out.println(define);
                    variableInMemory.put(define,memory);
                    memory += 4;
                }
            }
        }
        registerInMemory.put(31,memory);
        memory += 4;
    }

    public int getMemory() {
        return memory;
    }

    public void functionReturn(OutputStreamWriter writer) throws IOException {
        for(int i: registerInMemory.keySet()){
            writer.append("lw $").append(String.valueOf(i)).append(", ").append(String.valueOf(registerInMemory.get(i))).append("($sp)").append("\n");
        }
        writer.append("addi $sp, $sp, ").append(String.valueOf(memory)).append("\n");
        writer.append("jr $31\n");
    }
}
