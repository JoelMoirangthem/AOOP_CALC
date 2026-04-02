package com.instructions;
import com.parser.nodes.Expression;
import com.runtime.Environment;
public class AssignInstruction implements Instruction{
    private final String variableName;
    private final Expression expression;
    public AssignInstruction(String variableName, Expression expression) {
        this.variableName = variableName;
        this.expression = expression;
    }
    // execute the instruction by evaluating the expression and assigning it to the variable in the environment
    // eg x = 5 + 3 will evaluate 5 + 3 and assign the result to x in the environment
    public void execute(Environment env){
        Object value = expression.evaluate(env);
        env.set(variableName,value);
    }

}