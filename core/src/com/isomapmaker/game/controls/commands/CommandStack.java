package com.isomapmaker.game.controls.commands;

import java.util.Vector;

public class CommandStack {
    int max;
    Vector<Command> stack;
    public CommandStack(int maxSize){
        max = maxSize;
        stack = new Vector<Command>(max);
    }

    public Command pop(){
        if(stack.isEmpty()) return null;
        Command c = stack.remove(stack.size()-1);
        return c;
    }

    public void push(Command c){
        if(stack.size() == max){
            popOldest();
        }
        stack.add(c);
    }

    /**
     * remove the oldest item in the stack
     */
    public void popOldest(){
        if(stack.isEmpty()) return;
        stack.remove(0);
    }
}
