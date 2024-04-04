package com.isomapmaker.game.controls.commands;

import java.util.Vector;

/**
 * Singleton to manage command history and operations
 */
public class Commander {
    private static Commander instance;
    private Vector<Command> editStack, undoStack;

    public static Commander getInstance(){
        if(Commander.instance == null){
            Commander.instance = new Commander();
        }

        return Commander.instance;
    }

    private Commander(){
        editStack = new Vector<Command>();
        undoStack = new Vector<Command>();
    }

    private Command pop(Vector<Command> stack){
        if(stack.size() < 1) return null;
        Command com = stack.get(stack.size()-1);
        stack.remove(stack.size()-1);
        return com;
    }

    public void run(Command com){
        com.execute();
        editStack.add(com);
    }

    public void undo(){
        Command com = pop(editStack);
        if(com == null) return;
        undoStack.add(com);
        com.undo();
    }

    public void redo(){
        Command com = pop(undoStack);
        if(com == null) return;
        run(com);
    }
}
