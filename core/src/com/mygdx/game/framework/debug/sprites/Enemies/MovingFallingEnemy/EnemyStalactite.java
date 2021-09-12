package com.mygdx.game.framework.debug.sprites.Enemies.MovingFallingEnemy;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.framework.debug.managers.GameManagerAssets;
import com.mygdx.game.framework.debug.screens.PlayScreen;
import com.mygdx.game.framework.debug.util.GameUtility;

public class EnemyStalactite extends MovingFallingEnemyDef {

    private enum EnemyState {
        SET_INACTIVE_STATE,
        GUARDING,
        FALLING,
        DESTROYED
    }
    private float stateTimer;
    private int enemyID;
    private Vector2 startPositionMap;
    private GameManagerAssets gameManagerAssetsInstance;
    private MapObject enemyMapObject;

    private EnemyState currentEnemyState, prevEnemyState, currentEnemyStateTime, prevEnemyStateTime;
    private Animation stalactitFallAnimation;
    private Array<TextureRegion> frames;

    private boolean enemyIsDestroyed;
    private boolean enemyIsDead;

    private boolean enemyIsHit;

    private boolean enemyOnGround;
    private boolean time_to_defineHit_Floor;
    private boolean setToDestroy;
    private boolean destroyed;

    public EnemyStalactite(PlayScreen screen, float x, float y, MapObject object, GameManagerAssets instance) {
        super(screen, x, y);

        this.gameManagerAssetsInstance = instance;
        this.startPositionMap = new Vector2(x,y);
        this.enemyMapObject = object;
        this.enemyID = object.getProperties().get("id", int.class); //idOfEnemyFromTiledMap;

        frames = new Array<TextureRegion>();
        frames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/FallingStalg.atlas").findRegion("fallingStalg"), 0, 0, 99, 136));

        stalactitFallAnimation = new Animation(0.2f, frames);
        frames.clear();

        stateTimer = 0;
        enemyOnGround = false;
        setToDestroy = false;
        destroyed = false;

        setBounds(getX(), getY(), 50 / GameUtility.PPM, 65 / GameUtility.PPM);

        this.prevEnemyState = this.currentEnemyState = EnemyState.SET_INACTIVE_STATE;
        // this for animation time sequence
        this.prevEnemyStateTime = this.currentEnemyStateTime = EnemyState.SET_INACTIVE_STATE;
    }


    protected void defineMovingFallingEnemyHitFloor() {

        Vector2 position = b2body.getPosition();
        world.destroyBody(b2body);

        BodyDef bdef = new BodyDef();
        bdef.position.set(position);
        bdef.type = BodyDef.BodyType.StaticBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(20 / 2 / GameUtility.PPM);
        //fdef.filter.categoryBits = GameUtility.ENEMY_BIT;

        /** testing !!! with ground and wall */
        //fdef.filter.groupIndex = GameUtility.ENEMY_BIT;

        fdef.filter.maskBits = GameUtility.GROUND_BIT; // |
                //GameUtility.ENEMY_LEGS_BIT |
                //GameUtility.PLAYER_BIT |
                //GameUtility.PLAYER_POWER_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        time_to_defineHit_Floor = false;
    }

    @Override
    protected void defineMovingFallingEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(20 / 2 / GameUtility.PPM);
        fdef.filter.categoryBits = GameUtility.ENEMY_BIT;

        /** testing !!! with ground and wall */
        fdef.filter.groupIndex = GameUtility.ENEMY_BIT;

