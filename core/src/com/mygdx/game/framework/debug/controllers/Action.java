package com.mygdx.game.framework.debug.controllers;

public class Action {

    /**
     * @param value to be printed on the screen when the action is triggered.
     */
    public void execute(String value) {
        System.out.println("Executed action: " + value);

        if(value.equals("attack")) {
            System.out.println("value check attack : " + value);
        }
    }
}

