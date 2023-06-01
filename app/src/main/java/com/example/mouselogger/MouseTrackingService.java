package com.example.mouselogger;

import static androidx.core.app.ActivityCompat.startActivityForResult;

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
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import androidx.core.app.NotificationCompat;
import android.util.Log;
import java.io.IOException;
//  import  com.example.mouselogger.MouseServer;
//  import fi.iki.elonen.NanoHTTPD;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
//===========================================================================
public class MouseTrackingService extends Service {
    private static final int NOTIFICATION_ID = 1;
    private WindowManager windowManager;
    private View rootView;
    private MouseServer mouseServer;

    private ActivityResultLauncher<Intent> activityResultLauncher;

//    final int REQUEST_CODE_OVERLAY_PERMISSION = 107;

    private void addWindow() {
        windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        rootView = LayoutInflater.from(this).inflate(R.layout.mouse_tracking_layout, null);

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
//                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );

 //       windowManager.addView(rootView, params);
    }

    private void chkWindow() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        addWindow();
        startMouseTracking();
        mouseServer = new MouseServer();
        Log.i("MM>>>>>>>>>>MM", "MouseServer");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Create and display a notification for the foreground service
        Notification notification = createNotification();
        startForeground(NOTIFICATION_ID, notification);
       // Log.i("MM>>>>>>>>>>MM", "openBrowser");
        try {
        mouseServer.start();
        openBrowser("http://127.0.0.1:8888/coords");
            Log.i("MM>>>>>>>>>>MM", "openBrowser");
        } catch (IOException e) {
            Log.i("MM>>>>>>>>>>MM", String.valueOf(e));
            e.printStackTrace();
        }
        return START_STICKY;
    }
    private Notification createNotification() {
        // Create a notification channel for Android Oreo and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("ForegroundServiceChannel",
                    "Foreground Service Channel", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        // Create the notification builder
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "ForegroundServiceChannel")
                .setContentTitle("Mouse Tracking Service")
                .setContentText("Tracking mouse events")
                //.setSmallIcon(R.drawable.notification_icon)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // Create the pending intent for when the notification is clicked
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        // Set the pending intent to the notification
        builder.setContentIntent(pendingIntent);

        // Build and return the notification
        return builder.build();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopMouseTracking();
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
                    // Handle the mouse cursor coordinates (x, y) here
                    // You can perform any necessary actions based on the cursor position
                    return true; // Return true to indicate that the event is consumed
                }
                return false; // Return false if the event is not consumed
            }
        });
    }

    private void stopMouseTracking() {
        // Clean up any resources related to mouse tracking
    }

    private void sendMouseCoordinates(float x, float y) {
        OkHttpClient client = new OkHttpClient();

        // Build the request body with the mouse coordinates
        FormBody.Builder formBuilder = new FormBody.Builder()
                .add("x", String.valueOf(x))
                .add("y", String.valueOf(y));
        RequestBody requestBody = formBuilder.build();

        // Build the HTTP POST request
        Request request = new Request.Builder()
                .url("http://127.0.0.1:8888/coords")
                .post(requestBody)
                .build();

        // Send the HTTP request asynchronously
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Handle request failure
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // Handle request success
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    // Process the response if needed
                }
            }
        });
    }
    private void openBrowser(String url) {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Add this line to ensure the browser opens in a new task
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

//===========================================================================

