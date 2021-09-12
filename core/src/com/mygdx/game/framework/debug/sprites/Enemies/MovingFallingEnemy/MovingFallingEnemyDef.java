package com.mygdx.game.framework.debug.sprites.Enemies.MovingFallingEnemy;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.framework.debug.screens.PlayScreen;

public abstract class MovingFallingEnemyDef extends Sprite {

    protected World world;
    protected PlayScreen screen;
    public Body b2body;

    public MovingFallingEnemyDef(PlayScreen screen, float x, float y) {
       this.world = screen.getWorld();
       this.screen = screen;
       setPosition(x, y);

       defineMovingFallingEnemy();
       b2body.setActive(false);
    }

    protected abstract void defineMovingFallingEnemy(); //String type, String direction);
    public abstract void update(float dt);
    public abstract int getMovingFallingEnemyObjectID();
    public abstract void hitWithPlayerPower();
}
