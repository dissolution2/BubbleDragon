package com.mygdx.game.framework.debug.world.gamePfxObject;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.framework.debug.screens.PlayScreen;


public abstract class PfxObjectDef extends Sprite {

    protected World world;
    protected PlayScreen screen;
    public Body b2body;

    public MapObject mapObject;
    private String objectIdentity;

    public PfxObjectDef(PlayScreen screen, float x, float y, MapObject object){
        this.world = screen.getWorld();
        this.screen = screen;
        this.mapObject = object;

        this.objectIdentity = mapObject.getName();

        setPosition(x,y);
        defineGamePfxObject();
    }

    public abstract String getObjectIdentity();
    protected abstract void defineGamePfxObject();
    public abstract void update(float dt);
    public abstract int getObjectID();

}
