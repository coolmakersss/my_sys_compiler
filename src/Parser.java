import Lexer.*;
import TreeNodes.*;

import static Lexer.SyntaxKind.expFirst;

public class Parser {
    private TokenSource tokens;
    private TreeBuild build;

    public Parser(TokenSource tokens) {
        this.tokens = tokens;
        build = new TreeBuild();
    }

    public Node parse() {
        compUnit();
        return build.root();
    }

    public void compUnit() {
        build.startNode(SyntaxKind.COMP_UNIT);
        SyntaxKind kind = tokens.first().kind;
        //print
        while (kind != SyntaxKind.EOF) {
            if (kind == SyntaxKind.VOIDTK) {
                funcDef();
            } else if (kind == SyntaxKind.CONSTTK) {
                constDecl();
            } else if (kind == SyntaxKind.INTTK) {
                if(tokens.ntoken(1).kind == SyntaxKind.MAINTK){
                    mainFuncDef();
                }
                else if (tokens.ntoken(2).kind == SyntaxKind.LPARENT) {
                    funcDef();
                } else {
                    varDecl();
                }
            } else {
                System.out.println("error!");
                return;
            }
            kind = tokens.first().kind;
        }
        //print

        build.finishNode(new CompUnitNode());
    }

    private void mainFuncDef() {
        build.startNode(SyntaxKind.MAINFUNC_DEF);
        terminalSymbol();
        terminalSymbol();
        terminalSymbol();
        terminalSymbol();
        block();
        build.finishNode(new MainFuncDef());
    }

    public void funcType() {
        build.startNode(SyntaxKind.FUNC_TYPE);
        SyntaxKind kind = tokens.first().kind;
        if (kind != SyntaxKind.VOIDTK && kind != SyntaxKind.INTTK) {
            System.out.println("error!!");
            return;
        }
        terminalSymbol();
        build.finishNode(new FuncTypeNode());
    }

    public void funcDef() {
        build.startNode(SyntaxKind.FUNC_DEF);
        funcType();
        terminalSymbol(); //ident
        terminalSymbol(); //(
        while (tokens.first().kind == SyntaxKind.INTTK) {
            funcFormalParams();
        }
        terminalSymbol(); //)
        block();
        build.finishNode(new FuncDefNode());
    }

    private void funcFormalParams() {
        build.startNode(SyntaxKind.FUNC_F_PARAMS);
        funcFormalParam();
        while(tokens.first().kind == SyntaxKind.COMMA){
            terminalSymbol();
            funcFormalParam();
        }
        build.finishNode(new FuncFormalParamsNode());
    }

    private void funcFormalParam() {
        build.startNode(SyntaxKind.FUNC_F_PARAM);
        terminalSymbol();
        terminalSymbol();
        if(tokens.first().kind == SyntaxKind.LBRACK){
            terminalSymbol();
            terminalSymbol();
            while(tokens.first().kind == SyntaxKind.LBRACK) {
                terminalSymbol();
                constExp();
                terminalSymbol();
            }
        }
        build.finishNode(new FuncFormalParamNode());
    }

    private void constExp() {
        build.startNode(SyntaxKind.CONST_EXP);
        addExp();
        build.finishNode(new ConstExpNode());
    }

    private void block() {
        build.startNode(SyntaxKind.BLOCK);
        terminalSymbol();
        while(true){
            SyntaxKind kind =  tokens.first().kind;
            if(kind == SyntaxKind.CONSTTK || kind == SyntaxKind.INTTK){
                decl();
            } else if(kind == SyntaxKind.RBRACE){
                break;
            } else {
                stmt();
            }
        }
        terminalSymbol();
        build.finishNode(new BlockNode());
    }

