package com.giot.meeting;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

/**
 * Created by 伟 on 2015/10/20.
 */
public class ShareActivity extends AppCompatActivity {

    private Toolbar toolbarShare;
    private ImageButton shareEmail, shareWechat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        initView();
    }

    void initView() {
        toolbarShare = (Toolbar) findViewById(R.id.toolbar_share);
        shareEmail = (ImageButton) findViewById(R.id.share_email);
        shareWechat = (ImageButton) findViewById(R.id.share_wechat);

        toolbarShare.setTitle("");
        setSupportActionBar(toolbarShare);
        toolbarShare.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.exit_menu:
                        finish();
                        break;
                }
                return false;
            }
        });

        shareEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "邮箱发送", Snackbar.LENGTH_SHORT).show();
            }
        });

        shareWechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "微信发送", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.exit_menu, menu);
        return true;
    }
}