        fdef.filter.maskBits = GameUtility.GROUND_BIT |
                GameUtility.PLAYER_BIT |
                GameUtility.PLAYER_POWER_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
    }

    private float death_Grapich_Timer = 0;
    private boolean death_Gapich_Timer_Activate = false;
    @Override
    public void update(float dt) {
        if(gameManagerAssetsInstance.getGameState() != GameManagerAssets.GameState.GAME_PAUSED ) {


            if(enemyOnGround){
                time_to_defineHit_Floor = true;
                //System.out.println("Stalg on the floor");
                //setdeath_Gapich_Timer_Active();
            }

            /** Stag Enemy after hit floor player don't take damage and cant move it */
            if(time_to_defineHit_Floor){
                defineMovingFallingEnemyHitFloor();
                //System.out.println("define activated and timer");
                //setToDestroy = true; // get's destroyed immediately
                //death_Gapich_Timer_Activate = true;
            }


            if(death_Gapich_Timer_Activate){

                death_Grapich_Timer += dt;
//System.out.println("destroy timer: " + death_Grapich_Timer);

                if(death_Grapich_Timer > 1.5f){

                    System.out.println("setTodestroy id " + this.enemyID );
                    setToDestroy = true;
                    death_Gapich_Timer_Activate = false;
                    death_Grapich_Timer = 0;
                }
            }

            if(setToDestroy && !destroyed) {
//System.out.println("stalg object get's destroyed !! id " + this.enemyID );



                world.destroyBody(this.b2body);
                destroyed = true;
System.out.println("EnemyStalcitite is destroyed");
            }else if (!destroyed){

                if(isHit()) {
                   die();
                }

            }

            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
            setRegion(getFrame(dt));
        }
    }

    public TextureRegion getFrame(float dt) {

        currentEnemyStateTime = currentEnemyState = getState();
        //currentEnemyState = getState();
        TextureRegion region = null;

        switch (currentEnemyState) {
            case SET_INACTIVE_STATE:
                region = (TextureRegion) stalactitFallAnimation.getKeyFrame(stateTimer, true);
                break;
            case GUARDING:
                region = (TextureRegion) stalactitFallAnimation.getKeyFrame(stateTimer, true);
                break;
            case FALLING:
                region = (TextureRegion) stalactitFallAnimation.getKeyFrame(stateTimer, true);
                break;
            case DESTROYED: // same as dead on regular enemy's
                //ToDo:: change graphcs
                region = (TextureRegion) stalactitFallAnimation.getKeyFrame(stateTimer, true);
                break;
        }



        stateTimer = currentEnemyStateTime == prevEnemyStateTime ? stateTimer + dt : 0;
        prevEnemyStateTime = currentEnemyStateTime;

        return region;
    }

    public EnemyState getState() {
        if (enemyIsDestroyed) {
            setEnemyState(EnemyState.DESTROYED);
            return EnemyState.DESTROYED;
        }else if(b2body.getLinearVelocity().y < 0){
            //System.out.println("EnemyA getState -Falling y: " + b2body.getLinearVelocity().y );
            setEnemyState(EnemyState.FALLING);
            return EnemyState.FALLING;
        }else {
            setEnemyState(EnemyState.GUARDING);
            return EnemyState.GUARDING;
        }
    }


    public void setEnemyState(EnemyState state) {

        if(!currentEnemyState.equals(state)) {
            this.prevEnemyState = currentEnemyState;
            this.currentEnemyState = state; // new state, but be for we set a new the old must be sett into history
        }
    }

    public EnemyState getEnemyState() {

        return this.currentEnemyState;
    }

    public void setIsDead(boolean status) { enemyIsDestroyed = status;}

    public boolean isDead() { return enemyIsDestroyed; }

    @Override
    public int getMovingFallingEnemyObjectID() {
        return 0;
    }

    @Override
    public void hitWithPlayerPower() {

    }

    /** get set from ContactListener */
    public void setEnemyHitGround(){

        this.enemyOnGround = true;
        if(!isHit()){

            setIsHit(true);

        }
    }
    public boolean getEnemyHitGround(){ return this.enemyOnGround; }

    public void die() {
        //System.out.println("public void die() inn EnemyA");
        if (!isDead()) {

            //System.out.println("public void die() inside !isDead()");
            //setIsDead(true);


            Filter filter = new Filter();
            filter.maskBits = GameUtility.NOTHING_BIT;

            for (Fixture fixture : b2body.getFixtureList()) {
                fixture.setFilterData(filter);
            }

            setIsDead(true);

            System.out.println("Stalg set is dead finished!! id " + this.enemyID );
        }
    }


    public boolean getIsDestroyed(){ return  this.destroyed; }
    public void setToDestroyed(boolean value){this.setToDestroy = value;}

    public void setdeath_Gapich_Timer_Active(){

        death_Gapich_Timer_Activate = true;

    }

    public void setIsHit(boolean status) { enemyIsHit = status; }

    public boolean isHit(){ return enemyIsHit; }
    public int getEnemyID() {
        return this.enemyID;
    }

    public void draw(Batch batch){
        if(!destroyed) { // || stateTimer < 0.5)
            super.draw(batch);
        }
    }
}

