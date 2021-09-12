package com.mygdx.game.framework.debug.sprites.items;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.framework.debug.screens.PlayScreen;
import com.mygdx.game.framework.debug.util.GameUtility;
import com.mygdx.game.framework.debug.world.gameObjects.ItemObjectDef;

public class TreasureChestGameItem extends ItemObjectDef {

    private String objectIdentity;
    private Array<TextureRegion> textureFrames;
    private Animation chestAnimation, chestOpenWithTreasureAnimation, chestOpenWithOutTreasureAnimation;

    private int happenOnTime;

    private float stateTime;
    public boolean setToDestroy;
    private boolean destroyed;
    private boolean changeTextureBool;

    private int enemyDeadorMapSpawnID;

    public TreasureChestGameItem(PlayScreen screen, float x, float y, MapObject object, int id) {
        super(screen, x, y, object);

        this.objectIdentity = object.getName();

        this.enemyDeadorMapSpawnID = id;
        changeTextureBool = false;

        textureFrames = new Array<TextureRegion>();
        textureFrames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/items/ItemChest.atlas").findRegion("chest01"), 0, 0, 78, 72));

        chestAnimation = new Animation(0.15f, textureFrames);

        textureFrames.clear();

        textureFrames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/items/ItemChest.atlas").findRegion("chest02"), 0, 0, 78, 72));
        chestOpenWithTreasureAnimation = new Animation(0.15f, textureFrames);

        textureFrames.clear();

        textureFrames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/items/ItemChest.atlas").findRegion("chest03"), 0, 0, 78, 72));
        chestOpenWithOutTreasureAnimation = new Animation(0.15f, textureFrames);

        happenOnTime = 0;
        setBounds(getX(), getY(), 78/ GameUtility.PPM, 72 / GameUtility.PPM); //32
    }

    public TreasureChestGameItem(PlayScreen screen, float x, float y, String name, int id) {
        super(screen, x, y, name);

        this.objectIdentity = name;
        changeTextureBool = false;

        textureFrames = new Array<TextureRegion>();
        textureFrames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/items/ItemChest.atlas").findRegion("chest01"), 0, 0, 78, 72));

        chestAnimation = new Animation(0.15f, textureFrames);
        happenOnTime = 0;
        setBounds(getX(), getY(), 78 / GameUtility.PPM, 72 / GameUtility.PPM); //32
    }

    @Override
    public String getObjectIdentity() {
        return objectIdentity;
    }

    @Override
    protected void defineItemObject() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.StaticBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef_one = new FixtureDef();
        CircleShape shape_one = new CircleShape();
        shape_one.setRadius(35 / 2 / GameUtility.PPM);


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
            destroyed = true;
            //setRegion(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/ItemHeartAtlas.atlas").findRegion("HeartAnimation01"), 0, 0, 86, 69));
            stateTime = 0;
        }
        else if(!destroyed ) {
            /** Had to twick the With and Height to be same pos close & open */
            if(!changeTextureBool) {
                setPosition(b2body.getPosition().x - getWidth() / 2.5f, b2body.getPosition().y - getHeight() / 1.7f);
                setRegion((TextureRegion) chestAnimation.getKeyFrame(stateTime, true));
            }else {
                setPosition(b2body.getPosition().x - getWidth() / 3, b2body.getPosition().y - getHeight() / 2.7f);
                setRegion((TextureRegion) chestOpenWithTreasureAnimation.getKeyFrame(stateTime, true));
            }
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

    @Override
    public int getObjectID() { return this.enemyDeadorMapSpawnID;}

    public void setChangeTexture(){
        changeTextureBool = true;
    }

    public void setToDestroy(){
        setToDestroy = true;
    }

    public void draw(Batch batch){
        if(!destroyed )
            super.draw(batch);
    }
}
