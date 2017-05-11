package com.codingstudiosuk.flirds;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class ActivitySimulation extends AppCompatActivity {

    private Button buttonFlirdList;

    boolean exit = false;
    Timer timer = new Timer();

//    Intent activity = new Intent(MyActivity.this,NextActivity.class);
//    activity.putExtra("myObject", new Gson().toJson(myobject));
//    startActivity(activity);

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    ViewSim simview;
    ViewFlird flirdview;
    ArrayList<String> listItems = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    private TextView debug_text, debug_dna;
    Flird selected = null;
    String[][] debugText = {{"Framerate","Population (Current)", "Population (Total)", "Generation Number", "Generation Length", "Best flird",
                            "Speed (Move)","Speed (Turn)","Hunger","Size","Aggro",
                            "Speed (Move)", "  Range"},
                            {"0", "0", "0", "0", "0", "0",
                            "0","0","0","0","0",
                            "0", "0"}};
    String[] debugInfo = {"FPS: 0", "Population: 0", "Best flird: 23",
            "No. of generations: 0", "\nAve. speedMove: 0\n (0-0)", "\nAve. moveTurn: 0\n (0-0)", "\nAve. hunger: 0\n (0-0)",
            "\nAve. size: 0\n (0-0)", "\nAve. aggro: 0 (0)\n (0-0) 0 \n (0-0) 0"}; //String array used to display basic debugging info
    String[] advancedDebugInfo = {"CHROMOSOMES\n", "SpeedMove: 0,0,0", "SpeedTurn: 0,0,0", "Hunger: 0,0,0", "Size: 0,0,0", "Aggro: 0,0,0"}; //String array used to display basic debugging info

    @Override
    protected void onCreate(Bundle savedInstanceState) { //Called when activity is created
        super.onCreate(savedInstanceState); //Call super
        setContentView(R.layout.activity_simulation); //Set the layout to be used

        simview = (ViewSim) findViewById(R.id.view_sim_view); //Get the View object
        simview.mAct = this; //Pass a reference to this activity
        flirdview = (ViewFlird) findViewById(R.id.view_flird_view);
        flirdview.mainActivity = this;
        flirdview.setVisibility(View.INVISIBLE);

        debug_text = (TextView) findViewById(R.id.debug_basic); //Grab some textviews

        //Init button
        buttonFlirdList = (Button) findViewById(R.id.button_sim_toggledebug);
        buttonFlirdList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { //Show flird button
                if (simview.getVisibility() == View.INVISIBLE) {
                    simview.setVisibility(View.VISIBLE);
                    flirdview.setVisibility(View.INVISIBLE);
                } else {
                    simview.setVisibility(View.INVISIBLE);
                    flirdview.setVisibility(View.INVISIBLE);
                    addItems(simview);
                }
                setNavDrawer();
                mDrawerLayout.closeDrawer(Gravity.END);

            }
        });
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout); //Get the drawerLayout (used to open and close nav drawer)
        mDrawerLayout.setScrimColor(getResources().getColor(android.R.color.transparent));
        setNavDrawer();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listItems);
        ListView lv = (ListView) findViewById(R.id.list_sim_flirds);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() { //When you click on the flirdlist
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
                String item = (String) adapter.getItemAtPosition(position > 0 ? position : 1);

                int uuid = Integer.parseInt(item.substring(0, item.indexOf(":")));
                for (int i = 0; i < simview.flock.size(); i++) {
                    if (simview.flock.get(i).uuid == uuid) {
                        selected = simview.flock.get(i);
                        break;
                    }
                }
                simview.selected = selected;
                flirdview.setVisibility(View.VISIBLE);
                flirdview.setFlird(selected);
                setNavDrawer();
                mDrawerLayout.closeDrawer(Gravity.END);
            }
        });

    }

    public void setNavDrawer() { //Init navigation drawer (used to display debugging stuff
        if (simview.getVisibility() == View.VISIBLE) {
            String t = "GENERAL\n";
            for (int i = 0; i < 6; i++){
                t += debugText[0][i]+": "+debugText[1][i]+"\n";
            }
            t += "\nCHROMOSOMES\n";
            for (int i = 6; i < 11; i++){
                t += debugText[0][i]+": "+debugText[1][i]+"\n";
            }
            t += "\nAVERAGE PROPERTIES\n";
            for (int i = 11; i < 13; i+=2){
                t += debugText[0][i]+": "+debugText[1][i]+"\n";
                t += debugText[0][i+1]+": "+debugText[1][i+1]+"\n";
                t += "\n";
            }
            debug_text.setText(t); //Set the text of the textview
        } else {
            debug_text.setText(selected == null ? "None selected" : selected.uuid + "\n" + selected.aggro);
        }
        mDrawerToggle = new ActionBarDrawerToggle( //Create a new toggle for opening/closing the drawer
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle); //Some deprecated method to open the drawer **NEEDS UPDATING**

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Some methods for enabling the navigation bar
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    protected void onResume() {
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
    public boolean onCreateOptionsMenu(Menu menu) { //When you tap the three dots on the top right
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //Called when you tap a menu item
        int id = item.getItemId();
        if (mDrawerToggle.onOptionsItemSelected(item)) { //Opens the nav drawer when you tap the three lines top left
            mDrawerLayout.closeDrawer(Gravity.END);
            return true;
        }
        switch (id) { //Used to open the relevant activity
            case R.id.mitem_about:
                activityStart(ActivityAbout.class); //Pass the class into the activityStart method
                break;
            case R.id.mitem_help:
                activityStart(ActivityAbout.class);
                break;
            case R.id.mitem_info:
                if(mDrawerLayout.isDrawerOpen(Gravity.END)){
                    mDrawerLayout.closeDrawer(Gravity.END);
                }else{
                    mDrawerLayout.openDrawer(Gravity.END);
                    mDrawerLayout.closeDrawer(Gravity.START);
                }
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

    public void addItems(View v) {
        listItems.clear();
        for (int i = 0; i < simview.flock.size(); i++) {
            listItems.add(simview.flock.get(i).uuid + ": " + simview.flock.get(i).aggro);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(mDrawerLayout.isDrawerOpen(Gravity.START) || mDrawerLayout.isDrawerOpen(Gravity.END)){
                mDrawerLayout.closeDrawer(Gravity.START);
                mDrawerLayout.closeDrawer(Gravity.END);
            }else if(simview.getVisibility() == View.INVISIBLE){
                simview.setVisibility(View.VISIBLE);
                flirdview.setVisibility(View.INVISIBLE);
            }else{
                Toast.makeText(getApplicationContext(), (CharSequence)"Press back again to exit.", Toast.LENGTH_LONG).show();
                if(!exit) {
                    exit = true;
                    timer.schedule(new TimerTask() {
                        public void run() {
                            System.out.print("IT WORKEDSDFJLADS");
                            exit = false;
                        }
                    }, Toast.LENGTH_LONG*1000);
                }else{
                    System.exit(0); //Need to save stuff
                }
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
