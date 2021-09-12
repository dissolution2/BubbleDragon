package com.mygdx.game.framework.debug.sprites.Enemies.StationaryEnemies;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.framework.debug.screens.PlayScreen;

/** A Sensor Def abstract Class for now */
public abstract class StationaryEnemyDef extends Sprite {

    protected World world;
    protected PlayScreen screen;
    public Body b2body;

    public StationaryEnemyDef(PlayScreen screen, float x, float y) {
        this.world = screen.getWorld();
        this.screen = screen;
        setPosition(x, y);

        defineStationaryEnemy();
                //type.getProperties().get("Value").toString(),
                //type.getProperties().get("Direction").toString() );
        b2body.setActive(true);
    }

    protected abstract void defineStationaryEnemy(); //String type, String direction);
    public abstract void update(float dt);
    public abstract int getStationaryEnemyObjectID();
    public abstract void hitWithPlayerPower();

}
