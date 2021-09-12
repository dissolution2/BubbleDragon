package com.mygdx.game.framework.debug.world.gameLightObjcets;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.framework.debug.screens.PlayScreen;
import com.mygdx.game.framework.debug.world.gameObstacles.Obstacle;

public abstract class GameLightObjectDef extends Sprite {

    protected World world;
    protected PlayScreen screen;
    public Body b2body;
    public Vector2 velocityMovement;

    public MapObject mapObject;
    private String objectIdentity;

    public GameLightObjectDef(PlayScreen screen, float x, float y, MapObject object) {
        this.world = screen.getWorld();
        this.screen = screen;
        this.mapObject = object;
        this.objectIdentity = mapObject.getName();

        setPosition(x, y);
        defineLightPointObject();
        b2body.setActive(true);
    }

    // all use
    public abstract String getObjectIdentity();
    protected abstract void defineLightPointObject();
    public abstract void update(float dt);
    public abstract int getObjectID();



}