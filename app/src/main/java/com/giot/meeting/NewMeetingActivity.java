package com.giot.meeting;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.giot.meeting.adapters.DateAdapter;
import com.giot.meeting.configs.SysConstants;
import com.giot.meeting.utils.VolleyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class NewMeetingActivity extends AppCompatActivity {

    private final static String TAG = NewMeetingActivity.class.toString();

    private Toolbar toolbarNewMeeting;
    private TextInputLayout textInputLayoutTitle, textInputLayoutContent, textInputLayoutLocation;
    private EditText editTextTitle, editTextContent, editTextLocation, editTextRemark;
    private Spinner spinnerDuration;
    private String title, content, location, remark;
    private int duration;
    private SwitchCompat switchCompatVisible;
    private TextView textViewVisible;
    private TabLayout tabDate;
    private ViewPager viewPagerDate;
    private int month, day, week, hour, minute;
    private String date, time;
    private MeetApplication app;
    private JSONArray jsonArray = new JSONArray();
    private RelativeLayout layoutDate;
    private LinearLayout addLayout;
    private int count = 0;
    private boolean flag = true;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_meeting);
        app = (MeetApplication) getApplication();
        initView();
        initListener();
    }

    /**
     * 初始化布局
     */
    protected void initView() {
        toolbarNewMeeting = (Toolbar) findViewById(R.id.toolbar_new_meeting);
        textInputLayoutTitle = (TextInputLayout) findViewById(R.id.textInputLayout_title);
        textInputLayoutContent = (TextInputLayout) findViewById(R.id.textInputLayout_content);
        textInputLayoutLocation = (TextInputLayout) findViewById(R.id.textInputLayout_location);
        editTextTitle = (EditText) findViewById(R.id.editText_title);
        editTextContent = (EditText) findViewById(R.id.editText_content);
        editTextLocation = (EditText) findViewById(R.id.editText_location);
        editTextRemark = (EditText) findViewById(R.id.editText_remark);
        spinnerDuration = (Spinner) findViewById(R.id.spinner_duration);
        switchCompatVisible = (SwitchCompat) findViewById(R.id.switchCompat_visible);
        textViewVisible = (TextView) findViewById(R.id.textView_visible);
        layoutDate = (RelativeLayout) findViewById(R.id.layout_date);
        addLayout = (LinearLayout) findViewById(R.id.add_layout);

        toolbarNewMeeting.setTitle("");
        setSupportActionBar(toolbarNewMeeting);
        toolbarNewMeeting.setNavigationIcon(R.mipmap.toolbar_back);

        textInputLayoutTitle.setHint("主题* ");
        textInputLayoutContent.setHint("内容* ");
        textInputLayoutLocation.setHint("地点* ");

        String[] spinnerItem = getResources().getStringArray(R.array.spinner_hour);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spinnerItem);
        spinnerDuration.setAdapter(spinnerAdapter);

    }

    /**
     * 初始化监听事件
     */
    protected void initListener() {
        toolbarNewMeeting.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        toolbarNewMeeting.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.email_share:
                        if (check()) {
                            share();
                            addMeeting(1);
                        }
                        break;
                    case R.id.weChat_share:
                        Toast.makeText(getApplicationContext(), "亲，此功能暂未开发哦，敬请期待！", Toast.LENGTH_SHORT).show();
                        /*if (check()) {
                            share();
                            addMeeting();
                        }*/
                        break;
                }
                return false;
            }
        });
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
        layoutDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View layout = getLayoutInflater().inflate(R.layout.dialog_date, null);
                tabDate = (TabLayout) layout.findViewById(R.id.tab_date);
                viewPagerDate = (ViewPager) layout.findViewById(R.id.viewpager_date);
                initDateDialog();
                AlertDialog.Builder builder = new AlertDialog.Builder(NewMeetingActivity.this);
                builder.setView(layout);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        showDate();
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.create().show();
            }
        });
        spinnerDuration.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                switch (position) {
                    case 0:
                        duration = 1;
                        break;
                    case 1:
                        duration = 2;
                        break;
                    case 2:
                        duration = 4;
                        break;
                    case 3:
                        duration = 6;
                        break;
                    case 4:
                        duration = 8;
                        break;
                    case 5:
                        duration = 10;
                        break;
                    case 6:
                        duration = 12;
                        break;
                    case 7:
                        duration = 16;
                        break;
                    case 8:
                        duration = 20;
                        break;
                    case 9:
                        duration = 24;
                        break;
                    case 10:
                        duration = 96;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    /**
     * 初始化时间选择器
     */
    protected void initDateDialog() {
        View dateView = LayoutInflater.from(this).inflate(R.layout.view_date, null);
        View timeView = LayoutInflater.from(this).inflate(R.layout.view_time, null);

        DatePicker datePicker = (DatePicker) dateView.findViewById(R.id.date_picker);
        TimePicker timePicker = (TimePicker) timeView.findViewById(R.id.time_picker);
        timePicker.setIs24HourView(true);

        final TabLayout.Tab tab1 = tabDate.newTab();
        final TabLayout.Tab tab2 = tabDate.newTab();
        int currentYear;
        Calendar calendar = Calendar.getInstance();
        currentYear = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE) / 15 * 15;
        week = calendar.get(Calendar.DAY_OF_WEEK);
        calendar.set(currentYear, month, day, hour, minute);
        date = formatString(month + 1) + "月" + formatString(day) + "日" + week(week);
        time = formatString(hour) + ":" + formatString(minute);


        datePicker.setMinDate(System.currentTimeMillis() - 1000);
        setNumberPickerTextSize(timePicker);
        timePicker.setCurrentHour(hour);
        timePicker.setCurrentMinute(minute / 15);

        List<View> mViewList = new ArrayList<>();
        mViewList.add(dateView);
        mViewList.add(timeView);

        tab1.setText("日期:" + date);
        tab2.setText("时间:" + time);

        tabDate.addTab(tab1);
        tabDate.addTab(tab2);

        DateAdapter pagerAdapter = new DateAdapter(mViewList);

        viewPagerDate.setAdapter(pagerAdapter);
        viewPagerDate.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabDate));
        tabDate.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPagerDate));

        datePicker.init(currentYear, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
                month = i1;
                day = i2;
                Calendar ca = Calendar.getInstance();
                ca.set(i, i1, i2);
                week = ca.get(Calendar.DAY_OF_WEEK);
                date = formatString(month + 1) + "月" + formatString(day) + "日" + week(week);
                tab1.setText("日期:" + date);
            }
        });
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                hour = i;
                minute = i1 * 15;
                time = formatString(hour) + ":" + formatString(minute);
                tab2.setText("时间:" + time);
            }
        });
    }

    private String formatString(int s) {
        String str;
        if (s < 10) {
            str = "0" + s;
        } else {
            str = Integer.toString(s);
        }
        return str;
    }

    /**
     * 显示选择的时间
     */
    protected boolean showDate() {
        if ((time(hour, minute) + duration > 96)) {
            Toast.makeText(getApplicationContext(), "你选择的时间与时长已经超过当天时间，请重新选择！", Toast.LENGTH_SHORT).show();
            return false;
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("week", week(week));
            jsonObject.put("day", (month + 1) + "月" + day + "日");
            jsonObject.put("startTime", time(hour, minute));
            if (existTime(jsonArray, jsonObject)) {
                Toast.makeText(getApplicationContext(), "该时间您已经选择了，请选择其他时间", Toast.LENGTH_SHORT).show();
                return false;
            }
            jsonArray.put(jsonObject);
            Log.i(TAG, "jsonArray:" + jsonArray);
            LinearLayout linearLayout = new LinearLayout(getApplicationContext());
            TextView tv = new TextView(getApplicationContext());
            linearLayout.setId(count++);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 20, 0, 0);
            tv.setLayoutParams(layoutParams);
            tv.setTextSize(17);
            String text = date + " " + time;
            tv.setText(text);
            tv.setTextColor(Color.BLACK);
            linearLayout.addView(tv);
            addLayout.addView(linearLayout);
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (flag) {
                        try {
                            jsonArray.put(view.getId(), null);
                            addLayout.removeView(view);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.i(TAG, "json:" + jsonArray);
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 转化星期
     *
     * @param week 获取的整型星期
     * @return 字符串型星期
     */
    protected String week(int week) {
        String weekday;
        switch (week) {
            case 1:
                weekday = "日";
                break;
            case 2:
                weekday = "一";
                break;
            case 3:
                weekday = "二";
                break;
            case 4:
                weekday = "三";
                break;
            case 5:
                weekday = "四";
                break;
            case 6:
                weekday = "五";
                break;
            default:
                weekday = "六";
                break;
        }
        return "星期" + weekday;
    }

    /**
     * 调用addMeeting.do
     */
    protected void addMeeting(final int selection) {

        mProgressDialog = ProgressDialog.show(this, null, "请稍后...");

        String url = SysConstants.BaseUrl + SysConstants.DoAddMeeting;

        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (!s.isEmpty()) {
                    try {
                        JSONObject meet = new JSONObject(s);
                        addTime(meet.getString("meetid"), selection);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "meeting创建失败，请重试", Toast.LENGTH_SHORT).show();
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e(TAG, volleyError.getMessage(), volleyError);
                Toast.makeText(getApplicationContext(), "网络出现错误！", Toast.LENGTH_SHORT).show();
            }
        };

        StringRequest addRequest = new StringRequest(Request.Method.POST, url, listener, errorListener) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String visible;
                if (switchCompatVisible.isChecked()) {
                    visible = "1";
                } else {
                    visible = "0";
                }
                Map<String, String> map = new HashMap<>();
                map.put("organiser", app.getUser());
                map.put("organiser_mail", app.getEmail());
                map.put("title", title);
                map.put("content", content);
                map.put("duration", Integer.toString(duration));
                map.put("visible", visible);
                map.put("location", location);
                map.put("remark", remark);
                return map;
            }
        };
        VolleyUtil.getRequestQueue(this).cancelAll(this);
        addRequest.setTag(this);
        VolleyUtil.getRequestQueue(this).add(addRequest);
    }

    /**
     * 调用addTime.do
     */
    protected void addTime(final String meetId, final int selection) {

        String url = SysConstants.BaseUrl + SysConstants.DoAddTime;
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                mProgressDialog.dismiss();
                if (selection == 1) {
                    Intent intent = new Intent(NewMeetingActivity.this, SelectActivity.class);
                    intent.putExtra("meetId", meetId);
                    intent.putExtra("meetTheme",title);
                    startActivity(intent);
                    finish();
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e(TAG, volleyError.getMessage(), volleyError);
                Toast.makeText(getApplicationContext(), "网络出现错误！", Toast.LENGTH_SHORT).show();
            }
        };
        StringRequest timeRequest = new StringRequest(Request.Method.POST, url, listener, errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        JSONObject jo = jsonArray.getJSONObject(i);
                        jo.put("endTime", Integer.parseInt(jo.getString("startTime")) + duration);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                map.put("meetId", meetId);
                map.put("times", jsonArray.toString());
                return map;
            }
        };
        VolleyUtil.getRequestQueue(this).cancelAll(this);
        timeRequest.setTag(this);
        VolleyUtil.getRequestQueue(this).add(timeRequest);
    }

    /**
     * 检查必填项
     *
     * @return 填写是否正确
     */
    protected boolean check() {
        title = editTextTitle.getText().toString();
        content = editTextContent.getText().toString();
        location = editTextLocation.getText().toString();
        remark = editTextRemark.getText().toString();

        if (title == null || title.isEmpty()) {
            textInputLayoutTitle.setError("标题不能为空");
            textInputLayoutTitle.setErrorEnabled(true);
            return false;
        }
        textInputLayoutTitle.setErrorEnabled(false);

        if (content == null || content.isEmpty()) {
            textInputLayoutContent.setError("内容不能为空");
            textInputLayoutContent.setErrorEnabled(true);
            return false;
        }
        textInputLayoutContent.setErrorEnabled(false);

        if (location == null || location.isEmpty()) {
            textInputLayoutLocation.setError("地点不能为空");
            textInputLayoutLocation.setErrorEnabled(true);
            return false;
        }
        textInputLayoutLocation.setErrorEnabled(false);
        jsonArray = remove(jsonArray);
        if (jsonArray.length() == 0) {
            Toast.makeText(getApplicationContext(), "您还没有选择时间哦", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * 点击分享后不可编辑
     */
    protected void share() {
        editTextTitle.setEnabled(false);
        editTextContent.setEnabled(false);
        editTextLocation.setEnabled(false);
        editTextRemark.setEnabled(false);
        spinnerDuration.setEnabled(false);
        layoutDate.setClickable(false);
        switchCompatVisible.setClickable(false);
        flag = false;
    }

    /**
     * 转化时间传递给后台的数据
     *
     * @param h 小时
     * @param m 分钟
     * @return 格子数
     */
    protected int time(int h, int m) {
        return h * 4 + m / 15;
    }

    /**
     * 获取TimePicker分钟选择器
     *
     * @param viewGroup TimePicker组件
     * @return TimePicker中的NumberPicker
     */
    private List<NumberPicker> findNumberPicker(ViewGroup viewGroup) {
        List<NumberPicker> npList = new ArrayList<>();
        View child;

        if (null != viewGroup) {
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                child = viewGroup.getChildAt(i);
                if (child instanceof NumberPicker) {
                    npList.add((NumberPicker) child);
                } else if (child instanceof LinearLayout) {
                    List<NumberPicker> result = findNumberPicker((ViewGroup) child);
                    if (result.size() > 0) {
                        return result;
                    }
                }
            }
        }

        return npList;
    }

    /**
     * 调整TimePicker时间间隔
     *
     * @param viewGroup TimePicker组件
     */
    protected void setNumberPickerTextSize(ViewGroup viewGroup) {
        String[] minutes = new String[]{"00分", "15分", "30分", "45分"};
        List<NumberPicker> numberPicker = findNumberPicker(viewGroup);
        if (null != numberPicker) {
            for (NumberPicker mMinuteSpinner : numberPicker) {
                if (mMinuteSpinner.toString().contains("id/minute")) {
                    mMinuteSpinner.setMinValue(0);
                    mMinuteSpinner.setMaxValue(minutes.length - 1);
                    mMinuteSpinner.setDisplayedValues(minutes);
                    mMinuteSpinner.setWrapSelectorWheel(true);
                }
            }
        }
    }

    /**
     * 移除JSONArray中的null
     *
     * @param jsonArray 原本JSONArray
     * @return 处理后的JSONArray
     */
    public JSONArray remove(JSONArray jsonArray) {
        JSONArray result = new JSONArray();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                result.put(jsonArray.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 判断jsonArray是否已存在jsonObject
     *
     * @param jsonArray  已存在jsonArray
     * @param jsonObject 待判断的jsonObject
     * @return 存在返回true, 不存在返回false
     */
    public boolean existTime(JSONArray jsonArray, JSONObject jsonObject) {
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                if (jsonArray.getJSONObject(i).toString().equals(jsonObject.toString())) {
                    return true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu, menu);
        return true;
    }
}
