package Lexer;

public class Cursor {
    private Source source;
    private static int begin;
    private static int end;
    private int now;

    public Cursor(Source source) {
        this.source = source;
        begin = 0;
        end = source.sources.length();
        now = begin;
    }

    public String previous() {
        if (now == begin) {
            return null;
        } else {
            return source.sources.substring(now - 1, now);
        }
    }

    public String n_char(int n) {
        if (now + n >= end) {
            return null;
        } else {
            return source.sources.substring(now + n, now + n + 1);
        }
    }

    public String first() {
        return n_char(0);
    }

    public boolean isEOF() {
        return now >= end;
    }
    public void bump() {
        if (now < end) {
            now++;
        }
    }

    public static int getBegin() {
        return begin;
    }

    public static int getEnd() {
        return end;
    }

    public int getNow() {
        return now;
    }

}
