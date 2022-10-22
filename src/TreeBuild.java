import Lexer.SyntaxKind;
import Tools.Pair;
import TreeNodes.ErrorNode;
import TreeNodes.Node;
import TreeNodes.TokenNode;
import Parser.*;

import java.util.Stack;

public class TreeBuild {
    private Pair<SyntaxKind, Integer> Parent;
    private Stack<Pair<SyntaxKind, Integer>> parent = new Stack<>();
    private Stack<Node> children = new Stack<>();

    public void startNode(SyntaxKind kind) {
        parent.push(Pair.of(kind, children.size()));
    }
    void finishNode(Node node) {
        Pair<SyntaxKind, Integer> tmp = parent.peek();
        parent.pop();
        while (children.size() > tmp.second) {
            node.addChild(children.peek());
            children.pop();
        }
        node.setNodeElement(tmp.first);
        children.push(node);
    }

    void startNodeAt(int point, SyntaxKind kind) {
        parent.push(Pair.of(kind, point));
    }

    void terminalSymbol(SyntaxKind kind, String content, int line) {
        children.push(new TokenNode(kind, content, line));
    }

    public void error(Errorkind errorkind, int line) {
        Node node = new ErrorNode(errorkind, line);
        node.setNodeElement(SyntaxKind.ERROR, line);
        if (errorkind != Errorkind.UNDEFINED_ERROR) {
            children.push(node);
            return;
        }
        Pair<SyntaxKind, Integer> tmp = parent.peek();
        parent.pop();
        while (children.size() > tmp.second) {
            node.addChild(children.peek());
            children.pop();
        }
        children.push(node);
    }

    public Node root() {
        if (parent.size() == 0) {
            return children.peek();
        } else {
            System.out.println("Parent is not empty!\n");
            return children.peek();
        }
    }
}
