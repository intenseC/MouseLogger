package com.example.mouselogger;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import androidx.core.app.NotificationCompat;
import android.util.Log;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import androidx.activity.result.ActivityResultLauncher;


public class MouseTrackingService extends Service {
    private static final int NOTIFICATION_ID = 1;
    private WindowManager windowManager;
    private View rootView;
    private MouseServer mouseServer;

    private ActivityResultLauncher<Intent> activityResultLauncher;



    private void addWindow() {
        windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        rootView = LayoutInflater.from(this).inflate(R.layout.mouse_tracking_layout, null);

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );


    }

    @Override
    public void onCreate() {
        super.onCreate();
        addWindow();
        startMouseTracking();
        mouseServer = new MouseServer();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Notification notification = createNotification();
        startForeground(NOTIFICATION_ID, notification);

        try {
        mouseServer.start();
        openBrowser("http://127.0.0.1:8888/coords");
        } catch (IOException e) {
            Log.i("MM>>>>>>>>>>MM", String.valueOf(e));
            e.printStackTrace();
        }
        return START_STICKY;
    }
    private Notification createNotification() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("ForegroundServiceChannel",
                    "Foreground Service Channel", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "ForegroundServiceChannel")
                .setContentTitle("Mouse Tracking Service")
                .setContentText("Tracking mouse events")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);


        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        builder.setContentIntent(pendingIntent);
        return builder.build();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mouseServer.stop();
        if (windowManager != null && rootView != null) {
            windowManager.removeView(rootView);
        }
    }

    private void startMouseTracking() {
        rootView.setOnHoverListener(new View.OnHoverListener() {
            @Override
            public boolean onHover(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_HOVER_MOVE) {
                    float x = event.getX();
                    float y = event.getY();
                    sendMouseCoordinates(x, y);
                    return true;
                }
                return false;
            }
        });
    }

    private void sendMouseCoordinates(float x, float y) {
        OkHttpClient client = new OkHttpClient();

        FormBody.Builder formBuilder = new FormBody.Builder()
                .add("x", String.valueOf(x))
                .add("y", String.valueOf(y));
        RequestBody requestBody = formBuilder.build();

        Request request = new Request.Builder()
                .url("http://127.0.0.1:8888/coords")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                }
            }
        });
    }
    private void openBrowser(String url) {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public class LocalBinder extends Binder {
        public MouseTrackingService getService() {
            return MouseTrackingService.this;
        }
    }

    private final IBinder binder = new LocalBinder();
}
