package com.giot.meeting;

import android.app.Application;

public class MeetApplication extends Application {

    private Boolean isLogin = false;
    private String user;
    private String email;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public Boolean getIsLogin() {
        return isLogin;
    }

    public void setIsLogin(Boolean isLogin) {
        this.isLogin = isLogin;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