    private void stmt() {
        build.startNode(SyntaxKind.STMT);
        SyntaxKind kind = tokens.first().kind;
        if (expFirst(kind)) {
            if(kind!=SyntaxKind.IDENFR) {
                exp();
                terminalSymbol();
            }
            else{
                int i=1,flag=0;
                while(tokens.ntoken(i).kind != SyntaxKind.SEMICN){
                    if(tokens.ntoken(i).kind == SyntaxKind.ASSIGN){
                        flag++;
                    }
                    i++;
                }
                if(flag == 0){
                    exp();
                    terminalSymbol();
                } else {
                    lval();
                    terminalSymbol();
                    if(expFirst(tokens.first().kind)){
                        exp();
                        terminalSymbol();
                    } else if(tokens.first().kind == SyntaxKind.GETINTTK){
                        terminalSymbol();
                        terminalSymbol();
                        terminalSymbol();
                        terminalSymbol();
                    } else {
                        System.out.println("error!");
                    }
                }
            }
        } else if(kind == SyntaxKind.SEMICN){
            terminalSymbol();
        } else if (kind == SyntaxKind.LBRACE) {
            block();
        } else if (kind == SyntaxKind.IFTK) {
            terminalSymbol();
            terminalSymbol();
            cond();
            terminalSymbol();
            stmt();
            if(tokens.first().kind == SyntaxKind.ELSETK){
                terminalSymbol();
                stmt();
            }
        } else if (kind == SyntaxKind.WHILETK) {
            terminalSymbol();
            terminalSymbol();
            cond();
            terminalSymbol();
            stmt();
        } else if (kind == SyntaxKind.BREAKTK) {
            terminalSymbol();
            terminalSymbol();
        } else if (kind == SyntaxKind.CONTINUETK) {
            terminalSymbol();
            terminalSymbol();
        } else if (kind == SyntaxKind.RETURNTK) {
            terminalSymbol();
            if(expFirst(tokens.first().kind)) {
                exp();
            }
            terminalSymbol();
        } else if (kind == SyntaxKind.PRINTFTK) {
            terminalSymbol();
            terminalSymbol();
            terminalSymbol();
            while(tokens.first().kind == SyntaxKind.COMMA){
                terminalSymbol();
                exp();
            }
            terminalSymbol();
            terminalSymbol();
        } else {
            System.out.println("error!");
            return;
        }
        build.finishNode(new StmtNode());
    }

    private void cond() {
        build.startNode(SyntaxKind.COND);
        lorExp();
        build.finishNode(new CondNode());
    }

    private void lorExp() {
        build.startNode(SyntaxKind.LOR_EXP);
        landExp();
        while(tokens.first().kind == SyntaxKind.OR){
            terminalSymbol();
            landExp();
        }
        build.finishNode(new LorExpNode());
    }

    private void landExp() {
        build.startNode(SyntaxKind.LAND_EXP);
        eqExp();
        while(tokens.first().kind == SyntaxKind.AND){
            terminalSymbol();
            eqExp();
        }
        build.finishNode(new LandExpNode());
    }

    private void eqExp() {
        build.startNode(SyntaxKind.EQ_EXP);
        realExp();
        while(tokens.first().kind == SyntaxKind.EQL || tokens.first().kind == SyntaxKind.NEQ){
            terminalSymbol();
            realExp();
        }
        build.finishNode(new EqExpNode());
    }

    private void realExp() {
        build.startNode(SyntaxKind.REAL_EXP);
        addExp();
        SyntaxKind kind = tokens.first().kind;
        while(kind == SyntaxKind.LSS || kind == SyntaxKind.LEQ || kind == SyntaxKind.GRE || kind == SyntaxKind.GEQ){
            terminalSymbol();
            addExp();
            kind = tokens.first().kind;
        }
        build.finishNode(new RealExpNode());
    }

    private void decl() {
        build.startNode(SyntaxKind.DECL);
        if(tokens.first().kind == SyntaxKind.CONSTTK){
            constDecl();
        } else {
            varDecl();
        }
        build.finishNode(new DeclNode());
    }

    private void varDecl() {
        build.startNode(SyntaxKind.VAR_DECL);
        terminalSymbol();
        varDef();
        while(tokens.first().kind == SyntaxKind.COMMA){
            terminalSymbol();
            varDef();
        }
        terminalSymbol();
        build.finishNode(new VarDeclNode());
    }

    private void varDef() {
        build.startNode(SyntaxKind.VAR_DEF);
        terminalSymbol();
        while (tokens.first().kind == SyntaxKind.LBRACK) {
            terminalSymbol();
            constExp();
            terminalSymbol();
        }
        if (tokens.first().kind == SyntaxKind.ASSIGN) {
            terminalSymbol();
            initVal();
        }
        build.finishNode(new VarDefNode());
    }

    private void initVal() {
        build.startNode(SyntaxKind.INIT_VAL);
        SyntaxKind kind = tokens.first().kind;
        if (expFirst(kind)) {
            exp();
        } else if (kind == SyntaxKind.LBRACE) {
            terminalSymbol();
            kind = tokens.first().kind;
            if (kind == SyntaxKind.LBRACE || expFirst(kind)) {
                initVal();
                while (tokens.first().kind == SyntaxKind.COMMA) {
                    terminalSymbol();
                    initVal();
                }
                terminalSymbol();
            } else if (kind == SyntaxKind.RBRACE) {
                terminalSymbol();
            } else {
                System.out.println("error!");
            }
        } else {
            System.out.println("error!");
            return;
        }
        build.finishNode(new InitValNode());
    }

    private void exp() {
        build.startNode(SyntaxKind.EXP);
        addExp();
        build.finishNode(new ExpNode());
    }

