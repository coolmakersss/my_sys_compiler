package TreeNodes;

import java.io.IOException;
import java.io.OutputStreamWriter;

import static Lexer.SyntaxKind.getPrint;
import static Lexer.SyntaxKind.isParserToken;

public class LandExpNode extends Node {

    @Override
    public void print(OutputStreamWriter writer) throws IOException {
        for (Node child : children) {
            child.print(writer);
            if(isParserToken(child.kind))
                writer.append('<').append(getPrint(kind)).append('>').append("\n");
        }
        //writer.append('<').append(getPrint(kind)).append('>').append("\n");

    }
}
