package com.codingstudiosuk.flirds;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.View;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class SimView extends View {

    private Handler h;
    public Paint black = new Paint(0);
    public ArrayList<Flird> flock = new ArrayList<>();
    public ArrayList<Plant> plants = new ArrayList<>();
    public int width = 1, height = 1, diag;
    int num = 0;
    public long frameFPS, timeFPS, frameCount;
    public int[][] code = new int[8][3];
    public boolean setup = true;
    FullscreenActivity fullscreenActivity;
    float averages[] = new float[5]; //speedmove, speedturn, hunger, size, aggro

    public void setup() {
        frameCount = 0;
        frameFPS = 0;
        timeFPS = SystemClock.elapsedRealtime();
        if (setup) {
            width = this.getWidth();
            height = this.getHeight();
            diag = (int)Math.sqrt(Math.pow(width,2)+Math.pow(height,2));
            for (int i = 0; i < code.length; i++){
                code[i][0] = inte(random(0, 8));
                code[i][1] = inte(random(0, 8));
                while (code[i][1] == code[i][0]) {
                    code[i][1] = inte(random(0, 8));
                }
                code[i][2] = inte(random(0, 8));
                while (code[i][2] == code[i][0] || code[i][2] == code[i][1]) {
                    code[i][2] = inte(random(0, 8));
                }
            }
            for (int i = 0; i < 150; i++) {
                flock.add(new Flird(this));
            }
            for (int i = 0; i < inte(random(75, 150)); i++) {
                plants.add(new Plant(this));
            }
            setup = false;
            black.setARGB(255, 0, 0, 0);
            black.setTextSize(50);
        }
    }

    public SimView(Context context, AttributeSet attrs) {
        super(context, attrs);
        h = new Handler();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        setup();
    }

    private Runnable r = new Runnable() {
        @Override
        public void run() {
            invalidate();
        }
    };

    protected void onDraw(Canvas c) {
        for (int i = 0; i < plants.size(); i++) {
            plants.get(i).run();
            plants.get(i).display(c);
        }
        for (int i = 0; i < flock.size(); i++) {
            flock.get(i).run();
            flock.get(i).display(c);
        }
        for (int i = flock.size()-1; i > -1; i--) {
            if (flock.get(i).dead) {
                flock.remove(i);
                if(flock.size() == 20){
                    breed();
                }
            }
        }
        for (int i = plants.size()-1; i > -1; i--) {
            if (plants.get(i).dead) {
                plants.remove(i);
            }
        }
        float rantemp = random(1);
        if(rantemp > 0.4 && rantemp < 0.6){
            plants.add(new Plant(this));
        }
        frameFPS++;
        frameCount++;
        if (timeFPS + 1000 < SystemClock.elapsedRealtime()) {
            float min[] = {101,101,101,101,101};
            float max[] = {-1,-1,-1,-1,-1};
            float[] aggroRange = new float[flock.size()];
            float fps = (float)(frameFPS)/(SystemClock.elapsedRealtime()- timeFPS)*1000;
            for(int i = 0; i < flock.size(); i++){
                Flird f = flock.get(i);
                averages[0] += f.speedMove;
                averages[1] += f.speedTurn;
                averages[2] += f.hunger;
                averages[3] += f.size;
                averages[4] += f.aggro;
                aggroRange[i] = f.aggro;
                if (f.speedMove<min[0]){min[0] = f.speedMove;}
                if (f.speedMove>max[0]){max[0] = f.speedMove;}
                if (f.speedTurn<min[1]){min[1] = f.speedTurn;}
                if (f.speedTurn>max[1]){max[1] = f.speedTurn;}
                if (f.hunger<min[2]){min[2] = f.hunger;}
                if (f.hunger>max[2]){max[2] = f.hunger;}
                if (f.size<min[3]){min[3] = f.size;}
                if (f.size>max[3]){max[3] = f.size;}
                if (f.aggro<min[4]){min[4] = f.aggro;}
                if (f.aggro>max[4]){max[4] = f.aggro;}
            }
            for(int i = 0; i < averages.length; i++){
                averages[i]/=flock.size();
            }
            Arrays.sort(aggroRange);
            fullscreenActivity.debugInfo[0] = "FPS: "+fps;
            fullscreenActivity.debugInfo[1] = "\nPopulation: "+flock.size();
            fullscreenActivity.debugInfo[2] = "\nBest flird: "+flock.get(0).uuid;
            fullscreenActivity.debugInfo[3] = "\nspeedMove: "+averages[0]+"\n ("+min[0]+"-"+max[0]+")";
            fullscreenActivity.debugInfo[4] = "\nspeedTurn: "+averages[1]+"\n ("+min[1]+"-"+max[1]+")";
            fullscreenActivity.debugInfo[5] = "\nhunger: "+averages[2]+"\n ("+min[2]+"-"+max[2]+")";
            fullscreenActivity.debugInfo[6] = "\nsize: "+averages[3]+"\n ("+min[3]+"-"+max[3]+")";
            fullscreenActivity.debugInfo[7] = "\naggro: "+averages[4];
            fullscreenActivity.debugInfo[7] +="\n "+aggroRange[Math.round(aggroRange.length*0.5f)];
            fullscreenActivity.debugInfo[7] +="\n "+min[4]+"-"+max[4];
            fullscreenActivity.debugInfo[7] +="\n "+(max[4]-min[4]);
            fullscreenActivity.debugInfo[7] +="\n "+aggroRange[Math.round(aggroRange.length*0.25f)]+"-"+aggroRange[Math.round(aggroRange.length*0.75f)];
            fullscreenActivity.debugInfo[7] +="\n "+(aggroRange[Math.round(aggroRange.length*0.75f)]-aggroRange[Math.round(aggroRange.length*0.25f)]);
            fullscreenActivity.setNavDrawer();
            frameFPS = 0;
            timeFPS = SystemClock.elapsedRealtime();
        }
        h.postDelayed(r, 1000/60);
        //some change
    }
    public float random(float max) {
        float min = 0;
        Random r = new Random();
        float temp = r.nextFloat();
        r.nextFloat();r.nextFloat();r.nextFloat();r.nextFloat();
        return map(temp,0,1,min,max);
    }
    public float random(float min, float max) {
        Random r = new Random();
        float temp = r.nextFloat() + r.nextFloat() + r.nextFloat() + r.nextFloat();
        return map(temp,0,4,min,max);
    }
    public int inte(float f) {
        return (int) f;
    }
    public float map(float n, float minOld, float maxOld, float minNew, float maxNew){
        float rangeOld = maxOld-minOld, rangeNew = maxNew-minNew;
        return ((n-minOld)/rangeOld*rangeNew)+minNew;
    }
    public float constrain(float n, float min, float max){
        if (n < min){
            n = min;
        }
        if (n > max){
            n = max;
        }
        return n;
    }
    public void breed(){
        ArrayList<Flird> newFlock = new ArrayList<>();
        for(int i = 0; i < 100; i++){
            newFlock.add(new Flird(this, flock.get(inte(random(0, flock.size()-1))), flock.get(inte(random(0, flock.size()-1)))));
        }
        for(int i = 0; i < newFlock.size(); i++) {
            flock.add(newFlock.get(i));
        }
    }
    public void breedNew(){
        ArrayList<Flird> breedPool = new ArrayList<>();
        ArrayList<Flird> newFlock = new ArrayList<>();
        for (int j = 0; j < 10; j++){
            for (int i = 0; i < flock.size(); i++){
                breedPool.add(flock.get(i));
            }
        }
        for (int i = 0; i < breedPool.size()/2; i++){
            int f1 = inte(random(0, breedPool.size()-1));
            int f2 = inte(random(0, breedPool.size()-1));
            while (f2 == f1){
                f2 = inte(random(0, breedPool.size()-1));
            }
            newFlock.add(new Flird(this, breedPool.get(f1), breedPool.get(f2)));
            breedPool.remove(f1);
            breedPool.remove(f2>f1?f2-1:f2);
        }
        for(int i = 0; i < newFlock.size(); i++) {
            flock.add(newFlock.get(i));
        }
    }
}