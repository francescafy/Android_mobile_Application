package com.example.dimgion.parkinson;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
    }

    public void backButton(View view){
        Intent i = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(i);
    }

    public void searchButton(View view){
        try{
            InputStream is1 =  getAssets().open("demographics.txt");
            InputStreamReader isr1 = new InputStreamReader(is1);
            myRead1(isr1);

            TextView patientIdText = (TextView)findViewById(R.id.editId);
            RequestQueue queue = Volley.newRequestQueue(this);
            String url ="https://www.physionet.org/pn3/gaitpdb/JuCo"+patientIdText.getText()+"_01.txt";
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the response string.
                            Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                            InputStream is2 = new ByteArrayInputStream(response.getBytes(StandardCharsets.UTF_8));
                            InputStreamReader isr2 = new InputStreamReader(is2);
                            myRead2(isr2);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Error at HTTP call.", Toast.LENGTH_LONG).show();
                }
            });
            // Add the request to the RequestQueue.
            queue.add(stringRequest);

        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void myRead1(InputStreamReader isr){
        try {
            BufferedReader bufferedReader = new BufferedReader(isr);
            String line = new String();

            TextView patientIdText = (TextView)findViewById(R.id.editId);
            TextView nameText = (TextView)findViewById(R.id.editName);
            TextView ageText = (TextView)findViewById(R.id.editAge);
            TextView heightText = (TextView)findViewById(R.id.editHeight);
            TextView weightText = (TextView)findViewById(R.id.editWeight);
            TextView speedText = (TextView)findViewById(R.id.editSpeed);

            while((line = bufferedReader.readLine()) != null){
                String [] demographics = line.split("\t");
                if(demographics[0].compareTo("JuCo"+patientIdText.getText()) == 0){
                    nameText.setText(demographics[0]);
                    ageText.setText(demographics[1]);
                    heightText.setText(demographics[2]);
                    weightText.setText(demographics[3]);
                    speedText.setText(demographics[4]);
                }
            }
            bufferedReader.close();
        }
        catch(IOException ex) {
            Log.d("IOException", ex.getMessage());
            ex.printStackTrace();
        }
    }


    public void myRead2(InputStreamReader isr){
        try {
            int cntL =0, cntR=0;
            boolean iszeroL = false, iszeroR = false;

            BufferedReader bufferedReader = new BufferedReader(isr);
            String line = new String();
            TextView stepsText = (TextView)findViewById(R.id.editSteps);

            while((line = bufferedReader.readLine()) != null){
                String [] demographics = line.split("\t");

                float L = Float.parseFloat(demographics[2]);
                float R = Float.parseFloat(demographics[10]);
                if(L==0){
                    iszeroL = true;
                }
                if(L>50 && iszeroL == true){
                    iszeroL = false;
                    cntL++;
                }
                if(R==0){
                    iszeroR = true;
                }
                if(R>50 && iszeroR == true){
                    iszeroR = false;
                    cntR++;
                }
            }
            bufferedReader.close();
            int cnt = cntL+cntR;
            stepsText.setText(Integer.toString(cnt));
        }
        catch(IOException ex) {
            Log.d("IOException", ex.getMessage());
            ex.printStackTrace();
        }
    }
}
