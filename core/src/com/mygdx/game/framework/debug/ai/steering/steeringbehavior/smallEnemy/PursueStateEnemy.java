package com.mygdx.game.framework.debug.ai.steering.steeringbehavior.smallEnemy;

import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.framework.debug.NameGame;
import com.mygdx.game.framework.debug.ai.steering.SteeringEntity;
import com.mygdx.game.framework.debug.managers.GameSteeringStateManagerEnemy;
import com.mygdx.game.framework.debug.sprites.BubblePlayer;
import com.mygdx.game.framework.debug.sprites.Enemies.SmallEnemyDef;

public class PursueStateEnemy extends BaseStateEnemy {

    SteeringEntity aiEntity;
    private SteeringEntity targetEntity;

    public PursueStateEnemy(NameGame app, GameSteeringStateManagerEnemy gameSteeringStateManagerEnemy,
                            BubblePlayer player,
                            Array<SmallEnemyDef> enemyList, float velocity, int radius){
        super(app, gameSteeringStateManagerEnemy);

        aiEntity = new SteeringEntity(enemyList.get(0).b2body, false, .5f);
        aiEntity.setMaxLinearSpeed(velocity);//5);
        aiEntity.setMaxLinearAcceleration(50);

        targetEntity = new SteeringEntity(player.b2body, false, .5f);
        //markAsSensor(targetEntity);

        //System.out.println("aiEntity " + aiEntity);
        //System.out.println("targetEntity " + targetEntity);

        final Arrive<Vector2> arriveSB = new Arrive<Vector2>(aiEntity, targetEntity)
                .setTimeToTarget(0.01f)
                .setArrivalTolerance(0.001f)
                .setDecelerationRadius(radius); // 0 = hit box Center , 2 , 8
        aiEntity.setSteeringBehavior(arriveSB);

        //System.out.println("PursueStateEnemy..... " + enemyList.get(0).getEnemyID() );
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
