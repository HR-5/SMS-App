package com.example.smsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {
    int hour, min, flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        flag = 0;
        checkPer();
        Button set = (Button) findViewById(R.id.setTime);
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTime();
            }
        });
        Button send = (Button) findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag == 0)
                    error();
                else
                    sendsms();
            }
        });
    }

    private void error() {
        Toast.makeText(this, "Set Time", Toast.LENGTH_LONG).show();
    }

    private void checkPer() {
        int permissioncheck = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);
        if (permissioncheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 0);
        }
    }

    @SuppressLint("NewApi")
    private void sendsms() {
        EditText num = (EditText) findViewById(R.id.number);
        EditText text = (EditText) findViewById(R.id.text);
        String number = num.getText().toString();
        String message = text.getText().toString();
        if (number.equals("") || message.equals(""))
            Toast.makeText(this, "Enter phone number and message", Toast.LENGTH_LONG).show();
        else {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY,hour);
            calendar.set(Calendar.MINUTE,min);
            calendar.set(Calendar.SECOND,0);
            AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(this,Reciever.class);
            intent.putExtra("num",number);
            intent.putExtra("msg",message);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, (int) System.currentTimeMillis(), intent, 0);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
            Toast.makeText(this,"Message has been set",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 0:
                if (grantResults.length >= 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    ;
                else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
        }
    }

    private void setTime() {
        DialogFragment timePicker = new TimePickerFragment();
        timePicker.show(getSupportFragmentManager(), "time picker");
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
        hour = hourOfDay;
        min = minute;
        flag = 1;
        String time = "Hour: " + hourOfDay + " Minute: " + minute;
        Toast.makeText(this, time, Toast.LENGTH_LONG).show();
    }
}
