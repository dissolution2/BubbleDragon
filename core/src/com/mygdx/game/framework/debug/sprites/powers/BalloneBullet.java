package com.mygdx.game.framework.debug.sprites.powers;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.framework.debug.screens.PlayScreen;
import com.mygdx.game.framework.debug.sprites.BubblePlayer;
import com.mygdx.game.framework.debug.sprites.Enemies.EnemyA;
import com.mygdx.game.framework.debug.util.GameUtility;


public class BalloneBullet extends Sprite{


    public enum StateBulletAnimation { START_OF_ANIMATION_BULLET, END_OF_ANIMATION_BULLET  };

    public StateBulletAnimation currentBulletAnimationState, previousBulletAnimationState ;

    PlayScreen screen;
    World world;
    Array<TextureRegion> textureFrames;
    Animation balloneBulletAnimation;

    private TextureRegion ballonBulletBig;

    float stateTime;
    boolean destroyed;
    boolean setToDestroy;
    boolean fireRight;
    boolean isPlayerOnBallon;
    Body b2body;

    private String coolorBalloonPower;
    private float xBounds,yBounds;

    //testvar
    boolean bullet_Hit_enemy = false; // ok!!
    boolean player_Hit_ballooon_with_head = false;

    public BalloneBullet(World w, float x, float y, int powerBalloon, boolean fireRight  ) {//PlayScreen screen, float x, float y, int powerBalloon, boolean fireRight  ) {

        this.fireRight = fireRight;
        //this.screen = screen;
        this.world = w;//screen.getWorld();


        this.xBounds = x;
        this.yBounds = y;

        // testing on getFrame
        stateTime = 0;
        // Default Power
        textureFrames = new Array<TextureRegion>();
        //defineBallonBullet();
        init(powerBalloon);
    }

    // TODO how to sett player powers !! when he / she picks it up
    public void init(int powerToBeDrawn){


        switch (powerToBeDrawn) {
            case 1:
                this.textureFrames.clear();
                for(int i = 0; i < 4; i++){
                    textureFrames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/player/powers/BubblePowerAtlas.atlas").findRegion("BulletA"), i * 64, 0, 64, 64));
                }
                balloneBulletAnimation = new Animation(0.2f, textureFrames);
                setBalloneBulletDamageColor("Green");
                break;
            case 2:
                this.textureFrames.clear();
                for(int i = 0; i < 4; i++){
                    textureFrames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/player/powers/BubblePowerAtlas2.atlas").findRegion("BulletA"), i * 64, 0, 64, 64));
                }
                balloneBulletAnimation = new Animation(0.2f, textureFrames);
                setBalloneBulletDamageColor("Blue");
                break;
        }
        textureFrames.clear();
        //setBounds(x, y, 6 / Utility.PPM, 6 / Utility.PPM);
        setBounds(this.xBounds, this.yBounds, 40 / GameUtility.PPM, 40 / GameUtility.PPM);
        setRegion((TextureRegion) balloneBulletAnimation.getKeyFrame(0));
        defineBallonBullet();

    }



    public void hitOnHead(BubblePlayer player) {
        //System.out.println("In Ballone Hit");
        isPlayerOnBallon = true;
    }

    public void endhitOnHead(BubblePlayer userData) {
        //System.out.println("End hit In Ballone Hit");
        isPlayerOnBallon = false;
    }

    public boolean getPlayerOnBallon(){
        return isPlayerOnBallon;
    }

