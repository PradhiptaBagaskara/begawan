package com.pt.begawanpolosoro;

import android.content.Context;

import com.pt.begawanpolosoro.adapter.SessionManager;

import java.util.HashMap;

public class CurrentUser {


    String sSaldo;

    SessionManager sm;
    HashMap map;

    public CurrentUser(Context context){
        this.sm = new SessionManager(context);
        this.map = sm.getLogged();

    }

    public String getsNama() {
        return this.map.get(this.sm.SES_NAMA).toString();

    }


    public String getsUsername() {
        return this.map.get(this.sm.SES_USERNAME).toString();

    }


    public String getsAuth() {
        return this.map.get(this.sm.SES_TOKEN).toString();
    }



    public String getsSaldo() {
        return sSaldo;
    }

    public void setsSaldo(String sSaldo) {
        this.sSaldo = sSaldo;
    }

}
