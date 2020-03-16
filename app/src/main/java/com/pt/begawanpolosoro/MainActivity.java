package com.pt.begawanpolosoro;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.pt.begawanpolosoro.adapter.ApiService;
import com.pt.begawanpolosoro.adapter.DownloadUtil;
import com.pt.begawanpolosoro.adapter.SessionManager;
import com.pt.begawanpolosoro.home.HomeAdminFragment;
import com.pt.begawanpolosoro.pekerja.PekerjaActivity;
import com.pt.begawanpolosoro.proyek.ProyekFragment;
import com.pt.begawanpolosoro.setting.SettingsActivity;
import com.pt.begawanpolosoro.user.UserFragment;

import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG ="MainActivity";
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
    CoordinatorLayout main;

    public int getPosisiHalaman() {
        return posisiHalaman;
    }

    public void setPosisiHalaman(int posisiHalaman) {
        this.posisiHalaman = posisiHalaman;
    }

    int posisiHalaman;
    AlertDialog.Builder alertDialog ;
    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int STORAGE_PERMISSION_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent extra = getIntent();




        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(),R.color.darkBlue));
        main = findViewById(R.id.main);
        sm = new SessionManager(this);
        sm.checkLogin();
        map = sm.getLogged();
        String rule = map.get(sm.SES_ROLE).toString();
        role = Integer.parseInt(rule);
        FirebaseMessaging.getInstance().subscribeToTopic("transaksi").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "onSuccess: suscribed");
            }
        });
        logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();

                    }
                })
                .check();
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
        checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);

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

    public void checkPermission(String permission, int requestCode)
    {
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission)
                == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[] { permission },
                    requestCode);
        }
        else {
            DownloadUtil downloadUtil = new DownloadUtil(getApplicationContext());

            downloadUtil.cekDir();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE){
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE);
                checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE);

            }
        }
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

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        alertDialog = new AlertDialog.Builder(MainActivity.this);

        alertDialog.setTitle("Keluar Aplikasi");
        alertDialog
                .setIcon(R.drawable.ic_warning_oren)
                .setMessage("Apakah Anda yakin keluar dari aplikasi?")
                .setCancelable(true)
                .setPositiveButton("Ya",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        finishAffinity();
                    }
                })
                .setNegativeButton("Tidak",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // jika tombol ini diklik, akan menutup dialog
                        // dan tidak terjadi apa2
                        dialog.cancel();
                    }
                });
        AlertDialog dialog = alertDialog.create();
        dialog.show();
    }
}
