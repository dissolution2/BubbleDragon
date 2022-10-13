package com.mygdx.game.framework.debug.ai.steering.steeringbehavior.smallEnemy;

import com.badlogic.gdx.ai.steer.behaviors.Wander;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.framework.debug.NameGame;
import com.mygdx.game.framework.debug.ai.steering.SteeringEntity;
import com.mygdx.game.framework.debug.managers.GameSteeringStateManagerEnemy;
import com.mygdx.game.framework.debug.sprites.BubblePlayer;
import com.mygdx.game.framework.debug.sprites.Enemies.SmallEnemyDef;
import com.mygdx.game.framework.debug.world.gameAiObjects.GameAIObject;

public class WanderStateSmallenemy extends BaseStateEnemy {

    private SteeringEntity targetEntity;
    private SteeringEntity aiEntity;
    private Array<SteeringEntity> aiEntityList = new Array<SteeringEntity>();
    //private Array<BossEnemyDef> enemyListFromGameBoss = new Array<BossEnemyDef>();
    private Array<SmallEnemyDef> enemyListFromGameSmall = new Array<SmallEnemyDef>();
    private Array<GameAIObject> gameAIObjects = new Array<GameAIObject>();


    public WanderStateSmallenemy(NameGame app, GameSteeringStateManagerEnemy gameSteeringStateManagerEnemy,
                       BubblePlayer player, Array<SmallEnemyDef> enemyList, Array<GameAIObject> objects) {
        super(app, gameSteeringStateManagerEnemy);

        this.enemyListFromGameSmall = enemyList;
        this.gameAIObjects = objects;

        for(int i =0; i < enemyListFromGameSmall.size; i++) {

            aiEntityList.add(new SteeringEntity(enemyListFromGameSmall.get(i).b2body, false, .5f));
            aiEntityList.get(i).setMaxLinearSpeed(10);
            aiEntityList.get(i).setMaxLinearAcceleration(100);
        }

        targetEntity = new SteeringEntity( gameAIObjects.get(0).b2body, false, .5f);

        for(int i =0; i < aiEntityList.size; i++) {


            final Wander<Vector2> wander = new Wander<Vector2>(aiEntityList.get(i))
                    .setFaceEnabled(false) // let wander behaviour manage facing
                    .setWanderOffset(0.8f) // distance away from entity to set target
                    .setWanderOrientation(0f) // the initial orientation
                    .setWanderRadius(1f) // size of target
                    .setWanderRate(2f); // higher values = more spinning
            aiEntityList.get(i).setSteeringBehavior(wander);
        }

    }


    @Override
    public void input() {
    }

    @Override
    public void update(float dt) {

        //System.out.println("WE are updating pursueState!!");
        for(int i =0; i < aiEntityList.size; i++) {
            aiEntityList.get(i).update(dt); //GdxAI.getTimepiece().getDeltaTime());
        }
        targetEntity.update(dt); //GdxAI.getTimepiece().getDeltaTime());
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
