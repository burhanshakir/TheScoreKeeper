package com.example.burhan.thescorekeeper;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


public class MainActivity extends AppCompatActivity
{
    Button create,load;
    Spinner sport;
    String selectedItemText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        create = (Button) findViewById(R.id.bCreate);
        load = (Button) findViewById(R.id.bLoad);
        sport = (Spinner) findViewById(R.id.spinnerSport);
        AdView adView = (AdView) findViewById(R.id.adView);

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.sport_array, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        sport.setAdapter(adapter);

        sport.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedItemText = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
    public void newT(View v)
    {
        String football = "Football";
        if(selectedItemText.equals(football))
        {
            Intent intent = new Intent("com.example.burhan.thescorekeeper.CreateFootball");
            startActivity(intent);
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Option not available for "+selectedItemText, Toast.LENGTH_SHORT).show();
        }
    }
    public void load(View v)
    {
        Intent intent =new Intent("com.example.burhan.thescorekeeper.Load");
        startActivity(intent);
    }
}

