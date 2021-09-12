package com.mygdx.game.framework.debug.sprites.Enemies.state;

import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.mygdx.game.framework.debug.sprites.Enemies.EnemyA;


public enum EnemyAState implements State<EnemyA> {

    DEFAULT_START_BEHAVIOR_OF_ENEMYA() {
        @Override
        public void enter (EnemyA enemyA) {

            //if(enemyA.i)

        }

        @Override
        public void update (EnemyA enemyA) {
            //talk(enemyA, "Tastes real good too!");
            //enemyA.getStateMachine().revertToPreviousState();
        }

        @Override
        public void exit (EnemyA enemyA) {
            //talk(bob, "Thankya li'lle lady. Ah better get back to whatever ah wuz doin'");
        }
    },

    SEEK_OUT_PLAYER() {
        @Override
        public void enter(EnemyA enemyA) {

        }

        @Override
        public void update(EnemyA enemyA) {

        }

        @Override
        public void exit(EnemyA enemyA) {

        }
    };

    protected void talk (EnemyA enemyA, String msg) {
        GdxAI.getLogger().info(enemyA.getClass().getSimpleName(), msg);
    }

    @Override
    public boolean onMessage(EnemyA entity, Telegram telegram) {
        return false;
    }
}
