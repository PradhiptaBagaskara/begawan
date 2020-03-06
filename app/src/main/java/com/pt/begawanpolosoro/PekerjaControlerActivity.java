package com.pt.begawanpolosoro;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.pt.begawanpolosoro.adapter.ApiService;
import com.pt.begawanpolosoro.adapter.SessionManager;
import com.pt.begawanpolosoro.pekerja.PekerjaActivity;
import com.pt.begawanpolosoro.pekerja.PekerjaHomeFragment;
import com.pt.begawanpolosoro.pekerja.TransaksiUserFragment;

import java.util.HashMap;

public class PekerjaControlerActivity extends AppCompatActivity {
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
    private FloatingActionMenu mFab;
    FloatingActionButton addTx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pekerja_controler);
        Intent extra = getIntent();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelId  = getString(R.string.app_id);
            String channelName = getString(R.string.app_name);
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW));
        }


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
        addTx = (FloatingActionButton) findViewById(R.id.fabItemAdd);
        addTx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PekerjaActivity.class);
                startActivity(intent);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sm.logout();
            }
        });

        bottomNavigationBar = findViewById(R.id.bottom_navigation_bar);
        bottomNavigationBar.setAutoHideEnabled(true);
        if (extra.hasExtra("halaman")){
            setPosisiHalaman(Integer.parseInt(extra.getStringExtra("halaman")));

        }else {
            setPosisiHalaman(0);

        }
        bottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                setPosisiHalaman(position);


                    halaman(getPosisiHalaman(), role);



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
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.ic_home, "DASHBOARD"))
                .addItem(new BottomNavigationItem(R.drawable.ic_add, ""))
                .addItem(new BottomNavigationItem(R.drawable.ic_proyek, "TRANSAKSI"))
//                .addItem(new BottomNavigationItem(R.drawable.ic_profil, "Profil"))
                .setBarBackgroundColor(R.color.lightGrey2)
                .setActiveColor(R.color.lightBlue)
                .setInActiveColor(R.color.grey)
                .setFirstSelectedPosition(getPosisiHalaman())
                .initialise();
        halaman(getPosisiHalaman(), role);

//        mFab.


    }


    private void halaman(int index, int rule){
        Fragment frg = null;
        FragmentManager fragmentManager = getSupportFragmentManager();
        String tag;

            switch (index){
                case 0:
                    PekerjaHomeFragment homeFragment = new PekerjaHomeFragment();
                    fragment = homeFragment;
                    tag = "HOME_FRAGMENT";
//                    Log.d("jalan", "halaman: home");
                    break;

                case 2:
                    TransaksiUserFragment userFragment = new TransaksiUserFragment();
                    fragment = userFragment;
                    break;
            }


        fragmentManager.beginTransaction()
                .replace(R.id.fl_container, fragment)
                .commit();
    }

}
