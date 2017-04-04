package com.codingstudiosuk.flirds;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class FullscreenActivity extends AppCompatActivity {

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    Button debug_advanced;
    SimView simview;
    private TextView debug_text;
    String[] debugInfo = {"FPS: 0", "\nPopulation: 0","\nBest flird: 23",
            "No. of generations: 0", "\nAve. speedMove: 0\n (0-0)", "\nAve. moveTurn: 0\n (0-0)", "\nAve. hunger: 0\n (0-0)",
            "\nAve. size: 0\n (0-0)", "\nAve. aggro: 0 (0)\n (0-0) 0 \n (0-0) 0"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_sim);
        simview = (SimView)findViewById(R.id.anim_view);
        simview.fullscreenActivity = this;
        debug_text = (TextView)findViewById(R.id.debug_basic);
        debug_advanced = (Button)findViewById(R.id.button_debug_advanced);
//        debug_advanced.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                activityStart(DebugActivity.class, simview.flock);
//            }
//        });
        //Init drawerLayout
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        setNavDrawer();

    }

    public void setNavDrawer(){
        String t = Arrays.toString(debugInfo);
        t = t.substring(1, t.length()-1).replaceAll(",", "\n");
        debug_text.setText(t);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(R.string.app_name);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(R.string.app_name_debug);
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch(id){
            case R.id.mitem_about:
                activityStart(AboutActivity.class);
                break;
            case R.id.mitem_help:
                activityStart(AboutActivity.class);
                break;
            default:
                System.out.println("Error.");
                break;
        }
        return true;
    }

    public void activityStart(Class<?> c) {
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }

    public void activityStart(Class<?> c, ArrayList<Flird> flock) {
        String[] extra = new String[5];
        ArrayList<Flird> sorted = new ArrayList<Flird>();
        Intent intent = new Intent(this, c);
        intent.putExtra("Flock", flock);
        startActivity(intent);
    }

}