    /*
     * If we use Utility.GROUND_BIT with out that Player "BubblePlayer don't use categoryBits
     * we need to change State to include StateOnBallon ??
     *
     * Else we use categoryBits on Player also and use then WorldContactListener but might need to change State also then !!??
     */
    private void defineBallonBullet() {
        BodyDef bdef = new BodyDef();
        //bdef.position.set(fireRight ? getX() + 20 / Utility.PPM : getX() - 20 / Utility.PPM, getY()); // 12 / -12 Started to shoot to close to the player ???
        bdef.position.set(fireRight ? getX() + 22 / GameUtility.PPM : getX() - 22 / GameUtility.PPM, getY()); // 12 / -12 Started to shoot to close to the player ???
        bdef.type = BodyDef.BodyType.DynamicBody;
        if(!world.isLocked())
            b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(8 / GameUtility.PPM); // change to a Square if we want to jump on it or make a sensor on topp !!!
        // testing to take this one out to see if we still get stuck on ballon
        fdef.filter.categoryBits = GameUtility.PLAYER_POWER_BIT;


        fdef.filter.groupIndex = GameUtility.PLAYER_POWER_BIT;


        fdef.filter.maskBits = GameUtility.GROUND_BIT |
                GameUtility.WALL_AND_SEALING_BIT |
                GameUtility.GAME_OBJECT_BIT |
                GameUtility.WALL_JUMPING_BIT |
                GameUtility.ENEMY_LEGS_BIT   |
                GameUtility.ENEMY_BIT;

        fdef.shape = shape;
        fdef.isSensor = true;
        //fdef.restitution = 1; // makes it bounds from the height its shoot from
        //fdef.friction = 0.7f;
        b2body.createFixture(fdef).setUserData(this);
/*
        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2 / Utility.PPM, 6 / Utility.PPM), new Vector2(2 / Utility.PPM, 6 / Utility.PPM));
        fdef.filter.categoryBits = Utility.BALLONE_HEAD_BIT; //Change to Bubble bit
        fdef.shape = head;
        fdef.isSensor = true;
*/
/*
      //Create the Head here:
        PolygonShape head = new PolygonShape();
        Vector2[] vertice = new Vector2[4]; //try increase with 2
        vertice[0] = new Vector2(-12, 15).scl(1 / Utility.PPM);
        vertice[1] = new Vector2(12, 15).scl(1 / Utility.PPM);
        vertice[2] = new Vector2(-7, 7).scl(1 / Utility.PPM);
        vertice[3] = new Vector2(7, 7).scl(1 / Utility.PPM);
        head.set(vertice);

        fdef.shape = head;
        //fdef.restitution = 0.5f;
        fdef.filter.categoryBits = Utility.BALLONE_HEAD_BIT; //.GROUND_BIT;

        //fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData(this);
*/

/*
        EdgeShape head = new EdgeShape();
        //head.set(new Vector2(-4 / Utility.PPM, 12 / Utility.PPM), new Vector2( 4 / Utility.PPM, 12 / Utility.PPM));
        head.set(new Vector2(-4 / 32f, 12 / GameUtility.PPM), new Vector2( 4 / 32f, 12 / GameUtility.PPM));
        fdef.filter.categoryBits = GameUtility.BALLONE_HEAD_BIT;//.PLAYER_POWER_BIT;
        fdef.shape = head;
        fdef.friction = 2f;
*/
//        fdef.isSensor = true;

        //b2body.createFixture(fdef).setUserData(this);

        b2body.setGravityScale(0);
        //b2body.setLinearVelocity(new Vector2(fireRight ? 1.03f : -1.03f, 0f )); // ? 2 : -2 2.5f));
        b2body.setLinearVelocity(new Vector2(fireRight ? 2.03f : -2.03f, 0f )); // ? 2 : -2 2.5f));
    }



    public void update(float dt){
        stateTime += dt;
        //System.out.println("stateTimer: " + stateTime);
        // 0.8f end Animation
//      System.out.println("Animation Duration : " +  balloneBulletAnimation.getFrameDuration() );

        setRegion((TextureRegion) balloneBulletAnimation.getKeyFrame(stateTime, true));
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        if(stateTime >= 0.4f) {
            setRegion((TextureRegion) balloneBulletAnimation.getKeyFrame(0.4f, true));
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
            /* Switch Gravity when balloon is finished Test  OK work's */

            b2body.setGravityScale(0);
            b2body.setLinearVelocity(new Vector2(fireRight ? 0.1f : -0.1f, 0.40f ));
            /* gives Error Close App java serverTimer jv but not in android */
//        	b2body.setGravityScale(-1.4f); //Old
        }

        //System.out.println(balloneBulletAnimation.getKeyFrameIndex(stateTime));
        //System.out.println("Animation isF: " + balloneBulletAnimation.isAnimationFinished(stateTime));

// this is the actually code to destroy's Balloon
        if((stateTime > 5 || setToDestroy) && !destroyed) {
            world.destroyBody(b2body);
            //if(b2body.)
            b2body = null;
            destroyed = true;
            //setToDestroy = true;
        }else if(b2body.getLinearVelocity().y > 2f) {
            b2body.setLinearVelocity(b2body.getLinearVelocity().x, 2f);
        }else if((fireRight && b2body.getLinearVelocity().x < 0) || (!fireRight && b2body.getLinearVelocity().x > 0)) {
            setToDestroy();
        }

// this is the actually code to destroy's Balloon
    }

    public void setToDestroy(){
        setToDestroy = true;
    }

    public boolean getBalloneFireDirection(){
        return  this.fireRight;
    }

    public boolean isDestroyed(){
        return destroyed;
    }


    public boolean getHitwithBullet(){
        return bullet_Hit_enemy;
    }

    public void hitWithBullet(EnemyA userData) {
        // this works from world contact - case Utility.ENEMY_BIT | Utility.PLAYER_POWER_BIT:
        // == Utility.PLAYER_POWER_BIT
//System.out.println("enemy hit with bullet");
        //balloon_position_when_hit_enemy = b2body.getPosition().y;
//System.out.println("balloone Position : " + balloon_position_when_hit_enemy);
        //setToDestroy();
        bullet_Hit_enemy = true;
        setToDestroy = true;
        //PlayerHUD.addScore(100);
    }



    public boolean getPlayerHitBalloonWithHead() {
        return player_Hit_ballooon_with_head;
    }


    public void headWithBullet(BubblePlayer userData) {

//System.out.println("Player head with Balloon!!");
        player_Hit_ballooon_with_head = true;

    }

    public void setBalloneBulletDamageColor(String value){ this.coolorBalloonPower = value; }

    public String getBalloneBulletDamageColor(){ return this.coolorBalloonPower; }



}
