package com.pt.begawanpolosoro;

import android.content.Context;
import android.content.Intent;

import com.pt.begawanpolosoro.adapter.SessionManager;
import com.pt.begawanpolosoro.login.LoginActivity;

import java.util.HashMap;

public class CurrentUser {


    String sSaldo;



    int role;

    SessionManager sm;
    HashMap map;
    Context mContext;

    public CurrentUser(Context context){
        this.sm = new SessionManager(context);
        this.map = sm.getLogged();
        this.mContext = context;

    }
    public void logout(){
        sm.logout();
    }

    public String getsNama() {
        return this.map.get(this.sm.SES_NAMA).toString();

    }


    public String getsUsername() {
        return this.map.get(this.sm.SES_USERNAME).toString();

    }

    public int getRole() {
        String rule = this.map.get(sm.SES_ROLE).toString();
        role = Integer.parseInt(rule);
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }


    public String getsAuth() {
        return this.map.get(this.sm.SES_TOKEN).toString();
    }

    public void routing(String halaman){
        Intent intent;

        if (sm.Login())
        {

            switch (getRole()){
                case 0:
                    intent = new Intent(mContext, PekerjaControlerActivity.class);
                    break;
                case 1:
                    intent = new Intent(mContext, PekerjaControlerActivity.class);
                    break;

                case 2:
                    intent = new Intent(mContext, MainActivity.class);

                    break;
                case 3:
                    intent = new Intent(mContext, MainActivity.class);

                    break;
                default:
                    intent = new Intent(mContext, PekerjaControlerActivity.class);
                    break;

            }
            intent.putExtra("halaman", halaman);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_CLEAR_TASK |
                    Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
//            break;

        }else {
            intent = new Intent(mContext, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_CLEAR_TASK |
                    Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        }
    }

    public String getsSaldo() {
        return sSaldo;
    }

    public void setsSaldo(String sSaldo) {
        this.sSaldo = sSaldo;
    }

}
