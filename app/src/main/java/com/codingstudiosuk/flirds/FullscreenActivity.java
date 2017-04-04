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
    String[] debugInfo = {"FPS: 0", "Population: 0","Best flird: 23",
            "No. of generations: 0", "\nAve. speedMove: 0\n (0-0)", "\nAve. moveTurn: 0\n (0-0)", "\nAve. hunger: 0\n (0-0)",
            "\nAve. size: 0\n (0-0)", "\nAve. aggro: 0 (0)\n (0-0) 0 \n (0-0) 0"}; //String array used to display basic debugging info

    @Override
    protected void onCreate(Bundle savedInstanceState) { //Called when activity is created
        super.onCreate(savedInstanceState); //Call super
        setContentView(R.layout.view_sim); //Set the layout to be used
        simview = (SimView)findViewById(R.id.anim_view); //Get the View object
        simview.fullscreenActivity = this; //Pass a reference to this activity
        debug_text = (TextView)findViewById(R.id.debug_basic); //Grab some textviews
        debug_advanced = (Button)findViewById(R.id.button_debug_advanced); //Advanced debugging button
//        debug_advanced.setOnClickListener(new View.OnClickListener() { //Some code to start an activity
//            public void onClick(View v) {
//                activityStart(DebugActivity.class, simview.flock);
//            }
//        });
        //Init drawerLayout
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout); //Get the drawerLayout (used to open and close nav drawer)
        setNavDrawer();

    }

    public void setNavDrawer(){ //Init navigation drawer (used to display debugging stuff
        String t = Arrays.toString(debugInfo); //Convert debugInfo array to a string
        t = t.substring(1, t.length()-1).replaceAll(",", "\n"); //Each element is on a new line
        debug_text.setText(t); //Set the text of the textview
        mDrawerToggle = new ActionBarDrawerToggle( //Create a new toggle for opening/closing the drawer
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
                getSupportActionBar().setTitle(R.string.app_name);
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle); //Some deprecated method to open the drawer **NEEDS UPDATING**

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Some methods for enabling the navigation bar
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    protected void onResume(){
        super.onResume();
    } //Method called when switching to this activity from something more fun

    @Override
    protected void onPostCreate(Bundle savedInstanceState) { //Some more important methods
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) { //^^
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){ //When you tap the three dots on the top right
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){ //Called when you tap a menu item
        int id = item.getItemId();
        if (mDrawerToggle.onOptionsItemSelected(item)) { //Opens the nav drawer when you tap the three lines top left
            return true;
        }
        switch(id){ //Used to open the relevant activity
            case R.id.mitem_about:
                activityStart(AboutActivity.class); //Pass the class into the activityStart method
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

    public void activityStart(Class<?> c) { //Takes a class and starts it as an activity using intents
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }

    public void activityStart(Class<?> c, ArrayList<Flird> flock) { //WIP to pass the Flirds into another activity
        String[] extra = new String[5];
        ArrayList<Flird> sorted = new ArrayList<Flird>();
        Intent intent = new Intent(this, c);
        intent.putExtra("Flock", flock);
        startActivity(intent);
    }

}
