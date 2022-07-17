package com.ringtonesialab.utiliti;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ringtonesialab.ringtoneproject.R;
import com.ringtonesialab.decrypt.assets.DeskripsiAsset;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class BuatTempFile {

	private ProgressBar loadingBarTunggu;
	private Dialog dialog;
	private int ukuranTerdownload = 0;
	private TextView info;
	private String namaFile ;
	private Activity activity;
    private String lokasiFileTempDisimpan;
	
	public BuatTempFile(Activity activity, String namaFile) {
		this.activity = activity;
		this.namaFile = namaFile;
	}
	
	public void startDownload(){
		 tampilkanProgres();
		 new Thread(new Runnable() {
             public void run() {
                  unduhFile();
             }
           }).start();
	}
	
	private void unduhFile(){
        try {
            File rootKartuSD = Environment.getExternalStorageDirectory();
            String namaFileterunduh = "temp" + ".mp3";

            File file = new File(rootKartuSD, namaFileterunduh);
            lokasiFileTempDisimpan = file.getAbsolutePath();
            
            FileOutputStream fileOutput = new FileOutputStream(file);
            Log.d("tes", namaFile);
            InputStream inputStream = activity.getAssets().open(Constant.namaFolderAsset+"/" + namaFile);
            
            fileOutput.write(new DeskripsiAsset(activity, new Hubungan(activity).getKeyAssets()).getByte(namaFile));
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    loadingBarTunggu.setMax(100);
                }               
            });

            byte[] buffer = new byte[10240];
            int bufferLength;
            while ( (bufferLength = inputStream.read(buffer)) > 0 ) {
                ukuranTerdownload += bufferLength;
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        loadingBarTunggu.setProgress(ukuranTerdownload);
                        info.setText("terdeskripsi " + (ukuranTerdownload/1024) + "KB");
                    }
                });
            }
            fileOutput.close();
            activity.runOnUiThread(new Runnable() {
                public void run() {dialog.dismiss();
                File file = new File(lokasiFileTempDisimpan);
                Uri audioUri = Uri.fromFile(file);
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("audio/*");
                share.putExtra(Intent.EXTRA_STREAM, audioUri);
                share.putExtra(Intent.EXTRA_TEXT, activity.getResources().getString(R.string.app_name) + "\n \nDapatkan secara gratis di play store "+ "https://play.google.com/store/apps/details?id="+ activity.getApplicationContext().getPackageName());
                activity.startActivity(Intent.createChooser(share, "Bagikan Melalui"));
                }
            });         
         
        } catch (Exception e) {
            tampilkanEror("Error : gagal menulis temporary file " + e);
        }       
    }     

	private void tampilkanEror(final String err){
		activity.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(activity, err, Toast.LENGTH_LONG).show();
            }
        });      
    }
     
    private void tampilkanProgres(){
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.myprogressdialog);
        dialog.setTitle("Dalam Proses");
        dialog.setCancelable(false);
        TextView text = (TextView) dialog.findViewById(R.id.tv1);
        text.setText("Tunggu sejenak... ");
        info = (TextView) dialog.findViewById(R.id.cur_pg_tv);
        info.setText("Mendeskripsi...");
        dialog.show();
         
        loadingBarTunggu = (ProgressBar)dialog.findViewById(R.id.progress_bar);
        loadingBarTunggu.setProgress(0);
        loadingBarTunggu.setProgressDrawable(ContextCompat.getDrawable(activity, R.drawable.green_progress));
    }
}


