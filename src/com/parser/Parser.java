/*
 * Parser.java
 *
 * Fixes applied and documented below:
 * 1) Robust EOF/check handling: avoid accessing tokens out-of-bounds when at end.
 *    - Reason: original `check()` used `isAtEnd()` which prevented EOF matching and
 *      caused the parser to attempt parsing instructions at EOF, producing
 *      `Invalid instruction` errors.
 * 2) Comparison & logical operator parsing: added handling so conditions like
 *    `score > 50` parse correctly.
 *    - Reason: previously only additive/multiplicative ops were parsed; comparisons
 *      were unrecognized and led to parse errors when used as `if` conditions.
 * 3) String tokens support: parser already accepts `STRING` in `parsePrimary()`.
 *
 * These changes were intentionally minimal to preserve the original design.
 */
package com.parser;
import java.util.List;
import com.lexer.*;
import com.parser.nodes.*;
import com.instructions.*;
import java.util.ArrayList;
public class Parser {
    private final List<Token> tokens;
    private int pos = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }
    
    // first we have to parse the tokens and create an abstract syntax tree
    public List<Instruction> parse(){
        List<Instruction> intructions = new ArrayList<>();
        while(!match(TokenType.EOF)){
            if(match(TokenType.NEWLINE)) continue;
            intructions.add(parseInstruction());
        }
        return intructions;
    }

    // parse the instruction
    private Instruction parseInstruction(){
//        x := 7
        if(check(TokenType.IDENTIFIER)&& checkNext(TokenType.ASSIGN)){
            return parseAssign();
        }
        if(match(TokenType.PRINT)){
            return parsePrint();
        }
        if(match(TokenType.IF)){
            return parseIf();
        }
        if(match(TokenType.LOOP)){
            return parseLoop();
        }
        throw error("Invalid instruction");

    }

    // parse the assignment instruction
    private Instruction parseAssign(){
        // first we have to consume the variable name and then we have to consume the := token
        // eg x := 5
        // we have to consume x and then we have to consume := and then we have to parse the expression 5
        String name = consume(TokenType.IDENTIFIER,"Expected variable name").getValue();
        consume(TokenType.ASSIGN,"Expected :=");
        Expression value = parseExpression();
        return new AssignInstruction(name,value);
    }
    private Instruction parsePrint(){
        // first we have to consume the >> token and then we have to parse the expression
        // eg >> x + 5
        // we have to consume >> and then we have to parse the expression x + 5
        Expression value = parseExpression();
        return new PrintInstruction(value);
    }
    // parse the if instruction
    // eg ? x > 5 =>
    // we have to consume ? and then we have to parse the condition x > 5
    private Instruction parseIf(){
        int ifLine = tokens.get(pos-1).getLine();
        Expression condition = parseExpression();
//         ? x > y =>
        consume(TokenType.ARROW,"Expected =>");
        List<Instruction> body = new ArrayList<>();
        // Support both inline and block bodies.
        // Inline: ? cond => instr instr\n
        // Block: ? cond =>\n
        //           instr\n
        //           instr\n
        //         (blank line or EOF ends the block)
        if(match(TokenType.NEWLINE)){
            // we're in a block body; parse until a blank line (two consecutive NEWLINE) or EOF
            while(!check(TokenType.EOF) && !(check(TokenType.NEWLINE) && checkNext(TokenType.NEWLINE))){
                if(match(TokenType.NEWLINE)) continue; // skip stray newlines
                body.add(parseInstruction());
            }
            // consume the second newline if it's the blank-line terminator
            match(TokenType.NEWLINE);

//          ? x > y
//            ? x > b
        } else {
            // inline body: parse until end of line
            while(!check(TokenType.EOF) && !check(TokenType.NEWLINE)){
                body.add(parseInstruction());
            }
        }

        // Optional `else` or `elif` handling. `else` and `elif` are parsed as identifiers
        // with literal values "else"/"elif". This is a simple implementation that
        // supports one `elif` chained as an `else` containing an `IfInstruction`.
        List<Instruction> elseBody = null;
        if(check(TokenType.IDENTIFIER)){
            String id = tokens.get(pos).getValue();
            if("else".equals(id)){
                // consume 'else'
                pos++;
                // parse else body (block or inline)
                elseBody = new ArrayList<>();
                if(match(TokenType.NEWLINE)){
                    while(!check(TokenType.EOF) && !(check(TokenType.NEWLINE) && checkNext(TokenType.NEWLINE))){
                        if(match(TokenType.NEWLINE)) continue;
                        elseBody.add(parseInstruction());
                    }
                    match(TokenType.NEWLINE);
                } else {
                    while(!check(TokenType.EOF) && !check(TokenType.NEWLINE)){
                        elseBody.add(parseInstruction());
                    }
                }
            } else if("elif".equals(id)){
                // consume 'elif' and parse it as an if placed in elseBody
                pos++;
                Expression elifCond = parseExpression();
                consume(TokenType.ARROW, "Expected =>");
                List<Instruction> elifBody = new ArrayList<>();
                if(match(TokenType.NEWLINE)){
                    while(!check(TokenType.EOF) && !(check(TokenType.NEWLINE) && checkNext(TokenType.NEWLINE))){
                        if(match(TokenType.NEWLINE)) continue;
                        elifBody.add(parseInstruction());
                    }
                    match(TokenType.NEWLINE);
                } else {
                    while(!check(TokenType.EOF) && !check(TokenType.NEWLINE)){
                        elifBody.add(parseInstruction());
                    }
                }
                IfInstruction elifInstr = new IfInstruction(elifCond, elifBody);
                elseBody = new ArrayList<>();
                elseBody.add(elifInstr);
            }
        }

        return new IfInstruction(condition,body,elseBody, ifLine);
    } 
    private Instruction parseLoop(){
        int loopLine = tokens.get(pos-1).getLine();
        Expression countExpression = parseExpression();
        consume(TokenType.ARROW,"Expected =>");
        List<Instruction> body = new ArrayList<>();
        // Same handling as parseIf: allow block or inline body
        if(match(TokenType.NEWLINE)){
            while(!check(TokenType.EOF) && !(check(TokenType.NEWLINE) && checkNext(TokenType.NEWLINE))){
                if(match(TokenType.NEWLINE)) continue;
                body.add(parseInstruction());
            }
            match(TokenType.NEWLINE);
        } else {
            while(!check(TokenType.EOF) && !check(TokenType.NEWLINE)){
                body.add(parseInstruction());
            }
        }
        return new RepeatInstruction(countExpression,body, loopLine);
    }
    // parse the expression
    // eg x + 5
    // we have to parse the left ,then operator and then the right
    private Expression parseExpression(){
        Expression left = parseTerm();
        while(match(TokenType.PLUS,TokenType.MINUS)){
            String operator = previous().getValue();
            int opLine = previous().getLine();
            Expression right = parseTerm();
            left = new BinaryOpNode(left, operator, right,opLine);
        }
        // handle comparison and logical operators after additive ops
        while(match(TokenType.GT, TokenType.LT, TokenType.EQEQ, TokenType.NEQ, TokenType.GEQ, TokenType.LEQ, TokenType.AND, TokenType.OR)){
            String operator = previous().getValue();
            int opLine = previous().getLine();
            Expression right = parseTerm();
            left = new BinaryOpNode(left, operator, right, opLine);
        }
        return left;
//    x = 8 y = 9 +
//        y = x + y
//        return y
    }
    // parse the term
    // eg x * 5 
    // we have to parse the left ,then operator and then the right
    private Expression parseTerm(){
        Expression left = parsePrimary();
        int opLine = previous().getLine(); // Store line number of the operator for error reporting
        while(match(TokenType.MUL,TokenType.DIV,TokenType.MOD)){
            String operator = previous().getValue();
            Expression right = parsePrimary();
            left = new BinaryOpNode(left,operator,right,opLine);
        }
        return left;

    }
