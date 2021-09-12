package com.mygdx.game.framework.debug.sprites.Enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.framework.debug.managers.GameManagerAssets;
import com.mygdx.game.framework.debug.screens.PlayScreen;
import com.mygdx.game.framework.debug.sprites.powers.BalloneBullet;
import com.mygdx.game.framework.debug.util.GameUtility;

public class EnemyMimic extends SmallEnemyDef {

    private enum EnemyState {
        SET_INACTIVE_STATE,
        GUARDING,
        FIGHTING,
        FALLING,
        JUMPING,
        TAKINGDAMAGE,
        DEAD
    }

    private String objectIdentity;
    private Array<TextureRegion> textureFrames;
    private float stateTimer;
    private Animation guardingAnimation, walkAndAttackingkAnimation, enemyGetHit;
    private TextureRegion guarding;

    private EnemyState currentEnemyState, prevEnemyState, currentEnemyStateTime, prevEnemyStateTime;

    private int happenOnTime;


    public boolean setToDestroy;
    private boolean destroyed;
    private boolean runningRight;
    private boolean enemyIsDead;
    private boolean enemyIsHit;

    private GameManagerAssets gameManagerAssetsInstance;

    public EnemyMimic(PlayScreen screen, float x, float y, GameManagerAssets instance) {
        super(screen, x, y);

        this.gameManagerAssetsInstance = instance;

        textureFrames = new Array<TextureRegion>();

        guarding = new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/items/ItemChest.atlas").findRegion("chest01"), 0, 0, 78, 72);

        textureFrames.clear();

        textureFrames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/items/ItemChest.atlas").findRegion("chest01"), 0, 0, 78, 72));
        textureFrames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/items/ItemChest.atlas").findRegion("chest03"), 0, 0, 78, 72));

        walkAndAttackingkAnimation = new Animation(0.2f, textureFrames);

        textureFrames.clear();

        textureFrames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/items/ItemChest.atlas").findRegion("chest01"), 0, 0, 78, 72));
        textureFrames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/items/ItemChest.atlas").findRegion("chest03"), 0, 0, 78, 72));
        // change texture
        textureFrames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/items/ItemChest.atlas").findRegion("chest01"), 0, 0, 78, 72));
        textureFrames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/items/ItemChest.atlas").findRegion("chest03"), 0, 0, 78, 72));

        enemyGetHit = new Animation(0.2f, textureFrames);

        textureFrames.clear();

        stateTimer = 0;
        setToDestroy = false;
        destroyed = false;
        setBounds(getX(), getY(), 32 / GameUtility.PPM, 32 / GameUtility.PPM);

    }

    @Override
    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.StaticBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef_one = new FixtureDef();
        CircleShape shape_one = new CircleShape();
        shape_one.setRadius(35 / 2 / GameUtility.PPM);


        fdef_one.filter.categoryBits = GameUtility.GAME_ITEM_BIT;
        fdef_one.filter.maskBits = GameUtility.GROUND_BIT |
                GameUtility.GAME_AI_OBJECT_BIT |
                GameUtility.PLAYER_BIT |
                GameUtility.PLAYER_POWER_BIT;
        fdef_one.isSensor = true;
        fdef_one.shape = shape_one;
        b2body.createFixture(fdef_one).setUserData(this);
    }

    @Override
    public void update(float dt) {
        if(gameManagerAssetsInstance.getGameState() != GameManagerAssets.GameState.GAME_PAUSED ) {
            stateTimer += dt;
            //gravityTimer += dt;
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
            setRegion(getFrame(stateTimer));
        }
    }

    public TextureRegion getFrame(float dt) {

        currentEnemyStateTime = currentEnemyState = getState();
        //currentEnemyState = getState();
        TextureRegion region = null;

        switch(currentEnemyState) {
            case SET_INACTIVE_STATE:
                region = guarding;
                break;
            case GUARDING:
            case FALLING:
                region = guarding;
                break;
            case FIGHTING: //will have a animation for fall ?
            case JUMPING:
                region = (TextureRegion)walkAndAttackingkAnimation.getKeyFrame(stateTimer, true);
                break;
            case TAKINGDAMAGE: // Monster / boss that is to get spawned !!!
                region = (TextureRegion)enemyGetHit.getKeyFrame(stateTimer, true);
                break;
            case DEAD: // Monster / boss that is to get spawned !!!
                //region = (TextureRegion)enemyGetHit.getKeyFrame(stateTimer, true);
                region = (TextureRegion)enemyGetHit.getKeyFrame(stateTimer);
                break;
        }

        if((b2body.getLinearVelocity().x < 0 || !runningRight) && region.isFlipX()){
            region.flip(true, false);
            runningRight = false;
        }else if((b2body.getLinearVelocity().x > 0 || runningRight) && !region.isFlipX()){
            region.flip(true, false);
            runningRight = true;
        }

        stateTimer = currentEnemyStateTime == prevEnemyStateTime ? stateTimer + dt : 0;
        prevEnemyStateTime = currentEnemyStateTime;

        return region;
    }

    public EnemyState getState() {

        if (enemyIsDead) {
            return EnemyState.DEAD;
        }else if(enemyIsHit){
            return EnemyState.TAKINGDAMAGE;
        }else if(b2body.getLinearVelocity().x != 0 && !(b2body.getLinearVelocity().y > 0) && !(b2body.getLinearVelocity().y < 0)) { //b2body.getLinearVelocity().x != 0 && b2body.getLinearVelocity().y == 0 ) {
            return EnemyState.FIGHTING;
        }else if(b2body.getLinearVelocity().y < 0){
            //System.out.println("EnemyA getState -Falling y: " + b2body.getLinearVelocity().y );
            return EnemyState.FALLING;
        }else if(b2body.getLinearVelocity().y > 0) {
            //System.out.println("EnemyA getState -Jumping y: " + b2body.getLinearVelocity().y );
            return EnemyState.JUMPING;
        }else {
            return EnemyState.GUARDING;
        }
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

    public void setIsHit(boolean status) { enemyIsHit = status; }

    public boolean isHit(){ return enemyIsHit; }

    @Override
    public boolean getEnemyFaceDirection() {
        return runningRight;
    }

    public void draw(Batch batch){
        if(!destroyed) // || stateTimer < 0.5)
            super.draw(batch);
    }
}
