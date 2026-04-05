package com;

import com.interpreter.Interpreter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            // Path to your file
            String source = Files.readString(Path.of("Source.txt"));

            Interpreter interpreter = new Interpreter();
            interpreter.run(source);

        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }
}