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
        c.drawCircle(width*0.2f, height*0.2f, 50, fill);
        c.drawCircle(width*0.4f, height*0.2f, 50, fill);
        c.drawCircle(width*0.6f, height*0.2f, 50, fill);
        c.drawCircle(width*0.8f, height*0.2f, 50, fill);
        h.postDelayed(r, 100 / 60); //Call me again in 100/60ms
    }

    public void setFlird(Flird f){
        selectedFlird = f;
    }
}
