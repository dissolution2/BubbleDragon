package com.mygdx.game.framework.debug.sprites.Enemies.StationaryEnemies;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.mygdx.game.framework.debug.screens.PlayScreen;
import com.mygdx.game.framework.debug.util.GameUtility;

public class EnemyGraphicSensor extends StationaryEnemyDef {

    private String objectIdentityName;
    private int objectIdentityID;

    private float stateTime;
    public boolean setToDestroy;
    private boolean destroyed;
    private String sensorLength;
    private String sensorDirection;

    private boolean time_to_reDefineVertical_Short;
    private boolean time_to_reDefineVertical_Long;
    private boolean time_to_reDefineHorizontal_Short;
    private boolean time_to_reDefineHorizontal_Long;


    //private EdgeShape lowerHead;

    public EnemyGraphicSensor(PlayScreen screen, float x, float y, MapObject object, int id ) { //MapObject object, int id) {
        super(screen, x, y);//object);

        this.objectIdentityName = object.getName();
        this.objectIdentityID = id;

        if(object.getProperties().containsKey("Type")) {
            this.sensorLength = object.getProperties().get("Type").toString();
            //System.out.println("Sensor L: " + sensorLength);
        }
        if(object.getProperties().containsKey("Direction")) {
            this.sensorDirection = object.getProperties().get("Direction").toString();
            //System.out.println("Sensor D: " + sensorDirection);
        }



        defineStationaryEnemy();
        init();

        setBounds(getX(), getY(), 30 / GameUtility.PPM, 30 / GameUtility.PPM); //32
    }


    public void init(){

        if( this.sensorDirection.equals("V") && this.sensorLength.equals("Long")){
            //System.out.println("Define V - L ");
            time_to_reDefineVertical_Long = true;
        }

        if( this.sensorDirection.equals("H") && this.sensorLength.equals("Short")){
            //System.out.println("Define H - S ");
            time_to_reDefineHorizontal_Short = true;
        }

        if(this.sensorDirection.equals("H") && this.sensorLength.equals("Long")){
            //System.out.println("Define H - L ");
            time_to_reDefineHorizontal_Long = true;
        }

        if(time_to_reDefineHorizontal_Long){
            redDineStationaryEnemyHorizontalLong();
        }

        if(time_to_reDefineHorizontal_Short){
            redDineStationaryEnemyHorizontalShort();
        }

        if(time_to_reDefineVertical_Long){
            redDefineStationaryEnemyVerticalLong();
        }
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
    }


    /** Horizontal Long*/
    protected void redDineStationaryEnemyHorizontalLong(){

        //System.out.println("Define H - L ");

        Vector2 position = b2body.getPosition();
        world.destroyBody(b2body);

        BodyDef bdef = new BodyDef();
        bdef.position.set(position);
        bdef.type = BodyDef.BodyType.StaticBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef_one = new FixtureDef();
        CircleShape shape_one = new CircleShape();
        shape_one.setRadius(20 / 2 / GameUtility.PPM);

        fdef_one.isSensor = true;
        fdef_one.shape = shape_one;
        b2body.createFixture(fdef_one).setUserData(this);

        //Create the Head here:
        EdgeShape lowerHead = new EdgeShape();

        lowerHead.set(new Vector2(-10 / 20f, -0 / GameUtility.PPM), new Vector2(10 / 20f, -0 / GameUtility.PPM));

        fdef_one.filter.categoryBits = GameUtility.ENEMY_BIT;
        fdef_one.filter.maskBits = GameUtility.ENEMY_BIT |
                GameUtility.PLAYER_BIT;

        fdef_one.shape = lowerHead;
        fdef_one.isSensor = true;
        b2body.createFixture(fdef_one).setUserData(this);

        time_to_reDefineHorizontal_Long = false;
    }


    /** Horizontal Short*/
    protected void redDineStationaryEnemyHorizontalShort(){

        //System.out.println("Define H - S ");

        Vector2 position = b2body.getPosition();
        world.destroyBody(b2body);

        BodyDef bdef = new BodyDef();
        bdef.position.set(position);
        bdef.type = BodyDef.BodyType.StaticBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef_one = new FixtureDef();
        CircleShape shape_one = new CircleShape();
        shape_one.setRadius(20 / 2 / GameUtility.PPM);

        fdef_one.isSensor = true;
        fdef_one.shape = shape_one;
        b2body.createFixture(fdef_one).setUserData(this);

        //Create the Head here:
        EdgeShape lowerHead = new EdgeShape();

        lowerHead.set(new Vector2(-10 / 50f, -0 / GameUtility.PPM), new Vector2(10 / 50f, -0 / GameUtility.PPM));


        fdef_one.filter.categoryBits = GameUtility.ENEMY_BIT;
                fdef_one.filter.maskBits = GameUtility.ENEMY_BIT |
                GameUtility.PLAYER_BIT;

        fdef_one.shape = lowerHead;
        fdef_one.isSensor = true;
        b2body.createFixture(fdef_one).setUserData(this);

        time_to_reDefineHorizontal_Short = false;
    }

