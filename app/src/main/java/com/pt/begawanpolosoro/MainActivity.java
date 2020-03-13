package com.pt.begawanpolosoro;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.pt.begawanpolosoro.adapter.ApiService;
import com.pt.begawanpolosoro.adapter.SessionManager;
import com.pt.begawanpolosoro.home.HomeAdminFragment;
import com.pt.begawanpolosoro.pekerja.PekerjaActivity;
import com.pt.begawanpolosoro.proyek.ProyekFragment;
import com.pt.begawanpolosoro.setting.SettingsActivity;
import com.pt.begawanpolosoro.user.UserFragment;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    BottomNavigationBar bottomNavigationBar;
    SessionManager sm;
    HashMap map;
    private Fragment fragment;
    SwipeRefreshLayout swp;
    int role;
    int aktifFragment ;
    ImageButton logout;
    ApiService apiService;
    CurrentUser user;

    public int getPosisiHalaman() {
        return posisiHalaman;
    }

    public void setPosisiHalaman(int posisiHalaman) {
        this.posisiHalaman = posisiHalaman;
    }

    int posisiHalaman;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent extra = getIntent();




        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(),R.color.darkBlue));

        sm = new SessionManager(this);
        sm.checkLogin();
        map = sm.getLogged();
        String rule = map.get(sm.SES_ROLE).toString();
        role = Integer.parseInt(rule);
        logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                sm.logout();
                Intent i = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(i);
            }
        });

        bottomNavigationBar = findViewById(R.id.bottom_navigation_bar);

        if (extra.hasExtra("halaman")){
            setPosisiHalaman(Integer.parseInt(extra.getStringExtra("halaman")));

        }else {
            setPosisiHalaman(0);

        }
//        menuBottom(role);
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.ic_home, "HOME"))
                .addItem(new BottomNavigationItem(R.drawable.ic_proyek, "PEKERJAAN"))
                .addItem(new BottomNavigationItem(R.drawable.ic_user, "PENGGUNA"))
//                .addItem(new BottomNavigationItem(R.drawable.ic_profil, "Profil"))
                .setBarBackgroundColor(R.color.lightGrey2)
                .setActiveColor(R.color.lightBlue)
                .setInActiveColor(R.color.grey)
                .setFirstSelectedPosition(getPosisiHalaman())
                .initialise();
        halaman(getPosisiHalaman(), role);

        bottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                setPosisiHalaman(position);
                if (role == 0){
                    if (position == 1){
                        Intent intent = new Intent(getApplicationContext(), PekerjaActivity.class);
                        startActivity(intent);
                    }else {
                        halaman(getPosisiHalaman(), role);

                    }
                }else {
                    halaman(getPosisiHalaman(), role);

                }

//                setActionBar(position, rule);
                aktifFragment = position;
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });
    }



    private void halaman(int index, int rule){
        Fragment frg = null;
        FragmentManager fragmentManager = getSupportFragmentManager();
        String tag;


            switch (index){
                case 0:
                    HomeAdminFragment homeFragment = new HomeAdminFragment();
                    fragment = homeFragment;
                    tag = "HOME_FRAGMENT";
                    Log.d("jalan", "halaman: home");
                    break;

                case 1:
                    ProyekFragment proyekFragment = new ProyekFragment();
                    fragment = proyekFragment;
                    break;
                case 2:
                    UserFragment userFragment = new UserFragment();
                    fragment = userFragment;
                    break;
            }

//        else {
//            switch (index){
//                case 0:
//                    HomeAdminFragment homeFragment = new HomeAdminFragment();
//                    fragment = homeFragment;
//                    tag = "HOME_FRAGMENT";
//                    Log.d("jalan", "halaman: home");
//                    break;
//
//
//                default:
//                    fragment = null;
//                    tag = null;
//            }
//        }
        fragmentManager.beginTransaction()
                .replace(R.id.fl_container, fragment)
                .commit();
    }

}
