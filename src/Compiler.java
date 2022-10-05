import Lexer.*;
import TreeNodes.Node;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class Compiler {
    public static void main(String[] args) throws Exception {
        String testfile = "testfile.txt";
        Source source = new Source(testfile);
        Lexer lexer = new Lexer(source);
        ArrayList<Token> tokens = lexer.tokenize();
        tokens.removeIf(token -> token.kind == SyntaxKind.BC);
        tokens.removeIf(token -> token.kind == SyntaxKind.LC);
        /*for (Token token : tokens) {
            System.out.println(token.kind + " " + token.content);
        }*/
        TokenSource tokenSource = new TokenSource(tokens);
        Parser parser = new Parser(tokenSource);
        Node root = parser.parse();
        OutputStream fop = new FileOutputStream("output.txt");
        OutputStreamWriter writer = new OutputStreamWriter(fop, "UTF-8");
        /*for (Token token : tokens) {
            if(token.kind!= SyntaxKind.LC && token.kind!= SyntaxKind.BC)
            writer.append(String.valueOf(token.kind)).append(" ").append(token.content).append("\n");
        }*/
        root.print(writer);


        writer.close();
        fop.close();
    }
}
