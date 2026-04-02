package com.errors;

public class CalcException extends RuntimeException {
    private final int line;

    public CalcException(String message, int line) {
        super("Error at line " + line + ": " + message);
        this.line = line;
    }

    public int getLine() {
        return line;
    }
}
