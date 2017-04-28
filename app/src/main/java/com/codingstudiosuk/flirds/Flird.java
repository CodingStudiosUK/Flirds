package com.codingstudiosuk.flirds;

import android.graphics.Canvas;

class Flird extends Entity{

    int uuid;
    private boolean a = false, d = false;

    private float dir = v.random(0, 360);
    private float health = 1f;
    float speedMove;
    float speedTurn;
    float hunger;
    private int intel;
    private byte[] dna = new byte[8];

    private float[][][] network = new float[3][5][5];
    private float[] thresh = new float[2];
    private int type;
    private int choice;
    private int mateCoolDown = 0;
    transient private Entity closePrey, closeMate, closePred, closeAll;

    Flird(ViewSim s){
        super(s);
        fill.setARGB(255, 75, 0, 75);
        for (int i = 0; i < 8; i++){
            dna[i] = (byte)v.random(-128,127);
        }
        for (int j = 0; j < network[0].length; j++) {
            for (int k = 0; k < network[0][0].length; k++) {
                network[0][j][k] = -23;
                network[1][j][k] = v.random(0, 2);
                network[2][j][k] = v.random(0, 2);
            }
        }
        getCodes();
        //isSafe();
    }

    Flird(ViewSim s, Flird a, Flird b){
        super(s);
        fill.setARGB(255, 150, 75, 0);
        for(int i = 0; i < 8; i++){
            float t = v.random(0,1);
            dna[i] = t < 0.1?(byte)v.random(-128,127):(t < 0.55?a.dna[i]:b.dna[i]);
        }
        for (int j = 0; j < network[0].length; j++) {
            for (int k = 0; k < network[0][0].length; k++) {
                network[0][j][k] = -23;
                network[1][j][k] = v.random(1)>0.5?a.network[1][j][k]*v.random(0.65f, 0.99f):b.network[1][j][k]*v.random(0.65f, 0.99f);
                network[2][j][k] = v.random(1)>0.5?a.network[2][j][k]*v.random(0.65f, 0.99f):b.network[2][j][k]*v.random(0.65f, 0.99f);
                if (v.random(1)<0.1) {
                    network[1][j][k] = v.random(0, 2);
                    network[2][j][k] = v.random(0, 2);
                }
            }
        }
        getCodes();
        if(v.random(Math.abs(aggro-v.averages[4]))<5){
            dna[v.code[4][0]] = (byte)v.random(-128,127);
            dna[v.code[4][1]] = (byte)v.random(-128,127);
            dna[v.code[4][2]] = (byte)v.random(-128,127);
        }
        getCodes();
        isSafe();
    }

    private void isSafe(){
        while(true) {
            boolean safe = true;
            for (int i = 0; i < v.flock.size(); i++) {
                Flird f = v.flock.get(i);
                if (dist(f) < size + f.size) {
                    safe = false;
                    break;
                }
            }
            if(safe){break;}
            pos = new Vector(v.random(0, v.width), v.random(0, v.height));
        }
    }



    private Flird(ViewSim s, Flird a, Flird b, float x, float y) {
        this(s, a, b);
        pos.x = x; pos.y = y;
    }

    private void getCodes(){
        speedMove = getCode(0,5,15);
        speedTurn = getCode(1,2,6);
        hunger = getCode(2, 0.0001f, 0.001f);
        size = getCode(3,5,20);
        aggro = getCode(4,0,100);
        thresh[0] = getCode(5, 0.1f, 0.45f);
        thresh[1] = getCode(6, 0.55f, 0.9f);
        intel = v.inte(getCode(7,5,21));
        if (v.random(1)>0.8){
            type = v.inte(v.random(0, 1));
        }
        else{
            type = v.inte(v.constrain(aggro/50, 0, 1));
        }
        uuid = v.num;
        v.num++;
    }

    private float getCode(int n, float min, float max){
        return v.map(dna[v.code[n][0]]^dna[v.code[n][1]]^dna[v.code[n][2]],-128,127,min,max);
    }

    void run(){
        if (v.frameCount % Math.ceil(intel) == 0) {
            target();
            calculate();
        }
        decide();
        movement();
        interact();
        if(mateCoolDown>0) mateCoolDown--;
    }

    private void target() {
        closeAll = null;
        closePred = null;
        closePrey = null;
        closeMate = null;
        for (int i = 0; i < v.flock.size(); i++) {
            Flird f = v.flock.get(i);
            float aggroDif = f.aggro - aggro;
            if (aggroDif > 5) {
                if (((closePred == null || closePred.dead) || dist(f) < dist(closePred)) && f != this) {
                    closePred = f;
                }
            } else if (type != 0 && aggroDif < -5) {
                if (((closePrey == null || closePrey.dead) || dist(f) < dist(closePrey)) && f != this) {
                    closePrey = f;
                }
            } else {
                if (((closeMate == null || closeMate.dead) || dist(f) < dist(closeMate)) && f != this) {
                    closeMate = f;
                }
            }
        }
        if (type != 2){
            for (int i = 0; i < v.plants.size(); i++) {
                if ((closePrey == null || closePrey.dead) || dist(v.plants.get(i)) < dist(closePrey)) {
                    closePrey = v.plants.get(i);
                }
            }
        }
        if (closePred != null && closeAll == null && closeAll == null && closeAll == null) {
            closeAll = closePred;
        }
        if (closePrey != null && (closeAll == null || dist(closePrey) < dist(closeAll))) {
            closeAll = closePrey;
        }
        if (closeMate != null && (closeAll == null || dist(closeMate) < dist(closeAll))) {
            closeAll = closeMate;
        }
    }

