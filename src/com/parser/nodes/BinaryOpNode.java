
package com.parser.nodes;
import com.runtime.Environment;
public class BinaryOpNode implements Expression{
    private final Expression left , right;
    private final String operator;

    // adding line number for better error reporting
    private final int line;
    public BinaryOpNode(Expression left, String operator, Expression right,int line) {
        this.left = left;
        this.operator = operator;
        this.right = right;
        this.line = line; // Store line number for error reporting
    }
    @Override
    public Object evaluate(Environment env){
        Object leftVal = left.evaluate(env);
        Object rightVal = right.evaluate(env);
        if(!(leftVal instanceof Double) || !(rightVal instanceof Double)){
            throw new com.errors.CalcException("Both operands must be numbers", line == 0 ? 1 : line); // Use line number for error reporting, default to 1 if not set
        }
        
        double leftNum = (Double) leftVal;
        double rightNum = (Double) rightVal;
        switch (operator) {
            case "+":
                return leftNum + rightNum;
            case "-":
                return leftNum - rightNum;
            case "*":
                return leftNum * rightNum;
            case "/":
                if(rightNum == 0){
                    throw new com.errors.CalcException("Division by zero", line == 0 ? 1 : line); // Use line number for error reporting, default to 1 if not set of division by zero
                }
                return leftNum / rightNum;
            case ">": return leftNum > rightNum;
            case "<": return leftNum < rightNum;
            case "==": return leftNum == rightNum;
            case "!=": return leftNum != rightNum;
            case ">=": return leftNum >= rightNum;
            case "<=": return leftNum <= rightNum;
            case "&&": return (leftNum != 0) && (rightNum != 0);
            case "||": return (leftNum != 0) || (rightNum != 0);
            case "%" : return leftNum % rightNum;
            default:
                throw new com.errors.CalcException("Unknown operator: " + operator, line == 0 ? 1 : line); // Use line number for error reporting, default to 1 if not set
        }
    }
}

