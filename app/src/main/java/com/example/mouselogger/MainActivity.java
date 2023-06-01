package com.example.mouselogger;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import android.provider.Settings;
import android.view.MotionEvent;
import android.view.View;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.mouselogger.databinding.ActivityMainBinding;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import org.java_websocket.WebSocket;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    final int REQUEST_CODE_PERMISSION = 1001;

    private MouseServer mouseServer;
    private webSocketServer webSocketServer;

    private void UIprint(String dat) {
        TextView mouseDataTextView = findViewById(R.id.mouseDataTextView);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mouseDataTextView.setText(dat);
                    }
                });
    }

    private void getMouseMoves() {
        View rootView = findViewById(android.R.id.content);
        rootView.setOnHoverListener(new View.OnHoverListener() {
            @Override
            public boolean onHover(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_HOVER_MOVE) {
                    float x = event.getX();
                    float y = event.getY();
                    sendCoordinatesToServer(x, y);
                    String dat = ">>  X: " +  String.valueOf(x) + " >>  Y: " + String.valueOf(y);
                    webSocketServer.broadcast(dat);
                    UIprint(dat);
                    return true;
                }
                return false;
            }
        });
    }

    private void startForegroundService() {

        Intent serviceIntent = new Intent(this, MouseTrackingService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        NotificationChannel channel = new NotificationChannel("mouse_tracking_channel",
                "Mouse Tracking", NotificationManager.IMPORTANCE_DEFAULT);
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(channel);
        }
        serviceIntent.setAction(MouseTrackingService.class.getSimpleName());
        startForegroundService(serviceIntent);
        } else {
            startService(serviceIntent);
        }
    }

    private static final int REQUEST_CODE_OVERLAY_PERMISSION = 1010;
    private static  int svcFlg = 0;
    private ActivityResultLauncher<Intent> requestOverlayPermissionLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mouseServer = new MouseServer();
        try {
            mouseServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        webSocketServer = new webSocketServer(8889) {
            @Override
            public void onMessage(WebSocket conn, String message) {
                // Handle incoming messages from websocket clients
                UIprint(message);
            }
        };
        webSocketServer.start();


        if (!Settings.canDrawOverlays(this)) {

            requestOverlayPermissionLauncher = registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            svcFlg = 1;
                        } else {
                            svcFlg = 0;
                        }
                    }
            );


            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
            requestOverlayPermissionLauncher.launch(intent);
        } else {

            svcFlg = 1;
        }
        svcFlg = 0; // comment to run the service

        if (svcFlg == 1)
            startForegroundService(); // run the service
            svcFlg = 0;

        setSupportActionBar(binding.toolbar);
        getMouseMoves();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }

    private void sendCoordinatesToServer(float x, float y) {
        String coordinates = "X = " + x + ", Y = " + y;
        mouseServer.setMouseCoordinates(coordinates);
    }
}