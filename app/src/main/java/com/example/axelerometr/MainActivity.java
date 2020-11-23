package com.example.axelerometr;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        start_btn=findViewById(R.id.button_start);
        stop_btn=findViewById(R.id.button_stop);
     Connect();
       /* if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            saveButton.setEnabled(false);
        }
        else {
            myExternalFile = new File(getExternalFilesDir(filepath), filename);
        }
  //  Log.d(TAG, "Connect");*/

Log.d(TAG, String.valueOf(sm));

    /*    stop_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    FileOutputStream fos = new FileOutputStream(myExternalFile);
                    fos.write((json).getBytes());
                    fos.close();
                    Toast.makeText(MainActivity.this,"Log stat",Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });*/

    }
    public void Connect() {
        sm = new SocketManager();
        _ws = sm.Connect();
    }

  /*  private boolean isExternalStorageReadOnly() {
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
*/

    //  final byte[] ipAddr = new byte[]{(byte)176, (byte)15, (byte)174, (byte)137};



    

}