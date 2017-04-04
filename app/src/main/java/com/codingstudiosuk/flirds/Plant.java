package com.codingstudiosuk.flirds;

import android.graphics.Canvas;

class Plant extends Entity {

    Plant(SimView s){
        super(s);
        aggro = -10;
        size = v.inte(v.random(5, 25));
        fill.setARGB(255, 75, 150, 0);
    }
    Plant(SimView s, float x, float y, float si){
        super(s);
        aggro = -23;
        size = si;
        fill.setARGB(255, 75, 150, 0);
        pos.x = x; pos.y = y;
    }

    void run(){
        interact();
    }

    private void interact(){
        for (int i = 0; i < v.flock.size(); i++){
            Flird f = v.flock.get(i);
            if (dist(f) <= size+f.size){
                dead = true;
            }
        }
    }

    void display(Canvas c){
        c.drawCircle(pos.x, pos.y, size, fill);
    }

}