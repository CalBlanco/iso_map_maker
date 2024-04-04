package com.isomapmaker.game.controls.commands;

import java.util.Vector;

/**
 * Singleton to manage command history and operations
 */
public class Commander {
    private static Commander instance; // The instance we return 
    private Vector<Command> editStack, undoStack; // stacks to contain recently executed or undone commands

    // Ensure singleton (not considering multithread access)
    public static Commander getInstance(){
        if(Commander.instance == null){
            Commander.instance = new Commander();
        }

        return Commander.instance;
    }

    // Private constructor to ensure singleton status
    private Commander(){
        editStack = new Vector<Command>();
        undoStack = new Vector<Command>();
    }

    // Pop the last command if there is one 
    private Command pop(Vector<Command> stack){
        if(stack.size() < 1) return null;
        Command com = stack.get(stack.size()-1);
        stack.remove(stack.size()-1);
        return com;
    }

    /**
     * Execute the specified command and attach it to our command history 
     * @param com The command we want to execute
     */
    public void run(Command com){
        com.execute();
        editStack.add(com);
    }

    /**
     * Undo the most recently issued command and add it to our undo history
     */
    public void undo(){
        Command com = pop(editStack);
        if(com == null) return;
        undoStack.add(com);
        com.undo();
    }

    /**
     * Redo our last undo (This stuff gets kind of funky with saving the map if issued to fast)
     */
    public void redo(){
        Command com = pop(undoStack);
        if(com == null) return;
        run(com);
    }
}
