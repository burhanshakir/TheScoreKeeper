package com.example.burhan.thescorekeeper;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class FootballKnockout extends AppCompatActivity
{

    String tName,tId,tPw,oName,type;
    TextView matches;
    Intent intent;
    int count;
    int flag=0;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_football_knockout);
        tId = getIntent().getStringExtra("Tournament Id");
        tName = getIntent().getStringExtra("Tournament Name");
        oName = getIntent().getStringExtra("Organiser Name");
        tPw = getIntent().getStringExtra("Tournament Password");
        type = getIntent().getStringExtra("Type");
        AdView adView = (AdView) findViewById(R.id.adView);

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        SharedPreferences prefs = getSharedPreferences(tId,MODE_PRIVATE);
        count = prefs.getInt("matches",0);

    }
    public void addTeam(View v)
    {
        if(flag == 0)
            Toast.makeText(this,"No Matches created",Toast.LENGTH_SHORT).show();
        else {
            intent = new Intent("com.example.burhan.thescorekeeper.AddTeam");
            intent.putExtra("Tournament Name", tName);
            intent.putExtra("Organiser Name", oName);
            intent.putExtra("TId", tId);
            intent.putExtra("Tournament Password", tPw);
            startActivity(intent);
        }
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
    public void access (View v)
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        String alert1 = "Tournament Name: " + tName;
        String alert2 = "Organisers Name: " + oName;
        String alert3 = "Tournament Id: " + tId;
        String alert4 = "Keeper's Password: " + tPw;
        alertDialog.setTitle("Tournament Info:")
                .setMessage(alert1 +"\n"+ alert2 +"\n"+ alert3+"\n" + alert4)
                .setPositiveButton("OK", null);
        AlertDialog alert = alertDialog.create();
        alert.show();
    }
    public void results(View v)
    {
        if(flag == 0)
            Toast.makeText(this,"No Matches created",Toast.LENGTH_SHORT).show();
        else {
            intent = new Intent("com.example.burhan.thescorekeeper.Results");
            intent.putExtra("TId", tId);
            intent.putExtra("Count", String.valueOf(flag));
            startActivity(intent);
        }
    }
}
