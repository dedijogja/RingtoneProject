package com.ringtonesialab.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.ads.AdListener;
import com.ringtonesialab.ringtoneproject.R;
import com.ringtonesialab.utiliti.Constant;
import com.ringtonesialab.utiliti.ManajemenIklan;


public class SplashLoad extends AppCompatActivity {

    private boolean statusIklan = true;
    int hitung = 0;
    int loadIklanBerapaKali = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_load);

        final ManajemenIklan manajemenIklan = (ManajemenIklan) getApplication();
        manajemenIklan.initInterstitial();
        manajemenIklan.loadIntersTitial();
        manajemenIklan.getInterstitial().setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int i) {
                hitung++;
                Log.d("iklan", "gagal "+String.valueOf(hitung));
                if(hitung<loadIklanBerapaKali){
                    if(statusIklan) {
                        manajemenIklan.loadIntersTitial();
                    }
                }
                if(hitung == loadIklanBerapaKali){
                    if(statusIklan) {
                        statusIklan = false;
                        manajemenIklan.setStatusIklan(Constant.gagalLoadIklan);
                        Intent intent = new Intent(SplashLoad.this, Menu.class);
                        startActivity(intent);
                        finish();
                    }
                }
                super.onAdFailedToLoad(i);
            }

            @Override
            public void onAdLoaded() {
                if(statusIklan) {
                    Log.d("iklan", "berhasil");
                    statusIklan = false;
                    manajemenIklan.setStatusIklan(Constant.berhasilLoadIklan);
                    Intent intent = new Intent(SplashLoad.this, Menu.class);
                    startActivity(intent);
                    finish();
                }
                super.onAdLoaded();
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(statusIklan) {
                    statusIklan = false;
                    manajemenIklan.setStatusIklan(Constant.gagalLoadIklan);
                    Intent intent = new Intent(SplashLoad.this, Menu.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 15000);

    }

}