    private void addExp() {
        build.startNode(SyntaxKind.ADD_EXP);
        mulExp();
        while(tokens.first().kind == SyntaxKind.PLUS || tokens.first().kind == SyntaxKind.MINU){
            terminalSymbol();
            mulExp();
        }
        build.finishNode(new AddExpNode());
    }

    private void mulExp() {
        build.startNode(SyntaxKind.MUL_EXP);
        unaryExp();
        while(tokens.first().kind == SyntaxKind.MULT || tokens.first().kind == SyntaxKind.DIV || tokens.first().kind == SyntaxKind.MOD){
            terminalSymbol();
            unaryExp();
        }
        build.finishNode(new MulExpNode());
    }

    private void unaryExp() {
        build.startNode(SyntaxKind.UNARY_EXP);
        SyntaxKind kind = tokens.first().kind;
        if(kind == SyntaxKind.PLUS || kind == SyntaxKind.MINU || kind == SyntaxKind.NOT){
            unaryOp();
            unaryExp();
        } else if (kind == SyntaxKind.IDENFR && tokens.ntoken(1).kind == SyntaxKind.LPARENT){
            terminalSymbol();
            terminalSymbol();
            if(tokens.first().kind == SyntaxKind.RPARENT){
                terminalSymbol();
            } else {
                funcRParams();
                terminalSymbol();
            }
        } else if(kind == SyntaxKind.LPARENT || kind == SyntaxKind.IDENFR || kind == SyntaxKind.INTCON){
            primaryExp();
        } else{
            System.out.println("error!");
        }
        build.finishNode(new UnaryExpNode());
    }

    private void primaryExp() {
        build.startNode(SyntaxKind.PRIMARY_EXP);
        SyntaxKind kind = tokens.first().kind;
        if(kind == SyntaxKind.LPARENT) {
            terminalSymbol();
            exp();
            terminalSymbol();
        } else if(kind == SyntaxKind.INTCON){
            nubmer();
        } else if(kind == SyntaxKind.IDENFR) {
            lval();
        }
        build.finishNode(new PrimaryExpNode());

    }

    private void lval() {
        build.startNode(SyntaxKind.LVAL);
        terminalSymbol();
        if(tokens.first().kind == SyntaxKind.LBRACK){
            terminalSymbol();
            exp();
            terminalSymbol();
            while(tokens.first().kind == SyntaxKind.LBRACK) {
                terminalSymbol();
                exp();
                terminalSymbol();
            }
        }
        build.finishNode(new LvalNode());
    }

    private void nubmer() {
        build.startNode(SyntaxKind.NUMBER);
        terminalSymbol();
        build.finishNode(new NumberNode());
    }

    private void funcRParams() {
        build.startNode(SyntaxKind.FUNC_R_PARAMS);
        exp();
        while(tokens.first().kind == SyntaxKind.COMMA){
            terminalSymbol();
            exp();
        }
        build.finishNode(new FuncRParamsNode());
    }

    private void unaryOp() {
        build.startNode(SyntaxKind.UNARY_OP);
        terminalSymbol();
        build.finishNode(new UnaryOpNode());
    }

    private void constDecl() {
        build.startNode(SyntaxKind.CONST_DECL);
        terminalSymbol();
        terminalSymbol();
        constDef();
        while(tokens.first().kind == SyntaxKind.COMMA){
            terminalSymbol();
            constDef();
        }
        terminalSymbol();
        build.finishNode(new ConstDeclNode());
    }

    private void constDef() {
        build.startNode(SyntaxKind.CONST_DEF);
        terminalSymbol();
        while (tokens.first().kind == SyntaxKind.LBRACK) {
            terminalSymbol();
            constExp();
            terminalSymbol();
        }
        terminalSymbol();
        constInitVal();
        build.finishNode(new ConstDefNode());
    }

    private void constInitVal() {
        build.startNode(SyntaxKind.CONST_INIT_VAL);
        SyntaxKind kind = tokens.first().kind;
        if (expFirst(kind)) {       //ConstExp == Exp
            constExp();
        } else if (kind == SyntaxKind.LBRACE) {
            terminalSymbol();
            kind = tokens.first().kind;
            if (kind == SyntaxKind.LBRACE || expFirst(kind)) {
                constInitVal();
                while (tokens.first().kind == SyntaxKind.COMMA) {
                    terminalSymbol();
                    constInitVal();
                }
                terminalSymbol();
            } else if (kind == SyntaxKind.RBRACE) {
                terminalSymbol();
            } else {
                System.out.println("error!");
            }
        } else {
            System.out.println("error!");
            return;
        }
        build.finishNode(new ConstInitValNode());
    }

    public void terminalSymbol() {
        build.terminalSymbol(tokens.first().kind, tokens.first().content, tokens.first().line);
        tokens.bump();
    }

}
