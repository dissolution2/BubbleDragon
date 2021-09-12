package com.mygdx.game.framework.debug.sprites.items;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.framework.debug.screens.PlayScreen;
import com.mygdx.game.framework.debug.util.GameUtility;
import com.mygdx.game.framework.debug.world.gameObjects.ItemObjectDef;

public class DragonEggGameItem extends ItemObjectDef {

    private String objectIdentity;
    private Array<TextureRegion> textureFrames;
    private Animation dragonEggAnimation; //FromMap, lifeAnimationFromSpawnPool;

    private int happenOnTime;

    private float stateTime;
    public boolean setToDestroy;
    private boolean destroyed;

    private int enemyDeadorMapSpawnID;

    private boolean drawItemBool; // = false;

    private boolean itemObjectOnGround;
    private boolean time_to_defineHit_Floor;

    private String eggCoolor;

    public DragonEggGameItem(PlayScreen screen, float x, float y, MapObject object, int id, String coolor) {
        super(screen, x, y, object);

        this.objectIdentity = object.getName();
        this.eggCoolor = coolor;

        this.enemyDeadorMapSpawnID = id;
        this.drawItemBool = false;

        textureFrames = new Array<TextureRegion>();
        // old textureFrames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/items/ItemDragonEgg.atlas").findRegion("GragonEgg"), 0, 0, 28, 39));
        textureFrames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/items/Item_Amo.atlas").findRegion("amo_blue_z1"), 0, 0, 57, 56));

        dragonEggAnimation = new Animation(0.15f, textureFrames);
        happenOnTime = 0;
        setBounds(getX(), getY(), 15 / GameUtility.PPM, 15 / GameUtility.PPM); //32
    }

    public DragonEggGameItem(PlayScreen screen, float x, float y, String name, int id, String col) {
        super(screen, x, y, name);

        this.objectIdentity = name;
        this.eggCoolor = col; //"GREEN";

        textureFrames = new Array<TextureRegion>();
        textureFrames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/items/Item_Amo.atlas").findRegion("amo_blue_z1"), 0, 0, 57, 56));

        dragonEggAnimation = new Animation(0.15f, textureFrames);
        happenOnTime = 0;
        setBounds(getX(), getY(), 15 / GameUtility.PPM, 15 / GameUtility.PPM); //32
    }

    @Override
    public String getObjectIdentity() {
        return objectIdentity;
    }

    private void setLinearImpulseAtSpawnTime(){

        this.b2body.applyLinearImpulse( 0.2f, 0.8f,b2body.getPosition().x, b2body.getPosition().y, true);
    }



    protected void defineItemObjectHitFloor() {

        Vector2 position = b2body.getPosition();
        world.destroyBody(b2body);

        BodyDef bdef = new BodyDef();
        bdef.position.set(position);
        bdef.type = BodyDef.BodyType.KinematicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(20 / 2 / GameUtility.PPM);
        fdef.filter.categoryBits = GameUtility.GAME_ITEM_BIT;

        /** testing !!! with ground and wall */
        fdef.filter.groupIndex = GameUtility.GAME_ITEM_BIT;

        fdef.filter.maskBits = GameUtility.GROUND_BIT |
            GameUtility.PLAYER_BIT;

        //fdef.restitution = 1.4f;
        fdef.isSensor = true;
        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        time_to_defineHit_Floor = false;
    }

    @Override
    protected void defineItemObject() {

        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(20 / 2 / GameUtility.PPM);
        fdef.filter.categoryBits = GameUtility.GAME_ITEM_BIT;

        /** testing !!! with ground and wall */
        fdef.filter.groupIndex = GameUtility.GAME_ITEM_BIT;

        fdef.filter.maskBits = GameUtility.GROUND_BIT |
                GameUtility.PLAYER_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);


    }

    @Override
    public void update(float dt) {
        stateTime += dt;
        //b2body.setGravityScale(1.0f);

        if( stateTime > 0.2 && happenOnTime == 0 ) {
            //setLinearImpulseAtSpawnTime();
            happenOnTime = 1;
            //System.out.println("HappenOnTime true");
        }

        if(setToDestroy && !destroyed){
            world.destroyBody(b2body);
            destroyed = true;
            //setRegion(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/ItemHeartAtlas.atlas").findRegion("HeartAnimation01"), 0, 0, 86, 69));
            stateTime = 0;
        }
        else if(!destroyed ) {
            //System.out.println("heart is updated!!!");
            //b2body.setLinearVelocity(velocity);

            if(itemObjectOnGround){
                time_to_defineHit_Floor = true;
            }

            if(time_to_defineHit_Floor){
                defineItemObjectHitFloor();

            }


            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
            setRegion((TextureRegion) dragonEggAnimation.getKeyFrame(stateTime, true));

            /*
            if( !objectIdentity.equals("EX_SPAWN_LIFE")) { //object.getName();)
                setRegion((TextureRegion) lifeAnimationFromMap.getKeyFrame(stateTime, true));
            }else{
                setRegion((TextureRegion) lifeAnimationFromSpawnPool.getKeyFrame(stateTime, true));
              //  System.out.println("ExtraLifeGameItem Class - Texture from spawnPool!! ");
            }
            */
        }
        //setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        //setRegion(getFrame(dt));

    }
    public void setEnemyHitGround() { this.itemObjectOnGround = true; }

    public void setToDestroy(){
        setToDestroy = true;
    }

    public String getEggCoolor(){return this.eggCoolor;}

    @Override
    public int getObjectID() {
        return 0;
    }

    public void draw(Batch batch){
        if(!destroyed )
            super.draw(batch);
    }


}
