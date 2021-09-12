package com.mygdx.game.framework.debug.ai.steering.steeringbehavior.boss;

import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.framework.debug.NameGame;
import com.mygdx.game.framework.debug.ai.steering.SteeringEntityEnemy;
import com.mygdx.game.framework.debug.managers.GameSteeringStateManagerBoss;
import com.mygdx.game.framework.debug.sprites.BubblePlayer;
import com.mygdx.game.framework.debug.sprites.Enemies.BossEnemy.BossEnemyDef;


public class PursueStateBoss extends BaseStateBoss {

    SteeringEntityEnemy aiEntity;
    private SteeringEntityEnemy targetEntity;
    float linearSpeed = 10f;

    public PursueStateBoss(NameGame app, GameSteeringStateManagerBoss gameSteeringStateManagerBoss,
                           BubblePlayer player,
                           Array<BossEnemyDef> enemyList, float velocity, int radius) {
        super(app, gameSteeringStateManagerBoss);

        aiEntity = new SteeringEntityEnemy(enemyList.get(0).b2body, false, .5f);
        aiEntity.setMaxLinearSpeed(velocity);//5);
        aiEntity.setMaxLinearAcceleration(50);

        targetEntity = new SteeringEntityEnemy(player.b2body, false, .5f);
        //markAsSensor(targetEntity);

        final Arrive<Vector2> arriveSB = new Arrive<Vector2>(aiEntity, targetEntity)
                .setTimeToTarget(1f)
                .setArrivalTolerance(0.001f)
                .setDecelerationRadius(radius); // 0 = hit box Center , 2 , 8
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