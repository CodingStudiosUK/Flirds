package com.codingstudiosuk.flirds;

import android.graphics.Canvas;

class Plant extends Entity {

    Plant(ViewSim s){
        super(s);
        aggro = -10;
        size = v.inte(v.random(5, 25));
        fill.setARGB(255, 75, 150, 0);
        isSafe();
    }
    Plant(ViewSim s, float x, float y, float si){
        super(s);
        aggro = -10;
        size = si;
        fill.setARGB(255, 200, 120, 0);
        pos.x = x; pos.y = y;
    }

    private void isSafe() {
        while(true) {
            boolean safe = true;
            for (int i = 0; i < v.plants.size(); i++) {
                Plant f = v.plants.get(i);
                if (dist(f) < size + f.size) {
                    safe = false;
                    break;
                }
            }
            if(safe){break;}
            pos = new Vector(v.random(0, v.width), v.random(0, v.height));
        }
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
        size*=0.998f;
        if(size < 0.1){
            dead = true;
        }
    }

    void display(Canvas c){
        c.drawCircle(pos.x, pos.y, size, fill);
    }

}