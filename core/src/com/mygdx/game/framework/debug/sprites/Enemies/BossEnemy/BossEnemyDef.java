package com.mygdx.game.framework.debug.sprites.Enemies.BossEnemy;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.framework.debug.screens.PlayScreen;
import com.mygdx.game.framework.debug.sprites.powers.BalloneBullet;

public abstract class BossEnemyDef extends Sprite {

    protected World world;
    protected PlayScreen screen;
    public Body b2body;
    public Vector2 velocityMovment, velocityCeroStanding;



    private float stateTimer;

    public BossEnemyDef(PlayScreen screen, float x, float y) {
        this.world = screen.getWorld();
        this.screen = screen;
        setPosition(x, y);
        defineEnemyBoss();
        velocityMovment = new Vector2(-1.2f, 0f);//-1.2f); // -1 , -2
        velocityCeroStanding = new Vector2(0f, 0f);
        //b2body.setGravityScale(-10f);
        b2body.setActive(false);
    }

    protected abstract void defineEnemyBoss();
    public abstract void update(float dt);
    //public abstract void hitOnHead(BubblePlayer bubble);
    public abstract void hitWithBullet(BalloneBullet bullet);
    public abstract void hitByEnemy(BossEnemyDef bossEnemyDef);

    //public abstract void hitWithBullet(SmallEnemyDef bullet);

    public abstract void closeAttack(BossEnemyDef bossEnemyDef);
    public abstract void rangeAttack(BossEnemyDef bossEnemyDef);
    public abstract void rangeAttackFrenzy(BossEnemyDef bossEnemyDef, boolean right);
    public abstract void frenzyAttack(BossEnemyDef bossEnemyDef, float seed);
    public abstract void seekerAttack(BossEnemyDef bossEnemyDef);
    public abstract void closeAttackEnd(BossEnemyDef bossEnemyDef);
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
