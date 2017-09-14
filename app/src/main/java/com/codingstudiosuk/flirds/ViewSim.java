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
import java.util.Collections;
import java.util.Locale;
import java.util.Random;

public class ViewSim extends View { //The canvas used to draw the flirds

    final int POPULATION_NEW = 100;
    final int POPULATION_BUFFER = 20;

    private Handler h; //Used for frames
    public Paint black = new Paint(0); //Black (to draw black stuff)
    private Paint[] rings = {new Paint(0), new Paint(0), new Paint(0), new Paint(0)};
    private boolean setup = true; //Make sure init function is only called once (damn you canvas)
    ActivitySimulation mAct; //A reference to the main activity
    public Random random = new Random();

    public ArrayList<Flird> flock = new ArrayList<>(); //Create the flirds and plants arraylist
    public ArrayList<Plant> plants = new ArrayList<>();

    public int width = 1, height = 1, diag; //Some integers (diag is the diagonal length)

    int num = 0, numGens = 0, secondsElapsed = 0, pgenLength = 0; //Some counters for timing and counting
    public long frameFPS, timeFPS, frameCount; //Used for counting frames/managing framerate and timing

    public int[][] code = new int[8][3]; //Used for chromosomes, decides which chromosomes relate to which physical trait

    float averages[] = new float[5]; //speedmove, speedturn, hunger, size, aggro //Used to display the averages, debugging
    Flird selected;

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
            for (int i = 0; i < POPULATION_NEW; i++) { //Create a bunch of new Flirds and plants
                flock.add(new Flird(this));
            }
            for (int i = 0; i < inte(random(75, 150)); i++) {
                plants.add(new Plant(this));
            }
            setup = false;
            black.setARGB(255, 0, 0, 0);
            black.setTextSize(50);

            for (Paint ring : rings) {
                ring.setStyle(Paint.Style.STROKE);
                ring.setStrokeWidth(width * 0.008f);
            }
            rings[0].setARGB(255, 250, 175, 50);
            rings[1].setARGB(255, 160, 180, 180);
            rings[2].setARGB(255, 200, 175, 50);
            rings[3].setARGB(255, 10, 70, 160);
        }
    }

    public ViewSim(Context context, AttributeSet attrs) { //Constructor
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
        for (int i = 0; i < plants.size(); i++){
            Plant p = plants.get(i); //Run the plants
            p.run(c);
        }
        for (int i = 0; i < flock.size(); i++){
            Flird f = flock.get(i); //Run the flirds
            f.run(c);
        }
//        Draw rings on best Flirds
        for(int i = 0; i < 3; i++) {
            c.drawCircle(flock.get(i).pos.x, flock.get(i).pos.y, width * 0.05f, rings[i]);
        }
        //Selected Flird
        if(selected != null) {
            c.drawCircle(selected.pos.x, selected.pos.y, width*0.05f, rings[3]);
        }

        for (int i = flock.size()-1; i > -1; i--) { //Remove dead flirds
            if (flock.get(i).dead) {
                flock.get(i).dropFood();
                if(flock.get(i)==selected){
                    selected = null;
                }
                flock.remove(i);
                if(flock.size() == POPULATION_BUFFER){
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
        if(this.getVisibility() == View.INVISIBLE){
            Collections.sort(flock);
        }
        if (timeFPS + 1000 < SystemClock.elapsedRealtime()) { //Once a second update the debug stuff
            float fps = (float)(frameFPS)/(SystemClock.elapsedRealtime()- timeFPS)*1000;
            float min[] = {101,101,101,101,101};
            float max[] = {-1,-1,-1,-1,-1};
            float[] aggroRange = new float[flock.size()];
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
            Collections.sort(flock);
            mAct.addItems(this);
            Arrays.sort(aggroRange); //Sort the aggros, for median, LQ and UQ
            //mAct.debugInfo[0] = "FPS: "+fps; //Set averages
            //mAct.debugInfo[1] = "Population: "+flock.size()+"/"+num;
            //mAct.debugInfo[2] = "Best flird: "+flock.get(0).uuid;
            //mAct.debugInfo[3] = "No. gens: "+numGens+"/"+pgenLength;
            mAct.debugInfo[4] = "speedMove: "+averages[0]+"\n Range: "+min[0]+"-"+max[0]+"\n";
            mAct.debugInfo[5] = "speedTurn: "+averages[1]+"\n Range: "+min[1]+"-"+max[1]+"\n";
            mAct.debugInfo[6] = "hunger: "+averages[2]+"\n Range: "+min[2]+"-"+max[2]+"\n";
            mAct.debugInfo[7] = "size: "+averages[3]+"\n Range: "+min[3]+"-"+max[3]+"\n";
            mAct.debugInfo[8] = "aggro: "+averages[4];
            mAct.debugInfo[8] +="\n Median: "+aggroRange[Math.round(aggroRange.length*0.5f)];
            mAct.debugInfo[8] +="\n Range: "+min[4]+"-"+max[4];
            mAct.debugInfo[8] +="\n Range: "+(max[4]-min[4]);
            mAct.debugInfo[8] +="\n LQ-UQ: "+aggroRange[Math.round(aggroRange.length*0.25f)]+"-"+aggroRange[Math.round(aggroRange.length*0.75f)];
            mAct.debugInfo[8] +="\n Diff: "+(aggroRange[Math.round(aggroRange.length*0.75f)]-aggroRange[Math.round(aggroRange.length*0.25f)]);
            mAct.debugText[1][0] = String.format(Locale.getDefault(),"%.2f", fps);
            mAct.debugText[1][1] = Integer.toString(flock.size());
            mAct.debugText[1][2] = Integer.toString(num);
            mAct.debugText[1][3] = Integer.toString(numGens);
            mAct.debugText[1][4] = Integer.toString(pgenLength);
            mAct.debugText[1][5] = Integer.toString(flock.get(0).uuid);
            for (int i = 6; i < 11; i++){
                mAct.debugText[1][i] = code[i-6][0]+", "+code[i-6][1]+", "+code[i-6][2];
            }
            mAct.debugText[1][11] = String.format(Locale.getDefault(),"%.2f",averages[0]);
            mAct.setNavDrawer();
            frameFPS = 0;
            timeFPS = SystemClock.elapsedRealtime();
            secondsElapsed++;
        }
        h.postDelayed(r, 1000/60); //Call me again in 100/60ms
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
        if (n < min) n = min;
        if (n > max) n = max;
        return n;
    }
    public void breed(){ //Called if the population gets too low
        numGens++;
        pgenLength = numGens%5==0?pgenLength:secondsElapsed/(numGens%5);
        secondsElapsed = numGens%5==0?0:secondsElapsed;
        ArrayList<Flird> breedPool = new ArrayList<>(); //Create a breeding pool
        for (int j = 0; j < POPULATION_NEW*2; j++){ //Add each flird multiple times, gives each Flird an equal chance of breeding irrelevant of location in the arraylist
            for (int i = 0; i < flock.size(); i++){
                breedPool.add(flock.get(i));
            }
        }
        for (int i = 0; i < POPULATION_NEW; i++){ //Pick two (probably) different Flirds and breed them
            int tmp = inte(random(0, breedPool.size()-1));
            Flird f1 = breedPool.get(tmp);
            breedPool.remove(tmp); //Each Flird can only breed once (or 10 times)
            tmp = inte(random(0, breedPool.size()-1));
            Flird f2 = breedPool.get(tmp);
            breedPool.remove(tmp); //Each Flird can only breed once (or 10 times)
            flock.add(new Flird(this, f1, f2));
        }
    }

}