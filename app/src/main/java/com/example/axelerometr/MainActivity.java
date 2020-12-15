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
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import okhttp3.WebSocket;

public class MainActivity extends Activity implements View.OnClickListener {
    public static WebSocket _ws;
    public static SocketManager sm;
    final String TAG = "ACTIVITY_STATEecc";
    //TextView tvText;
    TextView msText;
    Button startlog;
    Button stoplog;
    SensorManager sensorManager;
    Sensor sensorAccel;
    Sensor sensorLinAccel;
    Sensor sensorGravity;
    Sensor sensorMagnetic_field;
    private Sensor magnetometer;
    //private SensorManager mSensorManager;
    private Sensor sensorGyroscopeEirler;
    private Sensor sensorGyroscope;
    private ImageView iv;
    private TextView tv;
    private TextView speed;
    private RadioGroup RBspeed;
    private boolean choose = false;



    int period = 400;
    private BufferedWriter mFileWriter;
    private final Object mFileLock = new Object();
    final String DIR_SD = "Logs";


    StringBuilder sb = new StringBuilder();

    Timer timer;
    Timer timer1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainscreen);
        //tvText = findViewById(R.id.tvText);

        startlog = findViewById(R.id.startlog);
        stoplog = findViewById(R.id.stoplog);


        RBspeed = findViewById(R.id.RGspeed);


        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorAccel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorGyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sensorLinAccel = sensorManager
                .getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        sensorGyroscopeEirler = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        sensorGravity = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        sensorMagnetic_field = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        magnetometer = sensorManager
                .getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        if (null == magnetometer)
            finish();

        startlog.setOnClickListener(this);
        stoplog.setOnClickListener(this);
        Log.d(TAG, String.valueOf(_ws));
        WebSocketGolos.getMeasurementsCallBack = this::settext;
        Connect();

    }

    public void settext(String text) {


        // this.text = text;
        synchronized (mFileLock) {
            String serverLog = String.format("%s%s%s", "server ", SystemClock.elapsedRealtimeNanos() + " ", text );
            try {
                if (mFileWriter == null) {
                    return;
                }
                mFileWriter.write(serverLog);
                mFileWriter.newLine();

            } catch (IOException e) {
                Log.e("pizda", String.valueOf(e));
            }
        }

        // text2[i]=text;
    }

    public void Connect() {
        sm = new SocketManager();
        _ws = sm.Connect();
    }

    @Override
    protected void onResume() {
        super.onResume();

        RBspeed.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case -1:
                    Toast.makeText(getApplicationContext(), "Ничего не выбрано",
                            Toast.LENGTH_SHORT).show();
                    sensorManager.registerListener(listener, sensorAccel,
                            SensorManager.SENSOR_DELAY_NORMAL);
                    sensorManager.registerListener(listener, sensorLinAccel,
                            SensorManager.SENSOR_DELAY_NORMAL);
                    sensorManager.registerListener(listener, sensorGravity,
                            SensorManager.SENSOR_DELAY_NORMAL);
                    sensorManager.registerListener(listener, sensorMagnetic_field,
                            SensorManager.SENSOR_DELAY_NORMAL);
                    sensorManager.registerListener(listener, magnetometer,
                            SensorManager.SENSOR_DELAY_NORMAL);
                    sensorManager.registerListener(listener, sensorGyroscopeEirler,
                            SensorManager.SENSOR_DELAY_NORMAL);
                    sensorManager.registerListener(listener, sensorGyroscope,
                            SensorManager.SENSOR_DELAY_NORMAL);

                    break;
                case R.id.RBslow:
                    choose = true;
                    Toast.makeText(getApplicationContext(), "slow",
                            Toast.LENGTH_SHORT).show();
                    sensorManager.registerListener(listener, sensorAccel,
                            SensorManager.SENSOR_DELAY_NORMAL);
                    sensorManager.registerListener(listener, sensorLinAccel,
                            SensorManager.SENSOR_DELAY_NORMAL);
                    sensorManager.registerListener(listener, sensorGravity,
                            SensorManager.SENSOR_DELAY_NORMAL);
                    sensorManager.registerListener(listener, sensorMagnetic_field,
                            SensorManager.SENSOR_DELAY_NORMAL);
                    sensorManager.registerListener(listener, magnetometer,
                            SensorManager.SENSOR_DELAY_NORMAL);
                    sensorManager.registerListener(listener, sensorGyroscopeEirler,
                            SensorManager.SENSOR_DELAY_NORMAL);
                    sensorManager.registerListener(listener, sensorGyroscope,
                            SensorManager.SENSOR_DELAY_NORMAL);
                    break;
                case R.id.RBnorm:
                    choose = true;
                    Toast.makeText(getApplicationContext(), "norm",
                            Toast.LENGTH_SHORT).show();
                    sensorManager.registerListener(listener, sensorAccel,
                            SensorManager.SENSOR_DELAY_UI);
                    sensorManager.registerListener(listener, sensorLinAccel,
                            SensorManager.SENSOR_DELAY_UI);
                    sensorManager.registerListener(listener, sensorGravity,
                            SensorManager.SENSOR_DELAY_UI);
                    sensorManager.registerListener(listener, sensorMagnetic_field,
                            SensorManager.SENSOR_DELAY_UI);
                    sensorManager.registerListener(listener, magnetometer,
                            SensorManager.SENSOR_DELAY_UI);
                    sensorManager.registerListener(listener, sensorGyroscopeEirler,
                            SensorManager.SENSOR_DELAY_UI);
                    sensorManager.registerListener(listener, sensorGyroscope,
                            SensorManager.SENSOR_DELAY_UI);
                    break;
                case R.id.RBfast:
                    choose = true;
                    Toast.makeText(getApplicationContext(), "fast",
                            Toast.LENGTH_SHORT).show();
                    sensorManager.registerListener(listener, sensorAccel,
                            SensorManager.SENSOR_DELAY_GAME);
                    sensorManager.registerListener(listener, sensorLinAccel,
                            SensorManager.SENSOR_DELAY_GAME);
                    sensorManager.registerListener(listener, sensorGravity,
                            SensorManager.SENSOR_DELAY_GAME);
                    sensorManager.registerListener(listener, sensorMagnetic_field,
                            SensorManager.SENSOR_DELAY_GAME);
                    sensorManager.registerListener(listener, magnetometer,
                            SensorManager.SENSOR_DELAY_GAME);
                    sensorManager.registerListener(listener, sensorGyroscopeEirler,
                            SensorManager.SENSOR_DELAY_GAME);
                    sensorManager.registerListener(listener, sensorGyroscope,
                            SensorManager.SENSOR_DELAY_GAME);
                    break;
                case R.id.RBultra:
                    choose = true;
                    Toast.makeText(getApplicationContext(), "ultra",
                            Toast.LENGTH_SHORT).show();
                    sensorManager.registerListener(listener, sensorAccel,
                            SensorManager.SENSOR_DELAY_FASTEST);
                    sensorManager.registerListener(listener, sensorLinAccel,
                            SensorManager.SENSOR_DELAY_FASTEST);
                    sensorManager.registerListener(listener, sensorGravity,
                            SensorManager.SENSOR_DELAY_FASTEST);
                    sensorManager.registerListener(listener, sensorMagnetic_field,
                            SensorManager.SENSOR_DELAY_FASTEST);
                    sensorManager.registerListener(listener, magnetometer,
                            SensorManager.SENSOR_DELAY_FASTEST);
                    sensorManager.registerListener(listener, sensorGyroscopeEirler,
                            SensorManager.SENSOR_DELAY_FASTEST);
                    sensorManager.registerListener(listener, sensorGyroscope,
                            SensorManager.SENSOR_DELAY_FASTEST);
                    break;

                default:
                    break;
            }
        });



       /* timer = new Timer();
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
        timer.schedule(task, 0, period);*/


    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(listener);
        timer.cancel();
    }

    /*String format(float values[]) {
        return String.format("%1$.1f\t\t%2$.1f\t\t%3$.1f", values[0], values[1],
                values[2]);
    }*/



   /* String Format(String Type, float values[])
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
    }*/

    float[] valuesAccel = new float[3];
    float[] valuesAccelMotion = new float[3];
    float[] valuesAccelGravity = new float[3];
    float[] valuesLinAccel = new float[3];
    float[] valuesGravity = new float[3];
    private final float[] mGeomagnetic = new float[3];

    SensorEventListener listener = new SensorEventListener() {

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            switch (event.sensor.getType()) {
                case Sensor.TYPE_ACCELEROMETER:
                    synchronized (mFileLock) {
                        String accelStream = String.format("%s%s%s%s%s", "ACC ", SystemClock.elapsedRealtimeNanos() + " ", event.values[0] + " ", event.values[1] + " ", event.values[2]);
                        try {
                            if (mFileWriter == null) {
                                return;
                            }
                            mFileWriter.write(accelStream);
                            mFileWriter.newLine();

                        } catch (IOException e) {
                            Log.e("pizda", String.valueOf(e));
                        }
                    }

                    /*for (int i = 0; i < 3; i++) {
                        valuesAccel[i] = event.values[i];
                        valuesAccelGravity[i] = (float) (0.1 * event.values[i] + 0.9 * valuesAccelGravity[i]);
                        valuesAccelMotion[i] = event.values[i]
                                - valuesAccelGravity[i];
                    }*/
                    break;
                case Sensor.TYPE_GYROSCOPE:
                    synchronized (mFileLock) {
                        String GYRStream = String.format("%s%s%s%s%s", "GYR ", SystemClock.elapsedRealtimeNanos() + " ", event.values[0] + " ", event.values[1] + " ", event.values[2]);
                        try {
                            if (mFileWriter == null) {
                                return;
                            }
                            mFileWriter.write(GYRStream);
                            mFileWriter.newLine();
                        } catch (IOException e) {
                            Log.e("pizda", String.valueOf(e));
                        }
                    }
                    break;
                case Sensor.TYPE_GRAVITY:
//                    for (int i = 0; i < 3; i++) {
//                        valuesGravity[i] = event.values[i];
//                    }
                    break;
                case Sensor.TYPE_MAGNETIC_FIELD:
                    synchronized (mFileLock) {
                        String magneticStream = String.format("%s%s%s%s%s", "MAG ", SystemClock.elapsedRealtimeNanos() + " ", event.values[0] + " ", event.values[1] + " ", event.values[2]);
                        try {
                            if (mFileWriter == null) {
                                return;
                            }
                            mFileWriter.write(magneticStream);
                            mFileWriter.newLine();

                        } catch (IOException e) {
                            Log.e("pizda", String.valueOf(e));
                        }
                    }
                    break;
                case Sensor.TYPE_ROTATION_VECTOR:
                    synchronized (mFileLock) {
                        float[] Quat = new float[4];
                        float[] RotVect = new float[] {event.values[0],event.values[1],event.values[2],event.values[3]};
                        float[] RotMatr = new float[16];
                        float[] UelerAngles = new float[3];

                        SensorManager.getQuaternionFromVector(Quat,RotVect);
                        SensorManager.getRotationMatrixFromVector(RotMatr,RotVect);
                        SensorManager.getOrientation(RotMatr,UelerAngles);

                        /*float[] rotationMatrix = new float[16];
                        SensorManager.getRotationMatrixFromVector(
                                rotationMatrix, event.values);
                        float[] remappedRotationMatrix = new float[16];
                        SensorManager.remapCoordinateSystem(rotationMatrix,
                                SensorManager.AXIS_X,
                                SensorManager.AXIS_Z,
                                remappedRotationMatrix);
                        float[] orientations = new float[3];
                        SensorManager.getOrientation(remappedRotationMatrix, orientations);
                        for(int i = 0; i < 3; i++) {
                            orientations[i] = (float)(Math.toDegrees(orientations[i]));
                        }
                        tv.setText(String.valueOf((int)orientations[2]));
                        iv.setRotation(-orientations[2]);*/


                        /*String RotStream = String.format("%s%s%s%s%s", "EYR ", SystemClock.elapsedRealtimeNanos() + " ", orientations[0] + " ", orientations[1] + " ", orientations[2]);*/
                        String RotStream = String.format("%s%s%s%s%s", "EYR ", SystemClock.elapsedRealtimeNanos() + " ", UelerAngles[0] + " ", UelerAngles[1] + " ", UelerAngles[2]);
                        try {
                            if (mFileWriter == null) {
                                return;
                            }
                            mFileWriter.write(RotStream);
                            mFileWriter.newLine();

                        } catch (IOException e) {
                            Log.e("pizda", String.valueOf(e));
                        }
                    }
                    break;
            }

        }

    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.startlog:
                if(choose)
                {writeFileSD();}
                else
                {Toast.makeText(MainActivity.this,"choose speed",Toast.LENGTH_LONG).show();}
            break;
            case R.id.stoplog:
                Toast.makeText(MainActivity.this,"Логи записаны ",Toast.LENGTH_LONG).show();
                if(choose)
                {
                    onStop();
                    Toast.makeText(MainActivity.this,"Логи записаны ",Toast.LENGTH_LONG).show();
                }
                else
                {Toast.makeText(MainActivity.this,"choose speed",Toast.LENGTH_LONG).show();}
                //msText.setText("Логи успешно записаны");
            break;
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (mFileWriter != null) {
            try {
                mFileWriter.flush();
                mFileWriter.close();
                mFileWriter = null;
            } catch (IOException e) {
                //logException("Unable to close all file streams.", e);
                return;
            }
        }
    }



    void writeFileSD() {
        synchronized (mFileLock) {
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
           //Toast.makeText(MainActivity.this, "Папка создана", Toast.LENGTH_LONG).show();
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
                Toast.makeText(MainActivity.this, "Производится запись логов", Toast.LENGTH_LONG).show();

                //for(int i = 0; i < 5; i++) {
             /*   for(int i = 0; i < 1000; i++) {
                    bw.write(Format("ACL", valuesAccel));
                    bw.write(Format("ACM", valuesAccelMotion));
                    bw.write(Format("ACG", valuesAccelGravity));
                }*/
                // }
                // закрываем поток
                //bw.write("ты еблан\n");
                //bw.close();
                //Toast.makeText(MainActivity.this,"Прошёл мимо ",Toast.LENGTH_LONG).show();
                //Log.d(LOG_TAG, "Файл записан на SD: " + sdFile.getAbsolutePath());
                if (mFileWriter != null) {
                    try {
                        mFileWriter.close();
                    } catch (IOException e) {
                        // logException("Unable to close all file streams.", e);
                        return;
                    }
                }
                mFileWriter = bw;
            } catch (IOException e) {
                e.printStackTrace();
            }
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