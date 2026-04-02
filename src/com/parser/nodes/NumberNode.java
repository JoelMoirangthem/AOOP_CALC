package com.parser.nodes;
import com.runtime.Environment;
public class NumberNode implements Expression{
    private final double value;
    private final int line; // Store line number for error reporting
    public NumberNode(double value,int line) {
        this.value = value;
        this.line = line;
    }
    @Override
    public Object evaluate(Environment env){
        return value;
    }
}