    /** Vertical Long */
    protected void redDefineStationaryEnemyVerticalLong(){

        //System.out.println("Define V - L ");

        Vector2 position = b2body.getPosition();
        world.destroyBody(b2body);

        BodyDef bdef = new BodyDef();
        bdef.position.set(position);
        bdef.type = BodyDef.BodyType.StaticBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef_one = new FixtureDef();
        CircleShape shape_one = new CircleShape();
        shape_one.setRadius(20 / 2 / GameUtility.PPM);

        fdef_one.isSensor = true;
        fdef_one.shape = shape_one;
        b2body.createFixture(fdef_one).setUserData(this);

        //Create the Head here:
        EdgeShape lowerHead = new EdgeShape();


        lowerHead.set(new Vector2(-0 / 20f, 50 / GameUtility.PPM), new Vector2(-0 / 20f, -45 / GameUtility.PPM));


        fdef_one.filter.categoryBits = GameUtility.ENEMY_BIT;
        fdef_one.filter.maskBits = GameUtility.ENEMY_BIT |
                GameUtility.PLAYER_BIT;

        fdef_one.shape = lowerHead;
        fdef_one.isSensor = true;
        b2body.createFixture(fdef_one).setUserData(this);

        time_to_reDefineVertical_Long = false;

    }

    /** Vertical Short */
    @Override
    protected void defineStationaryEnemy() {

        //System.out.println("Define V - S ");

        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.StaticBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef_one = new FixtureDef();
        CircleShape shape_one = new CircleShape();
        shape_one.setRadius(20 / 2 / GameUtility.PPM);

        fdef_one.isSensor = true;
        fdef_one.shape = shape_one;
        b2body.createFixture(fdef_one).setUserData(this);

        //Create the Head here:
        EdgeShape lowerHead = new EdgeShape();

        /** Vertical short */
        lowerHead.set(new Vector2(-0 / 20f, 15 / GameUtility.PPM), new Vector2(-0 / 20f, -15 / GameUtility.PPM));

        fdef_one.filter.categoryBits = GameUtility.ENEMY_BIT;
        fdef_one.filter.maskBits = GameUtility.ENEMY_BIT |
                GameUtility.PLAYER_BIT;

        fdef_one.shape = lowerHead;
        fdef_one.isSensor = true;
        b2body.createFixture(fdef_one).setUserData(this);

    }


    @Override
    public void update(float dt) {
/*
        stateTime += dt;
        if(setToDestroy && !destroyed){
            world.destroyBody(b2body);
            destroyed = true;
            stateTime = 0;
        }
        else if(!destroyed ) {

            // Base is Direction V - Short... if nothing mach it setts Base.
            if( this.sensorDirection.equals("V") && this.sensorLength.equals("Short")) {
                //System.out.println("Define V - S ");
            }

            if( this.sensorDirection.equals("V") && this.sensorLength.equals("Long")){
                //System.out.println("Define V - L ");
                time_to_reDefineVertical_Long = true;
            }

            if( this.sensorDirection.equals("H") && this.sensorLength.equals("Short")){
                //System.out.println("Define H - S ");
                time_to_reDefineHorizontal_Short = true;
            }

            if(this.sensorDirection.equals("H") && this.sensorLength.equals("Long")){
                //System.out.println("Define H - L ");
                time_to_reDefineHorizontal_Long = true;
            }

            if(time_to_reDefineHorizontal_Long){
                redDineStationaryEnemyHorizontalLong();
            }

            if(time_to_reDefineHorizontal_Short){
                redDineStationaryEnemyHorizontalShort();
            }

            if(time_to_reDefineVertical_Long){
                redDefineStationaryEnemyVerticalLong();
            }
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        }
*/
    }

    public void setToDestroy(){ setToDestroy = true; }

    @Override
    public int getStationaryEnemyObjectID() { return objectIdentityID; }

    @Override
    public void hitWithPlayerPower() { }

    /** if we use texture */
    /*
    public void draw(Batch batch){
        if(!destroyed )
            super.draw(batch);
    }
    */
}
