package com.example.smsapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.widget.Toast;

public class Reciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String number = intent.getStringExtra("num");
        String message = intent.getStringExtra("msg");
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(number, null, message, null, null);
        Toast.makeText(context, "Message Sent", Toast.LENGTH_SHORT).show();
    }
}
