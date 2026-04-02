package com;
import com.interpreter.Interpreter;
public class Main {
    public static void main(String[] args) {
        String source = """
            >> x
        """;
        Interpreter interpreter = new Interpreter();
        interpreter.run(source);
    }
}