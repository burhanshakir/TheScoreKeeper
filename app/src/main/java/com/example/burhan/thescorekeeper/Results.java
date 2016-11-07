package com.example.burhan.thescorekeeper;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

public class Results extends AppCompatActivity
{
    String c,tId,scr;
    String[] team1,team2,matchNo;
    int count;
    TableLayout tl;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        setTitle("The Score Keeper");
        tl = (TableLayout) findViewById(R.id.tableLayout);
        AdView adView = (AdView) findViewById(R.id.adView);

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        c = getIntent().getStringExtra("Count");
        tId = getIntent().getStringExtra("TId");

        count = Integer.parseInt(c);

        team1 = new String[count+1];
        team2 = new String[count+1];
        matchNo = new String[count+1];

        for (int i = 1; i <=count; i++)
        {
            BackgroundTask backgroundTask = new BackgroundTask(this);
            backgroundTask.execute(tId,String.valueOf(i));
        }

    }
    private class  BackgroundTask extends AsyncTask<String,Void,String>
    {
        String login_url,id,match;
        Context ctx;
        int i,j;
        BackgroundTask(Context ctx)
        {
            this.ctx = ctx;
        }
        @Override
        protected void onPreExecute()
        {
            login_url = "http://burhanshakir.xyz/tsk_get_results.php";
        }

        @Override
        protected String doInBackground(String... args)
        {
            String response = "";
            id = args[0];
            match = args[1];
            j = Integer.parseInt(match);
            try {
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data_string = URLEncoder.encode("count","UTF-8")+"="+URLEncoder.encode(match,"UTF-8") + "&" +
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
            char ch;
            String s = "";
            int flag = 1;
            matchNo[j] = match;
            TableRow row = new TableRow(Results.this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);


            row.setLayoutParams(lp);

            row.setBackgroundColor(Color.parseColor("#90A4AE"));

            TextView tv = new TextView(Results.this);

            tv.setPadding(30,30,30,30);
            tv.setTextSize(20);

            for(i=0;i<result.length();i++)
            {
                ch = result.charAt(i);
                if(ch!='+')
                {
                    s = s+ch;
                }
                else
                {
                    if(flag==1)
                    {
                        scr = s;
                        tv.setText(scr);
                        s = "";
                        flag++;
                    }
                    else if(flag == 2)
                    {
                        team1[j] = s;
                        s = "";
                        flag++;
                    }
                    else
                    {
                        team2[j] = s;
                    }
                }
            }

            row.addView(tv);
            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    Intent intent = new Intent("com.example.burhan.thescorekeeper.LoadFootball");
                    intent.putExtra("Team 1",team1[j]);
                    intent.putExtra("Team 2",team2[j]);
                    intent.putExtra("matches",Integer.parseInt(matchNo[j]));
                    intent.putExtra("Tournament id",tId);
                    startActivity(intent);
                }
            });

            tl.addView(row,Integer.parseInt(match)-1);

        }
    }
}
