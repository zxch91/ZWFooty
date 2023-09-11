package com.example.cw;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class FetchStandingsAlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent fetchStandingsIntent = new Intent(context, FetchStandingsService.class);
        context.startService(fetchStandingsIntent);
    }
}