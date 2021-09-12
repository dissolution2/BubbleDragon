package com.mygdx.game.framework.debug.sprites.items;

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

public class SpawnPoint extends GameObjectDef {

    public String mapWorld;
    public String mapLevel;
    public String mapSpawnPoint;


    public Vector2 positionSpawnPoint;


    private boolean spawnPointBooleanHit;
    private String objectIdentity;

    public SpawnPoint(PlayScreen screen, float x, float y, MapObject object) {
        super(screen, x, y, object);

        this.objectIdentity = object.getName();
        if(object.getProperties().containsKey("WORLD")){
            this.mapWorld = object.getProperties().get("WORLD").toString();
            this.mapLevel = object.getProperties().get("LEVEL").toString();
            this.mapSpawnPoint = object.getProperties().get("SPAWNPOINT").toString();

        }
        positionSpawnPoint = new Vector2(getX(), getY());
/*
        System.out.println("SavePoint Class is made : "
        + object.getName() +
        " level: " + this.level +
        " SavePoint " + this.savePoint);
*/
        spawnPointBooleanHit = false;
        setBounds(getX(), getY(), 32f / GameUtility.PPM, 32f / GameUtility.PPM);
    }

    @Override
    public String getObjectIdentity() {
        return  this.objectIdentity;
    }

    @Override
    protected void defineGameObject() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.StaticBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef_one = new FixtureDef();
        CircleShape shape_one = new CircleShape();
        shape_one.setRadius(22 / 2 / GameUtility.PPM);

        fdef_one.filter.categoryBits = GameUtility.GAME_OBJECT_BIT;
        fdef_one.filter.maskBits = GameUtility.GROUND_BIT |
                GameUtility.PLAYER_BIT;

        fdef_one.isSensor = true;
        fdef_one.shape = shape_one;
        b2body.createFixture(fdef_one).setUserData(this);

    }

    @Override
    public void update(float dt) {
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
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

    }

    @Override
    public String getPortalMapTravelType() {
        return null;
    }

    @Override
    public void onPortalTravelHit(String travelFrom, String travelTo) {

    }

    @Override
    public void setRunDoors(boolean t) {

    }

    @Override
    public void active(Array<Obstacle> obstacles) {

    }

    @Override
    public void activeAfterBossDeath(Array<Obstacle> obstacles) {

    }

    @Override
    public void getHitBossSpawn() {

    }
}
