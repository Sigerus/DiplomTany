package com.example.axelerometr;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
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
    Sensor sensorMagnetic_field;

    boolean stop = false;
    int period = 1000;


    final String DIR_SD = "Logs";


    StringBuilder sb = new StringBuilder();

    Timer timer;
    Timer timer1;

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
        sensorMagnetic_field = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

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
        sensorManager.registerListener(listener, sensorMagnetic_field,
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
        timer.schedule(task, 0, period);


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



    String Format(String Type, float values[])
    {
        Date currentDate = new Date();
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String timeText = timeFormat.format(currentDate);
        long nanos = TimeUnit.MILLISECONDS.toNanos(System.currentTimeMillis());
        String time = String.valueOf(nanos);

        return String.format("%1$.3s\t\t%2$.15s\t\t%3$.1f\t\t%4$.1f\t\t%5$.1f\n",Type, time, values[0], values[1],
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
                stop = false;
            break;
            case R.id.periodup:
                period += 100;
                break;
            case R.id.perioddown:
                period -= 100;
                break;
            case R.id.stoplog:
                stop = true;
                Toast.makeText(MainActivity.this,"Файл записан на SD: ",Toast.LENGTH_LONG).show();
                //msText.setText("Логи успешно записаны");
            break;
        }
    }




    void writeFileSD() {
        // проверяем доступность SD
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            //Log.d(LOG_TAG, "SD-карта не доступна: " + Environment.getExternalStorageState());
            Toast.makeText(MainActivity.this,"SD-карта не доступна: ",Toast.LENGTH_LONG).show();
            return;
        }
        // получаем путь к SD
        File sdPath = Environment.getExternalStorageDirectory();
        // добавляем свой каталог к пути
        sdPath = new File(sdPath.getAbsolutePath() + "/" + DIR_SD);
        // создаем каталог
        sdPath.mkdirs();
        Toast.makeText(MainActivity.this,"Папка создана",Toast.LENGTH_LONG).show();
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
            bw.write("# Type,ElapsedRealtimeNanos,xAcceleration,yAcceleration,zAcceleration\n");
            Toast.makeText(MainActivity.this, "шапка", Toast.LENGTH_LONG).show();

            //for(int i = 0; i < 5; i++) {
                for(int i = 0; i < 1000; i++) {
                    bw.write(Format("ACL", valuesAccel));
                    bw.write(Format("ACM", valuesAccelMotion));
                    bw.write(Format("ACG", valuesAccelGravity));
                }
           // }
            // закрываем поток
            //bw.write("ты еблан\n");
            bw.close();
            Toast.makeText(MainActivity.this,"Файл записан на SD: ",Toast.LENGTH_LONG).show();
            //Toast.makeText(MainActivity.this,"Прошёл мимо ",Toast.LENGTH_LONG).show();
            //Log.d(LOG_TAG, "Файл записан на SD: " + sdFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

/*
try {
final FileOutputStream fileOutput = openFileOutput("Zolupa",MODE_PRIVATE);
        timerlog = new Timer();
        TimerTask tasklog = new TimerTask() {
@Override
public void run() {
        runOnUiThread(new Runnable() {
@Override
public void run() {
        try {
        fileOutput.write(format(valuesAccel).getBytes());
        } catch (IOException e) {
        e.printStackTrace();
        }
        }
        });
        }
        };
        timer.schedule(tasklog, 0, 400);
        // for (int i = 0; i < 5; i++)
        //    fileOutput.write(Text.getBytes());
        if(stopbool)
        fileOutput.close();*/