package TreeNodes;

import java.io.IOException;
import java.io.OutputStreamWriter;

public class IfStmtNode extends Node {

    @Override
    public void print(OutputStreamWriter writer) throws IOException {
        for (Node child : children) {
            child.print(writer);
        }
    }
}
