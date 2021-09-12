package com.mygdx.game.framework.debug.world.gameAiObjects;


import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import com.badlogic.gdx.utils.Array;
import com.mygdx.game.framework.debug.screens.PlayScreen;
import com.mygdx.game.framework.debug.util.GameUtility;

import com.mygdx.game.framework.debug.world.gameAiObjects.AiObjectDef;
import com.mygdx.game.framework.debug.world.gameObstacles.Obstacle;


public class GameAIObject extends AiObjectDef {//GameObjectDef {

    private String objectIdentity;
    private boolean hiddenbossMarkerHit = false;
    private boolean hiddenEnemyAReversVelocityMarkerHit = false;
    private String hiddenPortalActivity;

    private String mapMarkerType;
    private String mapMarkerLength;

    private boolean time_to_reDefineBig;
    private boolean time_to_reDefineLarge;


    private String mapMarkerAssociationNumber;

    public GameAIObject(PlayScreen screen, float x, float y, MapObject object) {
        super(screen, x, y, object);

        this.objectIdentity = object.getName();
        if(object.getProperties().containsKey("type")){
            this.mapMarkerType = object.getProperties().get("type").toString();
            this.mapMarkerLength = object.getProperties().get("Length").toString();
            this.mapMarkerAssociationNumber = object.getProperties().get("Association Number").toString();
        }else{
            this.mapMarkerType = "Empty";
            this.mapMarkerLength = "";
            this.mapMarkerAssociationNumber = "";
        }
        //defineGameAIObject();
        init();

        setBounds(getX(), getY() ,42f / GameUtility.PPM, 49f / GameUtility.PPM);

    }
    public void init(){

        /*** If Length is Normal we don't do anything on size */

        if( !this.mapMarkerType.equals("Empty")) {

            if (this.mapMarkerLength.equals("Big")) {
                time_to_reDefineBig = true;
            }

            if (this.mapMarkerLength.equals("Large")) {
                time_to_reDefineLarge = true;
            }
        }

        if(time_to_reDefineBig){
            defineGameAIObjectBig();
        }

        if(time_to_reDefineLarge){
            defineGameAIObjectLarge();
        }


        //setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 4);
    }
    protected void defineGameAIObjectBig(){

        Vector2 position = b2body.getPosition();
        world.destroyBody(b2body);

        BodyDef bdef = new BodyDef();
        bdef.position.set(position);

        bdef.type = BodyDef.BodyType.StaticBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef_one = new FixtureDef();
        CircleShape shape_one = new CircleShape();
        shape_one.setRadius(35 / 2 / GameUtility.PPM);

        //fdef_one.filter.categoryBits = GameUtility.GAME_OBJECT_BIT;
        //fdef_one.filter.maskBits = GameUtility.GROUND_BIT |
        //        GameUtility.ENEMY_BIT |
        //        GameUtility.PLAYER_BIT;

        fdef_one.isSensor = true;
        fdef_one.shape = shape_one;

        b2body.createFixture(fdef_one).setUserData(this);

        /*Head of the Sensor*/
        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-0 / 20f , 600 / GameUtility.PPM), new Vector2( -0 / 20f, -600 / GameUtility.PPM));
        fdef_one.filter.categoryBits = GameUtility.GAME_AI_OBJECT_BIT; //.GAME_OBJECT_BIT; - old

        //fdef_one.filter.groupIndex = GameUtility.GAME_AI_OBJECT_BIT;

        fdef_one.filter.maskBits = GameUtility.ENEMY_BIT |
                //GameUtility.ENEMY_LEGS_BIT |
                GameUtility.PLAYER_BIT;

        fdef_one.shape = head;
        fdef_one.isSensor = true;

        b2body.createFixture(fdef_one).setUserData(this);

        time_to_reDefineBig = false;
    }

    protected void defineGameAIObjectLarge(){

        Vector2 position = b2body.getPosition();
        world.destroyBody(b2body);

        BodyDef bdef = new BodyDef();
        bdef.position.set(position);

        bdef.type = BodyDef.BodyType.StaticBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef_one = new FixtureDef();
        CircleShape shape_one = new CircleShape();
        shape_one.setRadius(35 / 2 / GameUtility.PPM);

        //fdef_one.filter.categoryBits = GameUtility.GAME_OBJECT_BIT;
        //fdef_one.filter.maskBits = GameUtility.GROUND_BIT |
        //        GameUtility.ENEMY_BIT |
        //        GameUtility.PLAYER_BIT;

        fdef_one.isSensor = true;
        fdef_one.shape = shape_one;

        b2body.createFixture(fdef_one).setUserData(this);

        /*Head of the Sensor*/
        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-0 / 20f , 300 / GameUtility.PPM), new Vector2( -0 / 20f, -300 / GameUtility.PPM));
        fdef_one.filter.categoryBits = GameUtility.GAME_AI_OBJECT_BIT; //.GAME_OBJECT_BIT; - old

        //fdef_one.filter.groupIndex = GameUtility.GAME_AI_OBJECT_BIT;

        fdef_one.filter.maskBits = GameUtility.ENEMY_BIT |
                //GameUtility.ENEMY_LEGS_BIT |
                GameUtility.PLAYER_BIT;

        fdef_one.shape = head;
        fdef_one.isSensor = true;

        b2body.createFixture(fdef_one).setUserData(this);

        time_to_reDefineLarge = false;
    }

    @Override
    protected void defineGameAIObject() {//defineGameObject() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY() + 0.10f);//0.05f);
        bdef.type = BodyDef.BodyType.StaticBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef_one = new FixtureDef();
        CircleShape shape_one = new CircleShape();
        shape_one.setRadius(35 / 2 / GameUtility.PPM);

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
        fdef_one.filter.categoryBits = GameUtility.GAME_AI_OBJECT_BIT; //.GAME_OBJECT_BIT; - old

        //fdef_one.filter.groupIndex = GameUtility.GAME_AI_OBJECT_BIT;

        fdef_one.filter.maskBits = GameUtility.ENEMY_BIT |
                //GameUtility.ENEMY_LEGS_BIT |
                GameUtility.PLAYER_BIT;

        fdef_one.shape = head;
        fdef_one.isSensor = true;

        b2body.createFixture(fdef_one).setUserData(this);

    }

    @Override
    public String getObjectIdentity() { return this.objectIdentity; }

    public String getMapMarkerType(){ return this.mapMarkerType;}

    public String getMapMarkerAssociationNumber() { return  this.mapMarkerAssociationNumber; }

   @Override
    public void update(float dt) {
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 4);
        //setRegion(getFrame(dt)); //stateTimer));
    }

    public boolean getHitBossBoolean(){ return this.hiddenbossMarkerHit;}


    @Override
    public void setPortalActivity(String value){ this.hiddenPortalActivity = value;}

    @Override
    public String getPortalActivity() {return this.hiddenPortalActivity; }

    @Override
    public void getHitBossSpawn(){ hiddenbossMarkerHit = true; }

    @Override
    public int getObjectID() {
        return 0;
    }

    public Body getGameAIObjectB2Body(){
        return this.b2body;
    }



}
