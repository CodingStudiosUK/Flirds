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

public class SimView extends View { //The canvas used to draw the flirds

    private Handler h; //Used for frames
    public Paint black = new Paint(0); //Black (to draw black stuff)
    public ArrayList<Flird> flock = new ArrayList<>(); //Create the flirds and plants arraylist
    public ArrayList<Plant> plants = new ArrayList<>();
    public int width = 1, height = 1, diag; //Some integers (diag is the diagonal length)
    int num = 0, numGens = 0;
    public long frameFPS, timeFPS, frameCount; //Used for counting frames/managing framerate and timing
    public int[][] code = new int[8][3]; //Used for chromosomes, decides which chromosomes relate to which physical trait
    public boolean setup = true; //Make sure init function is only called once (damn you canvas)
    FullscreenActivity fullscreenActivity; //A ference to the main activity
    float averages[] = new float[5]; //speedmove, speedturn, hunger, size, aggro //Used to display the averages, debugging
    public Random random = new Random();

    public void setup() { //Init function
        frameCount = 0; //Reset some stuff
        frameFPS = 0;
        timeFPS = SystemClock.elapsedRealtime(); //Get the current time in epochs
        if (setup) { //Only run once!
            width = this.getWidth(); //Init some more variables
            height = this.getHeight();
            diag = (int)Math.sqrt(Math.pow(width,2)+Math.pow(height,2)); //Woo Pythagoras
            for (int i = 0; i < code.length; i++){ //Initialise the chromeosomes with random numbers, which chromosomes to use
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
            for (int i = 0; i < 120; i++) { //Create a bunch of new Flirds and plants
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

    public SimView(Context context, AttributeSet attrs) { //Constructor
        super(context, attrs);
        h = new Handler(); //f handler
        setBackgroundColor(0xffffffff);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) { //Called a couple times when the object is created
        super.onLayout(changed, l, t, r, b);
        setup();
    }

    private Runnable r = new Runnable() { //Creates a draw loop
        @Override
        public void run() {
            invalidate();
        }
    };

    protected void onDraw(Canvas c) { //Called every 60th (ish) of a second
        for (int i = 0; i < plants.size(); i++) { //Run the plants
            plants.get(i).run();
            plants.get(i).display(c);
        }
        for (int i = 0; i < flock.size(); i++) { //Fun the flirds
            flock.get(i).run();
            flock.get(i).display(c);
        }
        for (int i = flock.size()-1; i > -1; i--) { //Remove dead flirds
            if (flock.get(i).dead) {
                flock.remove(i);
                if(flock.size() == 20){
                    breed();
                }
            }
        }
        for (int i = plants.size()-1; i > -1; i--) { //Remove dead plants
            if (plants.get(i).dead) {
                plants.remove(i);
            }
        }
        float rantemp = random(1); //Randomly add new plants
        if(rantemp > 0.4 && rantemp < 0.6){
            plants.add(new Plant(this));
        }
        frameFPS++; //Increment counters
        frameCount++;
        if (timeFPS + 1000 < SystemClock.elapsedRealtime()) { //Once a second update the debug stuff
            float min[] = {101,101,101,101,101};
            float max[] = {-1,-1,-1,-1,-1};
            float[] aggroRange = new float[flock.size()];
            float fps = (float)(frameFPS)/(SystemClock.elapsedRealtime()- timeFPS)*1000;
            for(int i = 0; i < flock.size(); i++){ //Get averages
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
            Arrays.sort(aggroRange); //Sort the aggros, for median, LQ and UQ
            fullscreenActivity.debugInfo[0] = "FPS: "+fps; //Set averages
            fullscreenActivity.debugInfo[1] = "Population: "+flock.size()+"/"+num;
            fullscreenActivity.debugInfo[2] = "Best flird: "+flock.get(0).uuid;
            fullscreenActivity.debugInfo[3] = "No. gens: "+numGens;
            fullscreenActivity.debugInfo[4] = "speedMove: "+averages[0]+"\n Range: "+min[0]+"-"+max[0]+"\n";
            fullscreenActivity.debugInfo[5] = "speedTurn: "+averages[1]+"\n Range: "+min[1]+"-"+max[1]+"\n";
            fullscreenActivity.debugInfo[6] = "hunger: "+averages[2]+"\n Range: "+min[2]+"-"+max[2]+"\n";
            fullscreenActivity.debugInfo[7] = "size: "+averages[3]+"\n Range: "+min[3]+"-"+max[3]+"\n";
            fullscreenActivity.debugInfo[8] = "aggro: "+averages[4];
            fullscreenActivity.debugInfo[8] +="\n Median: "+aggroRange[Math.round(aggroRange.length*0.5f)];
            fullscreenActivity.debugInfo[8] +="\n Range: "+min[4]+"-"+max[4];
            fullscreenActivity.debugInfo[8] +="\n Range: "+(max[4]-min[4]);
            fullscreenActivity.debugInfo[8] +="\n LQ-UQ: "+aggroRange[Math.round(aggroRange.length*0.25f)]+"-"+aggroRange[Math.round(aggroRange.length*0.75f)];
            fullscreenActivity.debugInfo[8] +="\n Diff: "+(aggroRange[Math.round(aggroRange.length*0.75f)]-aggroRange[Math.round(aggroRange.length*0.25f)]);
            //Set stuff for right nav drawer
            for(int i = 1; i < fullscreenActivity.advancedDebugInfo.length; i++) {
                String t = Arrays.toString(code[i]);
                t = t.substring(1, t.length()-1).replaceAll(" ", "").replaceAll(",", ".");
                String x = fullscreenActivity.advancedDebugInfo[i];
                x = x.subSequence(0, x.length()-5) + t;
                fullscreenActivity.advancedDebugInfo[i] = x;
            }
            fullscreenActivity.setNavDrawer();
            frameFPS = 0;
            timeFPS = SystemClock.elapsedRealtime();
        }
        h.postDelayed(r, 100 / 60); //Call me again in 100/60ms
    }
    public float random(float max) { //Pick a random number
        return random(0, max);
    }
    public float random(float min, float max) { //Same as above but with a minimum
        float temp = random.nextFloat();
        return map(temp,0,1,min,max);
    }
    public int inte(float f) { //Cuz we lazy
        return (int) f;
    }
    public float map(float n, float minOld, float maxOld, float minNew, float maxNew){ //For maping a value in one range to another range
        float rangeOld = maxOld-minOld, rangeNew = maxNew-minNew;
        return ((n-minOld)/rangeOld*rangeNew)+minNew;
    }
    public float constrain(float n, float min, float max){ //Constrain a value to a range
        if (n < min){
            n = min;
        }
        if (n > max){
            n = max;
        }
        return n;
    }
    public void breed(){ //Called if the population gets too low
        numGens++;
        ArrayList<Flird> breedPool = new ArrayList<>(); //Create a breeding pool
        ArrayList<Flird> newFlock = new ArrayList<>(); //New flock
        for (int j = 0; j < 10; j++){ //Add each flird 10 times, gives each Flird an equal chance of breeding irrelevant of location in the arraylist
            for (int i = 0; i < flock.size(); i++){
                breedPool.add(flock.get(i));
            }
        }
        for (int i = 0; i < breedPool.size()/2; i++){ //Pick two (probably) different Flirds and breed them
            int f1 = inte(random(0, breedPool.size()-1));
            int f2 = inte(random(0, breedPool.size()-1));
            while (f2 == f1){
                f2 = inte(random(0, breedPool.size()-1));
            }
            newFlock.add(new Flird(this, breedPool.get(f1), breedPool.get(f2)));
            breedPool.remove(f1); //Each Flird can only breed once (or 10 times)
            breedPool.remove(f2>f1?f2-1:f2); //Don't remove the wrong one!
        }
        for(int i = 0; i < newFlock.size(); i++) {
            flock.add(newFlock.get(i)); //Add the new flock to the existing flock
        }
    }
}