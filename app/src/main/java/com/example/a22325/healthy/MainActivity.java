package com.example.a22325.healthy;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.a22325.healthy.custom.DatePick;

public class MainActivity extends AppCompatActivity {

    private DatePick datePick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        datePick = (DatePick) findViewById(R.id.datePick);
        datePick.refresh(this);
    }
}
