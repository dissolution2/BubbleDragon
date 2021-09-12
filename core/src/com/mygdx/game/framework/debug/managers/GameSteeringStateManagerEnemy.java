package com.mygdx.game.framework.debug.managers;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.framework.debug.NameGame;
import com.mygdx.game.framework.debug.ai.steering.steeringbehavior.smallEnemy.BaseStateEnemy;
import com.mygdx.game.framework.debug.ai.steering.steeringbehavior.smallEnemy.HomeStateEnemy;
import com.mygdx.game.framework.debug.ai.steering.steeringbehavior.smallEnemy.PursueStateEnemy;
import com.mygdx.game.framework.debug.ai.steering.steeringbehavior.smallEnemy.WanderStateSmallenemy;
import com.mygdx.game.framework.debug.sprites.BubblePlayer;
//import com.mygdx.game.framework.debug.sprites.Enemies.BossEnemy.BossEnemyDef;
import com.mygdx.game.framework.debug.sprites.Enemies.SmallEnemyDef;
import com.mygdx.game.framework.debug.world.gameAiObjects.GameAIObject;

import java.util.Stack;

public class GameSteeringStateManagerEnemy {

    public NameGame app;
    public enum State {
        // Decision AI
        FSM,
        PUSHDOWN,
        TREES,


        HOMESTATE,
        INACTIVE,
        ACTIVE,


        // Steering/Movement AI
        FLOCK,
        FORMATION,
        PURSUE,
        PURSUE_AI_MARKER,
        WANDER,
        WANDER_AI_MARKER,

        // Pathfinding AI
        ASTAR,
        HIERARCHICAL
    }

    private Stack<BaseStateEnemy> states;
    //private Stack<GameScreen> states;
    private Array<SmallEnemyDef> gameEnemyList;
    //private Array<GameAIObject> gameAIMarkerList;
    private Array<GameAIObject> gameAIObjects;
    private BubblePlayer gamePlayer;
    private boolean usePatrollAiMarker = false;

    //public GameSteeringStateManagerBoss(MyGdxGame app, BubblePlayer player, Array<SmallEnemyDef> enemyList) {
    public GameSteeringStateManagerEnemy(NameGame app, BubblePlayer player, Array<SmallEnemyDef> enemyList, Array<GameAIObject> objectss) { //}, Array<GameAIObject> aiMarkerList, boolean useAiMarker) {
        this.app = app;
        states = new Stack<BaseStateEnemy>();
        //states = new Stack<GameScreen>();

        this.gamePlayer = player;
        this.gameEnemyList = enemyList;
        this.gameAIObjects = objectss;
        //this.gameAIMarkerList = aiMarkerList;
        //this.usePatrollAiMarker = useAiMarker;
    }

    public void setState(State state, float velocity, int decelerationRadius, int acceleration, int usingIAMarker ) {
        if (states.size() >= 1) {
            states.pop().dispose();
        }
        states.push(getState(state, velocity, decelerationRadius, acceleration, usingIAMarker));
    }
    /*
        public void setState(State state, String targetAIString) {
            if (states.size() >= 1) {
                states.pop().dispose();
            }
            states.push(getState(state, targetAIString ));
        }
    */
    String returnTest ="";
    //public String hasState(){
    public String hasState(){
        if(!states.empty()) {
            //return states.peek().getClass().getName();  //.toString();
            return states.peek().getClass().getSimpleName();
        }else{
            return returnTest = "null";
        }

    }



    // TODO change might just need the PURSUE OR SEEK STATE !!?
    private BaseStateEnemy getState(State state, float velocity, int decelerationRadius, int acceleration, int usingIAMarker){ //}, String targetName) {
        //private GameScreen getState(State state) {
        switch (state) {
            case FLOCK:
                return null;// new FlockStateBoss(app, this, gamePlayer, gameEnemyList);
            case FORMATION:
                return null;//new FormationState(app, this);
            case PURSUE:
                //System.out.println("GameSteeringManagerEnemy PURSUE ACTIVE ");
                return new PursueStateEnemy(app, this, gamePlayer, gameEnemyList, velocity, decelerationRadius);
            case HOMESTATE:
                return new HomeStateEnemy(app, this, gameEnemyList, gameAIObjects, velocity, decelerationRadius, acceleration, usingIAMarker ); // This would we need a body, marker as a home position to where the enemy should walk back to !!!
            case WANDER:
                return new WanderStateSmallenemy(app, this, gamePlayer, gameEnemyList,gameAIObjects); //new WanderStateBoss(app, this, gamePlayer, gameEnemyList, gameAIObjects);
            default:
                return null;
        }
    }

    public void render() {

        //states.peek().input(); //we get input from else where!!

        if(!states.empty()) {
//System.out.println("Render GameSteeringStateManagerBoss");
            states.peek().update(Gdx.graphics.getDeltaTime());
            states.peek().render();
        }
    }

    public void resize(int w, int h) {
        states.peek().resize(w, h);
    }

    public void dispose() {
        for (BaseStateEnemy gs : states) {
            gs.dispose();
        }
        states.clear();
    }
}
