package com.example.axelerometr;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import okhttp3.WebSocket;

public class MainActivity extends AppCompatActivity {
    public static WebSocket _ws;
    public static SocketManager sm;
    String json;
    Button start_btn;
    Button stop_btn;

    private String filename = "SampleFile.txt";
    private String filepath = "MyFileStorage";
    File myExternalFile;
    File myExternalFile2;
    String myData = "";
    private String filename2 = "SampleFile2.txt";
    private String filepath2 = "MyFileStorage";
    Button saveButton;
    final String TAG = "ACTIVITY_STATEecc";
    String A = "pizdec";
    private String text;
    private String[] text2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        start_btn = findViewById(R.id.button_start);
        stop_btn = findViewById(R.id.button_stop);

        Connect();
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            saveButton.setEnabled(false);
        } else {
            myExternalFile = new File(getExternalFilesDir(filepath), filename);
        }
       /* if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            saveButton.setEnabled(false);
        }
        else {
            myExternalFile = new File(getExternalFilesDir(filepath), filename);
        }
  //  Log.d(TAG, "Connect");*/

        Log.d(TAG, String.valueOf(_ws));
        // settext( text);

        start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // settext(text);
                try {
                   /* for (int i = 2; i < 30; i++) {
                        text2[i] = text;
                    }*/
                    FileOutputStream fos = new FileOutputStream(myExternalFile);
                    //fos.write(text).getBytes());

                    //  fos.write(text.getBytes( ));



                        fos.write((text + "\n").getBytes());
                    

                    fos.close();
                    Toast.makeText(MainActivity.this, "Log created", Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });

        WebSocketGolos.getMeasurementsCallBack = this::settext;

        stop_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text2[0]="0";
                if(text!=null){
                for (int i=0;i<30;i++){

                    text2[i]=text;
                    }
                }
            }
        });

    }



    public void settext(String text) {


        this.text = text;
        // text2[i]=text;
    }


    public void Connect() {
        sm = new SocketManager();
        _ws = sm.Connect();
    }

   private boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    private boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }


    //  final byte[] ipAddr = new byte[]{(byte)176, (byte)15, (byte)174, (byte)137};



}