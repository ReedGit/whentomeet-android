package com.giot.meeting;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.giot.meeting.fragments.AboutFragment;
import com.giot.meeting.fragments.ContactFragment;
import com.giot.meeting.fragments.MeetingFragment;
import com.giot.meeting.fragments.PasswordFragment;

import java.awt.font.TextAttribute;


public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Fragment meetingFragment;
    FragmentManager fragmentManager;
    FragmentTransaction transaction;
    private long exitTime;
    private MeetApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        app = (MeetApplication) getApplication();
        initView();
        navigationView.setNavigationItemSelectedListener(navigationViewListener);

    }

    public void initView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.BLACK);
        setSupportActionBar(toolbar);
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        View drawerHeader = navigationView.inflateHeaderView(R.layout.drawer_header);
        TextView tvUsername = (TextView) drawerHeader.findViewById(R.id.head_username);
        Intent intent = getIntent();
        tvUsername.setText(intent.getStringExtra("nickName"));
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
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
            menuItem.setChecked(true);
            switch (menuItem.getItemId()) {
                case R.id.drawer_meeting:
                    toolbar.setTitle("Meeting");
                    meetingFragment = new MeetingFragment();
                    transaction.replace(R.id.main_frame, meetingFragment);
                    transaction.commit();
                    break;
                case R.id.drawer_contact:
                    toolbar.setTitle("联系人");
                    ContactFragment contactFragment = new ContactFragment();
                    transaction.replace(R.id.main_frame, contactFragment);
                    transaction.commit();
                    break;
                case R.id.drawer_password:
                    Toast.makeText(getApplicationContext(),"暂未开放此功能！",Toast.LENGTH_SHORT).show();
                    /*toolbar.setTitle("修改密码");
                    PasswordFragment passwordFragment = new PasswordFragment();
                    transaction.replace(R.id.main_frame, passwordFragment);
                    transaction.commit();*/
                    break;
                case R.id.drawer_about:
                    toolbar.setTitle("关于我们");
                    AboutFragment aboutFragment = new AboutFragment();
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
                            app.setIsLogin(false);
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(intent);
                            dialogInterface.dismiss();
                            finish();
                        }
                    });
                    builder.setNegativeButton("取消", null);
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
