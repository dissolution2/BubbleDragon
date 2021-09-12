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

public class FireBall extends Sprite {

    PlayScreen screen;
    World world;
    Array<TextureRegion> textureFrames;
    Animation fireAnimation;
    float stateTime;
    boolean destroyed;
    boolean setToDestroy;
    boolean fireRight;

    boolean bullet_Hit_enemy = false;

    Body b2body;
    public FireBall(PlayScreen screen, float x, float y, boolean fireRight){
        this.fireRight = fireRight;
        this.screen = screen;
        this.world = screen.getWorld();

        textureFrames = new Array<TextureRegion>();
        for(int i = 0; i < 4; i++){
            textureFrames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/BubblePowerAtlas.atlas").findRegion("BulletA"), i * 64, 0, 64, 64));
        }
        fireAnimation = new Animation(0.2f, textureFrames);

        setRegion((TextureRegion)fireAnimation.getKeyFrame(0));
        setBounds(x, y, 20 / GameUtility.PPM, 20 / GameUtility.PPM);

        defineFireBall();
    }



    public void defineFireBall(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(fireRight ? getX() + 12  /GameUtility.PPM : getX() - 12  /GameUtility.PPM, getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        if(!world.isLocked()) {
            b2body = world.createBody(bdef);
            System.out.println("is the body created!!!");
        }
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(8 / GameUtility.PPM);
        fdef.filter.categoryBits = GameUtility.ENEMY_POWER_BIT;
        fdef.filter.maskBits = GameUtility.GROUND_BIT |
                GameUtility.PLAYER_BIT;

        fdef.shape = shape;
        //fdef.restitution = 1;
        //fdef.friction = 0;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData(this);


        b2body.setLinearVelocity(new Vector2(fireRight ? 2f : -2f, 2.5f));
    }

    public void update(float dt){
        stateTime += dt;

        setRegion((TextureRegion) fireAnimation.getKeyFrame(stateTime, true));
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        if(stateTime >= 0.4f) {
            setRegion((TextureRegion) fireAnimation.getKeyFrame(0.4f, true));
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
            /* Switch Gravity when balloon is finished Test  OK work's */

            //b2body.setGravityScale(0);
            b2body.setLinearVelocity(new Vector2(fireRight ? 1.7f : -1.7f, -0.5f)); //0.40f ));
            /* gives Error Close App java serverTimer jv but not in android */
//        	b2body.setGravityScale(-1.4f); //Old
        }


        if((stateTime > 3 || setToDestroy) && !destroyed) {
            world.destroyBody(b2body);
            b2body = null;
            destroyed = true;
        }
        if(b2body.getLinearVelocity().y > 2f)
            b2body.setLinearVelocity(b2body.getLinearVelocity().x, 2f);
        if((fireRight && b2body.getLinearVelocity().x < 0) || (!fireRight && b2body.getLinearVelocity().x > 0))
            setToDestroy();
    }

    public void setToDestroy(){
        setToDestroy = true;
    }

    public boolean isDestroyed(){
        return destroyed;
    }

    public boolean getHitwithBullet(){
        return bullet_Hit_enemy;
    }


}
