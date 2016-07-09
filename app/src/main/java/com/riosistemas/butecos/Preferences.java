package com.riosistemas.butecos;

import android.app.Application;

public class Preferences extends Application {

    private static int _id;
    private static String pre_name;
    private static String pre_email;
    private static String pre_city;
    private static String pre_ano;
    @Override
    public void onCreate(){
        super.onCreate();
        _id=0;
        pre_name ="";
        pre_email= "";
        pre_city="";
        pre_ano="";

    }
    public int get_id() {
        return _id;
    }
    public static void set_id(int _id) {
        Preferences._id = _id;
    }
    public static  String getPre_name() {
        return pre_name;
    }
    public static void setPre_name(String pre_name) {
        Preferences.pre_name = pre_name;
    }
    public static String getPre_email() {
        return pre_email;
    }
    public static void setPre_email(String pre_email) {
        Preferences.pre_email = pre_email;
    }
    public static String getPre_city() {
        return pre_city;
    }
    public static void setPre_city(String pre_city) {
        Preferences.pre_city = pre_city;
    }
    public static String getPre_ano() {
        return pre_ano;
    }
    public static void setPre_ano(String pre_ano) {
        Preferences.pre_ano = pre_ano;
    }

}
