package com.example.burhan.thescorekeeper;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import java.util.Random;

public class CreateFootball extends AppCompatActivity
{
    Button next;
    Spinner tournType;
    EditText tName,oName,etGroup;
    TextView tvGroup,check;
    String selectedItemText,Tname,Oname,type,tId,tPassword,group;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_football);

        next = (Button) findViewById(R.id.bNext);
        tournType   = (Spinner) findViewById(R.id.spinnerTournType);
        tName = (EditText) findViewById(R.id.etTournName);
        oName = (EditText) findViewById(R.id.etOrgName);
        etGroup = (EditText) findViewById(R.id.etGroup);
        tvGroup = (TextView) findViewById(R.id.tvGroup);
        check = (TextView) findViewById(R.id.tvCheck);
        AdView adView = (AdView) findViewById(R.id.adView);

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);



        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.tournament_array, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        tournType.setAdapter(adapter);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo!=null && networkInfo.isConnected())
        {
            check.setVisibility(View.INVISIBLE);
            next.setEnabled(true);
        }



        tournType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                selectedItemText = (String) parent.getItemAtPosition(position);

                if(selectedItemText.equals("Friendly Match"))
                {
                    etGroup.setVisibility(View.INVISIBLE);
                    tvGroup.setVisibility(View.INVISIBLE);
                    next.setOnClickListener
                            (
                                    new View.OnClickListener()
                                    {
                                        public void onClick(View v)
                                        {

                                            /*final Intent intent = new Intent("com.example.burhan.thescorekeeper.FootballFriendly");
                                            //Adding new stuff 2


                                            intent.putExtra("Tournament Name",tName.getText().toString());
                                            intent.putExtra("Organiser Name",oName.getText().toString());
                                            intent.putExtra("Type",selectedItemText);*/

                                            Tname = tName.getText().toString();
                                            Oname = oName.getText().toString();
                                            type = selectedItemText;

                                            if(TextUtils.isEmpty(Tname))
                                            {
                                                tName.setError("Tournament name cannot be empty");
                                                return;
                                            }

                                            Random r1 = new Random();
                                            int id = r1.nextInt(10000 - 1) + 1;
                                            tId = Tname +"_" + id;

                                            Random r2 = new Random();
                                            int pw = r2.nextInt(1000 - 1) + 1;
                                            tPassword = Tname + "_" + pw;
                                            /*intent.putExtra("TId",tId);
                                            intent.putExtra("Tournament Password",tPassword);*/




                                            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(CreateFootball.this);

                                            String alert1 = "Tournament Name: " + Tname;
                                            String alert2 = "Organisers Name: " + Oname;
                                            String alert3 = "Tournament Id: " + tId;
                                            String alert4 = "Keeper's Password: " + tPassword;
                                            alertDialog.setTitle("Tournament Info:")
                                            .setMessage(alert1 +"\n"+ alert2 +"\n"+ alert3+"\n" + alert4)
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    // Write your code here to execute after dialog closed
                                                    BackgroundTask backgroundTask = new BackgroundTask(CreateFootball.this);
                                                    backgroundTask.execute(Tname,Oname,type,tId,tPassword,"");
                                                    //finish();
                                                    //startActivity(intent);
                                                }
                                            });
                                            // Showing Alert Message
                                            AlertDialog alert = alertDialog.create();
                                            alert.show();


                                        }});
                }
                else if(selectedItemText.equals("Knockout"))
                {
                    etGroup.setVisibility(View.INVISIBLE);
                    tvGroup.setVisibility(View.INVISIBLE);
                    next.setOnClickListener
                            (
                                    new View.OnClickListener()
                                    {
                                        public void onClick(View v)
                                        {

                                            /*final Intent intent = new Intent("com.example.burhan.thescorekeeper.FootballKnockout");
                                            //Adding new stuff 2

                                            intent.putExtra("Tournament Name",tName.getText().toString());
                                            intent.putExtra("Organiser Name",oName.getText().toString());
                                            intent.putExtra("Type",selectedItemText);*/

                                            Tname = tName.getText().toString();
                                            Oname = oName.getText().toString();
                                            type = selectedItemText;

                                            if(TextUtils.isEmpty(Tname))
                                            {
                                                tName.setError("Tournament name cannot be empty");
                                                return;
                                            }

                                            Random r1 = new Random();
                                            int id = r1.nextInt(10000 - 1) + 1;
                                            tId = Tname +"_" + id;

                                            Random r2 = new Random();
                                            int pw = r2.nextInt(1000 - 1) + 1;
                                            tPassword = Tname + "_" + pw;
                                            /*intent.putExtra("Tournament Id",tId);
                                            intent.putExtra("Tournament Password",tPassword);*/




                                            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(CreateFootball.this);

                                            String alert1 = "Tournament Name: " + Tname;
                                            String alert2 = "Organisers Name: " + Oname;
                                            String alert3 = "Tournament Id: " + tId;
                                            String alert4 = "Keeper's Password: " + tPassword;
                                            alertDialog.setTitle("Tournament Info:")
                                                    .setMessage(alert1 +"\n"+ alert2 +"\n"+ alert3+"\n" + alert4)
                                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            // Write your code here to execute after dialog closed
                                                            BackgroundTask backgroundTask = new BackgroundTask(CreateFootball.this);
                                                            backgroundTask.execute(Tname,Oname,type,tId,tPassword,"");
                                                            //finish();
                                                            //startActivity(intent);


                                                        }
                                                    });
                                            // Showing Alert Message
                                            AlertDialog alert = alertDialog.create();
                                            alert.show();


                                        }});
                }
                else if(selectedItemText.equals("League"))
                {
                    etGroup.setVisibility(View.INVISIBLE);
                    tvGroup.setVisibility(View.INVISIBLE);
                    next.setOnClickListener
                            (
                                    new View.OnClickListener()
                                    {
                                        public void onClick(View v)
                                        {

                                            /*final Intent intent = new Intent("com.example.burhan.thescorekeeper.FootballLeague");
                                            //Adding new stuff 2

                                            intent.putExtra("Tournament Name",tName.getText().toString());
                                            intent.putExtra("Organiser Name",oName.getText().toString());
                                            intent.putExtra("Type",selectedItemText);*/

                                            Tname = tName.getText().toString();
                                            Oname = oName.getText().toString();
                                            type = selectedItemText;

                                            if(TextUtils.isEmpty(Tname))
                                            {
                                                tName.setError("Tournament name cannot be empty");
                                                return;
                                            }

                                            Random r1 = new Random();
                                            int id = r1.nextInt(10000 - 1) + 1;
                                            tId = Tname +"_" + id;

                                            Random r2 = new Random();
                                            int pw = r2.nextInt(1000 - 1) + 1;
                                            tPassword = Tname + "_" + pw;
                                            /*intent.putExtra("Tournament Id",tId);
                                            intent.putExtra("Tournament Password",tPassword);*/




                                            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(CreateFootball.this);

                                            String alert1 = "Tournament Name: " + Tname;
                                            String alert2 = "Organisers Name: " + Oname;
                                            String alert3 = "Tournament Id: " + tId;
                                            String alert4 = "Keeper's Password: " + tPassword;
                                            alertDialog.setTitle("Tournament Info:")
                                                    .setMessage(alert1 +"\n"+ alert2 +"\n"+ alert3+"\n" + alert4)
                                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            // Write your code here to execute after dialog closed
                                                            BackgroundTask backgroundTask = new BackgroundTask(CreateFootball.this);
                                                            backgroundTask.execute(Tname,Oname,type,tId,tPassword,"");
                                                            //finish();
                                                            //startActivity(intent);


                                                        }
                                                    });
                                            // Showing Alert Message
                                            AlertDialog alert = alertDialog.create();
                                            alert.show();


                                        }});
                }
                else if(selectedItemText.equals("Group and Knockout"))
                {
                    etGroup.setVisibility(View.VISIBLE);
                    tvGroup.setVisibility(View.VISIBLE);


                    next.setOnClickListener
                            (
                                    new View.OnClickListener()
                                    {
                                        public void onClick(View v)
                                        {
                                            group = etGroup.getText().toString();
                                            //final Intent intent = new Intent("com.example.burhan.thescorekeeper.FootballGroup");

                                            if(group.isEmpty())
                                            {

                                                Toast.makeText(CreateFootball.this,"Enter no.of groups",Toast.LENGTH_SHORT).show();
                                            }
                                            else
                                            {
                                                /*intent.putExtra("Tournament Name",tName.getText().toString());
                                                intent.putExtra("Organiser Name",oName.getText().toString());
                                                intent.putExtra("Type",selectedItemText);
                                                intent.putExtra("Group",group);*/

                                                Tname = tName.getText().toString();
                                                Oname = oName.getText().toString();
                                                type = selectedItemText;

                                                if(TextUtils.isEmpty(Tname))
                                                {
                                                    tName.setError("Tournament name cannot be empty");
                                                    return;
                                                }

                                                Random r1 = new Random();
                                                int id = r1.nextInt(10000 - 1) + 1;
                                                tId = Tname +"_" + id;

                                                Random r2 = new Random();
                                                int pw = r2.nextInt(1000 - 1) + 1;
                                                tPassword = Tname + "_" + pw;
                                                /*intent.putExtra("Tournament Id",tId);
                                                intent.putExtra("Tournament Password",tPassword);*/




                                                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(CreateFootball.this);

                                                String alert1 = "Tournament Name: " + Tname;
                                                String alert2 = "Organisers Name: " + Oname;
                                                String alert3 = "Tournament Id: " + tId;
                                                String alert4 = "Keeper's Password: " + tPassword;
                                                alertDialog.setTitle("Tournament Info:")
                                                        .setMessage(alert1 +"\n"+ alert2 +"\n"+ alert3+"\n" + alert4)
                                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                // Write your code here to execute after dialog closed
                                                                BackgroundTask backgroundTask = new BackgroundTask(CreateFootball.this);
                                                                backgroundTask.execute(Tname,Oname,type,tId,tPassword,group);
                                                                //finish();
                                                                //startActivity(intent);


                                                            }
                                                        });
                                                // Showing Alert Message
                                                AlertDialog alert = alertDialog.create();
                                                alert.show();


                                            }
                                        }});
                            }
                            else
                            {
                                next.setOnClickListener
                                        (
                                    new View.OnClickListener()
                                    {
                                        public void onClick(View v)
                                        {
                                            Toast.makeText
                                                    (getApplicationContext(), "Option not available for "+selectedItemText, Toast.LENGTH_SHORT)
                                                    .show();
                                        }
                                    }
                            );

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private class  BackgroundTask extends AsyncTask<String,Void,String>
    {
        String add_info_url,tname,oname,type,id,pw,group,response = "";
        Context ctx;
        ProgressDialog progress = new ProgressDialog(CreateFootball.this);
        BackgroundTask(Context ctx)
        {
            this.ctx = ctx;
        }
        @Override
        protected void onPreExecute()
        {
            add_info_url = "http://burhanshakir.xyz/tsk_add_info.php";
            progress.setMessage("Creating tournament. Please wait..");
            progress.show();
        }

        @Override
        protected String doInBackground(String... args)
        {

            tname = args[0];
            oname = args[1];
            type = args[2];
            id = args[3];
            pw = args [4];
            group = args[5];

            try {
                URL url = new URL(add_info_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data_string = URLEncoder.encode("t_name","UTF-8")+"="+URLEncoder.encode(tname,"UTF-8") + "&" +
                        URLEncoder.encode("o_name","UTF-8")+"="+URLEncoder.encode(oname,"UTF-8") + "&" +
                        URLEncoder.encode("type","UTF-8")+"="+URLEncoder.encode(type,"UTF-8") + "&" +
                        URLEncoder.encode("id","UTF-8")+"="+URLEncoder.encode(id,"UTF-8") + "&" +
                        URLEncoder.encode("group","UTF-8")+"="+URLEncoder.encode(group,"UTF-8") + "&" +
                        URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(pw,"UTF-8");
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
            /*if(result.isEmpty())
            {
                Toast.makeText(CreateFootball.this,"No Interner Connection. Please try again",Toast.LENGTH_LONG).show();
            }
            else
            Toast.makeText(ctx,result.trim(),Toast.LENGTH_LONG).show();
            */
            if(type.equals("Friendly Match"))
            {
                Intent intent = new Intent("com.example.burhan.thescorekeeper.FootballFriendly");
                intent.putExtra("Tournament Name",tname);
                intent.putExtra("Organiser Name",oname);
                intent.putExtra("Type",type);
                intent.putExtra("TId",id);
                intent.putExtra("Tournament Password",pw);
                startActivity(intent);
            }
            else if(type.equals("Knockout"))
            {
                Intent intent = new Intent("com.example.burhan.thescorekeeper.FootballKnockout");
                intent.putExtra("Tournament Name",tname);
                intent.putExtra("Organiser Name",oname);
                intent.putExtra("Type",type);
                intent.putExtra("Tournament Id",id);
                intent.putExtra("Tournament Password",pw);
                startActivity(intent);
            }
            else if(type.equals("League"))
            {
                Intent intent = new Intent("com.example.burhan.thescorekeeper.FootballLeague");
                intent.putExtra("Tournament Name",tname);
                intent.putExtra("Organiser Name",oname);
                intent.putExtra("Type",type);
                intent.putExtra("Tournament Id",id);
                intent.putExtra("Tournament Password",pw);
                startActivity(intent);
            }
            else if(type.equals("Group and Knockout"))
            {
                Intent intent = new Intent("com.example.burhan.thescorekeeper.FootballGroup");
                intent.putExtra("Tournament Name",tname);
                intent.putExtra("Organiser Name",oname);
                intent.putExtra("Type",type);
                intent.putExtra("Tournament Id",id);
                intent.putExtra("Tournament Password",pw);
                intent.putExtra("Group",group);
                startActivity(intent);
            }
            Toast.makeText(ctx,result.trim(),Toast.LENGTH_LONG).show();
            progress.dismiss();
        }
    }

}

