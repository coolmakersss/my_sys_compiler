package TreeNodes;
import Generation.BuildIRCtx;
import Generation.BuildIRRet;
import Lexer.SyntaxKind;
import Parser.ErrorCheckCtx;
import Parser.ErrorCheckRet;
import Parser.Errorkind;
import Tools.Pair;

import static Lexer.SyntaxKind.getPrint;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;



public class Node {
    protected int startLine;
    protected int finishLine;
    protected SyntaxKind kind;
    protected ArrayList<Node> children = new ArrayList<>();

    public void setNodeElement(SyntaxKind syntaxKind, int line) {
        this.kind = syntaxKind;
        this.startLine = this.finishLine = line;
    }

    public void setNodeElement(SyntaxKind syntaxKind) {
        this.kind = syntaxKind;
        this.startLine = children.get(0).startLine;
        this.finishLine = children.get(children.size() - 1).finishLine;
    }

    public void print(OutputStreamWriter writer) throws IOException {
        for (Node child : children) {
            child.print(writer);
        }
        writer.append('<').append(getPrint(kind)).append('>').append("\n");
    }
    public void checkError(ArrayList<Pair<Errorkind, Integer>> errorlist, ErrorCheckCtx ctx, ErrorCheckRet ret) {
        for (Node child : children) {
            child.checkError(errorlist, ctx, ret);
        }
    }
    public void buildIR(BuildIRCtx ctx, BuildIRRet ret) {
        for (Node child : children) {
            child.buildIR(ctx, ret);
        }
    }

    public int getStartLine() {
        return startLine;
    }

    public int getFinishLine() {
        return finishLine;
    }

    public void addChild(Node child) {
        children.add(0, child);
    }

    public SyntaxKind getKind() {
        return kind;
    }

}
