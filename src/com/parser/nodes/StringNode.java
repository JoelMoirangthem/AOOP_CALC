package com.parser.nodes;
import com.runtime.Environment;
public class StringNode implements Expression{
    private final String value;
    private final int line; // Store line number for error reporting
    public StringNode(String value,int line) {
         this.line = line;
        this.value = value;
    }
    @Override
    public Object evaluate(Environment env){
        return value;
    }
}