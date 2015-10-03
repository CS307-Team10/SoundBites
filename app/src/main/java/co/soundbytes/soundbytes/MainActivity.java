package co.soundbytes.soundbytes;

import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.viewpagerindicator.LinePageIndicator;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout drawerLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = (DrawerLayout)findViewById(R.id.main_activity_drawer_layout);
        mDrawerToggle = initializeActionBarToggle();
        mDrawerToggle.syncState();
        drawerLayout.setDrawerListener(mDrawerToggle);

        //TODO populate navbar

        //TODO populate viewpager
        viewPager = (ViewPager)findViewById(R.id.view_pager);
        viewPager.setAdapter(new CustomViewPagerAdapter(getSupportFragmentManager(), getFragments()));

//        //TODO attach viewpager indicator
//        TabPageIndicator indicator = (TabPageIndicator)findViewById(R.id.view_pager_indicator);
//        indicator.setViewPager(viewPager);
        getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
    }

    private ArrayList<TitledFragment> getFragments(){
        ArrayList<TitledFragment> fragmentList = new ArrayList<>();
        //Add fragments here
        fragmentList.add(new EmptyFragment());
        fragmentList.add(new EmptyFragment());
        fragmentList.add(new EmptyFragment());
        return  fragmentList;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

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
//                getActionBar().setTitle(mTitle);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
//                getActionBar().setTitle(mDrawerTitle);
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
}
