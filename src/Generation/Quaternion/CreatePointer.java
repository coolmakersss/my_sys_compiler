package Generation.Quaternion;

import java.io.IOException;
import java.io.OutputStreamWriter;

public class CreatePointer extends Quaternion {
    private String name;
    private int size;

    public CreatePointer(String name, int size) {
        kind = IRKind.CREATE_POINTER;
        this.name = name;
        this.size = size;
    }

    @Override
    public void print(OutputStreamWriter writer) throws IOException {
        writer.append("createPointer ").append(name).append(" ").append(String.valueOf(size)).append("\n");
    }

    @Override
    public String getDefine() {
        return name;
    }
    public int getSize(){
        return size;
    }
}
