package com.mygdx.game.framework.debug.world.gameLightObjcets;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.mygdx.game.framework.debug.screens.PlayScreen;
import com.mygdx.game.framework.debug.util.GameUtility;

public class GameLightObject extends GameLightObjectDef{

    private String objectIdentity;
    private String lightMarkerType;
    private String lightMarkerDirection;
    //private String mapMarkerAssociationNumber;

    public GameLightObject(PlayScreen screen, float x, float y, MapObject object) {
        super(screen, x, y, object);

        this.objectIdentity = object.getName();
        if(object.getProperties().containsKey("Type")){
            this.lightMarkerType = object.getProperties().get("Type").toString();
            this.lightMarkerDirection = object.getProperties().get("Direction").toString();
        }
        setBounds(getX(), getY() ,42f / GameUtility.PPM, 49f / GameUtility.PPM);
    }

    @Override
    public String getObjectIdentity() {
        return null;
    }

    @Override
    protected void defineLightPointObject() {
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

        //fdef_one.isSensor = true;
        fdef_one.shape = shape_one;

        b2body.createFixture(fdef_one).setUserData(this);
    }

    @Override
    public void update(float dt) {

    }

    @Override
    public int getObjectID() {
        return 0;
    }
}
