package com.example.axelerometr;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;

public class SocketManager {
    final String TAG = "ACTIVITY_log";
    //  final byte[] ipAddr = new byte[]{(byte)176, (byte)15, (byte)174, (byte)137};
    final int port = 5050;

    public WebSocket Connect()
    {
        OkHttpClient client = new OkHttpClient.Builder().build();
        Request request = new Request.Builder()
                .url("ws://192.168.43.51:5050/web")
                .build();

        WebSocketGolos wsc = new WebSocketGolos();
        WebSocket ws = client.newWebSocket(request, wsc);
        //ws.send("State:" + "Client:"  + Build.MANUFACTURER + " " +  Build.MODEL);
        return ws;
       // Log.d(TAG,wsc);
    }

}
