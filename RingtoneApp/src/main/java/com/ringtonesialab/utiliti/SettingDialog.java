package com.ringtonesialab.utiliti;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


import com.ringtonesialab.ringtoneproject.R;

import java.io.File;


public class SettingDialog implements OnItemClickListener {

	private Activity act;
	public static  String filePath;
	private Dialog dialog;
    private static int kode_write_setting = 109;

	SettingDialog(Activity activity, String path) {
		this.act = activity;
		filePath = path;	
		dialog = new Dialog(activity);
		dialog.setTitle("Gunakan Sebagai");	}

	
	void showDialog() {
		dialog.setContentView(R.layout.layout_listview);
		ListView listView = (ListView) dialog.findViewById(R.id.main_grid_view);

		String[] optionArray = {
			"Panggilan Default",
			"Nada Alarm",
			"Notifikasi",
		};
		listView.setAdapter(new ArrayAdapter<>(act,
				android.R.layout.simple_list_item_1, optionArray));
		listView.setOnItemClickListener(this);
		listView.setBackgroundColor(Color.parseColor("#ffffff"));
		dialog.show();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		switch (position) {
		case 0:
			this.useToneAs(true, false, false);
			break;
		case 1:
			this.useToneAs(false, false, true);
			break;
		case 2:
			this.useToneAs(false, true, false);
			break;

		default:
			break;
		}
		dialog.dismiss();
	}

	private void atur(boolean ring, boolean notification, boolean alarm){
        File ringFile = new File(filePath);
        if (ringFile.exists()) {
            ContentValues values = new ContentValues();
            values.put(MediaStore.MediaColumns.DATA, ringFile.getAbsolutePath());
            values.put(MediaStore.MediaColumns.TITLE, "ring");
            values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mp3");
            values.put(MediaStore.MediaColumns.SIZE, ringFile.length());
            values.put(MediaStore.Audio.Media.ARTIST,"tone"+SystemClock.elapsedRealtime());
            values.put(MediaStore.Audio.Media.IS_RINGTONE, true);
            values.put(MediaStore.Audio.Media.IS_NOTIFICATION, true);
            values.put(MediaStore.Audio.Media.IS_ALARM, true);
            values.put(MediaStore.Audio.Media.IS_MUSIC, false);

            Uri uri = MediaStore.Audio.Media.getContentUriForPath(ringFile
                    .getAbsolutePath());
            Uri newUri = act.getContentResolver().insert(uri, values);

            try {
                if(ring){
                    RingtoneManager.setActualDefaultRingtoneUri(act,
                            RingtoneManager.TYPE_RINGTONE, newUri);
                    Toast.makeText(act, "Mengubah Nada Panggilan Default...",Toast.LENGTH_LONG).show();
                }else if(notification){
                    RingtoneManager.setActualDefaultRingtoneUri(act,
                            RingtoneManager.TYPE_NOTIFICATION, newUri);
                    Toast.makeText(act, "Mengubah Nada Notifikasi Default...",Toast.LENGTH_LONG).show();
                }else if(alarm){
                    RingtoneManager.setActualDefaultRingtoneUri(act,
                            RingtoneManager.TYPE_ALARM, newUri);
                    Toast.makeText(act, "Mengubah Nada Alarm Default...",Toast.LENGTH_LONG).show();
                }
            } catch (Throwable t) {
                Toast.makeText(act, "Error " + t.toString(),Toast.LENGTH_SHORT).show();
                t.printStackTrace();

            }
            Toast.makeText(act, "Pengaturan berhasil diterapkan",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(act, "Error--404",Toast.LENGTH_SHORT).show();
        }
    }


	private void useToneAs(boolean ring, boolean notification, boolean alarm) {
		boolean permission;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			permission = Settings.System.canWrite(act);
		} else {
			permission = ContextCompat.checkSelfPermission(act, Manifest.permission.WRITE_SETTINGS) == PackageManager.PERMISSION_GRANTED;
		}

		if (permission) {
			atur(ring, notification, alarm);
		} else {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                AlertDialog.Builder builder = new AlertDialog.Builder(act);
                builder.setTitle("Membutuhkan Ijin");
                builder.setMessage("Kami membutuhkan ijin untuk mengubah pengaturan perangkat anda.");
                builder.setPositiveButton("Ijinkan", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                        intent.setData(Uri.parse("package:" + act.getPackageName()));
                        act.startActivityForResult(intent, kode_write_setting);
                    }
                });
                builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
			}
        }

	}
}
