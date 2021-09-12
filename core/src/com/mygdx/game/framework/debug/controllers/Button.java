package com.mygdx.game.framework.debug.controllers;

public class Button {

    private String name;
    private float cooldown;
    private Action action;

    public Button(String name, float cooldown, Action action) {
        this.name = name;
        this.cooldown = cooldown;
        this.action = action;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getCooldown() {
        return cooldown;
    }

    public void setCooldown(float cooldown) {
        this.cooldown = cooldown;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }
}
