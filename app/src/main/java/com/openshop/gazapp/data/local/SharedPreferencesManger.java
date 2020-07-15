package com.openshop.gazapp.data.local;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.openshop.gazapp.data.model.login.Address;
import com.openshop.gazapp.data.model.login.Client;
import com.openshop.gazapp.data.model.login.ClientData;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SharedPreferencesManger {

    private static SharedPreferences sharedPreferences = null;
    public static String USER_PASSWORD = "USER_PASSWORD";
    public static String REMEMBER = "REMEMBER";

    public static void setSharedPreferences(Activity activity) {
        if (sharedPreferences == null) {
            sharedPreferences = activity.getSharedPreferences(
                    "WooW", Context.MODE_PRIVATE);
        }
    }

    public static void SaveData(Activity activity, String data_Key, String data_Value) {
        setSharedPreferences(activity);
        if (sharedPreferences != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(data_Key, data_Value);
            editor.commit();
        } else {
            setSharedPreferences(activity);
        }
    }

    public static void SaveData(Activity activity, String data_Key, boolean data_Value) {
        setSharedPreferences(activity);
        if (sharedPreferences != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(data_Key, data_Value);
            editor.commit();
        } else {
            setSharedPreferences(activity);
        }
    }

    public static String LoadData(Activity activity, String data_Key) {
        setSharedPreferences(activity);
        if (sharedPreferences != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
        } else {
            setSharedPreferences(activity);
        }

        return sharedPreferences.getString(data_Key, null);
    }

    public static boolean LoadBoolean(Activity activity, String data_Key) {
        setSharedPreferences(activity);
        if (sharedPreferences != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
        } else {
            setSharedPreferences(activity);
        }

        return sharedPreferences.getBoolean(data_Key, false);
    }

    public static void SaveData(Activity activity, String data_Key, Object data_Value) {
        setSharedPreferences(activity);
        if (sharedPreferences != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            Gson gson = new Gson();
            String StringData = gson.toJson(data_Value);
            editor.putString(data_Key, StringData);
            editor.commit();
        }
    }

    public static void saveUserData(Activity activity,String key, Client userData) {
        SaveData(activity, key, userData);
    }

    public static Client loadUserData(Activity activity , String key) {
        Client userData = null;

        Gson gson = new Gson();
        userData = gson.fromJson(LoadData(activity, key), Client.class);

        return userData;
    }

    public static void clean(Activity activity) {
        setSharedPreferences(activity);
        if (sharedPreferences != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.commit();
        }
    }
    //TODO save list
    public static void saveList(Activity activity, String datakey, List<Address> data) {
        setSharedPreferences(activity);
        if (sharedPreferences != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            HashSet<Address> set = new HashSet<Address>();

            set.addAll(data);
//            editor.putStringSet("categories", set);
            editor.commit();
        } else {
            setSharedPreferences(activity);
        }

    }

    public static Set<String> LoadList(Activity activity, String data_Key) {
        setSharedPreferences(activity);
        if (sharedPreferences != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
        } else {
            setSharedPreferences(activity);
        }

        return sharedPreferences.getStringSet(data_Key, null);
    }
//    public static <Address>   void setList(Activity activity , String key , List<Address> data)
//    {
//        setSharedPreferences(activity);
//        Gson gson = new Gson();
//        String json = gson.toJson(data);
//        set(key,json);
//        Toast.makeText(activity,"saved" , Toast.LENGTH_LONG).show();
//    }

    private static void set(String key, String value ) {
        if (sharedPreferences != null) {


            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(key , value);
            editor.commit();
        }



    }
    public static List<Address> getAddresses(Activity activity , String key)
    {
        setSharedPreferences(activity);

        if (sharedPreferences != null) {
            Gson gson = new Gson();
            List<Address> data;
            String v = sharedPreferences.getString(key,"0");
            Type type = new TypeToken<List<Address>>()
            {

            }.getType();
            data=gson.fromJson(v , type);
            return data;
        }
        return  null;
    }
    //TODO save String list
    public static void saveStringList(Activity activity, String datakey, List<String> data) {
        setSharedPreferences(activity);
        if (sharedPreferences != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            Gson gson = new Gson();
            String json = gson.toJson(data);

            editor.putString(datakey, json);
            editor.commit();
        } else {
            setSharedPreferences(activity);
        }

    }

    public static ArrayList<String> LoadStringList(Activity activity, String data_Key) {
        setSharedPreferences(activity);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            Gson gson = new Gson();
            String json = sharedPreferences.getString(data_Key,null);
            Type type = new TypeToken<ArrayList<String>>(){}.getType();


        return gson.fromJson(json,type) ;
    }


}
