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
import com.mygdx.game.framework.debug.util.GameUtility;

public class EnemyKnightPowerSword extends Sprite {

    PlayScreen screen;
    World world;
    Array<TextureRegion> textureFrames;
    Animation fireAnimation;
    Body b2body;
    float stateTime;
    boolean destroyed;
    boolean setToDestroy;
    boolean fireRight;
    private float xBounds,yBounds;

    private float enemyKnightDevilPositionY;
    private boolean bullet_Hit_player = false; // ok!!
    private boolean testValueBool;

    public EnemyKnightPowerSword(PlayScreen screen, float x, float y, boolean fireRight, float enemyPositionFireY){
        this.fireRight = fireRight;
        this.screen = screen;
        this.world = screen.getWorld();

        this.xBounds = x;
        this.yBounds = y;

        this.enemyKnightDevilPositionY = enemyPositionFireY;
/*
        textureFrames = new Array<TextureRegion>();
        for(int i = 0; i < 4; i++){
            textureFrames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/BubblePowerAtlas.atlas").findRegion("BulletA"), i * 64, 0, 64, 64));
        }
        fireAnimation = new Animation(0.2f, textureFrames);

        setRegion((TextureRegion)fireAnimation.getKeyFrame(0));
*/
        setBounds(this.xBounds, this.yBounds, 40 / GameUtility.PPM, 40 / GameUtility.PPM);
        defineEnemyPowerSword();
    }

    public void defineEnemyPowerSword(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(fireRight ? getX() + 77 / GameUtility.PPM : getX() - 77 / GameUtility.PPM, getY() + 0.5f ); // -0.4f
        bdef.type = BodyDef.BodyType.KinematicBody;
        if(!world.isLocked())
            b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(22 / GameUtility.PPM);
        fdef.filter.categoryBits = GameUtility.ENEMY_POWER_BIT;
        fdef.filter.maskBits = GameUtility.GROUND_BIT |
                GameUtility.PLAYER_BIT;

        fdef.shape = shape;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData(this);
        b2body.setLinearVelocity(new Vector2(fireRight ? 0f : 0f, -2.2f)); //2.5f));
    }
    public void update(float dt){
        stateTime += dt;

        // 0.8f end Animation
        //System.out.println("Animation Duration : " +  balloneBulletAnimation.getFrameDuration() );

        //setRegion((TextureRegion) fireAnimation.getKeyFrame(stateTime, true));
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        if(stateTime >= 0.4f) {
            //setRegion((TextureRegion) fireAnimation.getKeyFrame(0.4f, true));
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
            /* Switch Gravity when balloon is finished Test  OK work's */

            b2body.setGravityScale(0);
            b2body.setLinearVelocity(new Vector2(fireRight ? 0f : 0f, -2.2f)); //0.40f ));
        }

        // this is the actually code to destroy's Balloon
        if((stateTime > 5 || setToDestroy) && !destroyed) {
            world.destroyBody(b2body);
            //b2body = null;
            destroyed = true;
        }


        if(b2body.getLinearVelocity().y > 2f)
            b2body.setLinearVelocity(b2body.getLinearVelocity().y, 2f);
        if((fireRight && b2body.getLinearVelocity().y > 0) || (!fireRight && b2body.getLinearVelocity().y > 0))
            setToDestroy();
        if(!fireRight && this.b2body.getPosition().y < (enemyKnightDevilPositionY - 0.70f)){
            //System.out.println("setToDestroy LeftFire");
            setToDestroy();
        }
        if(fireRight && this.b2body.getPosition().y < (enemyKnightDevilPositionY - 0.70f)){
            //System.out.println("setToDestroy RightFire" );
            setToDestroy();
        }

    }

    //public void setBoolBallDestroyed(boolean value){ testValueBool = value; }
    //public boolean getBoolBallDestroyed(){ return testValueBool; }

    public void setToDestroy(){ setToDestroy = true; }

    public boolean isDestroyed(){
        return destroyed;
    }

    public boolean getHitwithBullet(){
        return bullet_Hit_player;
    }

    public Body getB2body(){ return this.b2body; }

    public boolean getFireDirection(){ return fireRight; }
}