//    x + y + ( y  - 20)
    // parse the primary
    // eg x or 5
    // we have to check if it is a number or a variable
    private Expression parsePrimary(){
        // Parenthesized expression
        if(match(TokenType.LPAREN)){
            Expression expr = parseExpression();
            consume(TokenType.RPAREN, "Expected )");
            return expr;
        }
        if(match(TokenType.NUMBER)){
            return new NumberNode(Double.parseDouble(previous().getValue()), previous().getLine());
        }
        if(match(TokenType.STRING)){
            return new StringNode(previous().getValue(), previous().getLine());  
        }
        if(match(TokenType.IDENTIFIER)){
            return new VariableNode(previous().getValue(), previous().getLine());
        }
        throw error("Expected expression");
    }
    // helper methods
    private boolean match(TokenType type){
        // check current token is of the given type or not
        if(check(type)){
            pos++;
            return true;
        }
        return false;
    }
    
    private boolean match(TokenType... types){
        for(TokenType type : types){
            if(check(type)){
                pos++;
                return true;
            }
        }
        return false;
    }
    // check the current token is of the given type or not
    private boolean check(TokenType type){
        if(pos >= tokens.size()) return false;
        return tokens.get(pos).getType() == type;
    }
    // check the next token is of the given type or not
    // eg x := 5
    // if the x is current token then := is next token
    
    private boolean checkNext(TokenType type){
        if(pos + 1 >= tokens.size()) return false;
        return tokens.get(pos + 1).getType() == type;
    }
    // consume the current token if it is of the given type or throw an error
    private Token consume(TokenType type,String message){
        if(check(type)) return tokens.get(pos++);
        throw error(message);
    }
    
    
    // previous token
    private Token previous(){
        return tokens.get(pos - 1);
    }
    // runtime error with line information
    private com.errors.CalcException error(String message){
        int lineNum;
        if(pos < tokens.size()){
            lineNum = tokens.get(pos).getLine();
        } else {
            lineNum = tokens.get(tokens.size() - 1).getLine();
        }
        return new com.errors.CalcException("Parser error at token " + (pos > 0 ? previous() : tokens.get(Math.max(0,pos-1))) + ": " + message, lineNum);
    }
}   
