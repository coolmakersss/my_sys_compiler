package Lexer;

import java.util.ArrayList;

public class TokenSource {
    private ArrayList<Token> tokens;
    private int cur;


    public TokenSource(ArrayList<Token> tokens) {
        this.tokens = tokens;
        cur = 0;
    }

    public Token ntoken(int n) {
        if (cur + n >= tokens.size()) return null;
        else return tokens.get(cur + n);
    }

    public Token first() {
        return ntoken(0);
    }

    public int lastLinie() {
        if (cur == 0) return 0;
        else return ntoken(-1).line;
    }

    public void bump() {
        if(cur< tokens.size()){
            cur++;
        }
    }

}
