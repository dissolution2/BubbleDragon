package com.mygdx.game.framework.debug.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.TimeUtils;

public class FPSLogger {

    long startTime;
    String fpsMessage = "";

    public FPSLogger(){
        startTime = TimeUtils.nanoTime();
    }

    public String logg() {
        if(TimeUtils.nanoTime() - startTime > 1000000000) {
            //Gdx.app.log("FPSLogger", "fps: " + Gdx.graphics.getFramesPerSecond());
            fpsMessage = "FPSLogger fps: " + Gdx.graphics.getFramesPerSecond();
            startTime = TimeUtils.nanoTime();
        }
        return fpsMessage;
    }
}
