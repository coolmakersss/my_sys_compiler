import Lexer.*;
import Parser.*;
import Tools.Pair;
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
        tokens.removeIf(token -> token.kind == SyntaxKind.BC); //移除注释行
        tokens.removeIf(token -> token.kind == SyntaxKind.LC); //移除注释块
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


        ArrayList<Pair<Errorkind,Integer>> errorlist = new ArrayList<>();
        root.checkError(errorlist, new ErrorCheckCtx(),new ErrorCheckRet());

        OutputStream fop1 = new FileOutputStream("error.txt");
        OutputStreamWriter writer1 = new OutputStreamWriter(fop1, "UTF-8");
        for(Pair<Errorkind,Integer> i:errorlist){
            writer1.append(String.valueOf(i.second)).append(" ").append(Errorkind.error2kind(i.first)).append("\n");
            writer.append(String.valueOf(i.second)).append(" ").append(Errorkind.error2kind(i.first)).append("\n");
        }
        writer1.close();
        fop1.close();
        writer.close();
        fop.close();


    }
}
