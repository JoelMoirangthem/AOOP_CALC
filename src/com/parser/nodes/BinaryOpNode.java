
package com.parser.nodes;
import com.runtime.Environment;
public class BinaryOpNode implements Expression{
    private final Expression left , right;
    private final String operator;
    public BinaryOpNode(Expression left, String operator, Expression right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }
    @Override
    public Object evaluate(Environment env){
        Object leftVal = left.evaluate(env);
        Object rightVal = right.evaluate(env);
        if(!(leftVal instanceof Double) || !(rightVal instanceof Double)){
            throw new RuntimeException("Both operands must be numbers");
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
                    throw new RuntimeException("Division by zero");
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
                throw new RuntimeException("Unknown operator: " + operator);
        }
    }
}

