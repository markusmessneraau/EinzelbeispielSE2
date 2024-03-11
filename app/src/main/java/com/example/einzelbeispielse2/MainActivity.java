package com.example.einzelbeispielse2;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


public class MainActivity extends AppCompatActivity {

    EditText Matrikelnummer;
    TextView textView;
    TextView ergebnis;
    private String msg1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Matrikelnummer = findViewById(R.id.MatrNum);
        textView = findViewById(R.id.textView1);
        ergebnis = findViewById(R.id.Ergebnis1);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    public void buttonNext(View view) throws InterruptedException {
        // Matrikelnummer aus dem EditText abrufen
        String matrikelnummer = Matrikelnummer.getText().toString();


        // Überprüfen, ob das Feld nicht leer ist
        if (matrikelnummer.isEmpty()) {
            Toast.makeText(this, "Feld darf nicht leer sein", Toast.LENGTH_SHORT).show();

        } else {
            // Wenn das Feld nicht leer ist, Verbindung zum Server herstellen und Daten senden
            Toast.makeText(this, Matrikelnummer.getText() + " Abgeschickt", Toast.LENGTH_SHORT).show();
            myThread thread  = new myThread(matrikelnummer);
            thread.start();
            thread.join();
            msg1 = thread.getServerMessage();
            ergebnis.setText(msg1);

        }

    }

    @SuppressLint("SetTextI18n")
    public void quersummeBerechnen(View view) {

        String zahl = Matrikelnummer.getText().toString();

        if (zahl.isEmpty()){

            Toast.makeText(this, "Feld darf nicht leer sein", Toast.LENGTH_SHORT).show();

        }else {


            int size = zahl.length();
            int sum = 0;
            boolean ungerade = true;

            if (size % 2 == 0) {

                ungerade = false;
            }

            int x = Integer.parseInt(zahl);

            while (x > 0) {
                int digit = x % 10;
                if (ungerade) {
                    sum += digit;
                } else {
                    sum -= digit;
                }
                ungerade = !ungerade;
                x = x / 10;  //-> löscht letze ziffer
            }
            ergebnis.setText(Integer.toString(sum));

        }
    }


    private DataOutputStream dos;

    public class myThread extends Thread implements Runnable {

       Socket socket;
        String msg;
        String response;

        public myThread(String msg){
            this.msg = msg;
        }


        @Override
        public void run() {

            try {

                socket = new Socket("se2-submission.aau.at", 20080); //connection
                dos = new DataOutputStream(socket.getOutputStream());

                BufferedReader dis = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                dos.writeBytes(msg + '\n');

                response = dis.readLine();



                // Verbindung schließen
                dis.close();
                dos.close();
                socket.close();


            } catch (
                    IOException ex) {
                throw new RuntimeException(ex);
            }
        }

        public String getServerMessage() {
            return this.response;
        }

    }

}






