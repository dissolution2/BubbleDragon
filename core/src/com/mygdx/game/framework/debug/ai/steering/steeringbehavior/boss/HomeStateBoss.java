package com.mygdx.game.framework.debug.ai.steering.steeringbehavior.boss;

import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.framework.debug.NameGame;
import com.mygdx.game.framework.debug.ai.steering.SteeringEntity;
import com.mygdx.game.framework.debug.managers.GameSteeringStateManagerBoss;
import com.mygdx.game.framework.debug.sprites.Enemies.BossEnemy.BossEnemyDef;
import com.mygdx.game.framework.debug.world.gameAiObjects.GameAIObject;

public class HomeStateBoss extends BaseStateBoss {

    SteeringEntity aiEntity;
    private SteeringEntity targetEntity;
    float linearSpeed = 10f;

    public HomeStateBoss(NameGame app, GameSteeringStateManagerBoss gameSteeringStateManagerBoss,
                         Array<BossEnemyDef> enemyList, Array<GameAIObject> objects, float velocity, int radius, float acceleration, int bodyToUse ) {
        super(app, gameSteeringStateManagerBoss);

        aiEntity = new SteeringEntity(enemyList.get(0).b2body, false, .5f);
        aiEntity.setMaxLinearSpeed(velocity);
        aiEntity.setMaxLinearAcceleration(acceleration);

        targetEntity = new SteeringEntity(objects.get(bodyToUse).getGameAIObjectB2Body(), false, .5f);
        //markAsSensor(targetEntity);

        final Arrive<Vector2> arriveSB = new Arrive<Vector2>(aiEntity, targetEntity)
                .setTimeToTarget(1f)
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