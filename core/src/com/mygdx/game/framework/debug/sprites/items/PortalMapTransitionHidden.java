package com.mygdx.game.framework.debug.sprites.items;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.framework.debug.screens.PlayScreen;
import com.mygdx.game.framework.debug.util.GameUtility;
import com.mygdx.game.framework.debug.world.gameObjects.GameObjectDef;
import com.mygdx.game.framework.debug.world.gameObstacles.Obstacle;

public class PortalMapTransitionHidden extends GameObjectDef {
    private String objectIdentity;
    private boolean savePointBooleanHit;


    private float stateTime;
    private boolean setToDestroy;
    private boolean destroyed;

    public String mapTravelType;
    public String mapPortalId;
    public String mapWorld;
    public String mapLevel;
    public String mapTravelLevel;
    public String mapTravelWorld;

    public String mapSensorDirection;
    public String mapPortalDirectionTraveld;

    public boolean time_to_reDefineVertical;

    private String active;

    public PortalMapTransitionHidden(PlayScreen screen, float x, float y, MapObject object) {
        super(screen, x, y, object);

        this.objectIdentity = object.getName();
        this.mapTravelType = object.getProperties().get("TRAVEL TYPE").toString();
        this.mapWorld = object.getProperties().get("MAP WORLD").toString();
        this.mapLevel = object.getProperties().get("MAP LEVEL").toString();
        this.mapTravelLevel = object.getProperties().get("TRAVEL LEVEL").toString();
        this.mapTravelWorld = object.getProperties().get("TRAVEL WORLD").toString();
        this.mapPortalId = object.getProperties().get("PORTAL ID").toString();
        this.mapPortalDirectionTraveld = object.getProperties().get("TRAVEL DIRECTION").toString();

        if(object.getProperties().containsKey("KEY_ACTIVE")) {
            this.active = object.getProperties().get("KEY_ACTIVE").toString();
        }

        if(object.getProperties().containsKey("DIRECTION")){
                this.mapSensorDirection = object.getProperties().get("DIRECTION").toString();
        }
/*
        System.out.println("Portal Class is made : "
        + object.getName() +
        " Function: " + this.mapTravelType +
        " Portal Id: " + this.mapPortalId +
        " Type: " + this.mapWorld +
        " Destination: " + this.mapTravelLevel );
*/
        savePointBooleanHit = false;
        setBounds(getX(), getY(), 64 / GameUtility.PPM, 64 / GameUtility.PPM); //32
    }

    @Override
    public String getObjectIdentity() {return this.objectIdentity; }

    /** VERTICAL Sensor */
    protected void defineGameObjectVertical(){

        Vector2 position = b2body.getPosition();
        world.destroyBody(b2body);

        BodyDef bdef = new BodyDef();
        bdef.position.set(position);
        bdef.type = BodyDef.BodyType.StaticBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef_one = new FixtureDef();
        CircleShape shape_one = new CircleShape();
        shape_one.setRadius(40 / 2 / GameUtility.PPM);


        fdef_one.isSensor = true;
        fdef_one.shape = shape_one; //savePointShape;

        b2body.createFixture(fdef_one).setUserData(this);

        EdgeShape lowerHead = new EdgeShape();
        lowerHead.set(new Vector2(-0 / 20f, 100 / GameUtility.PPM), new Vector2(-0 / 20f, -50 / GameUtility.PPM));
        fdef_one.filter.categoryBits = GameUtility.GAME_OBJECT_BIT;
        fdef_one.filter.maskBits = GameUtility.GROUND_BIT |
                GameUtility.PLAYER_BIT;

        fdef_one.shape = lowerHead;
        fdef_one.isSensor = true;
        b2body.createFixture(fdef_one).setUserData(this);

        time_to_reDefineVertical = false;
    }

    /** HORIZONTAL Sensor*/
    @Override
    protected void defineGameObject() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.StaticBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef_one = new FixtureDef();
        CircleShape shape_one = new CircleShape();
        shape_one.setRadius(40 / 2 / GameUtility.PPM);

        fdef_one.isSensor = true;
        fdef_one.shape = shape_one; //savePointShape;
        b2body.createFixture(fdef_one).setUserData(this);

        EdgeShape lowerHead = new EdgeShape();
        lowerHead.set(new Vector2(-10 / 14f, 6 / GameUtility.PPM), new Vector2(10 / 14f, 6 / GameUtility.PPM));
        fdef_one.filter.categoryBits = GameUtility.GAME_OBJECT_BIT;
        fdef_one.filter.maskBits = GameUtility.GROUND_BIT |
                GameUtility.PLAYER_BIT;

        fdef_one.shape = lowerHead;
        fdef_one.isSensor = true;
        b2body.createFixture(fdef_one).setUserData(this);
    }

    //working on hidden map transfer !???

    @Override
    public void update(float dt) {

        stateTime += dt;
        if(setToDestroy && !destroyed){
            world.destroyBody(b2body);
            destroyed = true;
            //setRegion(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/portalBlueAtlas.atlas").findRegion("PortalA"), 0, 0, 96, 96));
            stateTime = 0;
        }
        else if(!destroyed) {
            //b2body.setLinearVelocity(velocity);

            if( this.mapSensorDirection.equals("V")){
                time_to_reDefineVertical = true;
            }

            if(time_to_reDefineVertical){
                defineGameObjectVertical();
            }

            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
            //setRegion((TextureRegion) portalAnimation.getKeyFrame(stateTime, true));
        }





        //setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        //setRegion(getFrame(dt));
    }
/*
    public TextureRegion getFrame(float dt) {

        if(savePointBooleanHit) {
            return animationPlayerHit;
        }else {
            return animation;
        }
    }
*/
    @Override
    public int getObjectID() { return 0; }

    @Override
    public void onSavePointHit(Vector2 player, String world, String level, String savePoint) {}

    @Override
    public void onGameObjectHitChangeTexture(Boolean textureChange) {}

    @Override
    public void onPortalTravelHit(String travelFrom, String travelTo) {
        // travelFrom = LEVEL / WORLD
        // travelTo = if ( level = portal id ) || if ( world = destination = (world map) -Use = SavePoint 0
    }

    @Override
    public void setRunDoors(boolean t) {}

    @Override
    public void active(Array<Obstacle> obstacles) {}

    @Override
    public void activeAfterBossDeath(Array<Obstacle> obstacles) {

    }

    @Override
    public void getHitBossSpawn() {}

    public String getPortalMapTravelType(){ return this.mapTravelType; }
    public String getThisPortalMapWorld() { return  this.mapWorld; }
    public String getThisPortalMapLevel() { return  this.mapLevel; }
    public String getPortalID(){return this.mapPortalId; }
    public String getPortalMapDestinationLevel() { return  this.mapTravelLevel; }
    public String getPortalMapDestinationWorld(){return this.mapTravelWorld;}

    public String getMapPortalDirectionTraveld() { return this.mapPortalDirectionTraveld; }

    public String getMapPortalSensorDirectionTraveld() { return this.mapSensorDirection; }

    public String getActive(){ return this.active; }

    public Vector2 getSpawnPointFromPortal(){

        if(!time_to_reDefineVertical){

            return this.b2body.getPosition().add(0.425f,0.4f);
        }else {
            return this.b2body.getPosition().add(0.4f,0.4f);
        }

    }

/*
    public void draw(Batch batch){
        //if(!destroyed || stateTimer < 4) //1
        super.draw(batch);
    }
*/

}
