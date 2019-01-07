package com.sample.mysharedpreferences;


import java.io.File;
import java.util.HashMap;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.sample.mysharedpreferences.sharedpreferenceimpl.SharedPreferencesHelper;
import com.sample.mysharedpreferences.sharedpreferenceimpl.SharedPreferencesImpl;

public class SampleApplication extends Application {
    private static final HashMap<String, SharedPreferencesImpl> sSharedPrefs = new HashMap<String, SharedPreferencesImpl>();

    public SampleApplication() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    protected void attachBaseContext(final Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public SharedPreferences getSharedPreferences(String name, int mode) {
        if (!SharedPreferencesHelper.canUseCustomSp()) {
            return super.getSharedPreferences(name, mode);
        }
        SharedPreferencesImpl sp;
        synchronized (sSharedPrefs) {
            sp = sSharedPrefs.get(name);
            if (sp == null) {
                File prefsFile = SharedPreferencesHelper.getSharedPrefsFile(this, name);
                sp = new SharedPreferencesImpl(prefsFile, mode);
                sSharedPrefs.put(name, sp);
                return sp;
            }
        }

        if ((mode & Context.MODE_MULTI_PROCESS) != 0) {
            // If somebody else (some other process) changed the prefs
            // file behind our back, we reload it.  This has been the
            // historical (if undocumented) behavior.
            sp.startReloadIfChangedUnexpectedly();
        }

        return sp;
    }
}
