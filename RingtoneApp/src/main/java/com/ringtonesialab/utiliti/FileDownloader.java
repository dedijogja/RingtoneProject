package com.ringtonesialab.utiliti;

import android.app.Activity;
import android.app.Dialog;
import android.os.Environment;
import android.os.SystemClock;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ringtonesialab.ringtoneproject.R;
import com.ringtonesialab.decrypt.assets.DeskripsiAsset;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class FileDownloader {

	private ProgressBar pb;
	private Dialog dialog;
	private int downloadedSize = 0;
	private int totalSize = 0;
	private TextView cur_val;
	private String dwnload_file_id ;
	private Activity c;
    private String localStoreFilePath;
	
	public FileDownloader(Activity context, String file_id) {
		this.c = context;
		this.dwnload_file_id=file_id;		
	}
	
	public void startDownload(){
		 showProgress();
		 
		 new Thread(new Runnable() {
             public void run() {
                  downloadFile();
             }
           }).start();
	}
	
	private void downloadFile(){
        
        try {           

            File SDCardRoot = Environment.getExternalStorageDirectory();
            String downloadedFileName = "tone" + SystemClock.elapsedRealtime() + ".mp3";

            File file = new File(SDCardRoot, downloadedFileName);
            localStoreFilePath=file.getAbsolutePath();
            
            FileOutputStream fileOutput = new FileOutputStream(file);           
            InputStream inputStream = c.getAssets().open(Constant.namaFolderAsset+"/"+dwnload_file_id);
            
            fileOutput.write(new DeskripsiAsset(c, new Hubungan(c).getKeyAssets()).getByte(dwnload_file_id));
 
            c.runOnUiThread(new Runnable() {
                public void run() {
                    pb.setMax(totalSize);
                }               
            });

            byte[] buffer = new byte[10240];
            int bufferLength = 0;
 
            while ( (bufferLength = inputStream.read(buffer)) > 0 ) {
                downloadedSize += bufferLength;
                
                c.runOnUiThread(new Runnable() {
                    public void run() {
                        pb.setProgress(downloadedSize);
                        float per = ((float)downloadedSize/totalSize) * 100;
                        cur_val.setText("Downloaded " + (downloadedSize/1024) + "KB / " +
                                (totalSize/1024) + "KB (" + (int)per + "%)" );
                    }
                });
            }
            fileOutput.close();
            c.runOnUiThread(new Runnable() {
                public void run() {
                 dialog.dismiss();
                 new SettingDialog(c, localStoreFilePath).showDialog();
                 
                }
            });         
         
        }
        catch (Exception e) {
            showError("Error : Failed to write temp file " + e);
        }       
    }     

	private void showError(final String err){
		c.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(c, err, Toast.LENGTH_LONG).show();
            }
        });      
    }
     
    private void showProgress(){
        dialog = new Dialog(c);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.myprogressdialog);
        dialog.setTitle("Sedang Memproses");
        dialog.setCancelable(false);
        TextView text = (TextView) dialog.findViewById(R.id.tv1);
        text.setText("Mohon tunggu... ");
        cur_val = (TextView) dialog.findViewById(R.id.cur_pg_tv);
        cur_val.setText("Mempersiapkan...");
        dialog.show();
         
        pb = (ProgressBar)dialog.findViewById(R.id.progress_bar);
        pb.setProgress(0);
        pb.setProgressDrawable(c.getResources().getDrawable(R.drawable.green_progress));  
    }
}


