package Generation.Quaternion;

import Generation.BasicBlock;
import Generation.Function;
import Parser.Symbol;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashSet;

public class JumpIfFalse extends Quaternion {
    BasicBlock basicBlock;
    String ans;
    public JumpIfFalse(BasicBlock finalBB, String res) {
        basicBlock = finalBB;
        ans = res;
    }

    @Override
    public void print(OutputStreamWriter writer) throws IOException {
        writer.append("JumpIfFalse ").append(basicBlock.getTag()).append(" ").append(ans).append("\n");
    }

    @Override
    public void assembly(OutputStreamWriter writer, Function function) throws IOException {
        if(Symbol.exprResIsNumber(ans)){
            if(Integer.parseInt(ans) == 0){
                writer.append("j ").append(basicBlock.getTag()).append("\n");
            }
        } else if(ans.charAt(0)=='$'){
            writer.append("beqz ").append(ans).append(", ").append(basicBlock.getTag()).append("\n");
        } else {
            writer.append("lw $27, ").append(ans.substring(2)).append("($sp)").append("\n");
            ans = "$27";
            writer.append("beqz ").append(ans).append(", ").append(basicBlock.getTag()).append("\n");
        }
    }

    @Override
    public HashSet<String> getUse() {
        HashSet<String> use = new HashSet<>();
        use.add(ans);
        return use;
    }

    @Override
    public void setUse(String k, String s) {
        ans = s;
    }
}
