package com.mygdx.game.framework.debug.managers;

/**
 * A simple game time controller which represents elapsed time. Move to game folder !!??
 */
public class GameManagerAITimer {

    private float currentTime = 0;

    public float getCurrentTime() {
        return currentTime;
    }

    public void updateCurrentTime(float addValue) {
        currentTime += addValue;
    }

    public void resetCurrentTime() { currentTime = 0;}

    public void stopCurrentTime() { currentTime += 0; }
}
