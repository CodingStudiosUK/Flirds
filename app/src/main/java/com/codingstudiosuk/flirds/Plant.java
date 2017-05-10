package com.codingstudiosuk.flirds;

import android.graphics.Canvas;

class Plant extends Entity {

    Plant(ViewSim s){
        super(s);
        aggro = -10;
        size = v.inte(v.random(5, 25));
        fillMain.setARGB(255, 75, 150, 0);
    }
    Plant(ViewSim s, float x, float y, float si){
        super(s);
        aggro = -10;
        size = si;
        fillMain.setARGB(255, 100, 125, 0);
        pos.x = x; pos.y = y;
    }

    void run(Canvas c){
        interact();
        display(c);
    }

    private void interact(){
        for (int i = 0; i < v.flock.size(); i++){
            Flird f = v.flock.get(i);
            if (dist(f) <= size+f.size){
                dead = true;
            }
        }
        size*=0.998f;
        if(size < 0.1){
            dead = true;
        }
    }

    void display(Canvas c){
        c.drawCircle(pos.x, pos.y, size, fillMain);
    }

}