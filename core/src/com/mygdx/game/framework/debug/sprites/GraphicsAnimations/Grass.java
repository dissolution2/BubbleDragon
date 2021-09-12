package com.mygdx.game.framework.debug.sprites.GraphicsAnimations;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.framework.debug.screens.PlayScreen;
import com.mygdx.game.framework.debug.util.GameUtility;
//import com.sun.tools.javac.comp.Todo;

public class Grass extends AnimationItemDef {

    private Array<TextureRegion> textureFrames;
    private Animation grassAnimation;

    private float stateTime;
    public boolean setToDestroy;
    private boolean destroyed;

    public Grass(PlayScreen screen, float x, float y, MapObject object) {
        super(screen, x, y);


        /** Use MapObject to set type = Front / Back of visual Graphics and Size of the Visual Object */

        /** first test grass object */

        //Todo:: random animation have, 3 - 6 dif stage that one randomise - with frameDuration random to
        textureFrames = new Array<TextureRegion>();
        textureFrames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/items/ItemAnimationGrassA.atlas").findRegion("1"), 0, 0, 52, 333));
        textureFrames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/items/ItemAnimationGrassA.atlas").findRegion("2"), 0, 0, 52, 333));
        textureFrames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/items/ItemAnimationGrassA.atlas").findRegion("3"), 0, 0, 78, 332));
        textureFrames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/items/ItemAnimationGrassA.atlas").findRegion("2"), 0, 0, 52, 333));
        textureFrames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/items/ItemAnimationGrassA.atlas").findRegion("1"), 0, 0, 52, 333));
        textureFrames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/items/ItemAnimationGrassA.atlas").findRegion("2"), 0, 0, 52, 333));
        textureFrames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/items/ItemAnimationGrassA.atlas").findRegion("1"), 0, 0, 52, 333));
        textureFrames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/items/ItemAnimationGrassA.atlas").findRegion("3"), 0, 0, 52, 333));

        grassAnimation = new Animation(0.28f, textureFrames);
        setBounds(getX(), getY(), 60 / GameUtility.PPM, 60 / GameUtility.PPM); //30
    }

    @Override
    protected void defineItem() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.StaticBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef_one = new FixtureDef();
        CircleShape shape_one = new CircleShape();
        shape_one.setRadius(40 / 2 / GameUtility.PPM);


        fdef_one.filter.categoryBits = GameUtility.GAME_ITEM_BIT;
        fdef_one.filter.maskBits = GameUtility.GROUND_BIT;

        fdef_one.isSensor = true;
        fdef_one.shape = shape_one;
        b2body.createFixture(fdef_one).setUserData(this);
    }

    @Override
    public void update(float dt) {
        stateTime += dt;
        if(setToDestroy && !destroyed){
            world.destroyBody(b2body);
            b2body = null;
            destroyed = true;
            stateTime = 0;
        }
        else if(!destroyed ) {
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
            setRegion((TextureRegion) grassAnimation.getKeyFrame(stateTime, true));
        }
    }

    public void setToDestroy(){
        setToDestroy = true;
    }

    public void draw(Batch batch){
        if(!destroyed )
            super.draw(batch);
    }
}
