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

public class EnemyKnightRangeAttack extends Sprite {

    PlayScreen screen;
    World world;
    Array<TextureRegion> textureFrames;
    Animation fireAnimation;
    float stateTime;
    boolean destroyed;
    boolean setToDestroy;
    boolean fireRight;
    private float xBounds,yBounds;

    private float enemyKnightDevilPositionX;
    private float bulletStartPosition;

    //testvar
    boolean bullet_Hit_enemy = false; // ok!!

    Body b2body;
    public EnemyKnightRangeAttack(PlayScreen screen, float x, float y, boolean fireRight, float enemyPositionFireX){
        this.fireRight = fireRight;
        this.screen = screen;
        this.world = screen.getWorld();

        this.xBounds = x;
        this.yBounds = y;
        //this.bulletStartPosition = fireRight ? getX() + 87 / GameUtility.PPM : getX() - 87 / GameUtility.PPM;
        this.enemyKnightDevilPositionX = enemyPositionFireX;

        textureFrames = new Array<TextureRegion>();
        /*
        for(int i = 0; i < 4; i++){
            frames.add(new TextureRegion(screen.getAtlas().findRegion("fireball"), i * 8, 0, 8, 8));
        }
        fireAnimation = new Animation(0.2f, frames);
        */
        for(int i = 0; i < 4; i++){
            textureFrames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/player/powers/BubblePowerAtlas.atlas").findRegion("BulletA"), i * 64, 0, 64, 64));
        }
        fireAnimation = new Animation(0.2f, textureFrames);

        setRegion((TextureRegion)fireAnimation.getKeyFrame(0));
        setBounds(this.xBounds, this.yBounds, 40 / GameUtility.PPM, 40 / GameUtility.PPM);
        define();
    }

    public void define(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(fireRight ? getX() + 87 / GameUtility.PPM : getX() - 87 / GameUtility.PPM, getY() -0.4f );
        bdef.type = BodyDef.BodyType.KinematicBody;
        if(!world.isLocked())
            b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(8 / GameUtility.PPM);
        fdef.filter.categoryBits = GameUtility.ENEMY_POWER_BIT;
        fdef.filter.maskBits = GameUtility.GROUND_BIT |
                GameUtility.PLAYER_BIT;

        fdef.shape = shape;
        fdef.restitution = 1;
        //fdef.friction = 0;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData(this);
        b2body.setLinearVelocity(new Vector2(fireRight ? 1.7f : -1.7f, 0f)); //2.5f));
    }
    public void update(float dt){
        stateTime += dt;

        // 0.8f end Animation
//      System.out.println("Animation Duration : " +  balloneBulletAnimation.getFrameDuration() );

        setRegion((TextureRegion) fireAnimation.getKeyFrame(stateTime, true));
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        if(stateTime >= 0.4f) {
            setRegion((TextureRegion) fireAnimation.getKeyFrame(0.4f, true));
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
            /* Switch Gravity when balloon is finished Test  OK work's */

            b2body.setGravityScale(0);
            b2body.setLinearVelocity(new Vector2(fireRight ? 1.7f : -1.7f, 0f)); //0.40f ));
            /* gives Error Close App java serverTimer jv but not in android */
//        	b2body.setGravityScale(-1.4f); //Old
        }

        //System.out.println(balloneBulletAnimation.getKeyFrameIndex(stateTime));
        //System.out.println("Animation isF: " + balloneBulletAnimation.isAnimationFinished(stateTime));

// this is the actually code to destroy's Balloon
        if((stateTime > 5 || setToDestroy) && !destroyed) {
            world.destroyBody(b2body);
            //b2body = null;
            destroyed = true;
        }


        if(b2body.getLinearVelocity().y > 2f)
            b2body.setLinearVelocity(b2body.getLinearVelocity().x, 2f);
        if((fireRight && b2body.getLinearVelocity().x < 0) || (!fireRight && b2body.getLinearVelocity().x > 0))
            setToDestroy();

        // bullet setToDestroy after (bullet Start 13.18 < (14.05 -3.70)) = traveled to
        if(!fireRight && this.b2body.getPosition().x < (enemyKnightDevilPositionX - 3.70f)){ // -0.87 enemyPos = bullet start Pos
            //System.out.println("buuletPos x: " + this.b2body.getPosition().x);
            setToDestroy();
        }

        // bullet setToDestroy after (bullet Start 14.05 > (14.05 + 3.70)) = traveled to
        if(fireRight && this.b2body.getPosition().x > (enemyKnightDevilPositionX + 3.70f)){
            //System.out.println("buuletPos x: " + this.b2body.getPosition().x);
            setToDestroy();
        }
    }

    public void setToDestroy(){
        setToDestroy = true;
    }

    public boolean getIsSetToDestroy() { return  setToDestroy; }

    public boolean isDestroyed(){
        return destroyed;
    }

    public boolean getHitwithBullet(){
        return bullet_Hit_enemy;
    }

    public boolean getDirectionFired(){ return this.fireRight; }

    public Body getB2body(){return this.b2body;}

    public void destroyBody(){
        this.b2body.getWorld().destroyBody(this.b2body);

    }
}
