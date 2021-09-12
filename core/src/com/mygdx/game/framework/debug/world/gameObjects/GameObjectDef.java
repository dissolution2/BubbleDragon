package com.mygdx.game.framework.debug.world.gameObjects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.framework.debug.screens.PlayScreen;
import com.mygdx.game.framework.debug.world.gameObstacles.Obstacle;

public abstract class GameObjectDef extends Sprite {

    protected World world;
    protected PlayScreen screen;
    public Body b2body;
    public Vector2 velocityMovement;

    public MapObject mapObject;
    private String objectIdentity;

    public GameObjectDef(PlayScreen screen, float x, float y, MapObject object) {
        this.world = screen.getWorld();
        this.screen = screen;
        this.mapObject = object;
        this.objectIdentity = mapObject.getName();

        setPosition(x, y);
        defineGameObject();
        //velocityMovement = new Vector2(0f, 0f);//-1.2f); // -1 , -2
        b2body.setActive(true);
    }

    // all use
    public abstract String getObjectIdentity();
    protected abstract void defineGameObject();
    public abstract void update(float dt);
    public abstract int getObjectID();

    // SavePoint use
    public abstract void onSavePointHit(Vector2 player, String world, String level, String savePoint);
    public abstract void onGameObjectHitChangeTexture(Boolean textureChange);

    // Portal's use
    public abstract String getPortalMapTravelType();
    public abstract void onPortalTravelHit(String travelFrom, String travelTo);

    // object Door & switch: use
    public abstract void setRunDoors(boolean t);
    public abstract void active(Array<Obstacle> obstacles);
    public abstract void activeAfterBossDeath(Array<Obstacle> obstacles);

    // Ai object use
    public abstract void getHitBossSpawn();

}
