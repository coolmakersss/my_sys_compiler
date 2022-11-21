package TreeNodes;

import Generation.BuildIRCtx;
import Generation.BuildIRRet;
import Generation.ControlFlowGraphBuilder;
import Generation.Quaternion.PrintInteger;
import Generation.Quaternion.PrintString;
import Lexer.SyntaxKind;
import Parser.*;
import Parser.Errorkind;
import Tools.Pair;

import javax.print.DocFlavor;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class PrintfStmtNode extends Node {

    @Override
    public void print(OutputStreamWriter writer) throws IOException {
        for (Node child : children) {
            child.print(writer);
        }
    }

    @Override
    public void checkError(ArrayList<Pair<Errorkind, Integer>> errorlist, ErrorCheckCtx ctx, ErrorCheckRet ret) {
        int cnt = 0;
        int line = 0;
        String formatString = null;
        for (Node i : children) {
            i.checkError(errorlist,ctx, ret);
            if (i.getKind() == SyntaxKind.EXP) {
                cnt++;
            } else if (i.getKind() == SyntaxKind.STRCON) {
                formatString = ((TokenNode)i).getContent();
                line = i.getFinishLine();
            }
        }
        boolean fsError = false;
        //System.out.println(formatString);
        if(formatString.charAt(formatString.length()-2) == '\\'){
            fsError=true;
        }
        for (int i = 1; i < formatString.length()-1; i++) {
            char c = formatString.charAt(i);
            char last = formatString.charAt(i-1);
            if (c != 32 && c != 33 && c != '%' && (c < 40 || c > 126)) {
                fsError = true;
            } else if (last == '\\' && c != 'n') {
                fsError = true;
            } else if (last == '%' && c != 'd') {
                fsError = true;
            }
            if (last == '%' && c == 'd') {
                cnt--;
            }
        }
        if (cnt != 0) {
            errorlist.add(Pair.of(Errorkind.FORMAT_CHAR_UNMATCHED, this.startLine));
        }
        if (fsError) {
            errorlist.add(Pair.of(Errorkind.INVALID_CHARACTER, line));
        }
    }

    @Override
    public void buildIR(BuildIRCtx ctx, BuildIRRet ret) {
        ArrayList<String> integers = new ArrayList<>();
        String formatString = "";
        for(Node child: children) {
            if(child.getKind() == SyntaxKind.EXP){
                child.buildIR(ctx, ret);
                integers.add(ret.res);
            } else if(child.getKind() == SyntaxKind.STRCON){
                formatString = ((TokenNode)child).getContent();
            }
        }
        String now = "";
        for (int i=1,j=0;i<formatString.length()-1;i++){
            char c = formatString.charAt(i);
            if(c == '%' && formatString.charAt(i+1) =='d') {
                String label = Symbol.generateStr();
                Symbol.getSymbol().addConstStr(label, now);
                if(!now.isEmpty()) {
                    now = "";
                    ControlFlowGraphBuilder.getCFGB().insert(new PrintString(label));
                }
                ControlFlowGraphBuilder.getCFGB().insert(new PrintInteger(integers.get(j)));
                j++;i++;
            } else {
                now+=c;
            }
        }
        String label = Symbol.generateStr();
        Symbol.getSymbol().addConstStr(label, now);
        if(!now.isEmpty()) {
            ControlFlowGraphBuilder.getCFGB().insert(new PrintString(label));
        }
    }

}
