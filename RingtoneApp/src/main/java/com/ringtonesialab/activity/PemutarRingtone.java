package com.ringtonesialab.activity;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.NativeExpressAdView;
import com.ringtonesialab.ringtoneproject.R;
import com.ringtonesialab.decrypt.assets.DeskripsiAsset;
import com.ringtonesialab.model.ListRingtoneModel;
import com.ringtonesialab.utiliti.BuatTempFile;
import com.ringtonesialab.utiliti.Constant;
import com.ringtonesialab.utiliti.FileDownloader;
import com.ringtonesialab.utiliti.Hubungan;
import com.ringtonesialab.utiliti.ManajemenIklan;
import com.startapp.android.publish.ads.banner.Banner;
import com.startapp.android.publish.ads.nativead.NativeAdDetails;
import com.startapp.android.publish.ads.nativead.NativeAdPreferences;
import com.startapp.android.publish.ads.nativead.StartAppNativeAd;
import com.startapp.android.publish.adsCommon.Ad;
import com.startapp.android.publish.adsCommon.adListeners.AdEventListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;


public class PemutarRingtone extends AppCompatActivity {

    private ListRingtoneModel[] listRingtoneModels;
    private int indexSaatIni = 0;

    private MediaPlayer mediaPlayer =  new MediaPlayer();
    private SeekBar seekBar;
    private final Handler handler = new Handler();

    private TextView judulRingtone, waktuSaatIni, panjangWaktuMusik;
    private ImageView prev, playpause, next, set, share;

