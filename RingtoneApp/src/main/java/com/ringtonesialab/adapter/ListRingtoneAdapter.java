package com.ringtonesialab.adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.ringtonesialab.ringtoneproject.R;
import com.ringtonesialab.activity.PemutarRingtone;
import com.ringtonesialab.admobwrapper.ViewWrapper;
import com.ringtonesialab.model.ListRingtoneModel;
import com.ringtonesialab.utiliti.Constant;
import com.ringtonesialab.utiliti.ManajemenIklan;
import com.startapp.android.publish.adsCommon.StartAppAd;

public class ListRingtoneAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private ListRingtoneModel[] listRingtoneModel;

    public ListRingtoneAdapter(Context context, ListRingtoneModel[] listRingtoneModels) {
        this.context = context;
        this.listRingtoneModel = listRingtoneModels;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewWrapper<>(new RecyPerItem(context));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holders, final int position) {
        final RecyPerItem holder = (RecyPerItem) holders.itemView;
        holder.penampunglist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = (Activity) context;
                final ManajemenIklan manajemenIklan = (ManajemenIklan) activity.getApplication();
                String status = manajemenIklan.getStatusIklan();

                if(status.equals(Constant.berhasilLoadIklan)) {
                    if (!manajemenIklan.isBolehMenampilkanIklanHitung() || !manajemenIklan.isBolehMenampilkanIklanWaktu()
                            || !manajemenIklan.getInterstitial().isLoaded()) {
                        Intent intent = new Intent(context, PemutarRingtone.class);
                        intent.putExtra(Constant.kodeIndexRingtone, position);
                        context.startActivity(intent);
                    }
                    manajemenIklan.getInterstitial().setAdListener(new AdListener() {
                        @Override
                        public void onAdClosed() {
                            Intent intent = new Intent(context, PemutarRingtone.class);
                            intent.putExtra(Constant.kodeIndexRingtone, position);
                            context.startActivity(intent);

                            manajemenIklan.loadIntersTitial();
                            super.onAdClosed();
                        }
                        @Override
                        public void onAdFailedToLoad(int i) {
                            if(manajemenIklan.getHitungFailed() < 2 ) {
                                manajemenIklan.loadIntersTitial();
                                manajemenIklan.setHitungFailed();
                            }
                            super.onAdFailedToLoad(i);
                        }

                        @Override
                        public void onAdLoaded() {
                            manajemenIklan.setHitungFailed(0);
                            super.onAdLoaded();
                        }
                    });
                    manajemenIklan.tampilkanInterstitial();
                }else{
                    if(manajemenIklan.getPenghitungStartApp() == 0) {
                        Log.d("iklan", "startApp inters tampil " + manajemenIklan.getPenghitungStartApp());
                        manajemenIklan.setPenghitungStartApp(1);
                        Intent intent = new Intent(context, PemutarRingtone.class);
                        intent.putExtra(Constant.kodeIndexRingtone, position);
                        context.startActivity(intent);
                        StartAppAd.showAd(context);
                    }else{
                        Log.d("iklan", "startApp tidak tampil " + manajemenIklan.getPenghitungStartApp());
                        manajemenIklan.setPenghitungStartApp(0);
                        Intent intent = new Intent(context, PemutarRingtone.class);
                        intent.putExtra(Constant.kodeIndexRingtone, position);
                        context.startActivity(intent);
                    }
                }

            }
        });


        holder.judulRingtone.setText(listRingtoneModel[position].getJudulRingtone());
        holder.judulTanggal.setText(listRingtoneModel[position].getTanggalRingtone());
    }

    @Override
    public int getItemCount() {
        return listRingtoneModel.length;
    }

    class RecyPerItem extends FrameLayout {

        private RelativeLayout penampunglist;
        private TextView judulRingtone;
        private TextView judulTanggal;

        public RecyPerItem(Context context) {
            super(context);
            inflate(context, R.layout.design_list_ringtone, this);
            penampunglist = (RelativeLayout) findViewById(R.id.penampunglist);
            judulRingtone = (TextView) findViewById(R.id.jRingtone);
            judulTanggal = (TextView) findViewById(R.id.jTanggal);
        }
    }


}
