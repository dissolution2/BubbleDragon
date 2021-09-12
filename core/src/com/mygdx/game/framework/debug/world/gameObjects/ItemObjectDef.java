package com.mygdx.game.framework.debug.world.gameObjects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.framework.debug.screens.PlayScreen;

public abstract class ItemObjectDef extends Sprite {

    protected World world;
    protected PlayScreen screen;
    public Body b2body;
    public Vector2 velocityMovement;

    public MapObject mapObject;
    public String mapObjectString;
    private String objectIdentity;

    public ItemObjectDef(PlayScreen screen, float x, float y, MapObject object) {
        this.world = screen.getWorld();
        this.screen = screen;
        this.mapObject = object;

        this.objectIdentity = mapObject.getName();
        setPosition(x, y);
        defineItemObject();

        b2body.setActive(true);
    }

    public ItemObjectDef(PlayScreen screen, float x, float y, String name) {
        this.world = screen.getWorld();
        this.screen = screen;
        this.mapObjectString = name;

        this.objectIdentity = this.mapObjectString; //"EX_LIFE";
        setPosition(x, y);
        defineItemObject();

        b2body.setActive(true);
    }

    // all use
    public abstract String getObjectIdentity();
    protected abstract void defineItemObject();
    public abstract void update(float dt);
    public abstract int getObjectID();


}
