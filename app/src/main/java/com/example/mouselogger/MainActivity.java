package com.example.mouselogger;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
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
import org.java_websocket.server.DefaultSSLWebSocketServerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

public class MainActivity extends AppCompatActivity {
    public static  final String keystore_password = "qwerty";
    public static KeyStore keyStore;
    public static InputStream keyStoreInputStream;
    public SSLContext sslContext;
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    final int REQUEST_CODE_PERMISSION = 1001;
    private MouseServer mouseServer;
    private webSocketServer webSocketServer;
    private static final int REQUEST_CODE_OVERLAY_PERMISSION = 1010;
    private static  int svcFlg = 0;
    private ActivityResultLauncher<Intent> requestOverlayPermissionLauncher;

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
                    String dat = ">>  X: " +  String.valueOf(x) + " >>  Y: " + String.valueOf(y);
                    mouseServer.setMouseCoordinates(dat);
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


    private void getCert()  {
        System.setProperty("javax.net.debug", "ssl");
        Context context = getApplicationContext();
        Resources resources = context.getResources();
        InputStream inputStream = resources.openRawResource(R.raw.websock_certificate);
//        try {
//            webSocketServer = new webSocketServer(8889) {
//                @Override
//                public void onMessage(WebSocket conn, String message) {
//                    UIprint(message);
//                }
//            };
//        } catch (IOException | CertificateException | NoSuchAlgorithmException | KeyStoreException |
//                 UnrecoverableKeyException | KeyManagementException | NoSuchProviderException e) {
//            throw new RuntimeException(e);
//        }



        try {

            keyStore = KeyStore.getInstance("PKCS12");
            keyStoreInputStream = resources.openRawResource(R.raw.websock_keystore);
            keyStore.load(keyStoreInputStream, keystore_password.toCharArray());

//        SSLContext sslContext = SSLContext.getInstance("TLS");
//        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
//        keyManagerFactory.init(keyStore, "keystore_password".toCharArray());
//        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
//        trustManagerFactory.init(keyStore);
//        sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);
//
//
//        webSocketServer.setWebSocketFactory(new DefaultSSLWebSocketServerFactory(sslContext));


            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            X509Certificate certificate = (X509Certificate) certificateFactory.generateCertificate(inputStream);
            //  test the certificate object
            System.out.println("Certificate subject: " + certificate.getSubjectDN());
            System.out.println("Certificate issuer: " + certificate.getIssuerDN());

/*
            // ...other operations with the certificate:

            // Create a KeyStore containing the certificate
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, null);
            keyStore.setCertificateEntry("my_cert", certificate);

            // Create a TrustManager that trusts the certificate in the KeyStore
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
            TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();

            // Create an SSLContext and configure it with the TrustManager
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustManagers, null);
*/


//            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);
//            webSocketServer.setWebSocketFactory(new DefaultSSLWebSocketServerFactory(sslContext));
    /////////////////////////////////////////////////////////////////////////////////////////////////////////

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    private void webServices() {


//        for (Provider provider : Security.getProviders()) {
//            System.out.println(provider.getName());
//            for (String algorithm : provider.getServices().stream().map(Provider.Service::getAlgorithm).sorted().collect(Collectors.toList())) {
//                System.out.println("- " + algorithm);
//            }
//        }


        mouseServer = new MouseServer();
        try {
        mouseServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }


        webSocketServer = new webSocketServer(8889) {
            @Override
            public void onMessage(WebSocket conn, String message) {
                UIprint(message);
            }
        };
//        getCert();
        webSocketServer.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        webServices();



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

        if (svcFlg == 1) {
            startForegroundService();
        }
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

}