package com.ringtonesialab.model;



public class ListRingtoneModel {
    private String judulRingtone;
    private String tanggalRingtone;
    private String alamatRingtone;
    private String durasiRingtone;

    public ListRingtoneModel(String judulRingtone, String tanggalRingtone, String alamatRingtone) {
        this.judulRingtone = judulRingtone;
        this.tanggalRingtone = tanggalRingtone;
        this.alamatRingtone = alamatRingtone;
    }

    public String getJudulRingtone() {
        return judulRingtone;
    }

    public String getTanggalRingtone() {
        return tanggalRingtone;
    }

    public String getAlamatRingtone() {
        return alamatRingtone;
    }

    public void setDurasiRingtone(String durasiRingtone){
        this.durasiRingtone = durasiRingtone;
    }

    public String getDurasiRingtone(){
        return durasiRingtone;
    }

}
