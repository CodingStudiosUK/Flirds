package com.codingstudiosuk.flirds;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TabHost;
import android.widget.TextView;

import java.util.ArrayList;

public class DebugActivity extends AppCompatActivity {

    TabHost tabHost;
    TextView[] debug_texts = new TextView[4];

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);
        tabHost = (TabHost)findViewById(R.id.tab_host);
        tabHost.setup();
        initTabs();
        debug_texts[0] = (TextView)findViewById(R.id.tab1_text);
        debug_texts[1] = (TextView)findViewById(R.id.tab2_text);
        debug_texts[2] = (TextView)findViewById(R.id.tab3_text);
        debug_texts[3] = (TextView)findViewById(R.id.tab4_text);
        ArrayList<Flird> flock = (ArrayList<Flird>)getIntent().getSerializableExtra("Flock");
        System.out.print(flock.size());
        System.out.print(flock.get(0).uuid);
    }

    void initTabs(){
        //Tab 1
        TabHost.TabSpec spec = tabHost.newTabSpec("Tab One");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Tab One");
        tabHost.addTab(spec);

        //Tab 2
        spec = tabHost.newTabSpec("Tab Two");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Tab Two");
        tabHost.addTab(spec);

        //Tab 3
        spec = tabHost.newTabSpec("Tab Three");
        spec.setContent(R.id.tab3);
        spec.setIndicator("Tab Three");
        tabHost.addTab(spec);

        //Tab 4
        spec = tabHost.newTabSpec("Tab Four");
        spec.setContent(R.id.tab3);
        spec.setIndicator("Tab Four");
        tabHost.addTab(spec);
    }

}
