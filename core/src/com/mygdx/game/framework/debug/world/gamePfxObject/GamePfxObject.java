package com.mygdx.game.framework.debug.world.gamePfxObject;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.mygdx.game.framework.debug.screens.PlayScreen;
import com.mygdx.game.framework.debug.util.GameUtility;

/** use the position to create Weather or effects on screen -Not Player or Enemy's */
public class GamePfxObject extends PfxObjectDef{

    private String objectIdentity;

    private String mapMarkerType;
    private String mapMarkerAssociationNumber;

    public ParticleEffect particleEffect = new ParticleEffect();

    public GamePfxObject(PlayScreen screen, float x, float y, MapObject object) {
        super(screen, x, y, object);

        this.objectIdentity = object.getName();
        if(object.getProperties().containsKey("type")){
            this.mapMarkerType = object.getProperties().get("type").toString();
            this.mapMarkerAssociationNumber = object.getProperties().get("Association Number").toString();
        }

        particleEffect.load(Gdx.files.internal("particles/fireflyGreen"), Gdx.files.internal("particles"));

        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 4);

        particleEffect.setPosition(200f,200f); //b2body.getPosition().x * 100f, b2body.getPosition().y * 100f);
        particleEffect.start();
        setBounds(getX(), getY() , 25f / GameUtility.PPM, 25f / GameUtility.PPM);
    }

    @Override
    public String getObjectIdentity() {
        return this.objectIdentity;
    }

    @Override
    protected void defineGamePfxObject() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY() );//0.05f);
        bdef.type = BodyDef.BodyType.StaticBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef_one = new FixtureDef();
        CircleShape shape_one = new CircleShape();
        shape_one.setRadius(25 / 2 / GameUtility.PPM);

        //fdef_one.filter.categoryBits = GameUtility.GAME_OBJECT_BIT;
        //fdef_one.filter.maskBits = GameUtility.GROUND_BIT |
        //        GameUtility.ENEMY_BIT |
        //        GameUtility.PLAYER_BIT;

        fdef_one.isSensor = true;
        fdef_one.shape = shape_one;

        b2body.createFixture(fdef_one).setUserData(this);

        /*Head of the Sensor*/
        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-0 / 20f , 100 / GameUtility.PPM), new Vector2( -0 / 20f, -50 / GameUtility.PPM));


        //fdef_one.filter.categoryBits = GameUtility.GAME_PFX_OBJECT_BIT; //.GAME_OBJECT_BIT; - old

        //fdef_one.filter.groupIndex = GameUtility.GAME_AI_OBJECT_BIT;

        /*
        fdef_one.filter.maskBits = GameUtility.ENEMY_BIT |
                //GameUtility.ENEMY_LEGS_BIT |
                GameUtility.PLAYER_BIT;
        */

        fdef_one.shape = head;
        fdef_one.isSensor = true;

        b2body.createFixture(fdef_one).setUserData(this);
    }
/*
    public void setParticleEffectOn(boolean run){
        if(run) {
            particleEffect.start();
        }
    }
*/
    @Override
    public void update(float dt) {
        //setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 4);
        //particleEffect.setPosition(b2body.getPosition().x * 100f, b2body.getPosition().y * 100f);

        particleEffect.update(dt);
    }

    @Override
    public int getObjectID() {
        return 0;
    }

    public void drawParticleEffect(Batch batch, float dt){


        particleEffect.draw(batch, dt);
    }
}