    private Context context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pemutar_ringtone);
        urusanIklan();

        try {
            listRingtoneModels = Constant.listSemuaRingtone(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        inisialisasiVariabel();
        indexSaatIni = getIntent().getIntExtra(Constant.kodeIndexRingtone, 0);
        seekBar.setPadding(0,0,0,0);
        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override public boolean onTouch(View v, MotionEvent event) {
                seekChange(v);
                return false;
            }
        });
        putarSuara(listRingtoneModels[indexSaatIni].getAlamatRingtone());
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mediaPlayer.pause();
                mediaPlayer.seekTo(1);
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                playpause.setImageResource(R.drawable.play);
            }
        });
    }

    private void inisialisasiVariabel(){
        seekBar = (SeekBar) findViewById(R.id.seekbar);
        waktuSaatIni = (TextView) findViewById(R.id.waktuSaatIni);
        panjangWaktuMusik = (TextView) findViewById(R.id.panjangWaktuRingtone);
        judulRingtone = (TextView) findViewById(R.id.judulRingtone);

        prev = (ImageView) findViewById(R.id.prev);
        playpause = (ImageView) findViewById(R.id.playpause);
        next = (ImageView) findViewById(R.id.next);
        set = (ImageView) findViewById(R.id.set);
        share = (ImageView) findViewById(R.id.share);


        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(indexSaatIni == 0){
                    indexSaatIni = listRingtoneModels.length-1;
                    mediaPlayer.stop();
                    putarSuara(listRingtoneModels[indexSaatIni].getAlamatRingtone());
                }else{
                    indexSaatIni--;
                    mediaPlayer.stop();
                    putarSuara(listRingtoneModels[indexSaatIni].getAlamatRingtone());
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(indexSaatIni == listRingtoneModels.length-1){
                    indexSaatIni = 0;
                    mediaPlayer.stop();
                    putarSuara(listRingtoneModels[indexSaatIni].getAlamatRingtone());
                }else{
                    indexSaatIni++;
                    mediaPlayer.stop();
                    putarSuara(listRingtoneModels[indexSaatIni].getAlamatRingtone());
                }
            }
        });

        playpause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()){
                    playpause.setImageResource(R.drawable.play);
                    mediaPlayer.pause();
                }else{
                    playpause.setImageResource(R.drawable.pause);
                    mediaPlayer.seekTo(seekBar.getProgress());
                    mediaPlayer.start();
                    startPlayProgressUpdater();
                }
            }
        });

        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileDownloader downloader = new FileDownloader(PemutarRingtone.this, listRingtoneModels[indexSaatIni].getAlamatRingtone());
                downloader.startDownload();
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = (Activity) context;
                BuatTempFile downloader = new BuatTempFile(activity, listRingtoneModels[indexSaatIni].getAlamatRingtone());
                downloader.startDownload();
            }
        });
    }


    private void putarSuara(String namaFile){
        DeskripsiAsset deskripsiAsset = new DeskripsiAsset(this, new Hubungan(this).getKeyAssets());
        deskripsiAsset.setListenerDecrypt(new DeskripsiAsset.ListenerDecrypt() {
            @Override
            public void onSelesaiDecrypt(byte[] bytes) {
                try {
                    File tempMp3 = File.createTempFile("temp", "mp3", getCacheDir());
                    tempMp3.deleteOnExit();
                    FileOutputStream fos = new FileOutputStream(tempMp3);
                    fos.write(bytes);
                    fos.close();
                    mediaPlayer.reset();
                    FileInputStream fis = new FileInputStream(tempMp3);
                    mediaPlayer.setDataSource(fis.getFD());
                    mediaPlayer.prepare();
                    seekBar.setMax(mediaPlayer.getDuration());
                    mediaPlayer.start();
                    updateInterface();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        deskripsiAsset.execute(namaFile);
    }

    private void updateInterface(){
        startPlayProgressUpdater();
        judulRingtone.setText(listRingtoneModels[indexSaatIni].getJudulRingtone());
        playpause.setImageResource(R.drawable.pause);
    }

    private void seekChange(View v){
        if(mediaPlayer.isPlaying()){
            SeekBar sb = (SeekBar)v;
            mediaPlayer.seekTo(sb.getProgress());
            int waktuSekarang = mediaPlayer.getCurrentPosition()/1000;
            int sekaranghours = waktuSekarang / 3600;
            int sekarangminutes = (waktuSekarang % 3600) / 60;
            int sekarangseconds = waktuSekarang % 60;
            String sekarang = String.format("%02d:%02d:%02d", sekaranghours, sekarangminutes, sekarangseconds);
            waktuSaatIni.setText(sekarang);
        }
    }

    Runnable notification;
    private void startPlayProgressUpdater() {
        seekBar.setProgress(mediaPlayer.getCurrentPosition());

        if (mediaPlayer.isPlaying()) {
            notification = new Runnable() {
                @Override
                public void run() {
                    int totalWaktu = mediaPlayer.getDuration()/1000;
                    int totalhours = totalWaktu / 3600;
                    int totalminutes = (totalWaktu % 3600) / 60;
                    int totalseconds = totalWaktu % 60;
                    String panjang = String.format("%02d:%02d:%02d", totalhours, totalminutes, totalseconds);
                    int waktuSekarang = mediaPlayer.getCurrentPosition()/1000;
                    int sekaranghours = waktuSekarang / 3600;
                    int sekarangminutes = (waktuSekarang % 3600) / 60;
                    int sekarangseconds = waktuSekarang % 60;
                    String sekarang = String.format("%02d:%02d:%02d", sekaranghours, sekarangminutes, sekarangseconds);
                    panjangWaktuMusik.setText(panjang);
                    waktuSaatIni.setText(sekarang);
                    startPlayProgressUpdater();
                }
            };
            handler.postDelayed(notification, 1000);
        }else{
            mediaPlayer.pause();
        }
    }

    private void urusanIklan(){
        final ManajemenIklan manajemenIklan = (ManajemenIklan) getApplication();
        String status = manajemenIklan.getStatusIklan();
        if(status.equals(Constant.berhasilLoadIklan)) {
            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
            int width = (int) dpWidth;

            NativeExpressAdView adViewAtas = new NativeExpressAdView(this);
            adViewAtas.setAdSize(new AdSize(width - 20, 300));
            adViewAtas.setAdUnitId(new Hubungan(this).getAdNativePlayer());
            AdRequest.Builder adRequestBuilder = new AdRequest.Builder();
            ((LinearLayout) findViewById(R.id.iklanNativePlayer)).addView(adViewAtas);
            adViewAtas.loadAd(adRequestBuilder.build());
        }else{
            if (!isNetworkConnected()) {
                ImageView imageView = new ImageView(this);
                imageView.setMaxWidth(300);
                imageView.setMaxHeight(250);
                imageView.setImageResource(R.drawable.logo);
                ((LinearLayout)findViewById(R.id.iklanNativePlayer)).addView(imageView);
                findViewById(R.id.iklanNativePlayer).setBackgroundResource(R.drawable.wadah_icon);
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

                ((LinearLayout) findViewById(R.id.iklanNativePlayer)).addView(layoutPenampung);
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
                            nativeAd[0].sendImpression(PemutarRingtone.this);
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

                LinearLayout linearLayout = (LinearLayout) findViewById(R.id.iklanBannerStartAppPlayer);
                linearLayout.setVisibility(View.VISIBLE);
                Banner startAppBanner = new Banner(context);
                linearLayout.addView(startAppBanner);
            }
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    @Override
    public void onBackPressed() {
        mediaPlayer.pause();
        playpause.setImageResource(R.drawable.play);
        finish();
    }

    @Override
    protected void onPause() {
        mediaPlayer.pause();
        playpause.setImageResource(R.drawable.play);
        super.onPause();
    }

}
