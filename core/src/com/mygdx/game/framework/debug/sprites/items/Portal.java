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
import com.mygdx.game.framework.debug.world.gameObstacles.Obstacle;

/**
 * Portals - "GAME_PORTAL" -START-
 * DESTINATION (PORTAL NR)
 * FUNCTION (START)
 * PORTAL (ID)
 * TYPE (LEVEL) / (WORLD)
 *
 * -END-
 * FUNCTION (END)
 * PORTAL (ID)
 * TYPE (LEVEL) / -(WORLD)- We don't need as we travel to SavePoint 0
 *
 * TYPE (WORLD) - DESTINATION (PORTAL NR = ALWAYS 0 (SAVE_POINT) ) -TRAVEL TO NEW MAP-
 * TYPE (LEVEL) - DESTINATION (PORTAL NR) -TRAVEL WITH INN THE MAP-
 *
 */
public class Portal extends GameObjectDef {

    private String objectIdentity;
    private boolean savePointBooleanHit;
    private TextureRegion animationPlayerHit, animation;
    private Animation portalAnimation;
    private Array<TextureRegion> textureFrames;

    private float stateTime;
    private boolean setToDestroy;
    private boolean destroyed;

    public String mapTravelType;
    public String mapPortalId;
    public String mapWorld;
    public String mapLevel;
    public String mapDestination;
    public String mapPortalDirectionTraveld;

    public Portal(PlayScreen screen, float x, float y, MapObject object) {
        super(screen, x, y, object);

        this.objectIdentity = object.getName();

        if(object.getProperties().containsKey("TRAVEL TYPE")){
            this.mapTravelType = object.getProperties().get("TRAVEL TYPE").toString();
            this.mapWorld = object.getProperties().get("MAP WORLD").toString();
            this.mapLevel = object.getProperties().get("MAP LEVEL").toString();
            this.mapDestination = object.getProperties().get("DESTINATION").toString();
            this.mapPortalId = object.getProperties().get("PORTAL ID").toString();
            this.mapPortalDirectionTraveld = object.getProperties().get("TRAVEL DIRECTION").toString();
        }else{
            System.out.println("fuck ****************************************************************");
            System.out.println("mapTravelType " + mapTravelType +
                    " mapWorld " + mapWorld +
                    " mapLevel " + mapLevel +
                    " mapDes " + mapDestination +
                    " portal id " + mapPortalId );
        }

        textureFrames = new Array<TextureRegion>();
        textureFrames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/gameObjects/portalBlueAtlas.atlas").findRegion("PortalA"), 0, 0, 96, 96));
        textureFrames.add(new TextureRegion(GameUtility.getTextureAtlas("spriteAtlas/gameObjects/portalBlueAtlas.atlas").findRegion("PortalB"), 0, 0, 96, 96));
        textureFrames.add(new TextureRegion(GameUtility.getTextureAtlas("spriteAtlas/gameObjects/portalBlueAtlas.atlas").findRegion("PortalC"), 0, 0, 96, 96));
        textureFrames.add(new TextureRegion(GameUtility.getTextureAtlas("spriteAtlas/gameObjects/portalBlueAtlas.atlas").findRegion("PortalD"), 0, 0, 96, 96));
        textureFrames.add(new TextureRegion(GameUtility.getTextureAtlas("spriteAtlas/gameObjects/portalBlueAtlas.atlas").findRegion("PortalE"), 0, 0, 96, 96));
        textureFrames.add(new TextureRegion(GameUtility.getTextureAtlas("spriteAtlas/gameObjects/portalBlueAtlas.atlas").findRegion("PortalF"), 0, 0, 96, 96));
        textureFrames.add(new TextureRegion(GameUtility.getTextureAtlas("spriteAtlas/gameObjects/portalBlueAtlas.atlas").findRegion("PortalG"), 0, 0, 96, 96));
        textureFrames.add(new TextureRegion(GameUtility.getTextureAtlas("spriteAtlas/gameObjects/portalBlueAtlas.atlas").findRegion("PortalH"), 0, 0, 96, 96));
        textureFrames.add(new TextureRegion(GameUtility.getTextureAtlas("spriteAtlas/gameObjects/portalBlueAtlas.atlas").findRegion("PortalI"), 0, 0, 96, 96));

        portalAnimation = new Animation(0.15f, textureFrames);



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

    @Override
    protected void defineGameObject() {
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
/*
        FixtureDef fdef_two = new FixtureDef();
        CircleShape shape_two = new CircleShape();
        shape_two.setRadius(30 / 2 / GameUtility.PPM);
        shape_two.setPosition(new Vector2( 0 / GameUtility.PPM, -35 / GameUtility.PPM));

        //fdef_two.filter.categoryBits = GameUtility.GAME_SAVE_POINT_BIT;
        //fdef_two.filter.maskBits = GameUtility.GROUND_BIT |
        //        GameUtility.PLAYER_BIT;

        fdef_two.shape = shape_two;
        fdef_two.isSensor = true;
        b2body.createFixture(fdef_two).setUserData(this);
*/
    }

    @Override
    public void update(float dt) {

        stateTime += dt;
        if(setToDestroy && !destroyed){
            world.destroyBody(b2body);
            destroyed = true;
            setRegion(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/portalBlueAtlas.atlas").findRegion("PortalA"), 0, 0, 96, 96));
            stateTime = 0;
        }
        else if(!destroyed) {
            //b2body.setLinearVelocity(velocity);
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
            setRegion((TextureRegion) portalAnimation.getKeyFrame(stateTime, true));
        }





        //setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        //setRegion(getFrame(dt));
    }

    public TextureRegion getFrame(float dt) {

        if(savePointBooleanHit) {
            return animationPlayerHit;
        }else {
            return animation;
        }
    }

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
    public String getPortalMapDestination() { return  this.mapDestination; }
    public String getMapPortalDirectionTraveld() { return  this.mapPortalDirectionTraveld;}

    public void draw(Batch batch){
        //if(!destroyed || stateTimer < 4) //1
        super.draw(batch);
    }

}
