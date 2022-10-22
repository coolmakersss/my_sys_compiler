package TreeNodes;

import Parser.ErrorCheckCtx;
import Parser.ErrorCheckRet;
import Parser.Errorkind;
import Tools.Pair;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import static Lexer.SyntaxKind.getPrint;
import static Lexer.SyntaxKind.isParserToken;

public class AddExpNode extends Node {

    @Override
    public void print(OutputStreamWriter writer) throws IOException {
        for (Node child : children) {
            child.print(writer);
            if(isParserToken(child.kind))
                writer.append('<').append(getPrint(kind)).append('>').append("\n");
        }
        //writer.append('<').append(getPrint(kind)).append('>').append("\n");

    }

    @Override
    public void checkError(ArrayList<Pair<Errorkind, Integer>> errorlist, ErrorCheckCtx ctx, ErrorCheckRet ret) {
        int max = -1;
        for(Node child: children){
            ErrorCheckRet ret1 = new ErrorCheckRet();
            child.checkError(errorlist, ctx, ret1);
            if(ret1.isConst) {
                ret.isConst = true;
            }
            if (max<ret1.dimension){
                max = ret1.dimension;
            }
        }
        ret.dimension = max;
    }
}
