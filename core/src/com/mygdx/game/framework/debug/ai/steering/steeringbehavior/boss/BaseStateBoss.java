package com.mygdx.game.framework.debug.ai.steering.steeringbehavior.boss;

import com.mygdx.game.framework.debug.NameGame;
import com.mygdx.game.framework.debug.managers.GameSteeringStateManagerBoss;

public abstract class BaseStateBoss {


    // References
    protected NameGame app;
    protected GameSteeringStateManagerBoss stateManager;

    protected BaseStateBoss(final NameGame app, final GameSteeringStateManagerBoss stateManager) {
        this.app = app;
        this.stateManager = stateManager;
    }

    public abstract void input();
    public abstract void update(float dt);
    public abstract void render();
    public abstract void resize(int w, int h);
    public abstract void dispose();

}
