package com.example.amar.smartalarm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class NewAlarmActivity extends AppCompatActivity {

    private Button btnStartAlarm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_alarm);
        btnStartAlarm = findViewById(R.id.btnStartAlarm);

        btnStartAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewAlarmActivity.this, AlarmActivity.class));
            }
        });


    }
}
