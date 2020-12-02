package com.example.axelerometr;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.WebSocket;

public class MainActivity extends AppCompatActivity {
    public static WebSocket _ws;
    public static SocketManager sm;
    String json;
    Button start_btn;
    Button stop_btn;

    private String filename = "SampleFile.txt";
    private String filepath = "MyFileStorage";
    private String DIR_SD = "Logs";
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
    Timer timer;

    boolean flag=false;
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
                flag=true;
                writeFileSD();
                Toast.makeText(MainActivity.this, "Log Start", Toast.LENGTH_LONG).show();
                // settext(text);


            }
        });

        WebSocketGolos.getMeasurementsCallBack = this::settext;

        stop_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag=false;
                Toast.makeText(MainActivity.this, "Log Stop", Toast.LENGTH_LONG).show();
            }
        });
      /*  try {


            FileOutputStream fos = new FileOutputStream(myExternalFile);
            //fos.write(text).getBytes());

            //  fos.write(text.getBytes( ));
            timer = new Timer();
            if(flag=true) {
              ////  if (text != null) {
                    TimerTask task = new TimerTask() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        if (text != null) {
                                            fos.write((text+ "\n").getBytes());
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    };
                    timer.schedule(task, 0, 400);

            //    }
            }
            if(flag=false) {
                fos.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    void writeFileSD() {
        // проверяем доступность SD
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            //Log.d(LOG_TAG, "SD-карта не доступна: " + Environment.getExternalStorageState());
            Toast.makeText(MainActivity.this, "SD-карта не доступна: ", Toast.LENGTH_LONG).show();
            return;
        }
        // получаем путь к SD
        File sdPath = Environment.getExternalStorageDirectory();
        // добавляем свой каталог к пути
        sdPath = new File(sdPath.getAbsolutePath() + "/" + DIR_SD);
        // создаем каталог
        sdPath.mkdirs();
        Toast.makeText(MainActivity.this, "Папка создана", Toast.LENGTH_LONG).show();
        // формируем объект File, который содержит путь к файлу
        Date currentDate = new Date();
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String timeText = timeFormat.format(currentDate);
        String FILENAME_SD = timeText + ".txt";
        File sdFile = new File(sdPath, FILENAME_SD);
        try {
            // открываем поток для записи
            final BufferedWriter bw = new BufferedWriter(new FileWriter(sdFile));
            // пишем данные
            Toast.makeText(MainActivity.this, "шапка", Toast.LENGTH_LONG).show();
            for (int i = 0; i < 100; i++)
                bw.write(String.valueOf((text + "\n").getBytes()));
            bw.close();
            //Toast.makeText(MainActivity.this,"Прошёл мимо ",Toast.LENGTH_LONG).show();
            //Log.d(LOG_TAG, "Файл записан на SD: " + sdFile.getAbsolutePath());

        } catch (IOException e) {
            e.printStackTrace();
        }

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