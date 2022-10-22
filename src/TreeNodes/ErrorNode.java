package TreeNodes;

import Parser.ErrorCheckCtx;
import Parser.ErrorCheckRet;
import Parser.Errorkind;
import Tools.Pair;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import static Lexer.SyntaxKind.getPrint;

public class ErrorNode extends Node {
    public Errorkind errorkind;
    public Integer line;

    public ErrorNode(Errorkind errorkind, int line) {
        this.errorkind = errorkind;
        this.line = line;
    }

    @Override
    public void print(OutputStreamWriter writer) throws IOException {
        writer.append('<').append(errorkind.toString()).append('>').append("\n");
    }

    @Override
    public void checkError(ArrayList<Pair<Errorkind, Integer>> errorlist, ErrorCheckCtx ctx, ErrorCheckRet ret) {
        errorlist.add(Pair.of(errorkind,line));
    }
}
