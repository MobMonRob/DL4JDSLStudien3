/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oracle.truffle.prepro.launcher;

/**
 * Denotes the result of the {@link PreProMain#main} method should call System.exit or not.
 * In case of e.g. the LSP being attached, it must not call System.exit since otherwise scheduling operations from GraalLSP would error.
 * 
 * For this, there are 3 static factory methods that contain the possible values.
 * 
 * @author Timo Klenk
 */
public class ExecutionResult {
    private final boolean shouldExit;
    private final int returnValue;

    private ExecutionResult(boolean shouldExit, int returnValue) {
        this.shouldExit = shouldExit;
        this.returnValue = returnValue;
    }
    
    /**
     * @return A new ExecutionResult object that denotes that {@link System#exit(int) } 
     * should be called with {@code 0} as return value
     */
    public static ExecutionResult success() {
        return new ExecutionResult(true, 0);
    }
    
    /**
     * @return A new ExecutionResult object that denotes that {@link System#exit(int) } 
     * should be called with {@code 1} as return value
     */
    public static ExecutionResult failure() {
        return new ExecutionResult(true, 1);
    }
    
    /**
     * @return A new ExecutionResult object that denotes that {@link System#exit(int) } 
     * should NOT be called.
     */
    public static ExecutionResult doContinue() {
        return new ExecutionResult(false, 0);
    }
    
    /**
     * Calls {@link System#exit(int)} with the provided value, if it is set to not continue.
     * 
     * If it is set to continue, this method call will do nothing.
     */
    public void exitIfRequired() {
        if(shouldExit) {
            System.exit(returnValue);
        }
    }
}
