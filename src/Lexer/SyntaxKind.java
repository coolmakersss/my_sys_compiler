package Lexer;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum SyntaxKind {
    IDENFR,               NOT,
    MULT,                 ASSIGN,
    INTCON,               AND,
    DIV,                  SEMICN,
    STRCON,               OR,
    MOD,                  COMMA,
    MAINTK,               WHILETK,
    LSS,                  LPARENT,
    CONSTTK,              GETINTTK,
    LEQ,                  RPARENT,
    INTTK,                PRINTFTK,
    GRE,
    LBRACK,
    BREAKTK,
    RETURNTK,
    GEQ,
    RBRACK,
    CONTINUETK,
    PLUS,
    EQL,
    LBRACE,
    IFTK,
    MINU,
    NEQ,
    RBRACE,
    ELSETK,
    VOIDTK,

    LC,BC,EOF,ERROR,

    //parser
    COMP_UNIT, FUNC_TYPE, FUNC_DEF, MAINFUNC_DEF, FUNC_F_PARAMS, FUNC_F_PARAM, BLOCK, DECL, STMT,
    VAR_DECL, CONST_DECL, VAR_DEF, CONST_DEF, INIT_VAL, CONST_INIT_VAL, EXP, CONST_EXP, ADD_EXP,
    UNARY_EXP, MUL_EXP, UNARY_OP, FUNC_R_PARAMS, PRIMARY_EXP, NUMBER, LVAL, COND, LOR_EXP, LAND_EXP, EQ_EXP, REAL_EXP;



    public static SyntaxKind judgeIdent(String ident) {
        Map<String, SyntaxKind> string2KeyWord = Stream.of(new Object[][] {
                {"main", SyntaxKind.MAINTK},
                {"const", SyntaxKind.CONSTTK},
                {"int", SyntaxKind.INTTK},
                {"break", SyntaxKind.BREAKTK},
                {"continue", SyntaxKind.CONTINUETK},
                {"if", SyntaxKind.IFTK},
                {"else", SyntaxKind.ELSETK},
                {"while", SyntaxKind.WHILETK},
                {"getint", SyntaxKind.GETINTTK},
                {"printf", SyntaxKind.PRINTFTK},
                {"return", SyntaxKind.RETURNTK},
                {"void", SyntaxKind.VOIDTK}
        }).collect(Collectors.toMap(data -> (String) data[0], data -> (SyntaxKind) data[1]));
        if (string2KeyWord.containsKey(ident)) {
            return string2KeyWord.get(ident);
        }
        return SyntaxKind.IDENFR;
    }
    public static boolean expFirst(SyntaxKind kind) {
        HashSet<SyntaxKind> exprFirst = new HashSet<>(Arrays.asList(
                SyntaxKind.IDENFR,
                SyntaxKind.LPARENT,
                SyntaxKind.INTCON,
                SyntaxKind.PLUS,
                SyntaxKind.MINU,
                SyntaxKind.NOT ));
        return exprFirst.contains(kind);
    }

    public static String getPrint(SyntaxKind kind) {
        Map<SyntaxKind,String> KeyWord2String = Stream.of(new Object[][] {
                {SyntaxKind.COMP_UNIT, "CompUnit"},
                {SyntaxKind.FUNC_TYPE,"FuncType"},
                {SyntaxKind.FUNC_DEF,"FuncDef"},
                {SyntaxKind.MAINFUNC_DEF ,"MainFuncDef"},
                {SyntaxKind.FUNC_F_PARAMS ,"FuncFParams"},
                {SyntaxKind.FUNC_F_PARAM ,"FuncFParam"},
                {SyntaxKind.BLOCK ,"Block"},
                {SyntaxKind.DECL ,"Decl"},
                {SyntaxKind.STMT, "Stmt"},
                {SyntaxKind.VAR_DECL ,"VarDecl"},
                {SyntaxKind.CONST_DECL ,"ConstDecl"},
                {SyntaxKind.VAR_DEF ,"VarDef"},
                {SyntaxKind.CONST_DEF ,"ConstDef"},
                {SyntaxKind.INIT_VAL ,"InitVal"},
                {SyntaxKind.CONST_INIT_VAL ,"ConstInitVal"},
                {SyntaxKind.EXP ,"Exp"},
                {SyntaxKind.CONST_EXP ,"ConstExp"},
                {SyntaxKind.ADD_EXP ,"AddExp"},
                {SyntaxKind.UNARY_EXP,"UnaryExp"},
                {SyntaxKind.MUL_EXP ,"MulExp"},
                {SyntaxKind.UNARY_OP ,"UnaryOp"},
                {SyntaxKind.FUNC_R_PARAMS ,"FuncRParams"},
                {SyntaxKind.PRIMARY_EXP ,"PrimaryExp"},
                {SyntaxKind.NUMBER ,"Number"},
                {SyntaxKind.LVAL ,"LVal"},
                {SyntaxKind.COND ,"Cond"},
                {SyntaxKind.LOR_EXP ,"LOrExp"},
                {SyntaxKind.LAND_EXP ,"LAndExp"},
                {SyntaxKind.EQ_EXP ,"EqExp"},
                {SyntaxKind.REAL_EXP ,"RelExp"}
        }).collect(Collectors.toMap(data -> (SyntaxKind) data[0], data -> (String) data[1]));

        return KeyWord2String.get(kind);
    }

    public static boolean isParserToken(SyntaxKind kind) {
        HashSet<SyntaxKind> parserToken = new HashSet<>(Arrays.asList(
                SyntaxKind.COMP_UNIT,
                SyntaxKind.FUNC_TYPE,
                SyntaxKind.FUNC_DEF,
                SyntaxKind.MAINFUNC_DEF ,
                SyntaxKind.FUNC_F_PARAMS,
                SyntaxKind.FUNC_F_PARAM ,
                SyntaxKind.BLOCK ,
                SyntaxKind.DECL ,
                SyntaxKind.STMT,
                SyntaxKind.VAR_DECL ,
                SyntaxKind.CONST_DECL ,
                SyntaxKind.VAR_DEF ,
                SyntaxKind.CONST_DEF ,
                SyntaxKind.INIT_VAL ,
                SyntaxKind.CONST_INIT_VAL ,
                SyntaxKind.EXP ,
                SyntaxKind.CONST_EXP ,
                SyntaxKind.ADD_EXP ,
                SyntaxKind.UNARY_EXP,
                SyntaxKind.MUL_EXP ,
                SyntaxKind.UNARY_OP ,
                SyntaxKind.FUNC_R_PARAMS ,
                SyntaxKind.PRIMARY_EXP ,
                SyntaxKind.NUMBER ,
                SyntaxKind.LVAL ,
                SyntaxKind.COND ,
                SyntaxKind.LOR_EXP ,
                SyntaxKind.LAND_EXP ,
                SyntaxKind.EQ_EXP ,
                SyntaxKind.REAL_EXP
        ));

        return parserToken.contains(kind);
    }
}




