package com.mygdx.game.framework.debug.sprites.GraphicsAnimations;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.framework.debug.screens.PlayScreen;

public abstract class AnimationItemDef extends Sprite {

    protected World world;
    protected PlayScreen screen;
    public Body b2body;

    public AnimationItemDef(PlayScreen screen, float x, float y) {
        this.world = screen.getWorld();
        this.screen = screen;
        setPosition(x, y);

        defineItem();

        b2body.setActive(false);
    }

    protected abstract void defineItem();
    public abstract void update(float dt);
}
