package com.interpreter;
import com.lexer.*;
import com.parser.*;
import com.parser.nodes.*;
import com.instructions.*;
import com.runtime.Environment;
import java.util.List;
public class Interpreter {
    public void run(String sourceCode){
        // tokenize the source code
        try{
            Tokenizer tokenizer = new Tokenizer(sourceCode);
            List<Token> tokens = tokenizer.tokenize();

            // parse the tokens into an AST
            Parser parser = new Parser(tokens);
            List<Instruction> instructions = parser.parse();
            // execute the instructions
            Environment env = new Environment();
            for(Instruction instruction : instructions){
                instruction.execute(env);
            }

        }catch(Exception e){
            // Prefer to print CalcException messages as-is (they include line info)
            if(e instanceof com.errors.CalcException){
                System.out.println(((com.errors.CalcException)e).getMessage());
            } else {
                System.out.println("Faaaahh..... Error: " + e.getMessage());
            }
        }
    }
}
