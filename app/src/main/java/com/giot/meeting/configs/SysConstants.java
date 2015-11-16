package com.giot.meeting.configs;


public class SysConstants {

    public final static String RuleMail = "^[a-z0-9]+([._\\\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$";
    public final static String errorString = "∑(っ °Д °;)っ你的网好像不太给力";
    public final static int items = 5;

    //server address
    public final static String BaseUrl = "http://192.168.1.106:8080/whentomeet/";

    //login&register interface
    public final static String DoUserRegister = "addUser.do";
    public final static String DoFindUser = "findUser.do?username=[arg1]";
    public final static String DoGetUser = "getUser.do?username=[arg1]&password=[arg2]";

    //Meeting interface
    public final static String DoAddMeeting = "addMeeting.do";
    public final static String DoFindMeeting = "findMeeting.do?meetid=[arg1]";
    public final static String DoFindAllMeeting = "findAllMeetingForPhone.do?organiser=[arg1]&start=[arg2]&items=" + items;
    public final static String DoDeleteMeeting = "deleteMeeting.do?meetid=[arg1]";
    //contact interface
    public final static String DoDeleteContact = "deleteContact.do?contactid=[arg1]";
    public final static String DoFindAllContact = "findAllContact.do?userid=[arg1]";

    //person interface
    public final static String DoFindAllPerson = "getAllPersonTime.do?meetid=[arg1]";

    //time interface
    public final static String DoAddTime = "addTime.do";
    public final static String DoFindAllTime = "findAllTime.do?meetId=[arg1]";

    //sendEmail interface
    public final static String DoSendMail = "sendMailForPhone.do";
    public final static String DoSendDecideTime = "sendDecideTime.do";

}
