package com.runtime;
import java.util.*;
public class Environment {
    private Map<String,Object> variables = new HashMap<>();

    // Set a variable in the environment eg x := 5
    public void set(String name, Object value){
        variables.put(name, value);
    }
    // Get a variable from the environment eg x
    public Object get(String name){
        if(!variables.containsKey(name)){
            
            throw new RuntimeException("Faaaaahhhhh...... Variable " + name + " is not defined");
        }
        return variables.get(name);
    }
}
