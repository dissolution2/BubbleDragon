package com.mygdx.game.framework.debug.ai.steering.steeringbehavior.smallEnemy;

import com.mygdx.game.framework.debug.NameGame;
import com.mygdx.game.framework.debug.managers.GameSteeringStateManagerEnemy;

public abstract class BaseStateEnemy{
    // References
    protected NameGame app;
    protected GameSteeringStateManagerEnemy stateManagerEnemy;

    protected BaseStateEnemy(final NameGame app,final GameSteeringStateManagerEnemy stateManagerEnemy) {

        this.app = app;
        this.stateManagerEnemy = stateManagerEnemy;
    }

    public abstract void input();
    public abstract void update(float dt);
    public abstract void render();
    public abstract void resize(int w,int h);
    public abstract void dispose();

}