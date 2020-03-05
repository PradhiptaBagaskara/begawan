package com.pt.begawanpolosoro;


import android.content.Intent;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.daimajia.androidanimations.library.Techniques;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.pt.begawanpolosoro.adapter.ApiService;
import com.pt.begawanpolosoro.adapter.InitRetro;
import com.pt.begawanpolosoro.adapter.SessionManager;
import com.pt.begawanpolosoro.home.api.ResponseSaldo;
import com.viksaa.sssplash.lib.activity.AwesomeSplash;
import com.viksaa.sssplash.lib.cnst.Flags;
import com.viksaa.sssplash.lib.model.ConfigSplash;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AwesomeSplash {

    SessionManager sm;

    @Override
    public void initSplash(ConfigSplash configSplash) {
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(),R.color.darkBlue));
        //Customize Circular Reveal
        configSplash.setBackgroundColor(R.color.darkBlue); //any color you want form colors.xml
        configSplash.setAnimCircularRevealDuration(1000); //int ms
        configSplash.setRevealFlagX(Flags.REVEAL_RIGHT);  //or Flags.REVEAL_LEFT
        configSplash.setRevealFlagY(Flags.REVEAL_BOTTOM); //or Flags.REVEAL_TOP

        //Choose LOGO OR PATH; if you don't provide String value for path it's logo by default

        //Customize Logo
        configSplash.setLogoSplash(R.drawable.ic_company); //or any other drawable
        configSplash.setAnimLogoSplashDuration(1000); //int ms
        configSplash.setPathSplashStrokeColor(R.color.whiteOri);
        configSplash.setPathSplashFillColor(R.color.light_gray);
        configSplash.setAnimLogoSplashTechnique(Techniques.FadeInUp); //choose one form Techniques (ref: https://github.com/daimajia/AndroidViewAnimations)


        //Customize Title
        configSplash.setTitleSplash("BEGAWAN POLOSORO");
        configSplash.setTitleTextColor(R.color.whiteOri);
        configSplash.setTitleTextSize(30f); //float value
        configSplash.setAnimTitleDuration(1000);
        configSplash.setAnimTitleTechnique(Techniques.Landing);
        configSplash.setTitleFont("fonts/mont_bold.otf");





    }

    @Override
    public void animationsFinished() {
        sm = new SessionManager(getApplicationContext());
        if(sm.Login()){
            final CurrentUser user = new CurrentUser(getApplicationContext());
            final InitRetro initRetro = new InitRetro(getApplicationContext());
            final ApiService apiService = initRetro.InitApi().create(ApiService.class);
            FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                            if (!task.isSuccessful()) {
                                Log.w("fcm", "getInstanceId failed", task.getException());
                                return;
                            }

                            // Get new Instance ID token
                            String token = task.getResult().getToken();

                            // Log and toast
                            String msg = token ;//getString(R.string.msg_token_fmt, token);
                            Log.d("fcm", msg);
                            Call<ResponseSaldo> newToken = apiService.updateToken(user.getsAuth(), token);
                            newToken.enqueue(new Callback<ResponseSaldo>() {
                                @Override
                                public void onResponse(Call<ResponseSaldo> call, Response<ResponseSaldo> response) {

                                }

                                @Override
                                public void onFailure(Call<ResponseSaldo> call, Throwable t) {

                                }
                            });
//                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        CurrentUser user = new CurrentUser(getApplicationContext());
        String hal= "0";
        Intent intent = getIntent();
        if (intent.hasExtra("halaman")){
            hal = intent.getStringExtra("halaman");
        }
        user.routing(hal);
//sm.logged();
//Log.d("ceklogin role", "role", String.valueOf(user.getRole()));

    }
}
