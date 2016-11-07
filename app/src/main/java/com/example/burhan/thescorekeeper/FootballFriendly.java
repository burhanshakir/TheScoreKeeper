package com.example.burhan.thescorekeeper;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.Map;
import java.util.Set;

public class FootballFriendly extends AppCompatActivity
{

    Button badd1,badd2,create;
    int flag1=0,flag2=15,count;
    EditText[] editText = new EditText[100];
    EditText team1,team2;
    TextView tvType;
    String type,tId,tName,oName,group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_football_friendly);

        badd1 = (Button) findViewById(R.id.bTeam1);
        badd2 = (Button) findViewById(R.id.bTeam2);
        create = (Button) findViewById(R.id.bFriendlyNext);
        team1 = (EditText) findViewById(R.id.etTeam1);
        team2 = (EditText) findViewById(R.id.etTeam2);
        tvType = (TextView) findViewById(R.id.textView5);
        AdView adView = (AdView) findViewById(R.id.adView);

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        tName = getIntent().getStringExtra("Tournament Name");
        oName = getIntent().getStringExtra("Organiser Name");
        type = getIntent().getStringExtra("Type");
        tId = getIntent().getStringExtra("TId");
        group = getIntent().getStringExtra("Group");

        tvType.setText(type);
        for (int i = 1; i <= 30; i++)
        {
            String editTextID = "editText" + i;
            int resID = getResources().getIdentifier(editTextID, "id", "com.example.burhan.thescorekeeper");
            editText[i] = ((EditText) findViewById(resID));
        }

        SharedPreferences prefs = getSharedPreferences(tId,MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        count = prefs.getInt("match",0);
        editor.putInt("match",count);
        editor.apply();
    }

       public void add1(View v)
        {
           flag1++;
           if (flag1 > 15) {
               Toast.makeText
                       (getApplicationContext(), "Only 15 players per team", Toast.LENGTH_SHORT)
                       .show();
                flag1 =15;
           } else {
               editText[flag1].setVisibility(View.VISIBLE);
           }
        }
        public void add2(View v)
        {
            flag2++;
            if(flag2>30)
            {
                Toast.makeText(getApplicationContext(), "Only 15 players per team", Toast.LENGTH_SHORT).show();
                flag2 = 30;
            }
            else
            {
                editText[flag2].setVisibility(View.VISIBLE);
            }
        }
        public void create(View v)
        {
            SharedPreferences prefs = getSharedPreferences(tId,MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            int i,matchNo;
            count++;
            editor.putInt("match",count);
            editor.apply();
            matchNo = prefs.getInt("match",0);
            if (team1.getText().toString().equals("") || team2.getText().toString().equals(""))
            {
                Toast.makeText(getApplicationContext(), "Enter Team Names", Toast.LENGTH_SHORT).show();
            }
            else if (flag1 == 0 || flag2 == 15)
            {
                Toast.makeText(getApplicationContext(), "Enter atleast one team member.", Toast.LENGTH_SHORT).show();
            }
            else
            {
                BackgroundTask1 backgroundTask1 = new BackgroundTask1(FootballFriendly.this);
                backgroundTask1.execute(tId);
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
            String response="";
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
                    response= line;
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
            int i,count;
            result = result +1;
            count = Integer.parseInt(result);
            Intent intent = new Intent ("com.example.burhan.thescorekeeper.FriendlyKickoff");
            intent.putExtra("TeamName1",team1.getText().toString());
            intent.putExtra("TeamName2",team2.getText().toString());
            intent.putExtra("Players1",flag1);
            intent.putExtra("Players2",(flag2-15));
            intent.putExtra("Tournament id",tId);
            intent.putExtra("Type",type);
            intent.putExtra("Matches",count);
            intent.putExtra("Group",group);
            for(i=1;i<=30;i++)
            {
                if(editText[i].getText().toString().isEmpty() || editText[i].getText().toString().equals(null) )
                    editText[i].setText(" ");
                intent.putExtra("Lineup"+i,editText[i].getText().toString());
            }
            //Toast.makeText(ctx,String.valueOf(Integer.parseInt(result)),Toast.LENGTH_SHORT).show();
            startActivity(intent);
            finish();
        }
    }
}

