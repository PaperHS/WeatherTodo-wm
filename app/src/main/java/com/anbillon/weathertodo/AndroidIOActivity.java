package com.anbillon.weathertodo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class AndroidIOActivity extends AppCompatActivity {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android_io);

    }

    public void javaWrite() throws IOException {
        File file = new File("text");
        if (!file.exists()){
            file.createNewFile();
        }
        FileOutputStream f = new FileOutputStream(file);
        OutputStreamWriter writer = new OutputStreamWriter(f,"utf-8");
        writer.append("123");
        writer.append("456");
        writer.close();
        f.close();
    }

    public void javaRead() throws IOException {
        File file = new File("text");
        FileInputStream fileInputStream = new FileInputStream(file);
        InputStreamReader reader = new InputStreamReader(fileInputStream,"utf-8");
        StringBuffer stringBuffer = new StringBuffer("");
        while (reader.ready()){
            stringBuffer.append((char)reader.read());
        }
        reader.close();
        fileInputStream.close();
        Log.d("debug",stringBuffer.toString());
    }

    public void getPreference(){
        SharedPreferences settings = getSharedPreferences("Weather", MODE_PRIVATE);

    }

}
