package com.mygdx.game.framework.debug.screens.transitions;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

public class ScreenTransitionAction extends Action {
    public static enum ScreenTransitionType{
        FADE_IN,
        FADE_OUT,
        NONE
    }

    private ScreenTransitionType transitionType = ScreenTransitionType.NONE;
    private float transitionDuration = 2.5f;

    public ScreenTransitionAction(){
    }

    public ScreenTransitionAction(ScreenTransitionType type, float duration){
        this.transitionType = type;
        this.transitionDuration = duration;
    }

    @Override
    public boolean act(float delta) {
        Actor actor = getTarget();
        if ( actor == null ){ return false;
        }
        switch(transitionType){
            case FADE_IN:
                SequenceAction fadeIn = Actions.sequence(
                        Actions.alpha(2), // 1 // 0 on check to see more
                        Actions.fadeOut(transitionDuration));
                actor.addAction(fadeIn);
                break;
            case FADE_OUT:
                SequenceAction fadeOut = Actions.sequence(
                        Actions.alpha(2), // 0
                        Actions.fadeIn(transitionDuration));
                actor.addAction(fadeOut);
                break;
            case NONE:
                break;
            default:
                break;
        }
        return true;
    }

    public static ScreenTransitionAction transition (ScreenTransitionType type, float duration) {
        ScreenTransitionAction action = Actions.action(ScreenTransitionAction.class);
        action.setTransitionType(type);
        action.setTransitionDuration(duration);
        return action;
    }

    public ScreenTransitionType getTransitionType() {
        return transitionType;
    }

    public void setTransitionType(ScreenTransitionType transitionType) {
        this.transitionType = transitionType;
    }

    public float getTransitionDuration() {
        return transitionDuration;
    }

    public void setTransitionDuration(float transitionDuration) {
        this.transitionDuration = transitionDuration;
    }

}

