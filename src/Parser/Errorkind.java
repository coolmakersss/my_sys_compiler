package Parser;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Errorkind {
    INVALID_CHARACTER,
    REDEFINE_IDENT,
    UNDEFINED_IDENT,
    PARAMS_NUM_UNMATCHED,
    PARAM_TYPE_UNMATCHED,
    VOID_FUNC_RETURN_INTEGER,
    INT_FUNC_RETURN_LACKED,
    CONSTANT_ASSIGNED,
    SEMICOLON_LACKED,
    R_PAREN_LACKED,
    R_BRACKET_LACKED,
    FORMAT_CHAR_UNMATCHED,
    BREAK_CONTINUE_OUT_LOOP,
    UNDEFINED_ERROR,
    CORRECT;

    public static String error2kind(Errorkind errorkind) {
        Map<Errorkind, String> error2kind = Stream.of(new Object[][] {
                {Errorkind.INVALID_CHARACTER, "a"},
                {REDEFINE_IDENT,"b"},
                {UNDEFINED_IDENT, "c"},
                {PARAMS_NUM_UNMATCHED, "d"},
                {PARAM_TYPE_UNMATCHED, "e"},
                {VOID_FUNC_RETURN_INTEGER, "f"},
                {INT_FUNC_RETURN_LACKED, "g"},
                {CONSTANT_ASSIGNED, "h"},
                {SEMICOLON_LACKED, "i"},
                {R_PAREN_LACKED, "j"},
                {R_BRACKET_LACKED, "k"},
                {FORMAT_CHAR_UNMATCHED, "l"},
                {BREAK_CONTINUE_OUT_LOOP, "m"},
                {UNDEFINED_ERROR, "n"}
        }).collect(Collectors.toMap(data -> (Errorkind) data[0], data -> (String) data[1]));
        return error2kind.get(errorkind);
    }
}
