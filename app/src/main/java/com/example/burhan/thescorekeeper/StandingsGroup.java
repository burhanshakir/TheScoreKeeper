package com.example.burhan.thescorekeeper;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class StandingsGroup extends AppCompatActivity
{
    String id,groups,selectedItemText;
    String[] spinnerArray;
    TableLayout t1;
    TextView ran;
    Spinner spinner;
    int count,flag = 0,i,noOfG;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standings_group);

        t1 =(TableLayout) findViewById(R.id.tableLayout1);
        ran =(TextView) findViewById(R.id.tvrandom);
        spinner = (Spinner) findViewById(R.id.spinnerGroup);

        id = getIntent().getStringExtra("TId");
        groups = getIntent().getStringExtra("Groups");

        noOfG = Integer.parseInt(groups);

        spinnerArray = new String[noOfG];

        for(i= 0;i<noOfG;i++)
        {
            spinnerArray[i] = "Group "+String.valueOf(i+1);
        }

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerArray);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long i)
            {
                selectedItemText = (String) parent.getItemAtPosition(position);
                BackgroundTask backgroundTask = new BackgroundTask(StandingsGroup.this);
                backgroundTask.execute(id,selectedItemText);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }
    private class  BackgroundTask extends AsyncTask<String,Void,String>
    {
        String login_url,id,group;
        Context ctx;
        int i,j;
        BackgroundTask(Context ctx)
        {
            this.ctx = ctx;
        }
        @Override
        protected void onPreExecute()
        {
            login_url = "http://burhanshakir.xyz/tsk_get_standingsgroup.php";
        }

        @Override
        protected String doInBackground(String... args)
        {
            String response = "";
            id = args[0];
            group = args[1];
            try {
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data_string =URLEncoder.encode("group","UTF-8")+"="+URLEncoder.encode(group,"UTF-8") + "&" +
                                    URLEncoder.encode("id","UTF-8")+"="+URLEncoder.encode(id,"UTF-8");
                bufferedWriter.write(data_string);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader((new InputStreamReader(inputStream,"iso-8859-1")));

                String line;
                while((line = bufferedReader.readLine())!= null)
                {
                    response+= line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return response;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values)
        {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result)
        {
            char ch, ch1;
            String s = "", s1 = "";
            int i, j = 0, flag =1, count =1;
            TextView[] t = new TextView[700];

            String[] tv = new String[700];
            for (i = 0;i<result.length();i++)
            {
                ch = result.charAt(i);
                if(ch!='%')
                {
                    s = s+ch;
                }
                else
                {
                    for(j = 0;j<s.length();j++)
                    {
                        ch1 = s.charAt(j);
                        if(ch1!='+')
                        {
                            s1 = s1 +ch1;
                        }
                        else
                        {
                            tv[flag] =(s1);
                            s1 = "";
                            flag++;
                        }
                    }
                    s = "";
                }
            }
            TableRow[] row = new TableRow[100];
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);

            while (t1.getChildCount() > 1)
            {
                TableRow r =  (TableRow)t1.getChildAt(1);
                t1.removeView(r);
            }
            for(i=1;i<flag;i = i+7)      //Since there are 7 columns
            {

                t[i] = new TextView(StandingsGroup.this);
                t[i+1] = new TextView(StandingsGroup.this);
                t[i+2] = new TextView(StandingsGroup.this);
                t[i+3] = new TextView(StandingsGroup.this);
                t[i+4] = new TextView(StandingsGroup.this);
                t[i+5] = new TextView(StandingsGroup.this);
                t[i+6] = new TextView(StandingsGroup.this);

                t[i].setText(tv[i]);
                t[i+1].setText(tv[i+1]);
                t[i+2].setText(tv[i+2]);
                t[i+3].setText(tv[i+3]);
                t[i+4].setText(tv[i+4]);
                t[i+5].setText(tv[i+5]);
                t[i+6].setText(tv[i+6]);

                t[i].setTextSize(20);
                t[i+1].setTextSize(20);
                t[i+2].setTextSize(20);
                t[i+3].setTextSize(20);
                t[i+4].setTextSize(20);
                t[i+5].setTextSize(20);
                t[i+6].setTextSize(20);

                row[j] = new TableRow(StandingsGroup.this);
                row[j].setLayoutParams(lp);
                row[j].addView(t[i]);
                row[j].addView(t[i+1]);
                row[j].addView(t[i+2]);
                row[j].addView(t[i+3]);
                row[j].addView(t[i+4]);
                row[j].addView(t[i+5]);
                row[j].addView(t[i+6]);

                t1.addView(row[j]);

                j = j+1;

            }
        }
    }
}
