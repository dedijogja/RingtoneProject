package com.ringtonesialab.utiliti;


import android.content.Context;

import com.ringtonesialab.model.ListRingtoneModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Constant {

    private static String tanggalRingtone = "23 May 2017";

    public static ListRingtoneModel[] listSemuaRingtone(Context context) throws IOException {
        List<ListRingtoneModel> modelist = new ArrayList<>();
        String[] fileNames = context.getAssets().list("ringtone");
        for(String name:fileNames){
            char[] temp = new char[name.toCharArray().length-4];
            for(int i = 0; i<name.toCharArray().length-4; i++){
                temp[i] = name.toCharArray()[i];
            }
            modelist.add(new ListRingtoneModel(String.valueOf(temp), tanggalRingtone, name));
        }

        ListRingtoneModel[] dap = new ListRingtoneModel[modelist.size()];
        for(int i = 0; i<modelist.size(); i++){
            dap[i] = modelist.get(i);
        }
        return dap;
    }


    public static String gagalLoadIklan = "gagal";
    public static String berhasilLoadIklan = "berhasil";
    public static String kodeIndexRingtone = "kodeindexringtone";
    public static String namaFolderAsset = "ringtone";

}
