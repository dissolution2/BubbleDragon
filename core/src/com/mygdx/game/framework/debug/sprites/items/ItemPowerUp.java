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

public class ItemPowerUp extends ItemObjectDef {

    private String objectIdentity;
    private int itemId;
    private String itemPowerUp;
    private Array<TextureRegion> textureFrames;
    private Animation wallJumpPowerUp, dashPowerUp, weaponPowerUpBlue;

    private float stateTime;
    public boolean setToDestroy;
    private boolean destroyed;

    public ItemPowerUp(PlayScreen screen, float x, float y, MapObject object, int id) {
        super(screen, x, y, object);

        this.objectIdentity = object.getName();
        this.itemId = id;
        this.itemPowerUp = object.getProperties().get("Type").toString();

        textureFrames = new Array<TextureRegion>();

        //old
        // textureFrames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/items/ItemPowerUpWJAtlas.atlas").findRegion("powerUp"), 0, 0, 65, 82));

        //new !!
        textureFrames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/items/Item_SkullAtlas.atlas").findRegion("1"), 0, 0, 65, 82));
        textureFrames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/items/Item_SkullAtlas.atlas").findRegion("2"), 0, 0, 65, 82));
        textureFrames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/items/Item_SkullAtlas.atlas").findRegion("3"), 0, 0, 65, 82));
        textureFrames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/items/Item_SkullAtlas.atlas").findRegion("4"), 0, 0, 65, 82));
        textureFrames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/items/Item_SkullAtlas.atlas").findRegion("5"), 0, 0, 65, 82));
        textureFrames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/items/Item_SkullAtlas.atlas").findRegion("6"), 0, 0, 65, 82));



        //textureFrames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/items/ItemHeartAtlas.atlas").findRegion("HeartAnimation02"), 0, 0, 74, 58));
        //textureFrames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/items/ItemHeartAtlas.atlas").findRegion("HeartAnimation03"), 0, 0, 86, 69));
        //textureFrames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/items/ItemHeartAtlas.atlas").findRegion("HeartAnimation02"), 0, 0, 74, 58));

        wallJumpPowerUp = new Animation(0.15f, textureFrames);

        textureFrames.clear();

        //textureFrames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/items/ItemPowerUpWJAtlas.atlas").findRegion("powerUp"), 0, 0, 65, 82));
        textureFrames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/items/Item_SkullAtlas.atlas").findRegion("1"), 0, 0, 65, 82));
        textureFrames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/items/Item_SkullAtlas.atlas").findRegion("2"), 0, 0, 65, 82));
        textureFrames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/items/Item_SkullAtlas.atlas").findRegion("3"), 0, 0, 65, 82));
        textureFrames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/items/Item_SkullAtlas.atlas").findRegion("4"), 0, 0, 65, 82));
        textureFrames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/items/Item_SkullAtlas.atlas").findRegion("5"), 0, 0, 65, 82));
        textureFrames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/items/Item_SkullAtlas.atlas").findRegion("6"), 0, 0, 65, 82));

        weaponPowerUpBlue = new Animation(0.15f, textureFrames);

        textureFrames.clear();

        //textureFrames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/items/ItemPowerUpWJAtlas.atlas").findRegion("powerUp"), 0, 0, 65, 82));
        textureFrames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/items/Item_SkullAtlas.atlas").findRegion("1"), 0, 0, 65, 82));
        textureFrames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/items/Item_SkullAtlas.atlas").findRegion("2"), 0, 0, 65, 82));
        textureFrames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/items/Item_SkullAtlas.atlas").findRegion("3"), 0, 0, 65, 82));
        textureFrames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/items/Item_SkullAtlas.atlas").findRegion("4"), 0, 0, 65, 82));
        textureFrames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/items/Item_SkullAtlas.atlas").findRegion("5"), 0, 0, 65, 82));
        textureFrames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/items/Item_SkullAtlas.atlas").findRegion("6"), 0, 0, 65, 82));

        dashPowerUp  = new Animation(0.15f, textureFrames);


        setBounds(getX(), getY(), 30 / GameUtility.PPM, 30 / GameUtility.PPM); //32
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
            destroyed = true;
            //setRegion(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/ItemHeartAtlas.atlas").findRegion("HeartAnimation01"), 0, 0, 86, 69));
            stateTime = 0;
        }
        else if(!destroyed ) {
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);

            switch (itemPowerUp){
                case "WALL JUMPING":
                    setRegion((TextureRegion) wallJumpPowerUp.getKeyFrame(stateTime, true));
                    break;
                case "WEAPON BLUE":
                    setRegion((TextureRegion) weaponPowerUpBlue.getKeyFrame(stateTime, true));
                    break;
                case "DASH":
                    setRegion((TextureRegion) dashPowerUp.getKeyFrame(stateTime, true));
                    break;
            }
        }
    }

    public void setToDestroy(){
        setToDestroy = true;
    }

    @Override
    public int getObjectID() {
        return itemId;
    }

    public String getItemPowerUpTypOfPower(){return this.itemPowerUp;}

    public void draw(Batch batch){
        if(!destroyed )
            super.draw(batch);
    }
}
