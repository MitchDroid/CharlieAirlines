package com.southwest.southwestapp.activities;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.southwest.southwestapp.AppHelper;
import com.southwest.southwestapp.R;
import com.southwest.southwestapp.apis.FlickrApi;
import com.southwest.southwestapp.fragments.homepage.BigPagerHomeFragment;
import com.southwest.southwestapp.fragments.homepage.TripActionsFragment;
import com.squareup.picasso.Picasso;

import retrofit.Callback;
import retrofit.Response;


public class MainActivity extends AppCompatActivity implements BigPagerHomeFragment.SlidePanelListener {

    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private ActionBarDrawerToggle mDrawerToggle;
    private TripActionsFragment tripFragment;
    private BigPagerHomeFragment homeFragment;
    private int mCurrentSelectedPosition;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpToolBar();
        setUpNavDrawer();

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
        } else {
            homeFragment = AppHelper.screenManager.showMainScreen(this);
            slideTripPanelUp();
        }
        final Context context = this;
        final FlickrApi flickrApi = new FlickrApi();
        flickrApi.getInterface().searchPhotos("paris").enqueue(new Callback<FlickrApi.SearchPhotoResponse>() {
            @Override
            public void onResponse(Response<FlickrApi.SearchPhotoResponse> response) {
                Log.e(this.getClass().getSimpleName(), response.toString());
                String photoid = response.body().photos.photo.get(1).id;
                String farmid = response.body().photos.photo.get(1).farm;
                String serverid = response.body().photos.photo.get(1).server;
                String secret = response.body().photos.photo.get(1).secret;
                String url = "https://farm" + farmid +".staticflickr.com/"+ serverid +"/"+photoid+"_"+ secret+"_c.jpg";
                Picasso.with(context).load(url).into(((ImageView) findViewById(R.id.drawer_background)));
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(this.getClass().getSimpleName(), t.toString());
            }
        });
    }


    private void setUpNavDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.nav_drawer);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        TextView text = (TextView) findViewById(R.id.passenger_name);
        text.setText(AppHelper.userController.getUserProfile().getUserName());
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                menuItem.setChecked(true);
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                switch (menuItem.getItemId()) {
                    case R.id.checkIn:
                        AppHelper.screenManager.showCheckInSearchScreen(MainActivity.this);
                        mCurrentSelectedPosition = 2;
                        return true;

                    case R.id.home:
                        homeFragment = AppHelper.screenManager.showMainScreen(MainActivity.this);
                        mCurrentSelectedPosition = 0;
                        slideTripPanelUp();
                        return true;

                    case R.id.logout:
                        return true;

                    default:
                        //Event not handled: return false.
                        return false;
                }
            }
        });
    }

    private void setUpToolBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        /*Call syncState() from your Activity's onPostCreate to synchronize the indicator with the
         state of the linked DrawerLayout after onRestoreInstanceState has occurred.*/
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //This method should always be called by your Activity's onConfigurationChanged method.
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION, 0);
        Menu menu = mNavigationView.getMenu();
        menu.getItem(mCurrentSelectedPosition).setChecked(true);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
            mDrawerLayout.closeDrawer(Gravity.LEFT);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public void slideTripPanelUp() {
        tripFragment = TripActionsFragment.newInstance(true);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.fade_in_bottom, R.anim.slide_out_bottom_with_fade_out);
        ft.replace(R.id.panel_container, tripFragment);
        ft.commit();
    }

    @Override
    public void slideTripPanelDown() {
        if (tripFragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.anim.fade_in_bottom, R.anim.slide_out_bottom_with_fade_out);
            ft.remove(tripFragment);
            ft.commit();
        }
    }
}
