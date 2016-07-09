package com.riosistemas.butecos;
import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;

/**
 * Created by nesralla on 7/6/16.
 */

public class Main extends TabActivity{
    TabHost tabHost;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        Resources res = getResources(); // Resource object to get Draw
        tabHost = getTabHost();  // The activity TabHost
        TabHost.TabSpec spec;  // Reusable TabSpec for each tab
        Intent intent;  // Reusable Intent for each tab

        try {
            Log.d("Class Splash","Criação das TABS");
            // Create an Intent to launch an Activity for the tab (to be reused)
            intent = new Intent().setClass(this, Restaurants.class);

            // Initialize a TabSpec for each tab and add it to the TabHost
            spec = tabHost.newTabSpec("Butecos").setIndicator("Butecos",res.getDrawable(R.mipmap.ic_tab_beermug)).setContent(intent);
            //,res.getDrawable(R.drawable.ic_tab_beer)
            tabHost.addTab(spec);
            // Do the same for the other tabs
            intent = new Intent().setClass(this, Foods.class);
            spec = tabHost.newTabSpec("Petiscos").setIndicator("Petiscos",res.getDrawable(R.mipmap.ic_tab_forknife)).setContent(intent);
            //,res.getDrawable(R.drawable.ic_tab_forknife)
            tabHost.addTab(spec);

            intent = new Intent().setClass(this, Go.class);
            spec = tabHost.newTabSpec("Ir").setIndicator("Ir",res.getDrawable(R.mipmap.ic_tab_compass)).setContent(intent);
            //, res.getDrawable(R.drawable.ic_tab_compass)
            tabHost.addTab(spec);
            tabHost.setCurrentTab(0);
            for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
                tabHost.getTabWidget().getChildAt(i);
            }
            tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab());
            tabHost.setOnTabChangedListener(new OnTabChangeListener() {

                public void onTabChanged(String tabId) {
                    // TODO Auto-generated method stub
                    for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
                        tabHost.getTabWidget().getChildAt(i);
                    }
                    tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab());
                }
            });
        } catch (Exception e) {
            // TODO: handle exception
            Log.e("Class Splash",e.getMessage());
        }



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                startActivity(new Intent(this, Settings.class));
                finish();
                return true;
            case R.id.about:
                startActivity(new Intent(this,About.class));
                return true;
            case R.id.help:
                startActivity(new Intent(this,Help.class));
                return true;
            case R.id.exit:
                finish();



        }
        return false;
    }


}
