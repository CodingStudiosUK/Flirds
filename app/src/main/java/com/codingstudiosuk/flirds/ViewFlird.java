package com.codingstudiosuk.flirds;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

public class ViewFlird extends View{

    Handler h;

    Activity mainActivity;

    Paint fill = new Paint();

    Flird selectedFlird;

    int width, height;

    public ViewFlird(Context context, AttributeSet attrs) { //Constructor
        super(context, attrs);
        h = new Handler(); //frame handler
        setBackgroundColor(0xffffffff);
        fill.setARGB(255, 150, 75, 0);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) { //Called a couple times when the object is created
        super.onLayout(changed, l, t, r, b);
        setup();
    }

    public void setup(){
        width = getWidth();
        height = getHeight();
    }

    private Runnable r = new Runnable() { //Creates a draw loop
        @Override
        public void run() {
            invalidate();
        }
    };

    protected void onDraw(Canvas c){
        c.drawCircle(width/5,   height*0.2f, 50, fill); //Input nodes
        c.drawCircle(width/5*2, height*0.2f, 50, fill);
        c.drawCircle(width/5*3, height*0.2f, 50, fill);
        c.drawCircle(width/5*4, height*0.2f, 50, fill);

        c.drawCircle(width/6,   height*0.4f, 50, fill); //Hidden nodes
        c.drawCircle(width/6*2, height*0.4f, 50, fill);
        c.drawCircle(width/6*3, height*0.4f, 50, fill);
        c.drawCircle(width/6*4, height*0.4f, 50, fill);
        c.drawCircle(width/6*5, height*0.4f, 50, fill);
        h.postDelayed(r, 1000); //Call me again in 1 second
    }

    public void setFlird(Flird f){
        selectedFlird = f;
    }
}
