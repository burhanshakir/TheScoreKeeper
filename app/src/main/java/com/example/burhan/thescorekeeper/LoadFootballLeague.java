package com.example.burhan.thescorekeeper;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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

public class LoadFootballLeague extends AppCompatActivity
{

    String tName,tId,tPw,oName,type,count,keeper;
    TextView matches;
    Intent intent;
    ImageButton create;
    int flag = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_football_league);
        tId = getIntent().getStringExtra("Tournament Id");
        type = getIntent().getStringExtra("Type");
        keeper = getIntent().getStringExtra("Keeper");


        create = (ImageButton) findViewById(R.id.bCreateMatch);
        AdView adView = (AdView) findViewById(R.id.adView);

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        if(keeper.equals("yes"))
            create.setEnabled(true);
        else
        {
            create.setClickable(false);
            create.setEnabled(false);
            Toast.makeText(this,"Matches can only be created by the tournament creator",Toast.LENGTH_SHORT).show();
        }

        BackgroundTask backgroundTask = new BackgroundTask(this);
        backgroundTask.execute(tId);
    }
    public void createMatch(View v)
    {
        flag++;
        intent = new Intent("com.example.burhan.thescorekeeper.FootballFriendly");
        intent.putExtra("Tournament Name",tName);
        intent.putExtra("Organiser Name",oName);
        intent.putExtra("TId",tId);
        intent.putExtra("Tournament Password",tPw);
        intent.putExtra("match number",flag);
        intent.putExtra("Type",type);
        startActivity(intent);

    }
    public void standings(View v)
    {
        intent = new Intent("com.example.burhan.thescorekeeper.Standings");
        intent.putExtra("Tournament Name",tName);
        intent.putExtra("Organiser Name",oName);
        intent.putExtra("TId",tId);
        intent.putExtra("Tournament Password",tPw);
        startActivity(intent);
    }
    public void access (View v)
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        String alert1 = "Tournament Name: " + tName;
        String alert2 = "Organisers Name: " + oName;
        String alert3 = "Tournament Id: " + tId;
        String alert4 = "Type: " + type;
        String alert5 = "Matches Played: " + count;
        alertDialog.setTitle("Tournament Info:")
                .setMessage(alert1 +"\n"+ alert2 +"\n"+ alert3+"\n"+alert4 +"\n" + alert5)
                .setPositiveButton("OK", null);
        AlertDialog alert = alertDialog.create();
        alert.show();
    }
    public void results(View v)
    {
        BackgroundTask1 backgroundTask1 = new BackgroundTask1(this);
        backgroundTask1.execute(tId);
    }
    private class  BackgroundTask extends AsyncTask<String,Void,String>
    {
        String login_url;
        Context ctx;
        ProgressDialog progress = new ProgressDialog(LoadFootballLeague.this);
        BackgroundTask(Context ctx)
        {
            this.ctx = ctx;
        }
        @Override
        protected void onPreExecute()
        {
            login_url = "http://burhanshakir.xyz/tsk_get_info.php";

            progress.setMessage("Getting Information..");
            progress.show();
        }

        @Override
        protected String doInBackground(String... args)
        {
            String response = "";
            String id;
            id = args[0];
            try {
                URL url = new URL(login_url);
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

            int i,flag=1;
            result = result.trim();
            String s="";
            char ch;
            //Toast.makeText(ctx,result,Toast.LENGTH_LONG).show();
            for(i=0;i<result.length();i++)
            {
                ch = result.charAt(i);
                if (ch != '+')
                {
                    s+= ch;
                }
                else
                {
                    if (flag == 1)
                    {
                        tName = s;
                        s = "";
                        flag= flag+1;
                    }
                    else if(flag ==2)
                    {
                        oName = s;
                        s = "";
                        flag++;
                    }
                    else if(flag ==3)
                    {
                        tPw = s;
                        s = "";
                        flag++;
                    }
                    else if(flag == 4)
                    {
                        count = s;
                        s = "";
                        flag++;
                    }
                }
            }
            progress.dismiss();
        }
    }
    private class  BackgroundTask1 extends AsyncTask<String,Void,String>
    {
        String login_url;
        Context ctx;
        BackgroundTask1(Context ctx)
        {
            this.ctx = ctx;
        }
        @Override
        protected void onPreExecute()
        {
            login_url = "http://burhanshakir.xyz/tsk_get_match_count.php";
        }

        @Override
        protected String doInBackground(String... args)
        {
            String response = "";
            String id;
            id = args[0];
            try {
                URL url = new URL(login_url);
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
            intent = new Intent("com.example.burhan.thescorekeeper.Results");
            intent.putExtra("TId",tId);
            intent.putExtra("Count",result);
            startActivity(intent);
        }
    }
}
