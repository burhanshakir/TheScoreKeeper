package com.example.burhan.thescorekeeper;


import android.content.Context;

import android.graphics.Color;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

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

public class AddTeam extends AppCompatActivity
{
    String tName,tId,tPw,oName,response="";
    TableLayout layout;
    int i = 0,count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_team);
        layout = (TableLayout) findViewById(R.id.tableLayout2);
        AdView adView = (AdView) findViewById(R.id.adView);

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        tId = getIntent().getStringExtra("TId");
        tName = getIntent().getStringExtra("Tournament Name");
        oName = getIntent().getStringExtra("Organiser Name");
        tPw = getIntent().getStringExtra("Tournament Password");

        BackgroundTask backgroundTask = new BackgroundTask(this);
        backgroundTask.execute(tId);
    }
    private class  BackgroundTask extends AsyncTask<String,Void,String>
    {
        String add_info_url;
        Context ctx;
        BackgroundTask(Context ctx)
        {
            this.ctx = ctx;
        }
        @Override
        protected void onPreExecute()
        {
            add_info_url = "http://burhanshakir.xyz/tsk_view_team.php";
        }

        @Override
        protected String doInBackground(String... args)
        {
            String id;
            id = args[0];
            try {
                URL url = new URL(add_info_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data_string = URLEncoder.encode("id","UTF-8")+"="+URLEncoder.encode(id,"UTF-8");
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
            char ch;
            String s = "";
            int i,flag = 0;
            String[] tv = new String[200];
            TextView[] t = new TextView[200];
            for(i = 0;i<result.length();i++)
            {
                ch = result.charAt(i);
                if(ch!='+')
                {
                    s = s+ch;
                }
                else
                {
                    tv[flag] = s;
                    flag++;
                    s = "";
                }
            }
            TableRow[] row = new TableRow[200];
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            for(i=0;i<flag;i++)
            {
                t[i] = new TextView(AddTeam.this);
                t[i].setText(tv[i]);
                t[i].setTextSize(20);
                t[i].setPadding(20,20,20,20);


                row[i] = new TableRow(AddTeam.this);
                row[i].setLayoutParams(lp);
                row[i].setBackgroundColor(Color.parseColor("#90A4AE"));
                row[i].addView(t[i]);

                layout.addView(row[i]);
            }
        }
    }

}
