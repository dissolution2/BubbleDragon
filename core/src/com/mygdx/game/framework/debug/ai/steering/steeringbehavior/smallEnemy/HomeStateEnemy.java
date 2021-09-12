package com.mygdx.game.framework.debug.ai.steering.steeringbehavior.smallEnemy;

import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.framework.debug.NameGame;
import com.mygdx.game.framework.debug.ai.steering.SteeringEntityEnemy;
import com.mygdx.game.framework.debug.managers.GameSteeringStateManagerEnemy;
import com.mygdx.game.framework.debug.sprites.Enemies.SmallEnemyDef;
import com.mygdx.game.framework.debug.world.gameAiObjects.GameAIObject;

public class HomeStateEnemy extends BaseStateEnemy {

    SteeringEntityEnemy aiEntity;
    private SteeringEntityEnemy targetEntity;
    float linearSpeed = 10f;

    public HomeStateEnemy(NameGame app, GameSteeringStateManagerEnemy gameSteeringStateManagerEnemy,
                          Array<SmallEnemyDef> enemyList, Array<GameAIObject> objects, float velocity, int radius, float acceleration, int bodyToUse ) {
        super(app, gameSteeringStateManagerEnemy);

        aiEntity = new SteeringEntityEnemy(enemyList.get(0).b2body, false, .5f);
        aiEntity.setMaxLinearSpeed(velocity);
        aiEntity.setMaxLinearAcceleration(acceleration);

        //Todo:: check out this
        targetEntity = new SteeringEntityEnemy(objects.get(bodyToUse).getGameAIObjectB2Body(), false, .5f);
        //markAsSensor(targetEntity);

        final Arrive<Vector2> arriveSB = new Arrive<Vector2>(aiEntity, targetEntity)
                .setTimeToTarget(0.01f)
                .setArrivalTolerance(0.001f)
                .setDecelerationRadius(radius);
        aiEntity.setSteeringBehavior(arriveSB);
    }
    @Override
    public void input() {

    }

    @Override
    public void update(float dt) {

        aiEntity.update(GdxAI.getTimepiece().getDeltaTime());
        targetEntity.update(GdxAI.getTimepiece().getDeltaTime());
    }

    @Override
    public void render() {

    }

    @Override
    public void resize(int w, int h) {

    }

    @Override
    public void dispose() {

    }

}
