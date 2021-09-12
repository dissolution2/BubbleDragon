package com.mygdx.game.framework.debug.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

/**
 * Move this to Utility.java One Utility... or with in GameManager !!! Refactoring later
 *
 **/

public class DrawerPauseScreenUtils {
    public static void addListeners(EventListener listener, Actor... actors) {
        for (Actor actor : actors)
            actor.addListener(listener);
    }

    public static Drawable getTintedDrawable(TextureAtlas.AtlasRegion region, Color color) {
        Sprite sprite = new Sprite(region);
        sprite.setColor(color);
        return new SpriteDrawable(sprite);
    }
}
