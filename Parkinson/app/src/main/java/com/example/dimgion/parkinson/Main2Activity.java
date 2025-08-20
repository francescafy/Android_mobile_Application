package com.example.dimgion.parkinson;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Main2Activity extends AppCompatActivity {

    AppDatabase db;
    public static LineGraphSeries<DataPoint> series;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        //Initialize DB
        db = AppDatabase.getAppDatabase(this);
        //Clean the table
        db.DemographicsDao().nukeTable();
    }

    public void backButton(View view){
        Intent i = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(i);
    }

    public void plotButton(View view){
        Intent i = new Intent(getApplicationContext(),Main3Activity.class);
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

            EditText from = (EditText) findViewById(R.id.editFrom);
            EditText to = (EditText)findViewById(R.id.editTo);
            EditText steps = (EditText)findViewById(R.id.editSteps);

            BufferedReader bufferedReader = new BufferedReader(isr);
            String line = new String();

            while((line = bufferedReader.readLine()) != null){
                String [] demographics = line.split("\t");
                double T = Double.parseDouble(demographics[0]);
                double L = Double.parseDouble(demographics[1]);
                double R = Double.parseDouble(demographics[9]);
                insertUserData(db, T, L, R);
            }
            bufferedReader.close();

            List<Demographics> list = db.DemographicsDao().getAll();

            String t1 = from.getText().toString();
            double timeFrom = Double.valueOf(t1).doubleValue();
            String t2 = to.getText().toString();
            double timeTo = Double.valueOf(t2).doubleValue();

            series = new LineGraphSeries<DataPoint>();

            for(int i=0; i<list.size(); i++){
                double time = list.get(i).getTime();
                if((time>=timeFrom) && (time<= timeTo)) {
                    double L = list.get(i).getLeftFoot();
                    double R = list.get(i).getRightFoot();

                    if(L==0){
                        iszeroL = true;
                    }
                    if(L>50 && iszeroL == true){
                        iszeroL = false;
                        cntL++;
                        series.appendData(new DataPoint(time, cntL+cntR), true, cntL+cntR);
                    }
                    if(R==0){
                        iszeroR = true;
                    }
                    if(R>50 && iszeroR == true){
                        iszeroR = false;
                        cntR++;
                        series.appendData(new DataPoint(time, cntR+cntL), true, cntL+cntR);
                    }
                }
            }
            int cnt = cntL+cntR;
            steps.setText(Integer.toString(cnt));
            db.DemographicsDao().nukeTable();
        }
        catch(IOException ex) {
            Log.d("IOException", ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void insertUserData(AppDatabase db, double time, double left, double right) {
        Demographics user = new Demographics();
        user.setTime(time);
        user.setRightFoot(right);
        user.setLeftFoot(left);
        db.DemographicsDao().insertAll(user);
    }
}
