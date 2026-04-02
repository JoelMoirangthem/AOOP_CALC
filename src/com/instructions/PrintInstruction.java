package  com.instructions;
import com.parser.nodes.Expression;
import com.runtime.Environment;

public class PrintInstruction implements Instruction{
    private final Expression expression;
    public PrintInstruction(Expression expression){
        this.expression = expression;
    }
    // execute the instruction by evaluating the expression and printing the result to the console
    // eg print 5 + 3 will evaluate 5 + 3 and print the
    // result to the console
    @Override
    public void execute(Environment env){
        Object value = expression.evaluate(env);
        // Format numbers: if it's a Double representing a whole number, print without .0
        if(value instanceof Double){
            double d = (Double) value;
            if(d == Math.rint(d)){
                // print as long to avoid decimal dot
                System.out.println(String.valueOf((long) d));
                return;
            }
        }
        System.out.println(value);
    }
}