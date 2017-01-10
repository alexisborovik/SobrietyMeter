package com.alexis.borovik.sobrietymeter;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

public class AlcoService extends IntentService {

    private static final String LOG_TAG = "alcLog" ;
    private static long intervalInSec = 5;
    private static String[] advice;
    private static Preferences pref;


    public AlcoService(String name) {
        super(name);
    }
    public AlcoService(){
        super("alco");
    }

    public void onCreate()
    {
        super.onCreate();
        Log.d(LOG_TAG, "onCreate");
        advice = getResources().getStringArray(R.array.tips);
        pref  = new Preferences(getBaseContext());
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        try {
            Thread.sleep(1000*intervalInSec);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        float promil = pref.getWeightOfAlcoholInBody();
        Log.d(LOG_TAG, "setAlc ^ "+promil +" diff:"+pref.getDiffInTimeWithLastModified());
        long periods = pref.getDiffInTimeWithLastModified() / intervalInSec;
        if(periods>0) {
            if (promil >= 0.15) {
                promil -= 0.15 * (float)periods;
            } else {
                promil = 0;
            }
            pref.setWeightOfAlcoholInBody(promil);
            pushNotification();
            startService(new Intent(getApplicationContext(), AlcoService.class));
        }
    }

    public void pushNotification()
    {
        float promil = pref.getWeightOfAlcoholInBody();
        Intent notificationIntent = new Intent(getApplicationContext(),MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(notificationIntent);
        PendingIntent pendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.drinking)
                        .setContentTitle(getString(R.string.app_name)+" ("+String.format("%.2f", promil)+"‰)")
                        .setContentText(advice[Tools.ChooseMessage(promil)])
                        .setContentIntent(pendingIntent)
                        .setProgress(400,Math.round(promil*100),false)
        ;
        if(promil==0) mBuilder.setProgress(0,0,false);
        startForeground(101010,mBuilder.build());
        Intent intent = new Intent(MainFragment.BROADCAST_ACTION);
        sendBroadcast(intent);
    }

    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "onDestroy");
    }
}
