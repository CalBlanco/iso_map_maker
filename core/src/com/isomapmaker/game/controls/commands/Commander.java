package com.isomapmaker.game.controls.commands;

/**
 * Singleton to manage command history and operations
 */
public class Commander {
    private static Commander instance; // The instance we return 
    private CommandStack editStack, undoStack; // stacks to contain recently executed or undone commands

    // Ensure singleton (not considering multithread access)
    public static Commander getInstance(){
        if(Commander.instance == null){
            Commander.instance = new Commander();
        }

        return Commander.instance;
    }

    // Private constructor to ensure singleton status
    private Commander(){
        editStack = new CommandStack(10);
        undoStack = new CommandStack(5);
    }


    /**
     * Execute the specified command and attach it to our command history 
     * @param com The command we want to execute
     */
    public void run(Command com){
        com.execute();
        editStack.push(com);
    }

    /**
     * Undo the most recently issued command and add it to our undo history
     */
    public void undo(){
        Command com = editStack.pop();
        if(com == null) return;
        undoStack.push(com);
        com.undo();
    }

    /**
     * Redo our last undo (This stuff gets kind of funky with saving the map if issued to fast)
     */
    public void redo(){
        Command com = undoStack.pop();
        if(com == null) return;

        editStack.push(com);
        com.redo();
    }
}
