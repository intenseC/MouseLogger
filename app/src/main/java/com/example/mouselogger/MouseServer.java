package com.example.mouselogger;
import fi.iki.elonen.NanoHTTPD;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;


public class MouseServer extends NanoHTTPD {
    private String mouseCoordinates = "";

    public MouseServer() {
        super(8888);
    }

    @Override
    public Response serve(IHTTPSession session) {
        String htmlResponse = "<html><body><h1>Mouse Coordinates:</h1><div id=\"scrollTarget\">"
        + mouseCoordinates + "</div><script>setTimeout( () => { location.reload(); }, 3000); " +
                "setTimeout( () => { window.scrollTo(0, document.body.scrollHeight); }, 2500);</script></body></html>";
        return newFixedLengthResponse(Response.Status.OK, NanoHTTPD.MIME_HTML, htmlResponse);
    }

    public void setMouseCoordinates(String coordinates) {
        if(mouseCoordinates.length() > 80000) {
            mouseCoordinates = "";
        }
        mouseCoordinates += coordinates + "\n";
    }
}
