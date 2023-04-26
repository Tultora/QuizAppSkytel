package com.skytel.quizapp.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import androidx.multidex.MultiDexApplication;

import com.skytel.quizapp.Networking.FingerRequest;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

public class Application extends MultiDexApplication {

    public static final String TAG_LANGUAGE = "Language";
    public static final String TAG_IS_WCDMA = "IS_WCDMA";
    public static final int TAG_LANGUAGE_MN = 1;
    public static final int TAG_LANGUAGE_EN = 2;
    public static final String TAG_WCDMA_VALUE = null;
    private static final String TAG = "Application";
    private static final String SECONDARY_DEX_NAME = "secondary_dex.jar";
    private static final int BUF_SIZE = 8 * 1024;
    public static boolean isEn = false;
    public static String TAG_LANGUAGE_VALUE = null;
    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor spEditor;
    public static Application instance;
    public static int basketCount;

    public static String getTagLanguageValue() {
        return TAG_LANGUAGE_VALUE;
    }

    public static void setTagLanguageValue(String tagLanguageValue) {
        TAG_LANGUAGE_VALUE = tagLanguageValue;
    }


    public static Application getInstance() {
        return instance;
    }

    //request header
    public static HashMap<String, String> getHeader() {
        HashMap<String, String> tokenHeader = new HashMap<String, String>();
        tokenHeader.put("Content-Type", "application/x-www-form-urlencoded;");
        tokenHeader.put("charset", "utf-8");
        tokenHeader.put("value", getTagLanguageValue());
        return tokenHeader;
    }

    public static int vmSize(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        return am.getMemoryClass();
    }


    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        Log.d(TAG, vmSize(instance) + "--size");

        final File dexInternalStoragePath = new File(getDir("dex", Context.MODE_PRIVATE),
                SECONDARY_DEX_NAME);
        (new PrepareDexTask()).execute(dexInternalStoragePath);

        FingerRequest.init(instance);

        sharedPreferences = getSharedPreferences(TAG, 0);
        spEditor = sharedPreferences.edit();


        spEditor.putString(TAG_IS_WCDMA, TAG_WCDMA_VALUE).apply();
        if (sharedPreferences.getInt(TAG_LANGUAGE, 0) == 0 || sharedPreferences.getInt(TAG_LANGUAGE, 0) == Application.TAG_LANGUAGE_MN) {
            spEditor.putInt(TAG_LANGUAGE, TAG_LANGUAGE_MN).apply();
            isEn = false;
            setTagLanguageValue("mn");
        } else if (sharedPreferences.getInt(TAG_LANGUAGE, 0) == Application.TAG_LANGUAGE_EN) {
            spEditor.putInt(TAG_LANGUAGE, TAG_LANGUAGE_EN).apply();
            isEn = true;
            setTagLanguageValue("en");
        }

    }



    public boolean prepareDex(File dexInternalStoragePath) {
        BufferedInputStream bis = null;
        OutputStream dexWriter = null;

        try {
            bis = new BufferedInputStream(getAssets().open(SECONDARY_DEX_NAME));
            dexWriter = new BufferedOutputStream(new FileOutputStream(dexInternalStoragePath));
            byte[] buf = new byte[BUF_SIZE];
            int len;
            while ((len = bis.read(buf, 0, BUF_SIZE)) > 0) {
                dexWriter.write(buf, 0, len);
            }
            dexWriter.close();
            bis.close();
            return true;
        } catch (IOException e) {
            if (dexWriter != null) {
                try {
                    dexWriter.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
            return false;
        }
    }

    public class PrepareDexTask extends AsyncTask<File, Void, Boolean> {

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
        }

        @Override
        protected Boolean doInBackground(File... dexInternalStoragePaths) {
            prepareDex(dexInternalStoragePaths[0]);
            return null;
        }
    }

}
