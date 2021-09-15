package com.mygdx.game.framework.debug.testingObjects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.mygdx.game.framework.debug.screens.PlayScreen;
import com.mygdx.game.framework.debug.sprites.BubblePlayer;
import com.mygdx.game.framework.debug.util.GameUtility;


public class Mushroom extends Item {
    public Mushroom(PlayScreen screen, float x, float y) {
        super(screen, x, y);

        setRegion(new TextureRegion( GameUtility.getTextureAtlas("spriteAtlas/ItemAtlas.atlas").findRegion("mushroom"), 0, 0, 64, 64));
        //setRegion(screen.getAtlas().findRegion("mushroom"), 0, 0, 16, 16);
        //velocity = new Vector2(0.7f, 0);
    }

    @Override
    public void defineItem() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / GameUtility.PPM);
        fdef.filter.categoryBits = GameUtility.GAME_ITEM_BIT;
        fdef.filter.maskBits = GameUtility.PLAYER_BIT |
                GameUtility.GAME_ITEM_BIT |
                GameUtility.GROUND_BIT;// |
                //MarioBros.COIN_BIT |
                //MarioBros.BRICK_BIT;

        fdef.shape = shape;
        body.createFixture(fdef).setUserData(this);
    }

    @Override
    public void use(BubblePlayer p) {
        destroy();
        //mario.grow();
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        //setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        //velocity.y = body.getLinearVelocity().y;
        //body.setLinearVelocity(velocity);
    }
}
