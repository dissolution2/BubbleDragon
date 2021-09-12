package com.mygdx.game.framework.debug.sprites.Enemies.StationaryEnemies;

import com.badlogic.gdx.graphics.g2d.Animation;
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

public class EnemyVineThorns extends StationaryEnemyWithAnimationDef {

    private String objectIdentityName;
    private int objectIdentityID;
    private Array<TextureRegion> textureFrames;
    private Animation vinesAnimationOne;

    private float stateTime;
    public boolean setToDestroy;
    private boolean destroyed;
    private String facingDirection;
    private String typeNumberVine;


    public EnemyVineThorns(PlayScreen screen, float x, float y, MapObject object, int id) {
        super(screen, x, y);

        this.objectIdentityName = object.getName();
        this.objectIdentityID = id;

        /** TiledMap TYPE NUMBER = graphics corresponding */
        if(object.getProperties().containsKey("TYPE NUMBER")){
            this.typeNumberVine = object.getProperties().get("TYPE NUMBER").toString();
            this.facingDirection = object.getProperties().get("DIRECTION").toString();
        }

//ToDo:: Vines texture
        textureFrames = new Array<TextureRegion>();
        //textureFrames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/enemyStationary/EnemyVinesAtlas.atlas").findRegion("EnemyVineLeft01"), 0, 0, 41, 118));
        //vinesAnimationOne = new Animation(0.15f, textureFrames);
/*
        textureFrames.clear();
        textureFrames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/enemyStationary/EnemySpikesAtlas.atlas").findRegion("EnemySpikesRight"), 0, 0, 62, 58));
        vinesAnimationOne = new Animation(0.15f, textureFrames);

        textureFrames.clear();
        textureFrames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/enemyStationary/EnemySpikesAtlas.atlas").findRegion("EnemySpikesUp"), 0, 0, 62, 58));
        vinesAnimationOne = new Animation(0.15f, textureFrames);

        textureFrames.clear();
        textureFrames.add(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/enemyStationary/EnemySpikesAtlas.atlas").findRegion("EnemySpikesDown"), 0, 0, 62, 58));
        vinesAnimationOne = new Animation(0.15f, textureFrames);
*/

        setBounds(getX(), getY(), 30 / GameUtility.PPM, 30 / GameUtility.PPM); //32
    }

    @Override
    protected void defineStationaryEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.StaticBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef_one = new FixtureDef();
        CircleShape shape_one = new CircleShape();
        shape_one.setRadius(40 / 2 / GameUtility.PPM);


        fdef_one.filter.categoryBits = GameUtility.GAME_ITEM_BIT;
        fdef_one.filter.maskBits = GameUtility.PLAYER_BIT;

        fdef_one.isSensor = false;
        fdef_one.shape = shape_one;
        b2body.createFixture(fdef_one).setUserData(this);

        //Create the Head here:
        EdgeShape lowerHead = new EdgeShape();

        //lowerHead.set(new Vector2(-10 / 14f, 6 / GameUtility.PPM), new Vector2(10 / 14f, 6 / GameUtility.PPM));
        lowerHead.set(new Vector2(-0 / 20f, 50 / GameUtility.PPM), new Vector2(-0 / 20f, -50 / GameUtility.PPM));

        fdef_one.filter.categoryBits = GameUtility.GAME_ITEM_BIT;
        fdef_one.filter.maskBits = GameUtility.PLAYER_BIT;

        fdef_one.shape = lowerHead;
        fdef_one.isSensor = true;
        b2body.createFixture(fdef_one).setUserData(this);
    }

    /*
    public TextureRegion getFrame(){

        TextureRegion region = null;
        region = (TextureRegion) spikesAnimation.getKeyFrame(stateTime, true);

        if(facingDirection.equals("true") && !region.isFlipX()){
            region.flip(true, false);
        }else if( facingDirection.equals("false") && !region.isFlipX() ){
            region.flip(false, false);
        }


        TextureRegion region = null;
        if(facingDirection.equals("UP") ){
            region = (TextureRegion) spikesAnimationFacingUp.getKeyFrame(stateTime, true);
        }else if( facingDirection.equals("DOWN") ){
            region = (TextureRegion) spikesAnimationFacingDown.getKeyFrame(stateTime, true);
        }else if( facingDirection.equals("RIGHT") ) {
            region = (TextureRegion) spikesAnimationFacingRight.getKeyFrame(stateTime, true);
        }else if( facingDirection.equals("LEFT") ) {
            region = (TextureRegion) spikesAnimationFacingLeft.getKeyFrame(stateTime, true);
        }


       return region;
    }
*/

    @Override
    public void update(float dt) {
        stateTime += dt;
        if(setToDestroy && !destroyed){
            world.destroyBody(b2body);
            destroyed = true;
            //setRegion(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/ItemHeartAtlas.atlas").findRegion("HeartAnimation01"), 0, 0, 86, 69));
            stateTime = 0;
        }
        else if(!destroyed ) {
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
            //setRegion(getFrame());

        }
    }

    public void setToDestroy(){ setToDestroy = true; }

    @Override
    public int getStationaryEnemyObjectID() { return objectIdentityID; }

    @Override
    public void hitWithPlayerPower() { }

    /** if we use texture */
    public void draw(Batch batch){
        if(!destroyed )
            super.draw(batch);
    }
}
