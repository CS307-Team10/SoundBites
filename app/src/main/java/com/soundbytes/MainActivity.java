package com.soundbytes;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;

import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout drawerLayout;
    private ViewPager viewPager;
    private static MainActivity thisActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Inflate the layout
        setContentView(R.layout.activity_main);
        //find the navigation drawer
        drawerLayout = (DrawerLayout)findViewById(R.id.main_activity_drawer_layout);
        //nav drawer stuff
        mDrawerToggle = initializeActionBarToggle();
        mDrawerToggle.syncState();
        drawerLayout.setDrawerListener(mDrawerToggle);
        thisActivity = this;
        //populate navbar drawer
        populateNavDrawer();

        //Populate viewpager
        viewPager = (ViewPager)findViewById(R.id.view_pager);
        viewPager.setAdapter(new CustomViewPagerAdapter(getSupportFragmentManager(), getFragments()));

        //Attach viewpager indicator
        TabPageIndicator indicator = (TabPageIndicator)findViewById(R.id.view_pager_indicator);
        indicator.setViewPager(viewPager);
    }

    protected static Activity getMainActivity(){
        return MainActivity.thisActivity;
    }

    /**
     * This populates the Navigation drawer
     */
    private void populateNavDrawer(){
        //find the nav drawer listView
        ListView listView = (ListView)findViewById(R.id.drawer_list_view);
        //Create a new adpater for the listView and attach it
        DrawerAdapter adapter = new DrawerAdapter(this);
        listView.setAdapter(adapter);
    }

    /**
     * This is what determines which swipe screen we have in mainActivity
     * @return an ArrayList of the fragments that would be added to the viewpager
     */
    private ArrayList<TitledFragment> getFragments(){
        ArrayList<TitledFragment> fragmentList = new ArrayList<>();
        //Add fragments here
        fragmentList.add(new ComposeFragment());
        fragmentList.add(new EmptyFragment());
        return  fragmentList;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    /**
     * Navigation drawer stuff
     * @return an ActionBarDrawer callback
     */
    private ActionBarDrawerToggle initializeActionBarToggle(){
        return new ActionBarDrawerToggle(
                this,
                drawerLayout,
                R.string.drawer_open,
                R.string.drawer_close
        ){
            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //check with navbar
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * This is a hacky method to get rid of the keyboard after typing stuff in the
     * add fried textbox. What it does is it assigns focus to the linearLayout which
     * doesn't need a keyboard. This method is called by touching the linearLayout,
     * which is everywhere
     * @param v this is the view that triggered this method
     */
    public void clearFocus(View v){
        //Closes the soft input keyboard and gives focus to the relative layout,
        // since something must always have focus
        EditText editText = (EditText)findViewById(R.id.add_person_text);
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        v.requestFocus();
    }
}
