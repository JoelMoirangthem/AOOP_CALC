package com.parser.nodes;
import com.runtime.Environment;
public class VariableNode implements Expression{
    private final String name;
    private final int line; // Store line number for error reporting
    public VariableNode(String name,int line) {
        this.name = name;
        this.line = line;
    }
    @Override
    public Object evaluate(Environment env){
        return env.get(name);
    }
}