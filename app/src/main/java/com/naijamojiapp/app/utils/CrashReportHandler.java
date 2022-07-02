package com.naijamojiapp.app.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Process;


import androidx.multidex.BuildConfig;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CrashReportHandler implements UncaughtExceptionHandler {

    private Context m_context;

    public static void attach(Context context) {
        Thread.setDefaultUncaughtExceptionHandler(new CrashReportHandler(context));
    }

    private CrashReportHandler(Context context) {
        m_context = context;
    }

    @SuppressLint("SimpleDateFormat")
    public void uncaughtException(Thread thread, Throwable exception) {
        StringWriter stackTrace = new StringWriter();
        exception.printStackTrace(new PrintWriter(stackTrace));
        int versionCode = BuildConfig.VERSION_CODE;
        String manufacturer = Build.MANUFACTURER;
        String versionName = BuildConfig.VERSION_NAME;
        String model_no = Build.DEVICE;
        String os =Build.VERSION.RELEASE;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String currentDateandTime = sdf.format(new Date());
        if (CheckConnection.getInstance(m_context).isConnectingToInternet()) {
            Intent i = new Intent(Intent.ACTION_SEND);// i.setType("text/plain"); //use this line for testing in

            i.setType("text/plain"); // use from live device
            i.putExtra(Intent.EXTRA_EMAIL, new String[]{"sanjay.panara@techinnate.com"});
            i.putExtra(Intent.EXTRA_SUBJECT, "Naijamoji: Something went wrong");
            i.putExtra(Intent.EXTRA_TEXT, "Send email error to developer for resolved this error " + currentDateandTime + "\n" + stackTrace.toString()+"\n"+"Version: "+versionName+"\n Model: "+model_no+" ("+manufacturer+")"+"\n OS: "+os);


            m_context.startActivity(i);

        }
        Process.killProcess(Process.myPid());
        System.exit(10);
    }
}