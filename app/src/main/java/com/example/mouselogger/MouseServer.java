package com.example.mouselogger;
import fi.iki.elonen.NanoHTTPD;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;


public class MouseServer extends NanoHTTPD {
    private String mouseCoordinates = "";;

    public MouseServer() {
        super(8888); // port to listen on
     //   mouseCoordinates = "No coordinates received yet"; // Default value
    }

    @Override
    public Response serve(IHTTPSession session) {

        String htmlResponse = "<html><body><h1>Mouse Coordinates:</h1><p>"
        + mouseCoordinates + "</p></body></html>";
        return newFixedLengthResponse(Response.Status.OK, NanoHTTPD.MIME_HTML, htmlResponse);
    }

    public void setMouseCoordinates(String coordinates) {
        mouseCoordinates += coordinates + "\n";
    }
}
