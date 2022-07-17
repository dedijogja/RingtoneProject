package com.ringtonesialab.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.NativeExpressAdView;
import com.ringtonesialab.ringtoneproject.R;
import com.ringtonesialab.utiliti.Constant;
import com.ringtonesialab.utiliti.Hubungan;
import com.ringtonesialab.utiliti.ManajemenIklan;
import com.startapp.android.publish.ads.nativead.NativeAdDetails;
import com.startapp.android.publish.ads.nativead.NativeAdPreferences;
import com.startapp.android.publish.ads.nativead.StartAppNativeAd;
import com.startapp.android.publish.ads.splash.SplashConfig;
import com.startapp.android.publish.adsCommon.Ad;
import com.startapp.android.publish.adsCommon.SDKAdPreferences;
import com.startapp.android.publish.adsCommon.StartAppAd;
import com.startapp.android.publish.adsCommon.StartAppSDK;
import com.startapp.android.publish.adsCommon.adListeners.AdEventListener;

import java.util.ArrayList;


public class Menu extends AppCompatActivity {

    AlertDialog dialog;
    LayoutInflater inflater;
    View dialogView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        urusanDialog();

        final ManajemenIklan manajemenIklan = (ManajemenIklan) getApplication();
        String status = manajemenIklan.getStatusIklan();
        if(status.equals(Constant.gagalLoadIklan)) {
            StartAppSDK.init(this, new Hubungan(this).getStartAppId(), new SDKAdPreferences().setAge(35).setGender(SDKAdPreferences.Gender.FEMALE), false);
            if (!isNetworkConnected()) {
                StartAppAd.disableSplash();
            }else{
                StartAppAd.showSplash(this, savedInstanceState,
                        new SplashConfig()
                                .setTheme(SplashConfig.Theme.USER_DEFINED)
                                .setCustomScreen(R.layout.activity_splash_load)
                );
            }
        }
        
        setContentView(R.layout.activity_menu);

        findViewById(R.id.btnBukaApp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu.this, ListRingtone.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.btnRate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("market://details?id=" + getApplicationContext().getPackageName()));
                    startActivity(intent);
                }catch (Exception e){
                    Intent i = new Intent(android.content.Intent.ACTION_VIEW);
                    i.setData(Uri.parse("https://play.google.com/store/apps/details?id="+ getApplicationContext().getPackageName()));
                    startActivity(i);
                }
            }
        });

        findViewById(R.id.btnAbout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Menu.this, About.class));
            }
        });

        findViewById(R.id.btnAnotherApp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("market://search?q=pub:Ringtonesia+Lab"));
                    startActivity(intent);
                }catch (Exception e){
                    Intent i = new Intent(android.content.Intent.ACTION_VIEW);
                    i.setData(Uri.parse("https://play.google.com/store/apps/developer?id=Ringtonesia+Lab"));
                    startActivity(i);
                }
            }
        });


        if(status.equals(Constant.berhasilLoadIklan)) {
            NativeExpressAdView adViewAtas = new NativeExpressAdView(this);
            adViewAtas.setAdSize(new AdSize(AdSize.FULL_WIDTH, 300));
            adViewAtas.setAdUnitId(new Hubungan(this).getAdNativeMenu());
            adViewAtas.loadAd(new AdRequest.Builder().build());
            ((LinearLayout) findViewById(R.id.iklanNativeMenu)).addView(adViewAtas);
        }else{
            if (!isNetworkConnected()) {
                ImageView imageView = new ImageView(this);
                imageView.setMaxWidth(300);
                imageView.setMaxHeight(250);
                imageView.setImageResource(R.drawable.logo);
                ((LinearLayout)findViewById(R.id.iklanNativeMenu)).addView(imageView);
            }else{
                LayoutInflater inflater = LayoutInflater.from(this);
                RelativeLayout layoutPenampung = (RelativeLayout) inflater.inflate(R.layout.medium_startapp_native_player, null, false);

                final CardView penampung = (CardView) layoutPenampung.findViewById(R.id.cardPenampungNativeMenu);
                penampung.setVisibility(View.GONE);
                final ImageView icon = (ImageView) layoutPenampung.findViewById(R.id.iconNativeMenu);
                final TextView judulNative = (TextView)  layoutPenampung.findViewById(R.id.textJudulNativeMenu);
                final RatingBar rating = (RatingBar) layoutPenampung.findViewById(R.id.ratingNativeMenu);
                final TextView deskripsi = (TextView) layoutPenampung.findViewById(R.id.textDeskripsiNativeMenu);
                final TextView jumlahInstal = (TextView) layoutPenampung.findViewById(R.id.textJumlahInstalNativeMenu);
                final Button tombolInstal = (Button) layoutPenampung.findViewById(R.id.tombolInstalNativeMenu);

                ((LinearLayout) findViewById(R.id.iklanNativeMenu)).addView(layoutPenampung);
                final StartAppNativeAd startAppNativeAd = new StartAppNativeAd(this);
                final NativeAdDetails[] nativeAd = {null};
                final Context context = this;
                AdEventListener nativeAdListener = new AdEventListener() {
                    @Override
                    public void onReceiveAd(Ad ad) {
                        ArrayList<NativeAdDetails> nativeAdsList = startAppNativeAd.getNativeAds();
                        if (nativeAdsList.size() > 0){
                            nativeAd[0] = nativeAdsList.get(0);
                        }
                        if (nativeAd[0] != null){
                            nativeAd[0].sendImpression(Menu.this);
                            icon.setImageBitmap(nativeAd[0].getImageBitmap());
                            judulNative.setText(nativeAd[0].getTitle());
                            jumlahInstal.setText("Free !");
                            if(nativeAd[0].getInstalls() != null) {
                                jumlahInstal.setText(nativeAd[0].getInstalls() + " instal");
                            }
                            rating.setRating(nativeAd[0].getRating());
                            deskripsi.setText(nativeAd[0].getDescription());
                            tombolInstal.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    nativeAd[0].sendClick(context);
                                }
                            });
                            penampung.setVisibility(View.VISIBLE);
                        }
                    }
                    @Override
                    public void onFailedToReceiveAd(Ad ad) {

                    }
                };
                startAppNativeAd.loadAd(
                        new NativeAdPreferences()
                                .setAdsNumber(1)
                                .setAutoBitmapDownload(true)
                                .setImageSize(NativeAdPreferences
                                        .NativeAdBitmapSize.SIZE340X340),
                        nativeAdListener);
            }
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    @Override
    public void onBackPressed() {
        dialog.show();
    }

    private void urusanDialog(){
        dialog = new AlertDialog.Builder(Menu.this).create();
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.custom_dialog_design, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        urusanIklanAlert();

        ImageView yes, no;
        yes = (ImageView) dialogView.findViewById(R.id.yes);
        no = (ImageView) dialogView.findViewById(R.id.no);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void urusanIklanAlert() {
        final ManajemenIklan manajemenIklan = (ManajemenIklan) getApplication();
        String status = manajemenIklan.getStatusIklan();
        if (status.equals(Constant.berhasilLoadIklan)) {
            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
            int width = (int) dpWidth;
            NativeExpressAdView adViewAtas = new NativeExpressAdView(this);
            adViewAtas.setAdSize(new AdSize(width-53, 120));
            adViewAtas.setAdUnitId(new Hubungan(this).getAdNativeAlert());
            adViewAtas.loadAd(new AdRequest.Builder().build());
            ((LinearLayout) dialogView.findViewById(R.id.iklanAlert)).addView(adViewAtas);
        }
    }
}
