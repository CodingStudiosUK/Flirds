package com.codingstudiosuk.flirds;

import java.util.ArrayList;

public class FlirdRunner implements Runnable{

    ArrayList<Flird> flockSegment;

    FlirdRunner(Flird[] fs){
        for (Flird f : fs){
            try {
                flockSegment.add(f);
            }catch(java.lang.NullPointerException e){}
        }
    }

    @Override
    public void run(){
        for(int i = 0; i < flockSegment.size(); i++){
            Flird f = flockSegment.get(i);
            f.threadRun();
        }
    }
}
