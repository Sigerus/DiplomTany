package com.example.axelerometr;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    final String DIR_SD = "Logs";
    final String FILENAME_SD = "fileSD.txt";
    Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainscreen);
        saveButton = findViewById(R.id.button2);

       /* if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            saveButton.setEnabled(false);
        }
        else {
            Logs = new File(getExternalFilesDir(filepath), FILENAME_SD);
        }*/

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeFileSD();
                /*try {
                    FileOutputStream fos = new FileOutputStream(Logs);
                    fos.write((A).getBytes());
                    fos.close();
                    Toast.makeText(MainActivity.this,"Log created",Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }*/

            }
        });



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
        // формируем объект File, который содержит путь к файлу
        File sdFile = new File(sdPath, FILENAME_SD);
        try {
            // открываем поток для записи
            BufferedWriter bw = new BufferedWriter(new FileWriter(sdFile));
            // пишем данные
            bw.write("Содержимое файла на SD");
            // закрываем поток
            bw.close();
            //Log.d(LOG_TAG, "Файл записан на SD: " + sdFile.getAbsolutePath());
            Toast.makeText(MainActivity.this,"Файл записан на SD: ",Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* public void onMyButtonClick(View view)
    {
        //saveNote();
        try {
            String a = "test 001";
            FileOutputStream fos = new FileOutputStream(myExternalFile);
            //fos.write(().getBytes());
            fos.write(Integer.parseInt(a));
            fos.close();
            Toast.makeText(this, "zaebis", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, "Pizdec1", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "Pizdec2", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }*/

/*
    private static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    private static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }
*/
/*
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
*/


}

   /* public void saveNote() {

        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Toast.makeText(MainActivity.this,"SD-карта не доступна: " + Environment.getExternalStorageState(),Toast.LENGTH_LONG).show();
            Log.d(LOG_TAG, "SD-карта не доступна: " + Environment.getExternalStorageState());
            return;
        }
        // получаем путь к SD
        File sdPath = Environment.getExternalStorageDirectory();
        // добавляем свой каталог к пути
        sdPath = new File(sdPath.getAbsolutePath() + "/"+ DIR_SD);
        // создаем каталог
        sdPath.mkdirs();
        // формируем объект File, который содержит путь к файлу
        File sdFile = new File(sdPath, fN);
        try {
            // открываем поток для записи
            BufferedWriter bw = new BufferedWriter(new FileWriter(sdFile));
            // пишем данные
            bw.write("Содержимое файла на SD");
            // закрываем поток
            bw.close();
            Log.d(LOG_TAG, "Файл записан на SD: " + sdFile.getAbsolutePath());
            Toast.makeText(MainActivity.this,"Файл записан на SD: " + sdFile.getAbsolutePath(),Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this,"Cосём",Toast.LENGTH_LONG).show();
        }

    }*/












