package com.mygdx.game.framework.debug.managers;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Comparator;
import java.util.PriorityQueue;


public class RayCastManager  {



    private class RayCastRequest {

        final public int priority;
        final public Vector2 point1;
        final public Vector2 point2;
        final public RayCastCallback callback;

        public RayCastRequest(int pri, Vector2 p1, Vector2 p2, RayCastCallback cb) {
            this.priority = pri;
            this.point1 = p1;
            this.point2 = p2;
            this.callback = cb;
        }
    }

    private static final String TAG = "RaycastManager";
    //private final static float SECONDS_TO_NANO = 1000000000f;
    private float budgetTime;
    private World world;
    private PriorityQueue<RayCastRequest> requestQueue;



    public RayCastManager (World w, float budgeT) {
        this.world = w;
        this.budgetTime = budgeT;
        this.requestQueue = new PriorityQueue<RayCastRequest>(1, new Comparator<RayCastRequest>() {
            @Override
            public int compare(RayCastRequest r1, RayCastRequest r2) {
                return r2.priority - r1.priority;
            }
        });
    }


    public boolean addRequest(int pri, Vector2 p1, Vector2 p2, RayCastCallback cb) {
        return requestQueue.add(new RayCastRequest(pri, new Vector2(p1), new Vector2(p2), cb));
    }


    public void update() {
        long startTime = TimeUtils.nanoTime();

        //Gdx.app.log(TAG, " -- Begining of Update tick (" + requestQueue.size() + ") --");

        RayCastRequest rr = requestQueue.poll();
        while(rr != null && TimeUtils.timeSinceNanos(startTime) < budgetTime * 1000000000f){ //budgetTime * seconds to nano
            world.rayCast(rr.callback, rr.point1, rr.point2);
            //Gdx.app.log(TAG, " " + rr.point1 + " - " + rr.point2 + " processed at (" + (TimeUtils.timeSinceNanos(startTime) / 1000000000f) + ") with priority: " + rr.priority);
            rr = requestQueue.poll();
        }

        //Gdx.app.log(TAG, " -- End of Update tick --");

    }

}

