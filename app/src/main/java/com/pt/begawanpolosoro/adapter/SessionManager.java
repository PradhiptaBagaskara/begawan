package com.pt.begawanpolosoro.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;


import com.pt.begawanpolosoro.MainActivity;
import com.pt.begawanpolosoro.login.LoginActivity;

import java.util.HashMap;


public class SessionManager {

    public static final String SES_ROLE = "sesRole";
    public static final String SES_USERNAME = "sesUsername";
    public static final String SES_NAMA = "sesNama";


    public static final String SES_TOKEN = "sesToken";
    private static final String SES_LOGED = "sesLoged";
    private final String LOGIN_STTS = "sesStatus";
    private final String IS_ADMIN = "sesStatus";
    private  final int MODE_PRIVATE = 0;

    private Context _context;

    SharedPreferences sp;
    SharedPreferences.Editor spEditor;

    public SessionManager(Context context)
    {
        this._context = context;
        sp = context.getSharedPreferences(SES_LOGED, MODE_PRIVATE);
        spEditor = sp.edit();
    }

    public void storeLogin(String role, String nama, String username, String token)
    {
        spEditor.putBoolean(LOGIN_STTS, true);
        spEditor.putString(SES_TOKEN, token);
        spEditor.putString(SES_NAMA, nama);
        spEditor.putString(SES_USERNAME, username);
        spEditor.putString(SES_ROLE, String.valueOf(role));
        spEditor.commit();

    }

    public HashMap getLogged()
    {
        HashMap<String, String> map = new HashMap<>();
        map.put(SES_TOKEN, sp.getString(SES_TOKEN, null));
        map.put(SES_NAMA, sp.getString(SES_NAMA, null));
        map.put(SES_USERNAME, sp.getString(SES_USERNAME, null));
        map.put(SES_ROLE, sp.getString(SES_ROLE, null));


        return map;
    }

    public void checkLogin()
    {
        if (!this.Login())
        {
            Intent intent = new Intent(_context, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_CLEAR_TASK |
                    Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(intent);

        }
    }




    public void logged()
    {
        if (this.Login())
        {
            Intent intent = new Intent(_context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_CLEAR_TASK |
                    Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(intent);

        }
    }

    public void logout()
    {
        spEditor.clear();
//        spEditor.commit();
        if (spEditor.commit()){
            Intent intent = new Intent(_context, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_CLEAR_TASK |
                    Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(intent);
        }
    }

    public  Boolean Login()
    {
        return sp.getBoolean(LOGIN_STTS, false);
    }






}
