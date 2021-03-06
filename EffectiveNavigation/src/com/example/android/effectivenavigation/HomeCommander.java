/*
 * Copyright 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.effectivenavigation;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;
import android.widget.Toast;

public class HomeCommander extends FragmentActivity implements
        ActionBar.TabListener {

    public static final String PREFS_NAME = "MySettings";
    private static final int RESULT_SETTINGS = 1;
    AppSectionsPagerAdapter mAppSectionsPagerAdapter;
    ViewPager mViewPager;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    //	private RabbitConsumeHelper ra;
    private String[] mMenuItems;

/*	@Override
    public void onPause() {
		super.onPause();
		try

		{
			ra.cancel(true);
		} catch (Exception ex) {

		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (ra.isCancelled()) {
			ra = new RabbitConsumeHelper();
			ra.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "", this);
		}
	}*/

	/*@Override
	protected void onStop() {
		super.onStop();
	};

	@Override
	protected void onStart() {
		super.onStart();
		if (ra.isCancelled()) {
			ra = new RabbitConsumeHelper();
			ra.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "", this);
		}
	};*/



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mMenuItems = getResources().getStringArray(R.array.environment_array);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, R.string.drawer_open,
                R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(
                getSupportFragmentManager());

        final ActionBar actionBar = getActionBar();

        actionBar.setHomeButtonEnabled(false);

        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mAppSectionsPagerAdapter);
        mViewPager
                .setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        actionBar.setSelectedNavigationItem(position);
                    }
                });

        for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {

            actionBar.addTab(actionBar.newTab()
                    .setText(mAppSectionsPagerAdapter.getPageTitle(i))
                    .setTabListener(this));
        }

        //	ra = new RabbitConsumeHelper();

        //ra.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "", this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return false;

    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab,
                                FragmentTransaction fragmentTransaction) {

        InputMethodManager mgr = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);

        mgr.hideSoftInputFromWindow(mDrawerLayout.getWindowToken(), 0);

    }

    @Override
    public void onTabSelected(ActionBar.Tab tab,
                              FragmentTransaction fragmentTransaction) {

        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab,
                                FragmentTransaction fragmentTransaction) {
    }

    public class AppSectionsPagerAdapter extends FragmentPagerAdapter {

        public AppSectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    return new Dashboard();
                case 1:

                    Fragment fragment = new PcFragment();
                    Bundle args = new Bundle();
                    args.putInt(PcFragment.ARG_SECTION_NUMBER, i + 1);
                    fragment.setArguments(args);
                    return fragment;
                case 2:
                    Fragment fragment4 = new uTorrent();
                    Bundle args4 = new Bundle();
                    args4.putInt(ArduinoFragment.ARG_SECTION_NUMBER, i + 1);
                    fragment4.setArguments(args4);
                    return fragment4;


                case 3:

                    Fragment fragment3 = new SettingsFragment();
                    Bundle args3 = new Bundle();
                    args3.putInt(ArduinoFragment.ARG_SECTION_NUMBER, i + 1);
                    fragment3.setArguments(args3);
                    return fragment3;

                case 4:
                    Fragment fragment2 = new ArduinoFragment();
                    Bundle args2 = new Bundle();
                    args2.putInt(ArduinoFragment.ARG_SECTION_NUMBER, i + 1);
                    fragment2.setArguments(args2);
                    return fragment2;


            }
            return null;
        }

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Dashboard";
                case 1:
                    return "PC";
                case 2:
                    return "uTorrent";
                case 3:

                    return "Settings";
                case 4:
                    return "Arduino";

            }
            return null;
        }
    }

}
