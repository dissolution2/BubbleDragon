package com.mygdx.game.framework.debug.sprites.items;


import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.framework.debug.managers.GameManagerAssets;
import com.mygdx.game.framework.debug.screens.PlayScreen;
import com.mygdx.game.framework.debug.world.gameObstacles.Obstacle;
import com.mygdx.game.framework.debug.util.GameUtility;
import com.mygdx.game.framework.debug.world.gameObjects.GameObjectDef;


// extends GameObjectDef - ContactListener 2 - 4 contact !!!
public class SavePoint extends GameObjectDef {

    //protected level world;
    //protected PlayScreen screen;
    public String mapWorld;
    public String mapLevel;
    public String mapSavePoint;
    public String mapSavePointVisible;

    public Vector2 positionSavePoint;

    private TextureRegion savePointTextureOn, savePointTextureOff;
    private boolean savePointBooleanHit;
    private String objectIdentity;

    private GameManagerAssets gameManagerAssetsInstance;

    public SavePoint(PlayScreen screen, float x, float y, MapObject object, GameManagerAssets instance) {
        super(screen, x, y, object);

        this.gameManagerAssetsInstance = instance;
        this.objectIdentity = object.getName();

        savePointTextureOn = new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/gameObjects/ItemAtlas.atlas").findRegion("savepointblue"), 0, 0, 68, 123);
        savePointTextureOff = new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/gameObjects/ItemAtlas.atlas").findRegion("savepointgreen"), 0, 0, 68, 123);

        if(object.getProperties().containsKey("WORLD")){
            this.mapWorld = object.getProperties().get("WORLD").toString();
            this.mapLevel = object.getProperties().get("LEVEL").toString();
            this.mapSavePoint = object.getProperties().get("SAVEPOINT").toString();
            this.mapSavePointVisible = object.getProperties().get("VISIBLE").toString();
        }
        positionSavePoint = new Vector2(getX(), getY());
/*
        System.out.println("SavePoint Class is made : "
        + object.getName() +
        " level: " + this.level +
        " SavePoint " + this.savePoint);
*/
        savePointBooleanHit = false;
        setBounds(getX(), getY(), 68f / GameUtility.PPM, 123f / GameUtility.PPM);
    }

    @Override
    public String getObjectIdentity() {
        return  this.objectIdentity;
    }

    public String getMapSavePointVisible() {return this.mapSavePointVisible; }

    public void update(float dt) {
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        //if(mapSavePointVisible.equals("true")) {
            setRegion(getFrame(dt));
        //}
    }


        // GameObjectDef
    @Override
    public int getObjectID() { return 0;}

    public TextureRegion getFrame(float dt) {

        if(savePointBooleanHit) {
            return savePointTextureOn;
        }else {
            return savePointTextureOff;
        }
    }

    public void setSavePointTextureOff(){ setRegion(savePointTextureOff); }
    public void setSavePointTextureOn(){ setRegion(savePointTextureOn); }

    // GameObjectDef
    @Override
    protected void defineGameObject(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.StaticBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef_one = new FixtureDef();
        CircleShape shape_one = new CircleShape();
        shape_one.setRadius(40 / 2 / GameUtility.PPM);

        //PolygonShape savePointShape = new PolygonShape();
        //savePointShape.setAsBox( (0.32500002f/2),(0.32500002f/2)) ;

        fdef_one.filter.categoryBits = GameUtility.GAME_OBJECT_BIT; //.GAME_SAVE_POINT_BIT;
        fdef_one.filter.maskBits = GameUtility.GROUND_BIT |
                //GameUtility.OBJECT_BIT |
                GameUtility.PLAYER_BIT; //|
        //GameUtility.ENEMY_A_BIT |
        //GameUtility.PLAYER_POWER_BIT;

        fdef_one.isSensor = true;
        fdef_one.shape = shape_one; //savePointShape;

        //b2body.setGravityScale(9.8f);
        //b2body.setLinearDamping(0.3f);

        b2body.createFixture(fdef_one).setUserData(this);

        FixtureDef fdef_two = new FixtureDef();
        CircleShape shape_two = new CircleShape();
        shape_two.setRadius(30 / 2 / GameUtility.PPM);
        shape_two.setPosition(new Vector2( 0 / GameUtility.PPM, -35 / GameUtility.PPM));

        //fdef_two.filter.categoryBits = GameUtility.GAME_SAVE_POINT_BIT;
        //fdef_two.filter.maskBits = GameUtility.GROUND_BIT |
        //        GameUtility.PLAYER_BIT;

        fdef_two.shape = shape_two;
        fdef_two.isSensor = true;
        //b2body.createFixture(fdef_two).setUserData(this);

    }

    @Override // Directly call GameManager to save data
    public void onSavePointHit(Vector2 player, String mapWorld, String mapLevel, String savePoint) {

        //System.out.println("SavePoint Class: onSavePointHit - positionPlayer: " + player +
        //       " mapWorld: " + mapWorld + " mapLevel: " + mapLevel + " savePoint " + savePoint);

        gameManagerAssetsInstance.setDataToSaveGamePlayer(player, mapWorld, mapLevel, savePoint);
    }

    @Override
    public void onGameObjectHitChangeTexture(Boolean textureChange) { savePointBooleanHit = true; }

    @Override
    public void onPortalTravelHit(String travelFrom, String travelTo) {

    }

    @Override
    public void setRunDoors(boolean t) {}
    public boolean getSavePointUsed(){ return savePointBooleanHit; }

    @Override
    public void active(Array<Obstacle> obstacles) {}

    @Override
    public void activeAfterBossDeath(Array<Obstacle> obstacles) {

    }

    @Override
    public void getHitBossSpawn() {}



    public Vector2 getPositionSavePoint(){ return  this.positionSavePoint; }
    public String getWorld(){ return this.mapWorld; }
    public String getLevel(){ return this.mapLevel; }
    public String getSavePoint(){ return this.mapSavePoint; }

    @Override
    public String getPortalMapTravelType() { return null; }


    public void draw(Batch batch){
        //if(this.mapSavePointVisible.equals("true"))
        super.draw(batch);
    }
}
