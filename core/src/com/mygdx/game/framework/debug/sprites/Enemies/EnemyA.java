package com.mygdx.game.framework.debug.sprites.Enemies;

import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.framework.debug.managers.GameManagerAITimer;
import com.mygdx.game.framework.debug.managers.GameManagerAssets;
import com.mygdx.game.framework.debug.screens.PlayScreen;
import com.mygdx.game.framework.debug.sprites.Enemies.state.EnemyAState;
import com.mygdx.game.framework.debug.sprites.powers.BalloneBullet;
import com.mygdx.game.framework.debug.util.GameUtility;


public class EnemyA extends SmallEnemyDef {

    private enum EnemyState {
        SET_INACTIVE_STATE,
        FALLING,
        //FLYING,
        JUMPING,
        GUARDING,
        //FIGHTING,
        //SHOOTING,
        RUNNING,
        TAKINGDAMAGE,
        DEAD
    }

    // testing StateMachine
    private StateMachine<EnemyA, EnemyAState> stateMachine;




    private float stateTime;
    private Animation walkAnimation, standingGuardAnimation, enemyGetHit, enemyDead;
    private Array<TextureRegion> frames;

    private float gravityFallingHard;

    private boolean setToDestroy;
    private boolean destroyed;
    private boolean runningRight;




    private boolean enemyIsDead;
    private boolean enemyIsHit;
    private int enemyLife;

    float angle;
    private float enemyStateTimer;

    private EnemyState currentEnemyState, prevEnemyState, currentEnemyStateTime, prevEnemyStateTime;

    private int enemyID;
    private String spawnItemType;
    private String spawnItemTypeActive;

    Vector2 collision = new Vector2(), normal = new Vector2();
    float dist;
    Vector2 enemyLosRayONE = new Vector2();
    float x1 =0f;
    float y1=0f;

    float x2 =0f;
    float y2=0f;

    float xProduct = 0;
    float yProduct = 0;

    boolean losOnPlayer = false;

    private Vector2 startPositionMap; // = new Vector2();

    private GameManagerAITimer enemyAJumpTimer;

    private float getDestroyedTimer;

    private Vector2 startPosition;

    private MapObject enemyMapObject;
    private boolean enemyOnGround;
    private boolean enemyHitWall;

    private GameManagerAssets gameManagerAssetsInstance;



