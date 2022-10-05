package Lexer;

import java.util.ArrayList;
import java.util.Objects;

public class Lexer {
    private Cursor cursor;
    private Source source;


    public Lexer(Source source) {
        cursor = new Cursor(source);
        this.source = source;
    }

    public SyntaxKind tokenKind() {
        String first = cursor.first();
        if (first == null) {
            return SyntaxKind.EOF;
        }
        cursor.bump();
        switch (first) {
            case "/":
                if(Objects.equals(cursor.first(), "/")){
                    cutLineComment();
                    return SyntaxKind.LC;
                } else if(Objects.equals(cursor.first(), "*")){
                    cutBlockComment();
                    return SyntaxKind.BC;
                }
                return SyntaxKind.DIV;
            case "+":
                return SyntaxKind.PLUS;
            case "-":
                return SyntaxKind.MINU;
            case "*":
                return SyntaxKind.MULT;
            case "%":
                return SyntaxKind.MOD;
            case ";":
                return SyntaxKind.SEMICN;
            case ",":
                return SyntaxKind.COMMA;
            case "(":
                return SyntaxKind.LPARENT;
            case ")":
                return SyntaxKind.RPARENT;
            case "[":
                return SyntaxKind.LBRACK;
            case "]":
                return SyntaxKind.RBRACK;
            case "{":
                return SyntaxKind.LBRACE;
            case "}":
                return SyntaxKind.RBRACE;
            case "&":
                if (Objects.equals(cursor.first(), "&")) {
                    cursor.bump();
                    return SyntaxKind.AND;
                }
                return SyntaxKind.ERROR;
            case "|":
                if (Objects.equals(cursor.first(), "|")) {
                    cursor.bump();
                    return SyntaxKind.OR;
                }
                return SyntaxKind.ERROR;
            case "!":
                if (Objects.equals(cursor.first(), "=")) {
                    cursor.bump();
                    return SyntaxKind.NEQ;
                } else {
                    return SyntaxKind.NOT;
                }
            case "=":
                if (Objects.equals(cursor.first(), "=")) {
                    cursor.bump();
                    return SyntaxKind.EQL;
                } else {
                    return SyntaxKind.ASSIGN;
                }
            case ">":
                if (Objects.equals(cursor.first(), "=")) {
                    cursor.bump();
                    return SyntaxKind.GEQ;
                } else {
                    return SyntaxKind.GRE;
                }
            case "<":
                if (Objects.equals(cursor.first(), "=")) {
                    cursor.bump();
                    return SyntaxKind.LEQ;
                } else {
                    return SyntaxKind.LSS;
                }
            case "\"":
                cutFormatString();
                return SyntaxKind.STRCON;
            default:
                if(Character.isDigit(first.charAt(0))){
                    cutDigit();
                    return SyntaxKind.INTCON;
                }
                if (isIdentStart(first)) {
                    String ident="";
                    ident += first;
                    while (isIdentMid(cursor.first())) {
                        ident += cursor.first();
                        cursor.bump();
                    }
                    return SyntaxKind.judgeIdent(ident);
                } else {
                    return SyntaxKind.ERROR;
                }
        }
    }

    private void cutBlockComment() {
        cursor.bump();
        cursor.bump();
        while (!cursor.isEOF()) {
            if(Objects.equals(cursor.first(), "/") && Objects.equals(cursor.previous(), "*")){
                cursor.bump();
                break;
            } else {
                cursor.bump();
            }
        }
    }

    private void cutLineComment() {
        while (true) {
            String c = cursor.first();
            if(!cursor.isEOF() && c.charAt(0) != '\n'){
                cursor.bump();
            } else {
                return;
            }
        }
    }


    public Token nextToken() {
        cutSpace();
        //System.out.println(cursor.getNow());
        int start = cursor.getNow();
        SyntaxKind kind = tokenKind();
        int length = cursor.getNow() - start;
        return new Token(kind, start, length, source);
    }

    public ArrayList<Token> tokenize() {
        ArrayList<Token> result = new ArrayList<>();
        Token token = nextToken();
        while(!token.isEOF()){
            if(token.isError()){
                System.out.println("ERROR!");
            } else {
                result.add(token);
            }
            token = nextToken();
        }
        result.add(token);
        return result;
    }

    public void cutFormatString() {
        boolean afterEscapechar = false;
        while (true) {
            String c = cursor.first();
            switch (c) {
                case "\"" :
                    if(afterEscapechar){
                        cursor.bump();
                        afterEscapechar=false;
                    } else{
                        cursor.bump();
                        return;
                    }
                    break;
                case"\\":
                    if(afterEscapechar){
                        cursor.bump();
                        afterEscapechar=false;
                    } else{
                        cursor.bump();
                        afterEscapechar=true;
                    }
                default:
                    afterEscapechar = false;
                    cursor.bump();
            }
        }
    }

    private void cutDigit() {
        while (true) {
            String c = cursor.first();
            if(!cursor.isEOF() && Character.isDigit(c.charAt(0))){
                cursor.bump();
            } else {
                return;
            }
        }
    }
    private void cutSpace() {
        while (true) {
            String c = cursor.first();
            if(!cursor.isEOF() && Character.isWhitespace(c.charAt(0))){
                cursor.bump();
            } else {
                return;
            }
        }
    }

    private boolean isIdentStart(String first) {
        return Character.isLetter(first.charAt(0)) || Objects.equals(first, "_");
    }
    private boolean isIdentMid(String first) {
        return isIdentStart(first) || Character.isDigit(first.charAt(0));
    }

}
