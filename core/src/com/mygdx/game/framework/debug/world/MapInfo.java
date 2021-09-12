package com.mygdx.game.framework.debug.world;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.mygdx.game.framework.debug.screens.PlayScreen;
import com.mygdx.game.framework.debug.util.GameUtility;
import com.mygdx.game.framework.debug.world.gameAiObjects.AiObjectDef;

public class MapInfo extends AiObjectDef {

    public String mapWorld;
    public String mapLevel;
    public String mapSavePoint;

    private String objectIdentity;
    public MapInfo(PlayScreen screen, float x, float y, MapObject object) {
        super(screen, x, y, object);

        this.objectIdentity = object.getName();
        if(object.getProperties().containsKey("WORLD")){
            this.mapWorld = object.getProperties().get("WORLD").toString();
            this.mapLevel = object.getProperties().get("LEVEL").toString();
            this.mapSavePoint = object.getProperties().get("SAVE POINT").toString();

        }
        setBounds(getX(), getY(), 68f / GameUtility.PPM, 123f / GameUtility.PPM);
    }

    @Override
    public String getObjectIdentity() {  return this.objectIdentity; }

    @Override
    protected void defineGameAIObject() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.StaticBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef_one = new FixtureDef();
        CircleShape shape_one = new CircleShape();
        shape_one.setRadius(30 / 2 / GameUtility.PPM);

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
    public void getHitBossSpawn() {

    }

    @Override
    public void setPortalActivity(String value) {

    }

    @Override
    public String getPortalActivity() {
        return null;
    }

    public String getMapInfoCurrentWorld(){ return this.mapWorld; }
    public String getMapInfoCurrentLevel(){ return this.mapLevel; }
    public String getMapInfoCurrentSavePoint(){ return this.mapSavePoint; }
}
