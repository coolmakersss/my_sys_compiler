import Lexer.SyntaxKind;
import TreeNodes.Node;
import TreeNodes.TokenNode;

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

    public Node root() {
        if (parent.size() == 0) {
            return children.peek();
        } else {
            System.out.println("Parent is not empty!\n");
            return children.peek();
        }
    }
}
