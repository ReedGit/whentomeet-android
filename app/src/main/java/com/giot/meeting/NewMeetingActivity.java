package com.giot.meeting;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

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
    private TextView textViewVisible;
    private String[] spinnerItem;
    private ArrayAdapter<String> spinnerAdapter;

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

                Intent intent = new Intent(NewMeetingActivity.this,ShareActivity.class);
                startActivity(intent);
                finish();

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
}
