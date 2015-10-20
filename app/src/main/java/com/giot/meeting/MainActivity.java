package com.giot.meeting;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.Toast;

import com.giot.meeting.fragments.AboutFragment;
import com.giot.meeting.fragments.ContactFragment;
import com.giot.meeting.fragments.MeetingFragment;
import com.giot.meeting.fragments.PasswordFragment;


/**
 * Created by 伟 on 2015/9/29.
 */
public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;
    private Fragment meetingFragment, contactFragment, passwordFragment, aboutFragment;
    FragmentManager fragmentManager;
    FragmentTransaction transaction;
    private long exitTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        navigationView.setNavigationItemSelectedListener(navigationViewListener);

    }

    public void initView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerToggle.syncState();
        drawerLayout.setDrawerListener(drawerToggle);
        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        meetingFragment = new MeetingFragment();
        transaction.replace(R.id.main_frame, meetingFragment);
        transaction.commit();

    }

    private NavigationView.OnNavigationItemSelectedListener navigationViewListener = new NavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(MenuItem menuItem) {
            fragmentManager = getSupportFragmentManager();
            transaction = fragmentManager.beginTransaction();
            drawerLayout.closeDrawers();
            switch (menuItem.getItemId()) {
                case R.id.drawer_meeting:
                    meetingFragment = new MeetingFragment();
                    transaction.replace(R.id.main_frame, meetingFragment);
                    transaction.commit();
                    break;
                case R.id.drawer_contact:
                    contactFragment = new ContactFragment();
                    transaction.replace(R.id.main_frame, contactFragment);
                    transaction.commit();
                    break;
                case R.id.drawer_password:
                    passwordFragment = new PasswordFragment();
                    transaction.replace(R.id.main_frame, passwordFragment);
                    transaction.commit();
                    break;
                case R.id.drawer_about:
                    aboutFragment = new AboutFragment();
                    transaction.replace(R.id.main_frame, aboutFragment);
                    transaction.commit();
                    break;
                case R.id.drawer_exit:
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("退出登录");
                    builder.setMessage("确认退出登录？");
                    builder.setPositiveButton("退出", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(intent);
                            dialogInterface.dismiss();
                            finish();
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    builder.create().show();
                    menuItem.setChecked(false);
                    break;
            }
            return false;
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //主界面左上角的icon点击反应
                drawerLayout.openDrawer(GravityCompat.START);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000)  //System.currentTimeMillis()无论何时调用，肯定大于2000
            {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
