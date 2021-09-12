package com.mygdx.game.framework.debug.world.gameObjects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.framework.debug.screens.PlayScreen;
import com.mygdx.game.framework.debug.util.GameUtility;
import com.mygdx.game.framework.debug.world.gameObstacles.Obstacle;

public class GameObjectSwitchHidden extends GameObjectDef {

    private Boolean runDoors = false;
    private String[] associationNumbers;
    private TextureRegion switchTextureOn, switchTextureOff;

    private boolean switchBooleanStatus;
    private String objectIdentity;
    private String isBossDeadOpenDoorSwitch;
    private String activateSwitch;

    public GameObjectSwitchHidden(PlayScreen screen, float x, float y, MapObject object) {
        super(screen, x, y, object);

        switchTextureOn = new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/gameObjects/ItemAtlas2.atlas").findRegion("SwitchOn"), 0, 0, 42, 49);
        switchTextureOff = new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/gameObjects/ItemAtlas2.atlas").findRegion("SwitchOff"), 0, 0, 42, 49);
        objectIdentity = object.getName();
        switchBooleanStatus = false;

        //Association Numbers
        if(mapObject.getProperties().get("Association Number") != null){
            associationNumbers = mapObject.getProperties().get("Association Number").toString().split(",");
        }

        if(mapObject.getProperties().get("BossSwitchDead") != null ) {
            this.isBossDeadOpenDoorSwitch = mapObject.getProperties().get("BossSwitchDead", String.class);
            this.activateSwitch = mapObject.getProperties().get("ActivateSwitch", String.class);
        }else{
            this.isBossDeadOpenDoorSwitch = "false";
            this.activateSwitch = "null";
        }

        setBounds(getX(), getY() ,42f / GameUtility.PPM, 49f / GameUtility.PPM);
    }

    @Override
    public String getObjectIdentity() {
        return objectIdentity;
    }

    @Override
    public String getPortalMapTravelType() {
        return null;
    }

    public String getIsBossDeadOpenDoorSwitch(){
        return this.isBossDeadOpenDoorSwitch;
    }

    public String getActivateSwitch(){
        return this.activateSwitch;
    }

    public void setActivateSwitch(String value){ this.activateSwitch = value; }

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
        bdef.position.set(getX(), getY() + 0.10f);
        bdef.type = BodyDef.BodyType.StaticBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef_one = new FixtureDef();
        CircleShape shape_one = new CircleShape();
        shape_one.setRadius(35 / 2 / GameUtility.PPM);

        //PolygonShape savePointShape = new PolygonShape();
        //savePointShape.setAsBox( (0.32500002f/2),(0.32500002f/2)) ;

        //fdef_one.filter.categoryBits = GameUtility.GAME_OBJECT_BIT;
        //fdef_one.filter.maskBits = GameUtility.GROUND_BIT |
        //        GameUtility.PLAYER_BIT |
        //        GameUtility.PLAYER_POWER_BIT;

        fdef_one.isSensor = true;
        fdef_one.shape = shape_one;


        b2body.createFixture(fdef_one).setUserData(this); //"GAME_TEST_ITEM_SWITCH");

        /*Head of the player Sensor*/
        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-0 / 20f , 100 / GameUtility.PPM), new Vector2( -0 / 20f, -50 / GameUtility.PPM));
        fdef_one.filter.categoryBits = GameUtility.GAME_OBJECT_BIT;
        fdef_one.filter.maskBits = GameUtility.GROUND_BIT |
                GameUtility.PLAYER_BIT;

        fdef_one.shape = head;
        fdef_one.isSensor = true;

        b2body.createFixture(fdef_one).setUserData(this);
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
    public void setRunDoors(boolean t) {
        this.runDoors = t;
    }


    public boolean getRunDoors(){
        return runDoors;
    }


    @Override
    public void active(Array<Obstacle> obstacles) {
        for(String number : associationNumbers){
            for(Obstacle obstacle : obstacles)
                if(obstacle.associationNumber == Integer.valueOf(number)){
                    //System.out.println("ItemSwithc Class Activate!!!");
                    obstacle.activateClose(); // .activateOpen();
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



    public void draw(Batch batch){
        //if(!destroyed || stateTimer < 4) //1
        super.draw(batch);
    }
}
