package com.ringtonesialab.decrypt.assets;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Base64;

import com.ringtonesialab.utiliti.Constant;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class DeskripsiAsset extends AsyncTask<String, Void, byte[]>{
    private static Context context;
    private static String key = "";
    private byte[] byteResult = null;

    private ProgressDialog dialog;
    private ListenerDecrypt listenerDecrypt;

    public DeskripsiAsset(Context context, String key){
        DeskripsiAsset.context = context;
        DeskripsiAsset.key = key;
    }

    synchronized public byte[] getByte(String namaFile){
        SecretKey baru =  new SecretKeySpec(Base64.decode(key, Base64.DEFAULT),
                0, Base64.decode(key, Base64.DEFAULT).length, "AES");

        byte[] byteResult = null;
        try {
            InputStream inputStream = context.getAssets().open(Constant.namaFolderAsset+"/"+namaFile);
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            inputStream.close();

            Cipher AesCipher = Cipher.getInstance("AES");
            AesCipher.init(Cipher.DECRYPT_MODE, baru);
            byteResult = AesCipher.doFinal(bytes);

        } catch (NoSuchAlgorithmException | IllegalBlockSizeException | InvalidKeyException |
                IOException | BadPaddingException | NoSuchPaddingException e) {
            e.printStackTrace();
        }
        return byteResult;
    }

    @Override
    protected void onPreExecute() {
        dialog = ProgressDialog.show(context, "Preparing file",
                "Please wait ...", true);
        dialog.show();
    }

    @Override
    protected byte[] doInBackground(String... params) {
        SecretKey baru =  new SecretKeySpec(Base64.decode(key, Base64.DEFAULT),
                0, Base64.decode(key, Base64.DEFAULT).length, "AES");
        try {
            InputStream inputStream = context.getAssets().open(Constant.namaFolderAsset+"/"+params[0]);
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            inputStream.close();

            Cipher AesCipher = Cipher.getInstance("AES");
            AesCipher.init(Cipher.DECRYPT_MODE, baru);
            byteResult = AesCipher.doFinal(bytes);

        } catch (NoSuchAlgorithmException | IllegalBlockSizeException | InvalidKeyException | IOException | BadPaddingException | NoSuchPaddingException e) {
            e.printStackTrace();
        }
        return byteResult;
    }

    @Override
    protected void onPostExecute(byte[] bytes) {
        dialog.dismiss();
        listenerDecrypt.onSelesaiDecrypt(bytes);
    }

    public void setListenerDecrypt(ListenerDecrypt listenerDecrypts){
        if(listenerDecrypt == null){
            this.listenerDecrypt = listenerDecrypts;
        }
    }

    public interface ListenerDecrypt{
        void onSelesaiDecrypt(byte[] bytes);
    }

}
