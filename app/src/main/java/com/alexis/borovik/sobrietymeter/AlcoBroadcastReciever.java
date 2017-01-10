package com.alexis.borovik.sobrietymeter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Alexis on 08.01.2017.
 */

public class AlcoBroadcastReciever extends BroadcastReceiver {
    private static final String LOG_TAG = "alcLog";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(LOG_TAG, "onReceive " + intent.getAction());
        context.startService(new Intent(context, AlcoService.class));
    }
}
