package com.mygdx.game.framework.debug.sprites.Enemies.BossEnemy;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.framework.debug.screens.PlayScreen;
import com.mygdx.game.framework.debug.sprites.Enemies.SmallEnemyDef;
import com.mygdx.game.framework.debug.sprites.powers.BalloneBullet;
import com.mygdx.game.framework.debug.util.GameUtility;

public class SpringRobotBoss extends SmallEnemyDef {

    private float stateTimer;
    private Animation walkAnimation, standingGuardAnimation;
    private Array<TextureRegion> frames;

    private boolean setToDestroy;
    private boolean destroyed;
    private boolean runningRight;

    private boolean enemyIsDead;
    private boolean enemyIsHit;
    private int enemyLife = 2;

    private int enemyID;
    public SpringRobotBoss(PlayScreen screen, float x, float y, int idOfEnemyFromTiledMap) {
        super(screen, x, y);

        this.enemyID = idOfEnemyFromTiledMap;
        frames = new Array<TextureRegion>();

        frames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/enemyBoss/SpringRobotBoss.atlas").findRegion("SpringRobotBoss01"), 0, 0, 115, 133));


        walkAnimation = new Animation(0.4f, frames);
    }

    @Override
    protected void defineEnemy() {

    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void hitWithBullet(BalloneBullet bullet) {

    }

    @Override
    public void hitWithBullet(String bulletCoolor) {

    }

    @Override
    public void hitByEnemy(SmallEnemyDef smallEnemyDef) {

    }

    @Override
    public void closeAttack(SmallEnemyDef smallEnemyDef) {

    }

    @Override
    public void rangeAttack(SmallEnemyDef smallEnemyDef) {

    }

    @Override
    public void rangeAttackFrenzy(SmallEnemyDef smallEnemyDef, boolean right) {

    }

    @Override
    public void frenzyAttack(SmallEnemyDef smallEnemyDef, float seed) {

    }

    @Override
    public void seekerAttack(SmallEnemyDef smallEnemyDef) {

    }

    @Override
    public void closeAttackEnd(SmallEnemyDef smallEnemyDef) {

    }

    @Override
    public int getEnemyID() {
        return 0;
    }

    @Override
    public boolean getEnemyFaceDirection() {
        return false;
    }
}
