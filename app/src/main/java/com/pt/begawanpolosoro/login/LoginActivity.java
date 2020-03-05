package com.pt.begawanpolosoro.login;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.pt.begawanpolosoro.CurrentUser;
import com.pt.begawanpolosoro.R;
import com.pt.begawanpolosoro.adapter.ApiService;
import com.pt.begawanpolosoro.adapter.InitRetro;
import com.pt.begawanpolosoro.adapter.SessionManager;
import com.pt.begawanpolosoro.home.api.ResponseSaldo;
import com.pt.begawanpolosoro.login.api.ResponseLogin;
import com.pt.begawanpolosoro.login.api.ResultLogin;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText uname,pass;
    private SessionManager sm;
    ApiService apiServices;
    ProgressBar pb;
    Button btn;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sm = new SessionManager(LoginActivity.this);
        sm.logged();
        InitRetro initRetro = new InitRetro(getApplicationContext());
        apiServices = initRetro.InitApi().create(ApiService.class);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(),R.color.my_statusbar_color));

        pb = (ProgressBar) findViewById(R.id.progressBar_login);
        pb.setVisibility(View.GONE);



        uname = findViewById(R.id.login_username);
        pass = findViewById(R.id.login_password);

        btn = findViewById(R.id.btn_login);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = uname.getText().toString();
                String password = pass.getText().toString();
                if (TextUtils.isEmpty(username)){
                    uname.setError("Username Tidak Boleh Kosong!");
                }else if(TextUtils.isEmpty(password)) {
                    pass.setError("Password Tidak Boleh Kosong!");
                }else {
                    btn.setVisibility(View.GONE);
                    pb.setVisibility(View.VISIBLE);


                Call<ResponseLogin> login = apiServices.authLogin(username, password);
                login.enqueue(new Callback<ResponseLogin>() {
                    @Override
                    public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {
                        if (response.isSuccessful()){
                            pb.setVisibility(View.GONE);
                            btn.setVisibility(View.VISIBLE);
                            if (response.body().isStatus()){
                                Log.d("tagger", response.body().getResult().getId().toString());
                                final ResultLogin rs = response.body().getResult();
                                sm.storeLogin(rs.getRole().toString(), rs.getNama().toString(), rs.getUsername().toString(), rs.getId().toString());
                                FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                        if (!task.isSuccessful()) {
                                            Log.w("fcm", "getInstanceId failed", task.getException());
                                            return;
                                        }

                                        // Get new Instance ID token
                                        String token = task.getResult().getToken();
                                        Call<ResponseSaldo> newToken = apiServices.updateToken(rs.getId().toString(), token);
                                        newToken.enqueue(new Callback<ResponseSaldo>() {
                                            @Override
                                            public void onResponse(Call<ResponseSaldo> call, Response<ResponseSaldo> response) {

                                            }

                                            @Override
                                            public void onFailure(Call<ResponseSaldo> call, Throwable t) {

                                            }
                                        });
                                    }
                                });
                                CurrentUser user = new CurrentUser(getApplicationContext());
                                user.routing("0");



                            }else {
                                Toast.makeText(getApplicationContext(), response.body().getMsg().toString(), Toast.LENGTH_LONG).show();

                            }
                        }
                        String res = response.body().toString();
                        Log.d("tager", "data: " + res);
                    }

                    @Override
                    public void onFailure(Call<ResponseLogin> call, Throwable t) {
                        btn.setVisibility(View.VISIBLE);
                        pb.setVisibility(View.GONE);
                        t.printStackTrace();
                    }
                });


                }


            }
        });

    }


}
