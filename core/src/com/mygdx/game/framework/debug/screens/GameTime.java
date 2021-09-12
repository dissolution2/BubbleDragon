package com.mygdx.game.framework.debug.screens;

/**
 * A simple game time controller which represents elapsed time. tracks Hud button's cool down time
 */
public class GameTime {

    private static float currentTime = 0;

    public static float getCurrentTime() {
        return currentTime;
    }

    static void updateCurrentTime(float addValue) {
        currentTime += addValue;
    }
}
