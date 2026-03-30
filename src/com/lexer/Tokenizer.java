package com.lexer;
import java.util.ArrayList;
import java.util.List;
public class Tokenizer {
    private final String source;
    private int pos = 0;
    private int line = 1;
    public Tokenizer(String source) {
        this.source = source;
    }

    public List<Token> tokenize(){
        List<Token> tokens = new ArrayList<>();
        while(pos < source.length()){
            // first we have to read the current character
            char current  = source.charAt(pos);
            
            // check the current character is whitespace or not
            if(Character.isWhitespace(current)){
                if(current == '\n'){
                    tokens.add(new Token(TokenType.NEWLINE,"\\n",line));
                    line++;
                }
                pos++;
                continue;
            }
            
            // check the current character is a digit or not
            if(Character.isDigit(current)){
                tokens.add(readNumber());
            
            }
            else if(Character.isLetter(current)){
                tokens.add(readIdentifier());
            }
            else if(current  == ':' && peek() == '='){
                tokens.add(new Token(TokenType.ASSIGN,":=",line));
                pos += 2;
            }
            else if(current == '>' && peek() == '>'){
                tokens.add(new Token(TokenType.PRINT,">>",line));
                pos += 2;
            }
            else if(current == '?' ){
                tokens.add(new Token(TokenType.IF,"?",line));
                pos++;
            }
            else if(current == '@' ){
                tokens.add(new Token(TokenType.LOOP,"@",line));
                pos++;
            }
            else if(current == '=' && peek() == '>'){
                tokens.add(new Token(TokenType.ARROW,"=>",line));
                pos += 2;
            }
            else if(current == '+' ){
                tokens.add(new Token(TokenType.PLUS,"+",line));
                pos++;
            }
            else if(current == '-' ){
                tokens.add(new Token(TokenType.MINUS,"-",line));
                pos++;
            }
            else if(current == '*' ){
                tokens.add(new Token(TokenType.MUL,"*",line));
                pos++;
            }
            else if(current == '/' ){
                tokens.add(new Token(TokenType.DIV,"/",line));
                pos++;
            }
            else if(current == '%' ){
                tokens.add(new Token(TokenType.MOD,"%",line));
                pos++;
            }
            else if(current == '>' ){
                tokens.add(new Token(TokenType.GT,">",line));
                pos++;
            }
            else if(current == '<' ){
                tokens.add(new Token(TokenType.LT,"<",line));
                pos++;
            }
            else if(current == '=' ){
                tokens.add(new Token(TokenType.EQ,"=",line));
                pos++;
            }
            else if(current == '!' && peek() == '='){
                tokens.add(new Token(TokenType.NEQ,"!=",line));
                pos += 2;
            }
            else if(current == '=' && peek() == '='){
                tokens.add(new Token(TokenType.EQEQ,"==",line));
                pos += 2;
            }
            else if(current == '>' && peek() == '='){
                tokens.add(new Token(TokenType.GEQ,">=",line));
                pos += 2;
            }
            else if(current == '<' && peek() == '='){
                tokens.add(new Token(TokenType.LEQ,"<=",line));
                pos += 2;
            }
            else if(current == '&' && peek() == '&'){
                tokens.add(new Token(TokenType.AND,"&&",line));
                pos += 2;
            }
            else if(current == '|' && peek() == '|'){
                tokens.add(new Token(TokenType.OR,"||",line));
                pos += 2;
             }
             else if(current == '!' ){
                 tokens.add(new Token(TokenType.NOT,"!",line));
                 pos++;
             }
             else{
                 throw new RuntimeException("Unexpected character: " + current + " at line " + line);
             }
        }
        tokens.add(new Token(TokenType.EOF,"",line));
        return tokens;
    }
    private char peek(){
        if(pos + 1 >= source.length()){
            return '\0';
        }
        return source.charAt(pos + 1);
    }
    private Token readNumber(){
        int start = pos;
        while(pos < source.length() && Character.isDigit(source.charAt(pos))){
            pos++;
        }
        return new Token(TokenType.NUMBER,source.substring(start,pos),line);
    }
    // we have to read the identifier eg variable name or function name
    // for example: x, y, add, subtract etc
    private Token readIdentifier(){
        int start = pos;
        while(pos < source.length() && (Character.isLetterOrDigit(source.charAt(pos)) || source.charAt(pos) == '_')){
            pos++;
        }
        return new Token(TokenType.IDENTIFIER,source.substring(start,pos),line);

        
    }
    // most important part is to read the string literal
    // for example: "Hello, World!" etc.
    private Token readString(){
        // we have to skip the opening quote like " or '
        pos++;
        int start = pos;
        while(pos < source.length() && source.charAt(pos) != '"' && source.charAt(pos) != '\''){
            // we have to handle the escape characters like \n, \t, \r etc.
            if(source.charAt(pos) == '\\' && pos + 1 < source.length()){
                pos += 2;
            }
            else{
                pos++;
            }
        }
        if(pos >= source.length()){
            throw new RuntimeException("Unterminated string literal at line " + line);
        }
        // now find value of string literal eg "Hello, World!" => Hello, World!
        String value = source.substring(start,pos);
        pos++; // skip the closing quote
        return new Token(TokenType.STRING, value, line);
    }

}