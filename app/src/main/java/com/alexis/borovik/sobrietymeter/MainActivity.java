package com.alexis.borovik.sobrietymeter;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {
    private String[] titles;
    private ListView titleList;
    private android.app.Fragment mainFragment;

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setPreferences();
        createDatabase();
        startService(new Intent(getBaseContext(), AlcoService.class));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        titles = getResources().getStringArray(R.array.titles);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, new MainFragment());
        ft.commit();
        createTitleList();
        createListeners();
    }

    private void createDatabase() {
        AlcoholDatabaseHelper alcoholDatabaseHelper = new AlcoholDatabaseHelper(getBaseContext());
        SQLiteDatabase db = alcoholDatabaseHelper.getReadableDatabase();
        db.close();
    }

    private void setPreferences() {
        Preferences pref = new Preferences(getBaseContext());
        if (!pref.hasChanged()){
            pref.setPreferences(70, 2);
            pref.setWeightOfAlcoholInBody(0);
            pref.setChanged(true);
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(drawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void createTitleList(){
        drawerLayout =(DrawerLayout) findViewById(R.id.drawer_layout);
        titleList = (ListView) findViewById(R.id.drawer);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.string.open_drawer, R.string.close_drawer){
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }
        };
        titleList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_activated_1, titles));
        drawerLayout.setDrawerListener(drawerToggle);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean drawerOpen = drawerLayout.isDrawerOpen(titleList);
        return super.onPrepareOptionsMenu(menu);
    }

    private void createFragmentField(Fragment fragment) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        ft.addToBackStack("fg");
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }

    private void createListeners() {
        titleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                selectItem(position);
                setActionBarTitle(position);
            }

            private void selectItem(int position) {
                Fragment fragment = null;
                switch (position){
                    case 0:
                        fragment = new MainFragment();
                        break;
                    case 1:
                        fragment = new ListOfAlc();
                        break;
                    case 2:
                        fragment = new AddAlcohole();
                        break;
                    case 3:
                        fragment = new SettingFragment();
                        break;
                }
                drawerLayout.closeDrawer(titleList);
                createFragmentField(fragment);
            }
        });
    }

    private void setActionBarTitle(int position) {
        String title;
        if (position == 0){
            title = getResources().getString(R.string.app_name);
        } else {
            title = titles[position];
        }
        getSupportActionBar().setTitle(title);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
