package Lexer;

public class Token {
    public SyntaxKind kind;
    public String content;
    public int line;

    public Token(SyntaxKind kind, int start, int len, Source source) {
        this.kind = kind;
        line = source.getLine(start);
        content = "";
        for (int i = 0; i < len; i++) {
            content += source.sources.charAt(start + i);
        }
    }
    public boolean isEOF(){
        return kind == SyntaxKind.EOF;
    }

    public boolean isError(){
        return kind == SyntaxKind.ERROR;
    }
}
