package com.ringtonesialab.utiliti;


import android.content.Context;
import android.util.Log;

import com.ringtonesialab.decrypt.text.deskriptor.DeskripsiText;


public class Hubungan {
    static {
        System.loadLibrary("native-lib");
    }

    public Hubungan(Context context){
        if(!context.getPackageName().equals(new DeskripsiText(keyDesText(), packageName()).dapatkanTextAsli())){
            throw new RuntimeException(new DeskripsiText(keyDesText(), smesek()).dapatkanTextAsli());
        }
    }

    public String getKeyAssets() {
        return keyDesAssets();
    }

    public String getAdNativeAlert() {
        Log.d("enskripsi nativeAlert", new DeskripsiText(keyDesText(), adNativeAlert()).dapatkanTextAsli());
        return new DeskripsiText(keyDesText(), adNativeAlert()).dapatkanTextAsli();
    }

    public String getAdInterstitial() {
        Log.d("enskripsi getAdInter", new DeskripsiText(keyDesText(), adInterstitial()).dapatkanTextAsli());
        return new DeskripsiText(keyDesText(), adInterstitial()).dapatkanTextAsli();
    }

    public String getAdNativeList() {
        Log.d("enskripsi nativeList", new DeskripsiText(keyDesText(), adNativeList()).dapatkanTextAsli());
        return new DeskripsiText(keyDesText(), adNativeList()).dapatkanTextAsli();
    }

    public String getAdNativePlayer() {
        Log.d("enskripsi nativePlayer", new DeskripsiText(keyDesText(), adNativePlayer()).dapatkanTextAsli());
        return new DeskripsiText(keyDesText(), adNativePlayer()).dapatkanTextAsli();
    }

    public String getAdNativeMenu() {
        Log.d("enskripsi nativeMenu", new DeskripsiText(keyDesText(), adNativeMenu()).dapatkanTextAsli());
        return new DeskripsiText(keyDesText(), adNativeMenu()).dapatkanTextAsli();
    }

    public String getStartAppId() {
        Log.d("enskripsi startAppId", new DeskripsiText(keyDesText(), startAppId()).dapatkanTextAsli());
        return new DeskripsiText(keyDesText(), startAppId()).dapatkanTextAsli();
    }

    public native String packageName();
    public native String keyDesText();
    public native String keyDesAssets();
    public native String adNativeAlert();
    public native String adInterstitial();
    public native String adNativeList();
    public native String adNativePlayer();
    public native String adNativeMenu();
    public native String startAppId();
    public native String smesek();
}
