package com.example.axelerometr;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener {

    TextView tvText;
    TextView msText;
    Button startlog;
    Button stoplog;
    SensorManager sensorManager;
    Sensor sensorAccel;
    Sensor sensorLinAccel;
    Sensor sensorGravity;
    private final Context mContext;
    final String LOG_TAG = "myLogs";
    final String FILENAME = "file.txt";
    final String DIR_SD = "MyFiles";
    final String FILENAME_SD = "fileSD1.txt";
    private static final String FILE_PREFIX = "gnss_log";
    StringBuilder sb = new StringBuilder();
    public static final String TAG = "GnssLogger";
    Timer timer;
    private BufferedWriter mFileWriter;
    private File mFile;


    private static final String COMMENT_START = "# ";
    public MainActivity(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainscreen);
        tvText = findViewById(R.id.tvText);
        msText = findViewById(R.id.zolupa);
        startlog = findViewById(R.id.startlog);
        stoplog = findViewById(R.id.stoplog);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorAccel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorLinAccel = sensorManager
                .getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        sensorGravity = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);

        startlog.setOnClickListener(this);
        stoplog.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(listener, sensorAccel,
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(listener, sensorLinAccel,
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(listener, sensorGravity,
                SensorManager.SENSOR_DELAY_NORMAL);

        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showInfo();
                    }
                });
            }
        };
        timer.schedule(task, 0, 400);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(listener);
        timer.cancel();
    }

    String format(float values[]) {
        return String.format("%1$.1f\t\t%2$.1f\t\t%3$.1f", values[0], values[1],
                values[2]);
    }

    void showInfo() {
        sb.setLength(0);
        sb.append("Accelerometer: " + format(valuesAccel))
                .append("\n\nAccel motion: " + format(valuesAccelMotion))
                .append("\nAccel gravity : " + format(valuesAccelGravity))
                .append("\n\nLin accel : " + format(valuesLinAccel))
                .append("\nGravity : " + format(valuesGravity));
        tvText.setText(sb);
    }

    float[] valuesAccel = new float[3];
    float[] valuesAccelMotion = new float[3];
    float[] valuesAccelGravity = new float[3];
    float[] valuesLinAccel = new float[3];
    float[] valuesGravity = new float[3];

    SensorEventListener listener = new SensorEventListener() {

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            switch (event.sensor.getType()) {
                case Sensor.TYPE_ACCELEROMETER:
                    for (int i = 0; i < 3; i++) {
                        valuesAccel[i] = event.values[i];
                        valuesAccelGravity[i] = (float) (0.1 * event.values[i] + 0.9 * valuesAccelGravity[i]);
                        valuesAccelMotion[i] = event.values[i]
                                - valuesAccelGravity[i];
                    }
                    break;
                case Sensor.TYPE_LINEAR_ACCELERATION:
                    for (int i = 0; i < 3; i++) {
                        valuesLinAccel[i] = event.values[i];
                    }
                    break;
                case Sensor.TYPE_GRAVITY:
                    for (int i = 0; i < 3; i++) {
                        valuesGravity[i] = event.values[i];
                    }
                    break;
            }

        }

    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.startlog:
               // msText.setText("Текстовый файл создан\nИдёт запись логов");
                writeFileSD();
            break;
            case R.id.stoplog:
                //msText.setText("Логи успешно записаны");
            break;
        }
    }
    private void logError(String errorMessage) {
        Log.e(MainActivity.TAG + TAG, errorMessage);
        Toast.makeText(mContext, errorMessage, Toast.LENGTH_LONG).show();
    }

    private void logException(String errorMessage, Exception e) {
        Log.e(MainActivity.TAG + TAG, errorMessage, e);
        Toast.makeText(mContext, errorMessage, Toast.LENGTH_LONG).show();
    }


    void writeFileSD() {
       File file;
       String state=Environment.getExternalStorageState();
       if(Environment.MEDIA_MOUNTED.equals(state)){
           file= new File(Environment.getExternalStorageDirectory(),FILE_PREFIX);
           file.mkdirs();
       }else if(Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)){
           logError(("Cannot write to external storage."));
           return;
       }else {
           logError("Cannot read external storage.");
           return;
       }

        SimpleDateFormat formatter=new SimpleDateFormat("yyy_MM_dd_HH_mm_ss");
        Date now = new Date();
        String fileName = String.format("%s_%s.txt", FILE_PREFIX, formatter.format(now));
        File currentFile = new File(file, fileName);
        String currentFilePath = currentFile.getAbsolutePath();
        BufferedWriter currentFileWriter;
        try {
            currentFileWriter = new BufferedWriter(new FileWriter(currentFile));
        } catch (IOException e) {
            logException("Could not open file: " + currentFilePath, e);
            return;
        }
        try{
            currentFileWriter.write(COMMENT_START);
            String fileVersion =
                    mContext.getString(R.string.app_version)
                            + " Platform: "
                            + Build.VERSION.RELEASE
                            + " "
                            + "accel: "
                            + format(valuesAccel)
                            + " "
                            + "gravity: "
                            + format(valuesGravity);
            currentFileWriter.write(fileVersion);
            currentFileWriter.newLine();
        }
        catch (IOException e) {
            logException("Count not initialize file: " + currentFilePath, e);
            return;
        }
        if (mFileWriter != null) {
            try {
                mFileWriter.close();
            } catch (IOException e) {
                logException("Unable to close all file streams.", e);
                return;
            }
        }


    }
}