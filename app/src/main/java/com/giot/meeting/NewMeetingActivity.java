package com.giot.meeting;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.giot.meeting.adapters.DateAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by 伟 on 2015/10/15.
 */
public class NewMeetingActivity extends AppCompatActivity {

    private TextInputLayout textInputLayoutTitle, textInputLayoutContent, textInputLayoutLocation;
    private EditText editTextTitle, editTextContent, editTextLocation, editTextRemark;
    private Button buttonPerson;
    private Spinner spinnerHour;
    private Toolbar toolbarNewMeeting;
    private String title, content, location, remark;
    private SwitchCompat switchCompatVisible;
    private TextView textViewVisible, textViewDate;
    private String[] spinnerItem;
    private ArrayAdapter<String> spinnerAdapter;
    private TabLayout tabDate;
    private ViewPager viewPagerDate;
    private List<View> mViewList;
    private DateAdapter pagerAdapter;
    private String date, time;
    private View dateView, timeView, layout;
    private DatePicker datePicker;
    private TimePicker timePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_meeting);
        initView();
        buttonPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view,""+,Snackbar.LENGTH_SHORT).show();
                title = editTextTitle.getText().toString();
                content = editTextContent.getText().toString();
                location = editTextLocation.getText().toString();
                remark = editTextRemark.getText().toString();

                if (title == null || title.isEmpty()) {
                    textInputLayoutTitle.setError("标题不能为空");
                    textInputLayoutTitle.setErrorEnabled(true);
                    return;
                }
                textInputLayoutTitle.setErrorEnabled(false);

                if (content == null || content.isEmpty()) {
                    textInputLayoutContent.setError("内容不能为空");
                    textInputLayoutContent.setErrorEnabled(true);
                    return;
                }
                textInputLayoutContent.setErrorEnabled(false);

                if (location == null || location.isEmpty()) {
                    textInputLayoutLocation.setError("地点不能为空");
                    textInputLayoutLocation.setErrorEnabled(true);
                    return;
                }
                textInputLayoutLocation.setErrorEnabled(false);

                Intent intent = new Intent(NewMeetingActivity.this, ShareActivity.class);
                startActivity(intent);
                finish();

            }
        });

        textViewDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "选择时间", Snackbar.LENGTH_SHORT).show();
                layout = getLayoutInflater().inflate(R.layout.dialog_date, null);
                tabDate = (TabLayout) layout.findViewById(R.id.tab_date);
                viewPagerDate = (ViewPager) layout.findViewById(R.id.viewpager_date);
                initDateDialog();
                AlertDialog.Builder builder = new AlertDialog.Builder(NewMeetingActivity.this);
                //builder.setTitle("选择时间");
                builder.setView(layout);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(NewMeetingActivity.this, date + " " + time, Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.create().show();
            }
        });
    }

    void initView() {
        toolbarNewMeeting = (Toolbar) findViewById(R.id.toolbar_new_meeting);
        textInputLayoutTitle = (TextInputLayout) findViewById(R.id.textInputLayout_title);
        textInputLayoutContent = (TextInputLayout) findViewById(R.id.textInputLayout_content);
        textInputLayoutLocation = (TextInputLayout) findViewById(R.id.textInputLayout_location);
        editTextTitle = (EditText) findViewById(R.id.editText_title);
        editTextContent = (EditText) findViewById(R.id.editText_content);
        editTextLocation = (EditText) findViewById(R.id.editText_location);
        editTextRemark = (EditText) findViewById(R.id.editText_remark);
        buttonPerson = (Button) findViewById(R.id.button_person);
        spinnerHour = (Spinner) findViewById(R.id.spinner_hour);
        switchCompatVisible = (SwitchCompat) findViewById(R.id.switchCompat_visible);
        textViewVisible = (TextView) findViewById(R.id.textView_visible);
        textViewDate = (TextView) findViewById(R.id.textView_date);

        toolbarNewMeeting.setTitle("");
        setSupportActionBar(toolbarNewMeeting);
        toolbarNewMeeting.setNavigationIcon(R.mipmap.toolbar_back);
        toolbarNewMeeting.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        textInputLayoutTitle.setHint("请输入标题* ");
        textInputLayoutContent.setHint("请输入内容* ");
        textInputLayoutLocation.setHint("请输入地点* ");

        spinnerItem = getResources().getStringArray(R.array.spinnerhour);
        spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spinnerItem);
        spinnerHour.setAdapter(spinnerAdapter);

        switchCompatVisible.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    textViewVisible.setText("是");
                } else {
                    textViewVisible.setText("否");
                }
            }
        });
    }

    void initDateDialog() {
        dateView = LayoutInflater.from(this).inflate(R.layout.view_date, null);
        timeView = LayoutInflater.from(this).inflate(R.layout.view_time, null);

        datePicker = (DatePicker) dateView.findViewById(R.id.date_picker);
        timePicker = (TimePicker) timeView.findViewById(R.id.time_picker);
        timePicker.setIs24HourView(true);

        final TabLayout.Tab tab1 = tabDate.newTab();
        final TabLayout.Tab tab2 = tabDate.newTab();

        int year, month, day, hour, minute;
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        date = year + "/" + (month + 1) + "/" + day;
        time = hour + ":" + minute;

        mViewList = new ArrayList<View>();
        mViewList.add(dateView);
        mViewList.add(timeView);

        tab1.setText("日期:  " + date);
        tab2.setText("时间:  " + time);

        tabDate.addTab(tab1);
        tabDate.addTab(tab2);

        pagerAdapter = new DateAdapter(mViewList);

        viewPagerDate.setAdapter(pagerAdapter);
        viewPagerDate.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabDate));
        tabDate.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPagerDate));

        datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
                date = i + "/" + (i1 + 1) + "/" + i2;
                tab1.setText("日期:  " + date);
            }
        });

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                time = i + ":" + i1;
                tab2.setText("时间:  " + time);
            }
        });
    }
}
