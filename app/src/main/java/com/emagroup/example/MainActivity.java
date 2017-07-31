package com.emagroup.example;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.emagroup.briefsdk.EMASDK;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EMASDK.getInstance().onCreat("864ed48309c7f9a259f769b92cc52814",this);
    }
}
