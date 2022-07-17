package com.ringtonesialab.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdSize;
import com.ringtonesialab.ringtoneproject.R;
import com.ringtonesialab.adapter.ListRingtoneAdapter;
import com.ringtonesialab.admobwrapper.WrapAdapter;
import com.ringtonesialab.utiliti.Constant;
import com.ringtonesialab.utiliti.Dekor;
import com.ringtonesialab.utiliti.Hubungan;
import com.ringtonesialab.utiliti.ManajemenIklan;
import com.startapp.android.publish.ads.banner.Banner;

import java.io.IOException;

public class ListRingtone extends AppCompatActivity {

    private ListRingtoneAdapter listRingtoneAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_ringtone);

        recyclerView = (RecyclerView) findViewById(R.id.recycleListRingtone);
        try {
            listRingtoneAdapter = new ListRingtoneAdapter(this, Constant.listSemuaRingtone(this));
        } catch (IOException e) {
            e.printStackTrace();
        }
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new Dekor(3, this));
        urusanIklan();
        listRingtoneAdapter.notifyDataSetChanged();

        findViewById(R.id.tombolKembali).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void urusanIklan(){
        final ManajemenIklan manajemenIklan = (ManajemenIklan) getApplication();
        String status = manajemenIklan.getStatusIklan();
        if(status.equals(Constant.berhasilLoadIklan)) {
            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
            int width = (int) dpWidth;
            WrapAdapter adapterWrapper = new WrapAdapter(this,
                    new Hubungan(this).getAdNativeList(),
                    new AdSize(width, 110));
            adapterWrapper.setAdapter(listRingtoneAdapter);
            adapterWrapper.setLimitOfAds(3);
            adapterWrapper.setNoOfDataBetweenAds(8);
            adapterWrapper.setFirstAdIndex(0);
            recyclerView.setAdapter(adapterWrapper);
        }else {
            recyclerView.setAdapter(listRingtoneAdapter);
            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.iklanBannerKecil);
            linearLayout.setVisibility(View.VISIBLE);
            Banner startAppBanner = new Banner(this);
            linearLayout.addView(startAppBanner);

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