    private void calculate() {
        float[][] nodeVals = new float[3][5];
        nodeVals[0][0] = 1; // Bias - test with different values
        nodeVals[0][1] = health; // Hunger
        nodeVals[0][2] = v.map(closeAll.aggro-aggro,-123,100,1,0); // Closest Flird type
        nodeVals[0][3] = v.map(aggro, 0, 100, 1, 0); // Inverse of aggro
        for (int j = 0; j < 5; j++) {
            nodeVals[1][j] = 0;
            for (int k = 0; k < 4; k++) {
                nodeVals[1][j] += nodeVals[0][k]*network[1][j][k];
            }
            nodeVals[1][j] = sig(nodeVals[1][j]);
        }
        nodeVals[2][0] = 0;
        for (int k = 0; k < 5; k++) {
            nodeVals[2][0] += nodeVals[1][k]*network[2][0][k];
        }
        nodeVals[2][0] = sig(nodeVals[2][0]);
        choice = (nodeVals[2][0] > 1 || nodeVals[2][0] < 0 ? 3 : (nodeVals[2][0] < thresh[0] ? 0 : (nodeVals[2][0] < thresh[1] ? 1 : 2)));
    }

    private void decide(){
        float ang, turn = 0;
        switch(choice) {
            case 0: // Prey
                if (closePrey == null || dist(closePrey) > v.diag*0.3) {
                    choice = 3;
                } else {
                    ang = (float)Math.toDegrees(Math.atan2(closePrey.pos.y-pos.y, closePrey.pos.x-pos.x));
                    turn = v.map((dir-ang+3780)%360, 0, 360, 180, -180);
                }
                break;
            case 1: // Mate
                if (closeMate == null || dist(closeMate) > v.diag*0.3) {
                    choice = 3;
                } else {
                    ang = (float)Math.toDegrees(Math.atan2(closeMate.pos.y-pos.y, closeMate.pos.x-pos.x));
                    turn = v.map((dir-ang+3780)%360, 0, 360, 180, -180);
                }
                break;
            case 2: // Predator
                if (closePred == null || dist(closePred) > v.diag*0.3) {
                    choice = 3;
                } else {
                    ang = (float)Math.toDegrees(Math.atan2(closePred.pos.y-pos.y, closePred.pos.x-pos.x));
                    turn = v.map((dir-ang+3600)%360, 0, 360, 180, -180);
                }
                break;
        }
        if (choice == 3) {
            a = d?v.inte(v.random(0, 4))==0:v.inte(v.random(0, 4))!=0;
            d = a?v.inte(v.random(0, 4))==0:v.inte(v.random(0, 4))!=0;
        }
        else{
            if (turn < 20 && turn > -20) { //Don't turn if I'm already within 20 of intended angle.
                a = false;
                d = false;
            } else if (turn < 0) { //Is it quicker to turn left or right?
                a = true;
                d = false;
            } else {
                a = false;
                d = true;
            }
        }
    }

    private void movement(){
        if (a) {
            dir -= speedTurn;
        }
        if (d) {
            dir += speedTurn;
        }
        Vector vel = new Vector(speedMove, 0);
        vel.rotate(dir);
        pos.add(vel);
        if (pos.x > v.width) {
            pos.x -= v.width;
        }
        if (pos.x < 0) {
            pos.x += v.width;
        }
        if (pos.y > v.height) {
            pos.y -= v.height;
        }
        if (pos.y < 0) {
            pos.y += v.height;
        }
    }

    private void interact(){
        health -= hunger;
        if (health <= 0){
            dead = true;
            dropFood();
        }
        fill.setARGB((int)v.map(health,0,1,0,255), 150, 75, 0);
        for (int i = 0; i < v.flock.size(); i++){
            Flird f = v.flock.get(i);
            if (dist(f) <= size+f.size && this != f){
                if(choice==2 && f.choice==2 && v.flock.size()<100 && mateCoolDown==0 && f.mateCoolDown==0) {
                    v.flock.add(new Flird(v, this, f, pos.x + size + v.width * 0.1f, pos.y));
                    mateCoolDown = 120;
                    f.mateCoolDown = 120;
                }
                else f.health -= aggro / 200;
            }
        }
    }

    private void dropFood(){
        int ranAmount = v.inte(v.random(1, 10));
        for(int i = 0; i < ranAmount; i++) {
            v.plants.add(new Plant(v, pos.x + v.random(-v.width * 0.05f, v.width * 0.05f), pos.y + v.random(-v.width * 0.05f, v.width * 0.05f), size/ranAmount+v.random(-1f, 1f)));
        }
    }

    void display(Canvas c){
        Vector face = new Vector(size/2, 0);
        face.rotate(dir);
        face.add(pos);
        c.drawCircle(pos.x, pos.y, size, fill);
        c.drawCircle(face.x,face.y,size/2,v.black);//Flird
    }

    private float sig(float val) {
        return (float)(1/(1+Math.pow((float)Math.E, -(val*0.8f)))); //To do: map sig functions to find the most effective
    }

    float fitness(){
        return 0;//constrain(timeAlive, 0, 30)+numConflicts*2
    }

}