    // this is new take a better look later on this way to implement super
    public EnemyA(PlayScreen screen, float x, float y, MapObject object, GameManagerAssets instance) { //}, int idOfEnemyFromTiledMap) {
        super(screen, x, y);

        this.gameManagerAssetsInstance = instance;
        this.startPositionMap = new Vector2(x,y); //.set(x,y);
        this.enemyAJumpTimer = new GameManagerAITimer();

        this.enemyMapObject = object;

        this.enemyID = object.getProperties().get("id", int.class); //idOfEnemyFromTiledMap;
        spawnItemType = object.getProperties().get("SpawnType", String.class);
        spawnItemTypeActive = object.getProperties().get("Active", String.class);

        frames = new Array<TextureRegion>();
        //for(int i = 0; i < 3; i++)
        //    frames.add(new TextureRegion(screen.getAtlas().findRegion("EnemyA"), i * 64, 0, 64, 64));

        //frames.add(new TextureRegion(screen.getAtlasPlayer().findRegion("EnemyA"), 0, 0, 64, 64));
        //frames.add(new TextureRegion(screen.getAtlasPlayer().findRegion("EnemyB"), 0, 0, 64, 64));
        //frames.add(new TextureRegion(screen.getAtlasPlayer().findRegion("EnemyC"), 0, 0, 64, 64));
        //screen.getAtlasEnemy().findRegion("EnemyA"), 0, 0, 64, 64));

        frames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/BubbleEnemyAtlas.atlas").findRegion("EnemyA"), 0, 0, 64, 64));
        frames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/BubbleEnemyAtlas.atlas").findRegion("EnemyB"), 0, 0, 64, 64));
        frames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/BubbleEnemyAtlas.atlas").findRegion("EnemyC"), 0, 0, 64, 64));



        walkAnimation = new Animation(0.3f, frames);

        frames.clear();

        frames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/BubbleEnemyAtlasHit.atlas").findRegion("EnemyA"), 0, 0, 64, 64));
        frames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/BubbleEnemyAtlas.atlas").findRegion("EnemyB"), 0, 0, 64, 64));
        frames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/BubbleEnemyAtlasHit.atlas").findRegion("EnemyC"), 0, 0, 64, 64));

        enemyGetHit = new Animation(0.3f, frames);

        frames.clear();

        frames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/BubbleEnemyAtlas.atlas").findRegion("EnemyDead"), 0, 0, 64, 64));

        enemyDead = new Animation(0.8f, frames);

        frames.clear();


        stateTime = 0;
        enemyStateTimer = 0;
        enemyOnGround = false;
        enemyHitWall = false;

        startPosition = new Vector2(getX() - getWidth() / 2, getY() - getHeight() / 2);

        setBounds(getX(), getY(), 32 / GameUtility.PPM, 32 / GameUtility.PPM);

        setToDestroy = false;
        destroyed = false;
        angle = 0;

        runningRight = false;


        this.enemyLife = 1;





        // x2 Is the length of the LOS Higher Value = Longer Range!! y2 Is to keep it at eye level straight ahead
        x2 = 0.94f;
        y2 = -0.2f;

// updateAllSpawnLifeFromEnemy on stateMachine
        //stateMachine = new DefaultStateMachine<EnemyA, EnemyAState>(this, EnemyAState.DEFAULT_START_BEHAVIOR_OF_ENEMYA);

//System.out.println( "EnemyA Class updateAllSpawnLifeFromEnemy: State Machine getPre state : " + stateMachine.getPreviousState() );
        //setEnemyState(EnemyState.IDLE);
        this.prevEnemyState = this.currentEnemyState = EnemyState.SET_INACTIVE_STATE;

        // this for animation time sequence
        this.prevEnemyStateTime = this.currentEnemyStateTime = EnemyState.SET_INACTIVE_STATE;
    }

    /*
     * If contact shift point to player, if shooting or jumping !!
     */
    RayCastCallback rayCallback = new RayCastCallback() {
        @Override
        public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {

            if( fixture.getBody().getType() != BodyType.StaticBody ) {
                dist = 1*fraction; // Closes collision to target !!
                collision.set(point);
                EnemyA.this.normal.set(normal).add(point);

                if( fixture.getFilterData().categoryBits == 4 ) {

                    losOnPlayer = true;

                }else {
                    losOnPlayer = false;
                }
            }
            return -1; //-1; // Continue with the rest of the fixtures
        }
    };

    public Vector2 getEnemyLosRayONE() {
        return enemyLosRayONE;
    }
    public Vector2 getEnemyCollision() {
        return collision;
    }
    public Vector2 getEnemyNormal() {
        return normal;
    }

    public Vector2 getEnemyStartMapPosition(){ return this.startPositionMap; }

    public int getEnemyID() {
        return this.enemyID;
    }

    public String getSpawnItemType() {return  this.spawnItemType; }
    public String getSpawnItemTypeActive() { return  this.spawnItemTypeActive; }
    public void setSpawnItemTypeActiveFalse(String value) {this.spawnItemTypeActive = value; }

    @Override
    public boolean getEnemyFaceDirection() {
        return runningRight;
    }

    /** updateAllSpawnLifeFromEnemy spawn item after death */
    public void setNewEnemyIDisDeadSpawnItem(int value){

        this.enemyID = value;
    }



    @Override
    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(20 / 2 / GameUtility.PPM);
        fdef.filter.categoryBits = GameUtility.ENEMY_BIT;

        /** testing !!! with ground and wall
         * Changed Collision categoryBits with ground groupIndex
         *  As. ENEMY_BIT with groupIndex the enemy collide with one and other... taken that away - see WorldContactListener
         */
        //fdef.filter.groupIndex = GameUtility.ENEMY_BIT;

