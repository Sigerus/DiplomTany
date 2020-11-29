package com.example.axelerometr;

import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

/**
 * Created by Игорь on 21.10.2018.
 */

public class WebSocketGolos extends WebSocketListener {

    final String TAG = "ACTIVITY_log";
    String[] txt;
    /*private Color parseColor(String Message){
        int Red = Integer.parseInt(Message.split(",")[0]);
        int Green = Integer.parseInt(Message.split(",")[1]);
        int Blue = Integer.parseInt(Message.split(",")[2]);
        return new Color(Red/255f,Green/255f,Blue/255f,1);
    }*/


    public static MyCallBack getMeasurementsCallBack;
   // private SatteliteMeasurement[];

    public interface MyCallBack{
        void callBackCall( String text );
    }


    WebSocketGolos()
    {
    }
    @Override
    public void onOpen(WebSocket webSocket, Response response) {

    }
    @Override
    public void onMessage(WebSocket webSocket, String text)
    {
        String[] Mass = text.split(":");
        String Code = Mass[0];
        String[] Message = {};
        if (Mass.length > 1) {
            Message = Mass[1].split(" ");
        }

        Log.d(TAG, text);


        getMeasurementsCallBack.callBackCall(text);
        for (int i=0;i<30;i++){

        }

    }

}
