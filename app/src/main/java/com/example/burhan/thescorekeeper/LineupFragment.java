package com.example.burhan.thescorekeeper;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;


public class LineupFragment extends Fragment {

    Bundle args;
    TextView teamName1, teamName2;
    String l[] = new String[100];
    String team1Lineup[] = new String[20];
    String team2Lineup[] = new String[20];
    String type, team1, team2, tId;
    TextView lineups[] = new TextView[100];
    int count, flag = 0;

    public LineupFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null)
            flag = bundle.getInt("flag", 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        int i;
        View frag = inflater.inflate(R.layout.activity_lineup_fragment, container, false);

        teamName1 = (TextView) frag.findViewById(R.id.tvTeam1);
        teamName2 = (TextView) frag.findViewById(R.id.tvTeam2);


        args = getActivity().getIntent().getExtras();
        team1 = args.getString("TeamName1");
        team2 = args.getString("TeamName2");
        type = args.getString("Type");
        tId = args.getString("Tournament id");
        count = args.getInt("Matches");
        int noOfPlayers1 = args.getInt("Players1");
        int noOfPlayers2 = args.getInt("Players2");


        for (i = 1; i <= 30; i++) {
            String TextViewID = "tvLineup" + i;
            int resID = getResources().getIdentifier(TextViewID, "id", "com.example.burhan.thescorekeeper");
            lineups[i] = ((TextView) frag.findViewById(resID));
        }

        teamName1.setText(team1);
        teamName2.setText(team2);

        for (i = 1; i <= 30; i++) {
            l[i] = args.getString("Lineup" + i);
            if (!l[i].equals("")) {
                lineups[i].setText(l[i]);
                if (i < 16)
                    team1Lineup[i] = l[i];
                else
                    team2Lineup[i - 15] = l[i];
            }

        }
        return frag;
    }

       /*//if(flag != 0) {
           for (i = 1; i <= noOfPlayers1; i++) {
               String serial = String.valueOf(i);
               BackgroundTask backgroundTask = new BackgroundTask(getActivity());
               backgroundTask.execute(team1, serial, team1Lineup[i], tId, String.valueOf(count));
           }
           for (i = 1; i <= noOfPlayers2; i++) {
               String serial = String.valueOf(i);
               BackgroundTask1 backgroundTask1 = new BackgroundTask1(getActivity());
               backgroundTask1.execute(team2, serial, team2Lineup[i], tId, String.valueOf(count));
           }
       //}

        return frag;
    }
       private class  BackgroundTask extends AsyncTask<String,Void,String> {
           String add_info_url;
           Context ctx;

           BackgroundTask(Context ctx) {
               this.ctx = ctx;
           }

           @Override
           protected void onPreExecute() {
               add_info_url = "http://burhanshakir.xyz/tsk_add_team1.php";
           }

           @Override
           protected String doInBackground(String... args) {
               String team1, serial, player, tId, count;
               team1 = args[0];
               serial = args[1];
               player = args[2];
               tId = args[3];
               count = args[4];

               try {
                   URL url = new URL(add_info_url);
                   HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                   httpURLConnection.setRequestMethod("POST");
                   OutputStream outputStream = httpURLConnection.getOutputStream();
                   BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                   String data_string = URLEncoder.encode("team1", "UTF-8") + "=" + URLEncoder.encode(team1, "UTF-8") + "&" +
                           URLEncoder.encode("serial", "UTF-8") + "=" + URLEncoder.encode(serial, "UTF-8") + "&" +
                           URLEncoder.encode("player", "UTF-8") + "=" + URLEncoder.encode(player, "UTF-8") + "&" +
                           URLEncoder.encode("count", "UTF-8") + "=" + URLEncoder.encode(count, "UTF-8") + "&" +
                           URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(tId, "UTF-8");

                   bufferedWriter.write(data_string);
                   bufferedWriter.flush();
                   bufferedWriter.close();
                   outputStream.close();
                   InputStream inputStream = httpURLConnection.getInputStream();
                   inputStream.close();
                   httpURLConnection.disconnect();
                   return "Adding lineup..";

               } catch (MalformedURLException e) {
                   e.printStackTrace();
               } catch (IOException e) {
                   e.printStackTrace();
               }


               return null;
           }

           @Override
           protected void onProgressUpdate(Void... values) {
               super.onProgressUpdate(values);
           }

           @Override
           protected void onPostExecute(String result) {
               //Toast.makeText(ctx,result,Toast.LENGTH_LONG).show();
           }
       }

    private class  BackgroundTask1 extends AsyncTask<String,Void,String>
    {
        String add_info_url;
        Context ctx;
        BackgroundTask1(Context ctx)
        {
            this.ctx = ctx;
        }
        @Override
        protected void onPreExecute()
        {
            add_info_url = "http://burhanshakir.xyz/tsk_add_team2.php";
        }

        @Override
        protected String doInBackground(String... args)
        {
            String team2,serial,player,tId,count;
            team2 = args[0];
            serial = args[1];
            player = args[2];
            tId = args[3];
            count = args[4];

            try {
                URL url = new URL(add_info_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data_string = URLEncoder.encode("team2","UTF-8")+"="+URLEncoder.encode(team2,"UTF-8") + "&" +
                        URLEncoder.encode("serial","UTF-8")+"="+URLEncoder.encode(serial,"UTF-8") + "&" +
                        URLEncoder.encode("player","UTF-8")+"="+URLEncoder.encode(player,"UTF-8") + "&" +
                        URLEncoder.encode("count","UTF-8")+"="+URLEncoder.encode(count,"UTF-8") + "&" +
                        URLEncoder.encode("id","UTF-8")+"="+URLEncoder.encode(tId,"UTF-8");

                bufferedWriter.write(data_string);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                inputStream.close();
                httpURLConnection.disconnect();
                return "Adding lineup..";

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
            //Toast.makeText(ctx,result,Toast.LENGTH_LONG).show();
        }
    }*/

}

