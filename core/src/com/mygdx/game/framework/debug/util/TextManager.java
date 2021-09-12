package com.mygdx.game.framework.debug.util;

import java.util.concurrent.TimeUnit;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.mygdx.game.framework.debug.util.FloatingText;

public class TextManager {

    private FloatingText floatingText;

    public void initCreateFloatingText(String text, float posX, float posY, float deltaY){

        this.floatingText = new FloatingText(text, TimeUnit.SECONDS.toMillis(2));
        this.floatingText.setPosition(posX, posY);
        this.floatingText.setDeltaY(deltaY);



    }




}
