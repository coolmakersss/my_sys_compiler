package TreeNodes;

import Generation.BuildIRCtx;
import Generation.BuildIRRet;
import Lexer.SyntaxKind;
import Parser.*;
import Parser.ErrorCheckRet;
import Parser.Errorkind;
import Tools.Pair;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;



public class CompUnitNode extends Node{

    @Override
    public void checkError(ArrayList<Pair<Errorkind, Integer>> errorlist, ErrorCheckCtx ctx, ErrorCheckRet ret) {
        Symbol.getSymbol().startBlock();
        for (Node child : children) {
            child.checkError(errorlist, ctx, ret);
        }
        Symbol.getSymbol().endBlock();
        Symbol.getSymbol().clear();
    }

    @Override
    public void buildIR(BuildIRCtx ctx, BuildIRRet ret) {
        Symbol.getSymbol().startBlock();
        ctx.isGlobal = true;
        for (Node child: children) {
            if (child.getKind() == SyntaxKind.FUNC_DEF || child.getKind() == SyntaxKind.MAINFUNC_DEF) {
                ctx.isGlobal = false;
            }
            child.buildIR(ctx, ret);
        }

        ///Symbol.getSymbol().endBlock();
    }

}
