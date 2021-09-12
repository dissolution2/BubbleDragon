package com.mygdx.game.framework.debug.sprites.Enemies;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.framework.debug.screens.PlayScreen;
import com.mygdx.game.framework.debug.sprites.powers.BalloneBullet;

//import com.mygdx.game.sprites.BubblePlayer;
//import com.mygdx.game.sprites.powers.BalloneBullet;

public abstract class SmallEnemyDef extends Sprite {

    protected World world;
    protected PlayScreen screen;
    public Body b2body;
    public Vector2 velocityMovment, velocityCeroStanding;



    private float stateTimer;

    /*
     * comment out setGravityScale because it didn't work with SteeringEntityBoss !!!
     */
    public SmallEnemyDef(PlayScreen screen, float x, float y) {
        this.world = screen.getWorld();
        this.screen = screen;
        setPosition(x, y);
        defineEnemy();
        velocityMovment = new Vector2(-1.2f, 0f);//-1.2f); // -1 , -2
        velocityCeroStanding = new Vector2(0f, 0f);
        //b2body.setGravityScale(-10f);
        b2body.setActive(false);
    }

//TODO change this... stateMachine enum states ??!!!

    protected abstract void defineEnemy();



    //protected abstract EnemyAState getEnemyState();

    //protected abstract EnemyStates getEnemyState();



    public abstract void update(float dt);
    //public abstract void hitOnHead(BubblePlayer bubble);
    public abstract void hitWithBullet(BalloneBullet bullet);

    public abstract void hitWithBullet(String bulletCoolor);

    public abstract void hitByEnemy(SmallEnemyDef smallEnemyDef);

    //public abstract void hitWithBullet(SmallEnemyDef bullet);

    public abstract void closeAttack(SmallEnemyDef smallEnemyDef);
    public abstract void rangeAttack(SmallEnemyDef smallEnemyDef);
    public abstract void rangeAttackFrenzy(SmallEnemyDef smallEnemyDef, boolean right);
    public abstract void frenzyAttack(SmallEnemyDef smallEnemyDef, float seed);
    public abstract void seekerAttack(SmallEnemyDef smallEnemyDef);
    public abstract void closeAttackEnd(SmallEnemyDef smallEnemyDef);
    public abstract int getEnemyID();
    public abstract boolean getEnemyFaceDirection();
    //public abstract void closeAttack(BubblePlayer userData);



    // change this !!
    public void reverseVelocity(boolean x, boolean y){
        if(x)
            velocityMovment.x = -velocityMovment.x;
        if(y)
            velocityMovment.y = -velocityMovment.y;
    }

}

