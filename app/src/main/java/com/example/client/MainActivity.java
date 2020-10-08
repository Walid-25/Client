package com.example.client;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

@SuppressLint("SetTextI18n")
public class MainActivity extends AppCompatActivity {
    Thread Thread1 = null;
    EditText etIP, etPort;
    TextView tvMessages;
    EditText etMessage;
    Button btnSend;
    String SERVER_IP;
    int SERVER_PORT;
    VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        else {
            // Permission has already been granted
            Log.d("MAIN_ACTIVITY", "WRITE PERMISSION ALREADY GRANTED");
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.ACCESS_WIFI_STATE}, 1);

        else {
            // Permission has already been granted
            Log.d("MAIN_ACTIVITY", "WRITE PERMISSION ALREADY GRANTED");
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_MEDIA_LOCATION) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_MEDIA_LOCATION}, 1);

        else {
            // Permission has already been granted
            Log.d("MAIN_ACTIVITY", "WRITE PERMISSION ALREADY GRANTED");
        }


        setContentView(R.layout.activity_main);
        etIP = findViewById(R.id.etIP);
        etPort = findViewById(R.id.etPort);
        tvMessages = findViewById(R.id.tvMessages);
        videoView = (VideoView) findViewById(R.id.videoView);
        Button btnConnect = findViewById(R.id.btnConnect);

        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvMessages.setText("");
                SERVER_IP = etIP.getText().toString().trim();
                SERVER_PORT = Integer.parseInt(etPort.getText().toString().trim());
                new DownloadingTask().execute();
            }
        });

    }

    public class DownloadingTask extends AsyncTask<Void, Void, Void> {

        String outputFile;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getApplicationContext(), "Receiving signal .....", Toast.LENGTH_SHORT).show();
        }

        private PrintWriter output;
        private BufferedReader input;

        @Override
        protected Void doInBackground(Void... arg0) {
            int bytesRead;
            int current = 0;
            FileOutputStream fos = null;
            BufferedOutputStream bos = null;
            Socket sock = null;
            try {
                sock = new Socket(SERVER_IP, SERVER_PORT);
                System.out.println("Connecting...");

                // receive file
                byte [] mybytearray  = new byte [3114374];
                InputStream is = sock.getInputStream();
                fos = new FileOutputStream("/storage/emulated/0/Video/bigbunny.mp4");
                bos = new BufferedOutputStream(fos);
                bytesRead = is.read(mybytearray,0,mybytearray.length);
                current = bytesRead;

                do {
                    bytesRead =
                            is.read(mybytearray, current, (mybytearray.length-current));
                    if(bytesRead >= 0) current += bytesRead;
                } while(bytesRead > -1);

                bos.write(mybytearray, 0 , current);
                bos.flush();
                System.out.println("File " + "/storage/emulated/0/Video/bigbunny.mp4"
                        + " downloaded (" + current + " bytes read)");
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fos != null) fos.close();
                    if (bos != null) bos.close();
                    if (sock != null) sock.close();
                }catch (IOException e){
                    System.out.println("Error");
                }
            }

        return null;
    }
    }
}











