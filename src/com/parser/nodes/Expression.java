package com.parser.nodes;
import com.runtime.Environment;
public interface Expression {
    Object evaluate(Environment env);
}

