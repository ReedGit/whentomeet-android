package com.giot.meeting.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.giot.meeting.LoginActivity;
import com.giot.meeting.R;

/**
 * Created by 伟 on 2015/10/6.
 */
public class PasswordFragment extends Fragment {

    private TextInputLayout textInputLayoutOldPassword, textInputLayoutNewPassword, textInputLayoutNewRe;
    private EditText editTextOldPassword, editTextNewPassword, editTextNewRe;
    private Button buttonUpdatePassword;
    private String oldPassword, newPassword, newRe;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.drawer_password, null);
        textInputLayoutOldPassword = (TextInputLayout) view.findViewById(R.id.textInputLayout_old_password);
        textInputLayoutNewPassword = (TextInputLayout) view.findViewById(R.id.textInputLayout_new_password);
        textInputLayoutNewRe = (TextInputLayout) view.findViewById(R.id.textInputLayout_new_re);
        editTextOldPassword = (EditText) view.findViewById(R.id.editText_old_password);
        editTextNewPassword = (EditText) view.findViewById(R.id.editText_new_password);
        editTextNewRe = (EditText) view.findViewById(R.id.editText_new_re);
        buttonUpdatePassword = (Button) view.findViewById(R.id.button_update_password);

        textInputLayoutOldPassword.setHint("请输入旧密码 * ");
        textInputLayoutNewPassword.setHint("请输入新密码 * ");
        textInputLayoutNewRe.setHint("请再次输入新密码 * ");

        buttonUpdatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                oldPassword = editTextOldPassword.getText().toString();
                newPassword = editTextNewPassword.getText().toString();
                newRe = editTextNewRe.getText().toString();

                if (oldPassword == null || oldPassword.isEmpty()) {
                    textInputLayoutOldPassword.setError("旧密码不能为空！");
                    textInputLayoutOldPassword.setErrorEnabled(true);
                    return;
                }
                textInputLayoutOldPassword.setErrorEnabled(false);

                if (newPassword == null || newPassword.isEmpty()) {
                    textInputLayoutNewPassword.setError("新密码不能为空！");
                    textInputLayoutNewPassword.setErrorEnabled(true);
                    return;
                } else if (newPassword.length() < 6) {
                    textInputLayoutNewPassword.setError("密码长度不能少于6位");
                    textInputLayoutNewPassword.setErrorEnabled(true);
                    return;
                }
                textInputLayoutNewPassword.setErrorEnabled(false);

                if (newRe == null || newRe.isEmpty()) {
                    textInputLayoutNewRe.setError("再次输入密码不能为空！");
                    textInputLayoutNewRe.setErrorEnabled(true);
                    return;
                } else if (!newRe.equals(newPassword)) {
                    textInputLayoutNewRe.setError("两次输入密码不一致");
                    textInputLayoutNewRe.setErrorEnabled(true);
                    return;
                }
                textInputLayoutNewRe.setErrorEnabled(false);
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                Toast.makeText(getActivity(), "密码修改成功！请重新登录！", Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
        });
        return view;
    }
}
