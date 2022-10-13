package com.mygdx.game.framework.debug.controllers;

public class Action {

    /**
     * @param value to be printed on the screen when the action is triggered.
     *
     *              ide that we use flowting text but moved it might bring it back!!!
     */
    public void execute(String value) {
        System.out.println("Executed action: " + value);

        if(value.equals("attack")) {
            System.out.println("value check attack : " + value);
        }
    }
}

