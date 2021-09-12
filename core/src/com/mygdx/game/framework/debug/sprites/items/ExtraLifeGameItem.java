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
import com.mygdx.game.framework.debug.world.gameObjects.GameObjectDef;
import com.mygdx.game.framework.debug.world.gameObjects.ItemObjectDef;
import com.mygdx.game.framework.debug.world.gameObstacles.Obstacle;

public class ExtraLifeGameItem extends ItemObjectDef {//GameObjectDef {

    private String objectIdentity;
    private Array<TextureRegion> textureFrames;
    private Animation lifeAnimation; //FromMap, lifeAnimationFromSpawnPool;

    private float stateTime;
    public boolean setToDestroy;
    private boolean destroyed;

    private int enemyDeadorMapSpawnID;

    private boolean drawItemBool; // = false;

    public ExtraLifeGameItem(PlayScreen screen, float x, float y, MapObject object, int id) {
        super(screen, x, y, object);


        this.objectIdentity = object.getName();
        this.enemyDeadorMapSpawnID = id;
        this.drawItemBool = false;

        textureFrames = new Array<TextureRegion>();
        textureFrames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/items/ItemHeartAtlas.atlas").findRegion("HeartAnimation01"), 0, 0, 86, 69));
        textureFrames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/items/ItemHeartAtlas.atlas").findRegion("HeartAnimation02"), 0, 0, 74, 58));
        textureFrames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/items/ItemHeartAtlas.atlas").findRegion("HeartAnimation03"), 0, 0, 86, 69));
        textureFrames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/items/ItemHeartAtlas.atlas").findRegion("HeartAnimation02"), 0, 0, 74, 58));

        lifeAnimation = new Animation(0.15f, textureFrames);
        setBounds(getX(), getY(), 30 / GameUtility.PPM, 30 / GameUtility.PPM); //32
    }

    public ExtraLifeGameItem(PlayScreen screen, float x, float y, String name, int id) {
        super(screen, x, y, name);

        this.objectIdentity = "EX_LIFE";//object.getName();

        textureFrames = new Array<TextureRegion>();
        textureFrames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/items/ItemHeartAtlas.atlas").findRegion("HeartAnimation01"), 0, 0, 86, 69));
        textureFrames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/items/ItemHeartAtlas.atlas").findRegion("HeartAnimation02"), 0, 0, 74, 58));
        textureFrames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/items/ItemHeartAtlas.atlas").findRegion("HeartAnimation03"), 0, 0, 86, 69));
        textureFrames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/items/ItemHeartAtlas.atlas").findRegion("HeartAnimation02"), 0, 0, 74, 58));

        lifeAnimation = new Animation(0.15f, textureFrames);

        this.enemyDeadorMapSpawnID = id;
        setBounds(getX(), getY(), 30 / GameUtility.PPM, 30 / GameUtility.PPM); //32
    }

    @Override
    public String getObjectIdentity() {
        return objectIdentity;
    }

    public int getItemSpawnIDorEnemyDeadID() {
        return enemyDeadorMapSpawnID;
    }

    public boolean getDrawItemBool(){ return this.drawItemBool; }
    public void setDrawItemBool(boolean value){this.drawItemBool = value; }

    @Override
    protected void defineItemObject() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.StaticBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef_one = new FixtureDef();
        CircleShape shape_one = new CircleShape();
        shape_one.setRadius(40 / 2 / GameUtility.PPM);


        fdef_one.filter.categoryBits = GameUtility.GAME_ITEM_BIT;
        fdef_one.filter.maskBits = GameUtility.GROUND_BIT |
                GameUtility.PLAYER_BIT; //|
        fdef_one.isSensor = true;
        fdef_one.shape = shape_one;
        b2body.createFixture(fdef_one).setUserData(this);
    }

    @Override
    public void update(float dt) {

        stateTime += dt;
        if(setToDestroy && !destroyed){
            world.destroyBody(b2body);
            b2body = null;
            destroyed = true;
            stateTime = 0;
        }
        else if(!destroyed ) {
            //System.out.println("heart is updated!!!");
            //b2body.setLinearVelocity(velocity);
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
            setRegion((TextureRegion) lifeAnimation.getKeyFrame(stateTime, true));

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

    public void setToDestroy(){
        setToDestroy = true;
    }

    @Override
    public int getObjectID() {
        return 0;
    }


    public void draw(Batch batch){
        if(!destroyed )
            super.draw(batch);
    }

}
