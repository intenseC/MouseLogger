package com.example.mouselogger;

import static com.example.mouselogger.MainActivity.keyStore;
//import static com.example.mouselogger.MainActivity.keyStoreInputStream;
import static com.example.mouselogger.MainActivity.keystore_password;

import org.bouncycastle.crypto.PBEParametersGenerator;
import org.bouncycastle.crypto.generators.PKCS5S2ParametersGenerator;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;


import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.DefaultSSLWebSocketServerFactory;
import org.java_websocket.server.WebSocketServer;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Arrays;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

public class webSocketServer extends WebSocketServer {


//    public webSocketServer(int port) throws
//            IOException, CertificateException, NoSuchAlgorithmException,
//            KeyStoreException, UnrecoverableKeyException, KeyManagementException, NoSuchProviderException {
//        super(new InetSocketAddress(port));
//
//
//        SSLContext sslContext = SSLContext.getInstance("TLS");
//        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
//        keyManagerFactory.init(keyStore, "keystore_password".toCharArray());
//        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
//        trustManagerFactory.init(keyStore);
//
//
//       sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);
//      setWebSocketFactory(new DefaultSSLWebSocketServerFactory(sslContext));
//    }

    public webSocketServer(int port) {
        super(new InetSocketAddress(port));
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        System.out.println("New connection opened: " + conn.getRemoteSocketAddress());
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        System.out.println("Connection closed: " + conn.getRemoteSocketAddress());
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        System.out.println("Received message: " + message);
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        System.err.println("An error occurred: ");
        ex.printStackTrace();
    }

    @Override
    public void onStart() {
        System.out.println("Websocket server started");
    }
}
