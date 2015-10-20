package com.giot.meeting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by 伟 on 2015/10/16.
 */
public class DetailActivity extends AppCompatActivity {

    private Toolbar toolbarDetail;
    private TextView detailTitle, detailContent, detailLocation, detailDate, detailPerson, detailRemark, detailVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initView();
    }

    void initView() {
        toolbarDetail = (Toolbar) findViewById(R.id.toolbar_detail);
        detailTitle = (TextView) findViewById(R.id.detail_title);
        detailContent = (TextView) findViewById(R.id.detail_content);
        detailLocation = (TextView) findViewById(R.id.detail_location);
        detailDate = (TextView) findViewById(R.id.detail_date);
        detailPerson = (TextView) findViewById(R.id.detail_person);
        detailRemark = (TextView) findViewById(R.id.detail_remark);
        detailVisible = (TextView) findViewById(R.id.detail_visible);

        toolbarDetail.setTitle("Meeting");
        setSupportActionBar(toolbarDetail);
        toolbarDetail.setNavigationIcon(R.mipmap.toolbar_back);
        toolbarDetail.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        toolbarDetail.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_share:
                        Intent intent = new Intent(DetailActivity.this,ShareActivity.class);
                        startActivity(intent);
                        break;
                }
                return false;
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share_menu,menu);
        return true;
    }
}
