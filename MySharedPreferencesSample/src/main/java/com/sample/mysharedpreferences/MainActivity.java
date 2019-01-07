package com.sample.mysharedpreferences;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
    public static Context sContext;


    /**
     * Activity 需要使用application的context
     * @param name
     * @param mode
     * @return
     */
    @Override
    public SharedPreferences getSharedPreferences(String name, int mode) {
        return this.getApplicationContext().getSharedPreferences(name, mode);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        sContext = getApplicationContext();
        final Button testGc= (Button) findViewById(R.id.test_readsp);
        testGc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readMySharedPreferences();
            }
        });

        final Button testIO = (Button) findViewById(R.id.test_writesp);
        testIO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeMySharedPreferences();
            }
        });
    }

    private void readMySharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("test", Context.MODE_PRIVATE);
        for (int i = 0; i < 100; i++) {
            String key = "test:" + i;
            Log.e("test", "key:" + key + ", value:" + sharedPreferences.getInt(key, -1));
        }
    }

    private void writeMySharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("test", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        for (int i = 0; i < 100; i++) {
            editor.putInt("test:" + i, i);
            editor.apply();
        }
    }

}
