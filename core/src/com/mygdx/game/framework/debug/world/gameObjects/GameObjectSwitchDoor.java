package com.mygdx.game.framework.debug.world.gameObjects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.framework.debug.screens.PlayScreen;
import com.mygdx.game.framework.debug.world.gameObstacles.Obstacle;
import com.mygdx.game.framework.debug.util.GameUtility;

public class GameObjectSwitchDoor extends GameObjectDef {

    private TextureRegion switchTextureOn, switchTextureOff;

    private boolean switchBooleanStatus;


    private String[] associationNumbers;
    private Boolean runDoors = false;
    private String objectIdentity;
    //private String isBossDeadOpenDoorSwitch;
    //private String activateSwitch;
    private String doorKeyType;
    private String doorKeyStatus;

    public GameObjectSwitchDoor(PlayScreen screen, float x, float y, MapObject mapObject) {
        super(screen, x, y, mapObject);

        switchTextureOn = new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/gameObjects/ItemAtlas2.atlas").findRegion("SwitchOn"), 0, 0, 42, 49);
        switchTextureOff = new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/gameObjects/ItemAtlas2.atlas").findRegion("SwitchOff"), 0, 0, 42, 49);

        switchBooleanStatus = false;
        this.objectIdentity = mapObject.getName();

        //Association Numbers
        if(mapObject.getProperties().get("Association Number") != null){
            associationNumbers = mapObject.getProperties().get("Association Number").toString().split(",");
        }

        if(mapObject.getProperties().get("Key Door Status") != null ) {
            this.doorKeyStatus = mapObject.getProperties().get("Key Door Status", String.class);
            this.doorKeyType = mapObject.getProperties().get("Key Type", String.class);
        }


        setBounds(getX(), getY() ,42f / GameUtility.PPM, 49f / GameUtility.PPM);
    }

    @Override
    public String getObjectIdentity() {

        return this.objectIdentity;
    }

    public String getDoorKeyType(){ return this.doorKeyType; }
    public String getDoorKeyStatus(){ return this.doorKeyStatus; }

    public void setDoorKeyType(String value){this.doorKeyType = value; }
    public void setDoorKeyStatus(String value){this.doorKeyStatus = value; }




/*
    public String getIsBossDeadOpenDoorSwitch(){
        return this.isBossDeadOpenDoorSwitch;
    }

    public String getActivateSwitch(){
        return this.activateSwitch;
    }

    public void setActivateSwitch(String value){ this.activateSwitch = value; }
*/

    @Override
    public String getPortalMapTravelType() {
        return null;
    }


    @Override
    public void update(float dt) {


        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 4);
        setRegion(getFrame(dt)); //stateTimer));
    }



    public TextureRegion getFrame(float dt) {

        if(switchBooleanStatus){
            return switchTextureOn;
        }else{
            return  switchTextureOff;
        }
    }

    @Override
    protected void defineGameObject() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY() + 0.05f);
        bdef.type = BodyDef.BodyType.StaticBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef_one = new FixtureDef();
        CircleShape shape_one = new CircleShape();
        shape_one.setRadius(35 / 2 / GameUtility.PPM);

        //PolygonShape savePointShape = new PolygonShape();
        //savePointShape.setAsBox( (0.32500002f/2),(0.32500002f/2)) ;

        fdef_one.filter.categoryBits = GameUtility.GAME_OBJECT_BIT;
        fdef_one.filter.maskBits = GameUtility.GROUND_BIT |
                GameUtility.PLAYER_BIT |
                GameUtility.PLAYER_POWER_BIT;

        fdef_one.isSensor = true;
        fdef_one.shape = shape_one; //savePointShape;

        //b2body.setGravityScale(9.8f);
        //b2body.setLinearDamping(0.3f);

        b2body.createFixture(fdef_one).setUserData(this); //"GAME_TEST_ITEM_SWITCH");
    }

    @Override
    public void active(Array<Obstacle> obstacles){

        for(String number : associationNumbers){
            for(Obstacle obstacle : obstacles)
                if(obstacle.associationNumber == Integer.valueOf(number)){
                    //System.out.println("ItemSwithc Class Activate!!!");
                    obstacle.activateOpen();
                    //obstacle.update();
                    //((ObstacleDoor)obstacle).activate();
                }
        }
    }

    @Override
    public void activeAfterBossDeath(Array<Obstacle> obstacles) {
        for(String number : associationNumbers){
            for(Obstacle obstacle : obstacles)
                if(obstacle.associationNumber == Integer.valueOf(number)){
                    obstacle.activateOpenAfterBossDeath();
                }
        }
    }

    @Override
    public void getHitBossSpawn() {

    }


    public void activeClose(Array<Obstacle> obstacles){

        for(String number : associationNumbers){
            for(Obstacle obstacle : obstacles)
                if(obstacle.associationNumber == Integer.valueOf(number)){
                    //System.out.println("ItemSwithc Class Activate!!!");
                    obstacle.activateClose();
                    //obstacle.update();
                    //((ObstacleDoor)obstacle).activate();
                }
        }
    }


    @Override
    public int getObjectID() {
        return 0;
    }

    @Override
    public void onSavePointHit(Vector2 player, String world, String level, String savePoint) {

    }

    @Override
    public void onGameObjectHitChangeTexture(Boolean textureChange) {
        switchBooleanStatus = true;
    }

    @Override
    public void onPortalTravelHit(String travelFrom, String travelTo) {

    }


    @Override
    public void setRunDoors(boolean t){
        this.runDoors = t;
    }


    public boolean getRunDoors(){
        return runDoors;
    }


    public void draw(Batch batch){
        //if(!destroyed || stateTimer < 4) //1
        super.draw(batch);
    }
}
