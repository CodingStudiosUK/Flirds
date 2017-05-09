package com.codingstudiosuk.flirds;

import android.graphics.Paint;

import java.util.Comparator;

class Entity {

    Vector pos;
    transient ViewSim v;
    float aggro;
    boolean dead = false;
    float size;
    Paint fill = new Paint(0);

    Entity(ViewSim s) {
        v = s;
        pos = new Vector(v.random(0, v.width), v.random(0, v.height));
    }

    float dist(Entity other) {
        float xDist = Math.abs(pos.x - other.pos.x);
        if (xDist > v.width / 2) {
            xDist = v.width - xDist;
        }
        float yDist = Math.abs(pos.y - other.pos.y);
        if (yDist > v.height / 2) {
            yDist = v.height - yDist;
        }
        return (float) Math.sqrt(Math.pow(xDist, 2) + Math.pow(yDist, 2));
    }


}