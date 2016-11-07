package com.example.burhan.thescorekeeper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class SelectMatchType extends AppCompatActivity
{
    Button next;
    Spinner spinner;
    String[] spinnerArray;
    String tName,type,tId,oName,groups,tPw,selectedItemText;
    int count,flag = 0,i,noOfG;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_match_type);

        next = (Button)findViewById(R.id.bGroupNext);
        spinner = (Spinner) findViewById(R.id.sGroupNo);
        AdView adView = (AdView) findViewById(R.id.adView);

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        tName = getIntent().getStringExtra("Tournament Name");
        oName = getIntent().getStringExtra("Organiser Name");
        type = getIntent().getStringExtra("Type");
        tId = getIntent().getStringExtra("TId");
        tPw = getIntent().getStringExtra("Tournament Password");
        groups = getIntent().getStringExtra("Groups");

        noOfG = Integer.parseInt(groups);

        spinnerArray = new String[noOfG];

        SharedPreferences prefs = getSharedPreferences(tId,MODE_PRIVATE);
        count = prefs.getInt("matches",0);

        for(i= 0;i<noOfG;i++)
        {
            spinnerArray[i] = "Group "+String.valueOf(i+1);
        }
    }
    public void knockout(View v)
    {
        flag++;
        Intent intent = new Intent("com.example.burhan.thescorekeeper.FootballFriendly");
        intent.putExtra("Tournament Name",tName);
        intent.putExtra("Organiser Name",oName);
        intent.putExtra("TId",tId);
        intent.putExtra("Type",type);
        intent.putExtra("Tournament Password",tPw);
        intent.putExtra("match number",flag);
        startActivity(intent);
    }
    public void group(View v)
    {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerArray);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);

        spinner.setVisibility(View.VISIBLE);
        next.setVisibility(View.VISIBLE);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                selectedItemText = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    public void next(View v)
    {
        flag++;
        Intent intent = new Intent("com.example.burhan.thescorekeeper.FootballFriendly");
        intent.putExtra("Tournament Name",tName);
        intent.putExtra("Organiser Name",oName);
        intent.putExtra("TId",tId);
        intent.putExtra("Type",type);
        intent.putExtra("Tournament Password",tPw);
        intent.putExtra("match number",flag);
        intent.putExtra("Group",selectedItemText);
        startActivity(intent);
    }
}
