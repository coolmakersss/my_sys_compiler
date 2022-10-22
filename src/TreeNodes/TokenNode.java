package TreeNodes;

import Lexer.SyntaxKind;

import java.io.IOException;
import java.io.OutputStreamWriter;

public class TokenNode extends Node {
    String content;

    public TokenNode(SyntaxKind kind, String content, int line) {
        setNodeElement(kind,line);
        this.content = content;
    }

    @Override
    public void print(OutputStreamWriter writer) throws IOException {
        writer.append(String.valueOf(kind)).append(" ").append(content).append("\n");
    }

    public String getContent(){
        return content;
    }
}
