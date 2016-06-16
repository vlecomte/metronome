package vlecomte.metronome;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private final static int[] tempos = {40, 42, 44, 46, 50, 52, 54, 56, 58, 60, 63, 66, 69, 72, 76,
            80, 84, 88, 92, 96, 100, 104, 108, 112, 116, 120, 126, 132, 138, 144, 152, 160, 168,
            176, 184, 192, 200, 208};

    public static class MyIntentService extends IntentService {
        public MyIntentService() {
            super("Click counter");
        }

        @Override
        protected void onHandleIntent(Intent intent) {
            Log.d("Metronome", "I was clicked!");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Locale locale = getResources().getConfiguration().locale;

        final TextView textTempo = (TextView) findViewById(R.id.textTempo);
        SeekBar barTempo = (SeekBar) findViewById(R.id.barTempo);
        assert textTempo != null;
        assert barTempo != null;
        barTempo.setMax(tempos.length - 1);
        barTempo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textTempo.setText(String.format(locale, "%d", tempos[progress]));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        barTempo.setProgress(tempos.length / 2);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void startNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setOngoing(true)
                .setSmallIcon(android.R.drawable.ic_dialog_alert)
                .setContentTitle("My notification")
                .setContentText("Hello World!");

        Intent counter = new Intent(this, MyIntentService.class);
        counter.setAction("count");
        counter.putExtra("foo", "this worked!");
        PendingIntent counterPending = PendingIntent.getService(this, 0, counter, PendingIntent.FLAG_CANCEL_CURRENT);

        RemoteViews view = new RemoteViews(getPackageName(), R.layout.notification);
        view.setOnClickPendingIntent(R.id.notificationButton, counterPending);
        builder.setContent(view);

        // Asks for unlock code
        //builder.setContentIntent(counterPending);

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, builder.build());
    }
}