        fdef.filter.maskBits = GameUtility.GROUND_BIT |
                //GameUtility.ENEMY_BIT |
                GameUtility.WALL_AND_SEALING_BIT |
                GameUtility.GAME_AI_OBJECT_BIT |
                GameUtility.PLAYER_BIT |
                GameUtility.PLAYER_POWER_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

/*
        //Create the Head here:
        EdgeShape lowerHead = new EdgeShape();

        //lowerHead.set(new Vector2(-10 / 14f, 6 / GameUtility.PPM), new Vector2(10 / 14f, 6 / GameUtility.PPM));
        lowerHead.set(new Vector2(-0 / 20f, 100 / GameUtility.PPM), new Vector2(-0 / 20f, -50 / GameUtility.PPM));

        fdef.filter.categoryBits = GameUtility.ENEMY_LEGS_BIT;
        fdef.filter.maskBits = GameUtility.GROUND_BIT;

        fdef.shape = lowerHead;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData(this);
*/
    }



    /**
     * We really need a better AI
     *
     */

    float walkingTimeRight =0;
    float walkingTimeLeft =0;
    float destroyTime = 0;

    float flytimerUp = 0;
    float flytimerDown = 0;
    float jumpTimer = 0;
    private float gravityTimer;
    private float death_Grapich_Timer = 0;
    private boolean death_Gapich_Timer_Activate = false;
    public StateMachine<EnemyA, EnemyAState> getStateMachine() {
        return stateMachine;
    }

    //test
    float hitTimer = 0;
    public void update(float dt) {

        if(gameManagerAssetsInstance.getGameState() != GameManagerAssets.GameState.GAME_PAUSED ) {
            //stateTime += dt;
//System.out.println("state: " + getEnemyState() );

            if(death_Gapich_Timer_Activate){

                death_Grapich_Timer += dt;

                if(death_Grapich_Timer > 1.5f){

                    setToDestroy = true;
                    death_Gapich_Timer_Activate = false;
                }

            }


            if (setToDestroy && !destroyed) {
                //System.out.println("EnemyA Update -setToDestroy-");
                //b2body.setActive(false);

                world.destroyBody(b2body);

                //b2body = null;
                destroyed = true;
                //System.out.println("Enemy is destroyed!!!");
                // if a dead Texture we want to have!!! set it here

                //enemyGetHit
                //stateTime = 0; // So draw will, draw the dead enemy for 0.0f -> 1f times, then puff gone!!

            } else if (!destroyed) {

                if(isHit()) {
                    hitTimer += dt;
                    if (this.enemyLife == 0 || this.enemyLife < 0 ) {
                        die();
                    }

                    if(hitTimer > 0.3f){
                        setIsHit(false);
                        hitTimer = 0;
                    }

                }

                if (!runningRight) {
                    xProduct = getX() - x2;
                    yProduct = getY() - y2;
                } else {
                    xProduct = getX() + x2;
                    yProduct = getY() - y2;
                }

                enemyLosRayONE.set(xProduct, yProduct);
                try {
                    world.rayCast(rayCallback, this.b2body.getPosition(), enemyLosRayONE);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Error on rayCasting EnemyA: " + e + " enemy ID: " + this.enemyID );
                }

                if (!isDead()) {

                    if (losOnPlayer) {
                        enemyAJumpTimer.updateCurrentTime(dt);

                        if (runningRight && getEnemyGroundBool()) {
                            setEnemyToJump(true); // true - false direction
                        } else {
                            setEnemyToJump(false);
                        }

                    }

                    if (enemyAJumpTimer.getCurrentTime() > 0.5f) {
                        losOnPlayer = false;

                    }
                    if (enemyAJumpTimer.getCurrentTime() > 0.8f) {
                        jump = 0; // set enemy up to be able to jump again.
                        enemyAJumpTimer.resetCurrentTime();
                    }


                    if (!losOnPlayer && getEnemyGroundBool() ) {
                       b2body.setLinearVelocity(velocityMovment);
                    }


                    //ToDo :: this was now a prob !!!
                    if(b2body.getPosition().y < 1.95f){
                        //b2body.setLinearVelocity(0f,-1.2f);

                    }

                    /** if enemy falls of the map set it to destroy */
                    if(b2body.getPosition().y < 0.5f){
                        setToDestroy = true;
                        //System.out.println("EnemyA check on position < 0.5f setToDestroy");
                    }
/*
                    System.out.println("getEnemyID: "
                                    + getEnemyID()  +
                            "Enemy Vel: " + b2body.getLinearVelocity() );

                    System.out.println("getEnemyID: "
                            + getEnemyID()  +
                            " Enemy State: " + getEnemyState() );
*/

                    /*
                    System.out.println("getEnemyID: "
                            + getEnemyID()  + " getY(): "
                            + getY() + " Pos.y: "
                            + b2body.getPosition().y + " starP.y: "
                            + startPosition.y );

                            System.out.println("getEnemyID: "
                            + getEnemyID() + " posY: "
                            + b2body.getPosition().y );
                    */
                }

            }
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
            setRegion(getFrame(dt));

        }
    }


    int jump = 0;
    public void setEnemyToJump(boolean direction){

        if(!direction) {
            if (!(jump > 0)) {
                this.b2body.applyLinearImpulse(new Vector2(-0.8f, 2.5f), this.b2body.getWorldCenter(), true); // -1.4f
                jump = 1;
            }
        }else{
            if (!(jump > 0)) {
                this.b2body.applyLinearImpulse(new Vector2(0.8f, 2.5f), this.b2body.getWorldCenter(), true); // 1.4f
                jump = 1;
            }
        }

    }

    @Override
    public void hitWithBullet(BalloneBullet bullet) {
        //enemyIsHitByPlayerPower(bulletCoolor);//bullet);
    }

    @Override
    public void hitWithBullet(String bulletCoolor) { //BalloneBullet bullet) {
        enemyIsHitByPlayerPower(bulletCoolor);//bullet);
    }



    public void enemyIsHitByPlayerPower(String balloneBullet){ //BalloneBullet balloneBullet){

        System.out.println("Enemy Hit by Power: " + balloneBullet);
        if(!isHit()) { // if isHit = false, set true

            setIsHit(true);

            if(balloneBullet.equals("Green")){
                this.enemyLife--;
                //System.out.println("Enemy: " + this.enemyID + " life: " + this.enemyLife);
            }
            if(balloneBullet.equals("Blue")){
                this.enemyLife = this.enemyLife - 2;
                //System.out.println("Enemy: " + this.enemyID + " life: " + this.enemyLife);
            }
        }
    }

    public void die() {
        //System.out.println("public void die() inn EnemyA");
        if (!isDead()) {

            //System.out.println("public void die() inside !isDead()");
            setIsDead(true);


            Filter filter = new Filter();
            filter.maskBits = GameUtility.NOTHING_BIT;

            for (Fixture fixture : b2body.getFixtureList()) {
                fixture.setFilterData(filter);
            }

            b2body.setLinearVelocity(0f,0f);
            b2body.applyLinearImpulse(new Vector2(0, 4.5f), b2body.getWorldCenter(), true);
        }
    }


    public void setIsDead(boolean status) { enemyIsDead = status;}

    public boolean isDead() { return enemyIsDead; }

    public void setIsHit(boolean status) { enemyIsHit = status; }

    public boolean isHit(){ return enemyIsHit; }

    public void enemyActionHover(float dt) {}




    public TextureRegion getFrame(float dt) {

        currentEnemyStateTime = currentEnemyState = getState();
        //currentEnemyState = getState();
        TextureRegion region = null;

        switch(currentEnemyState) {
            case DEAD: // Monster / boss that is to get spawned !!!
                //System.out.println("DEAD region called!!");
                region = (TextureRegion)enemyDead.getKeyFrame(stateTime, true);
                break;
            case TAKINGDAMAGE:
                region = (TextureRegion)enemyGetHit.getKeyFrame(stateTime, true);
                break;
            case SET_INACTIVE_STATE:
                region = (TextureRegion)walkAnimation.getKeyFrame(stateTime, true);
                break;
            case FALLING:
                region = (TextureRegion)walkAnimation.getKeyFrame(stateTime, true);
                break;
            case JUMPING:
                region = (TextureRegion)walkAnimation.getKeyFrame(stateTime, true);
                break;
            case RUNNING: // Monster / boss that is to get spawned !!!
                region = (TextureRegion)walkAnimation.getKeyFrame(stateTime, true);
                break;
            case GUARDING:
                region = (TextureRegion)walkAnimation.getKeyFrame(stateTime, true);
                break;
        }

        if((b2body.getLinearVelocity().x < 0 || !runningRight) && region.isFlipX()){
            region.flip(true, false);
            runningRight = false;
        }else if((b2body.getLinearVelocity().x > 0 || runningRight) && !region.isFlipX()){
            region.flip(true, false);
            runningRight = true;
        }

        stateTime = currentEnemyStateTime == prevEnemyStateTime ? stateTime + dt : 0;
        prevEnemyStateTime = currentEnemyStateTime;

        return region;
    }

    public EnemyState getState() {

        if (enemyIsDead) {
            setEnemyState(EnemyState.DEAD);
            return EnemyState.DEAD;
        }else if(enemyIsHit){
            setEnemyState(EnemyState.TAKINGDAMAGE);
            return EnemyState.TAKINGDAMAGE;
        }else if(b2body.getLinearVelocity().x != 0 && !(b2body.getLinearVelocity().y > 0) && !(b2body.getLinearVelocity().y < 0)) { //b2body.getLinearVelocity().x != 0 && b2body.getLinearVelocity().y == 0 ) {
            setEnemyState(EnemyState.RUNNING);
            return EnemyState.RUNNING;
        }else if(b2body.getLinearVelocity().y < 0){
            //System.out.println("EnemyA getState -Falling y: " + b2body.getLinearVelocity().y );
            setEnemyState(EnemyState.FALLING);
            return EnemyState.FALLING;
        }else if(b2body.getLinearVelocity().y > 0) {
            //System.out.println("EnemyA getState -Jumping y: " + b2body.getLinearVelocity().y );
            setEnemyState(EnemyState.JUMPING);
            return EnemyState.JUMPING;
        }else {
            setEnemyState(EnemyState.GUARDING);
            return EnemyState.GUARDING;
        }
    }

    public void draw(Batch batch){
        if(!destroyed){ // || stateTime < 0.5){
            super.draw(batch);
        }

    }

    @Override
    public void hitByEnemy(SmallEnemyDef smallEnemyDef) {
        // TODO Auto-generated method stub
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

    public boolean getIsDestroyed(){ return  this.destroyed; }


    public void setdeath_Gapich_Timer_Active(){

        death_Gapich_Timer_Activate = true;

    }

    public void setToDestroyed() {
        this.setToDestroy = true;
        //System.out.println("EnemyA call to sett enemyA ToDestroy = true!! " );

        //world.destroyBody(b2body);

    }

    public boolean getIsSetToDestroy() { return this.setToDestroy; }

    public Boolean getIsRunningRight() { return this.runningRight; }

    public void setIsRunningRight(boolean value){this.runningRight = value;}

    public void setLinearVelocity(){ this.velocityMovment = new Vector2(0f,0f); }

    public EnemyState getpreEnemyState() {

        return this.prevEnemyState;
    }

    public PlayScreen getEnemyPlayScreen() {return  this.screen; }

    public MapObject getEnemyMapObject() {return this.enemyMapObject; }


    public void setEnemyHitGround(){ this.enemyOnGround = true; }
    public void setEnemyLeftGround(){ this.enemyOnGround = false; }
    public boolean getEnemyGroundBool(){ return this.enemyOnGround; }

    public void setEnemyHitWall(){ this.enemyHitWall = true; }
    public void setEnemyLeftWall(){ this.enemyHitWall = false; }
    public boolean getEnemyWallBool(){ return this.enemyHitWall;}

    @Override
    public void closeAttack(SmallEnemyDef smallEnemyDef) {
        // TODO Auto-generated method stub

    }

    @Override
    public void rangeAttack(SmallEnemyDef smallEnemyDef) {

    }

    @Override
    public void rangeAttackFrenzy(SmallEnemyDef smallEnemyDef, boolean right) {

    }

    @Override
    public void seekerAttack(SmallEnemyDef smallEnemyDef){

    }

    @Override
    public void frenzyAttack(SmallEnemyDef smallEnemyDef, float seed) {

    }

    @Override
    public void closeAttackEnd(SmallEnemyDef smallEnemyDef) {
        // TODO Auto-generated method stub

    }
}

