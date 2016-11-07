package com.example.burhan.thescorekeeper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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

public class Load extends AppCompatActivity
{
    EditText id,pass;
    String tId,tPass,team1,team2;
    String response;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);
        id = (EditText) findViewById(R.id.etTournId);
        pass = (EditText) findViewById(R.id.etTournPassword);
        AdView adView = (AdView) findViewById(R.id.adView);

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }
    public void getScore(View v)
    {
        tId = id.getText().toString();
        tPass = pass.getText().toString();
        if(tPass.isEmpty())
        {
            BackgroundTask1 backgroundTask1 = new BackgroundTask1(this);
            backgroundTask1.execute(tId);
            BackgroundTask2 backgroundTask2 = new BackgroundTask2(this);
            backgroundTask2.execute(tId);
            BackgroundTask backgroundTask = new BackgroundTask(this);
            backgroundTask.execute(tId);
        }
        else
        {
            BackgroundTask1 backgroundTask1 = new BackgroundTask1(this);
            backgroundTask1.execute(tId);
            BackgroundTask2 backgroundTask2 = new BackgroundTask2(this);
            backgroundTask2.execute(tId);
            BackgroundTask3 backgroundTask3 = new BackgroundTask3(this);
            backgroundTask3.execute(tId,tPass);
        }

    }
    public void info(View v)
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Tournament Password");
        alertDialog.setMessage("Tournament Password is only provided to the tournament creator.\n"+
                               "You can login only with the tournament id to check the scores of the tournament.");
        alertDialog.setPositiveButton("OK",null);
        alertDialog.show();
    }
    private class  BackgroundTask extends AsyncTask<String,Void,String>
    {
        String login_url;
        Context ctx;
        BackgroundTask(Context ctx)
        {
            this.ctx = ctx;
        }
        @Override
        protected void onPreExecute()
        {
            login_url = "http://burhanshakir.xyz/tsk_login.php";
        }

        @Override
        protected String doInBackground(String... args)
        {
            response = "";
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
            String s="",type="";
            char ch;
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
                        type = s;
                        s = "";
                        flag= flag+1;
                    }
                }
            }

            if (result.equals(type+"+Login Success"))
            {
                Toast.makeText(ctx,"Login Success",Toast.LENGTH_LONG).show();
                if(type.equals("Friendly Match"))
                {
                    Intent intent = new Intent("com.example.burhan.thescorekeeper.LoadFootball");
                    intent.putExtra("Tournament id", tId);
                    intent.putExtra("Team 1", team1);
                    intent.putExtra("Team 2", team2);
                    intent.putExtra("matches",1);
                    startActivity(intent);
                }
                else if(type.equals("Knockout"))
                {
                    Intent intent = new Intent("com.example.burhan.thescorekeeper.LoadFootballKnockout");
                    intent.putExtra("Tournament Id",tId);
                    intent.putExtra("Type",type);
                    intent.putExtra("Keeper","no");
                    startActivity(intent);
                }
                else if(type.equals("League"))
                {
                    Intent intent = new Intent("com.example.burhan.thescorekeeper.LoadFootballLeague");
                    intent.putExtra("Tournament Id",tId);
                    intent.putExtra("Type",type);
                    intent.putExtra("Keeper","no");
                    startActivity(intent);
                }
                else if(type.equals("Group and Knockout"))
                {
                    Intent intent = new Intent("com.example.burhan.thescorekeeper.LoadFootballGroup");
                    intent.putExtra("Tournament Id",tId);
                    intent.putExtra("Type",type);
                    intent.putExtra("Keeper","no");
                    startActivity(intent);
                }
            }
            else
            {
                Toast.makeText(ctx,"Error logging in.",Toast.LENGTH_LONG).show();
            }
        }
    }
    private class  BackgroundTask1 extends AsyncTask<String,Void,String>
    {

        String get_url;
        Context ctx;
        BackgroundTask1(Context ctx)
        {
            this.ctx = ctx;
        }
        @Override
        protected void onPreExecute()
        {
            get_url = "http://burhanshakir.xyz/tsk_get_team1.php";
        }

        @Override
        protected String doInBackground(String... args)
        {
            String id;
            response ="";
            id = args[0];
            try {
                URL url = new URL(get_url);
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
            team1 = result.trim();
        }
    }
    private class  BackgroundTask2 extends AsyncTask<String,Void,String>
    {
        String get_url;
        Context ctx;
        BackgroundTask2(Context ctx)
        {
            this.ctx = ctx;
        }
        @Override
        protected void onPreExecute()
        {
            get_url = "http://burhanshakir.xyz/tsk_get_team2.php";
        }

        @Override
        protected String doInBackground(String... args)
        {
            String id;
            response="";
            id = args[0];

            try {
                URL url = new URL(get_url);
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
            team2 = result;
        }
    }
    private class  BackgroundTask3 extends AsyncTask<String,Void,String>
    {
        String login_url;
        Context ctx;
        BackgroundTask3(Context ctx)
        {
            this.ctx = ctx;
        }
        @Override
        protected void onPreExecute()
        {
            login_url = "http://burhanshakir.xyz/tsk_login_keeper.php";
        }

        @Override
        protected String doInBackground(String... args)
        {
            String id,pw,response="";
            id = args[0];
            pw = args[1];
            try {
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data_string = URLEncoder.encode("pw","UTF-8")+"="+URLEncoder.encode(pw,"UTF-8") + "&" +
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

            int i,flag=1;
            String s="",type="";
            result = result.trim();
            char ch;
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
                        type = s;
                        s = "";
                        flag= flag+1;
                    }
                }
            }

            if (result.equals(type+"+Login Success"))
            {
                Toast.makeText(ctx,"Login Success",Toast.LENGTH_LONG).show();

                if(type.equals("Friendly Match"))
                {
                    Intent intent = new Intent("com.example.burhan.thescorekeeper.LoadFootball");
                    intent.putExtra("Tournament id", tId);
                    intent.putExtra("Team 1", team1);
                    intent.putExtra("Team 2", team2);
                    intent.putExtra("Keeper","yes");
                    intent.putExtra("matches",1);
                    startActivity(intent);
                }
                else if(type.equals("Knockout"))
                {
                    Intent intent = new Intent("com.example.burhan.thescorekeeper.LoadFootballKnockout");
                    intent.putExtra("Tournament Id",tId);
                    intent.putExtra("Type",type);
                    intent.putExtra("Tournament Password",tPass);
                    intent.putExtra("Keeper","yes");
                    startActivity(intent);
                }
                else if(type.equals("League"))
                {
                    Intent intent = new Intent("com.example.burhan.thescorekeeper.LoadFootballLeague");
                    intent.putExtra("Tournament Id",tId);
                    intent.putExtra("Tournament Password",tPass);
                    intent.putExtra("Type",type);
                    intent.putExtra("Keeper","yes");
                    startActivity(intent);
                }
                else if(type.equals("Group and Knockout"))
                {
                    Intent intent = new Intent("com.example.burhan.thescorekeeper.LoadFootballGroup");
                    intent.putExtra("Tournament Id",tId);
                    intent.putExtra("Tournament Password",tPass);
                    intent.putExtra("Type",type);
                    intent.putExtra("Keeper","yes");
                    startActivity(intent);
                }
            }
            else
            {
                Toast.makeText(ctx,"Error logging in. Please try again.",Toast.LENGTH_LONG).show();
            }
        }
    }

}
