package com.example.burhan.thescorekeeper;

import android.content.Context;
import android.content.Intent;
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


public class LoadLineupFragment extends Fragment {

    Bundle args;
    TextView teamName1,teamName2;
    String team1,team2;
    int count;
    String teamLineup[] = new String[30];
    TextView lineups[] = new TextView[200];

    public LoadLineupFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        int i;
        View frag = inflater.inflate(R.layout.activity_load_lineup_fragment, container, false);
        args = getActivity().getIntent().getExtras();
        team1 = args.getString("Team 1");
        team2 = args.getString("Team 2");
        count = args.getInt("matches");
        final String tId = getActivity().getIntent().getStringExtra("Tournament id");
        teamName1 = (TextView)frag.findViewById(R.id.tvTeam1);
        teamName2 = (TextView)frag.findViewById(R.id.tvTeam2);
        AdView adView = (AdView) frag.findViewById(R.id.adView);

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        for (i=1;i<=30;i++)
        {
            String TextViewID = "tvLineup" + i;
            int resID = getResources().getIdentifier(TextViewID, "id", "com.example.burhan.thescorekeeper");
            lineups[i] = ((TextView) frag.findViewById(resID));
        }
        teamName1.setText(team1.trim());
        teamName2.setText(team2);
        for(i =1;i<=30;i++)
        {

            if(i<=15)
            {
                String flag = String.valueOf(i);
                BackgroundTask2 backgroundTask2 = new BackgroundTask2(getActivity());
                backgroundTask2.execute(tId, team1, flag,String.valueOf(count));
            }
            else
            {
                String flag = String.valueOf((i-15));
                BackgroundTask2 backgroundTask3 = new BackgroundTask2(getActivity());
                backgroundTask3.execute(tId,team2,flag,String.valueOf(count));
            }
        }
        return frag;
    }






    private class  BackgroundTask2 extends AsyncTask<String,Void,String>
    {
        String get_url,serial,id,team,count;
        Context ctx;
        BackgroundTask2(Context ctx)
        {
            this.ctx = ctx;
        }
        @Override
        protected void onPreExecute()
        {
            get_url = "http://burhanshakir.xyz/tsk_get_lineup.php";
        }

        @Override
        protected String doInBackground(String... args)
        {

            id = args[0];
            team = args[1];
            serial = args[2];
            count = args[3];
            try {
                URL url = new URL(get_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data_string = URLEncoder.encode("id","UTF-8")+"="+URLEncoder.encode(id,"UTF-8") + "&" +
                                     URLEncoder.encode("team","UTF-8")+"="+URLEncoder.encode(team,"UTF-8") + "&" +
                                     URLEncoder.encode("count","UTF-8")+"="+URLEncoder.encode(count,"UTF-8") + "&" +
                                     URLEncoder.encode("serial","UTF-8")+"="+URLEncoder.encode(serial,"UTF-8");
                bufferedWriter.write(data_string);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader((new InputStreamReader(inputStream,"iso-8859-1")));

                String line = "";

                while((line = bufferedReader.readLine())!= null)
                {
                    teamLineup[Integer.parseInt(serial)]=line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return teamLineup[Integer.parseInt(serial)];

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
            if(team.equalsIgnoreCase(team1))
            {
                if(result != null && !result.isEmpty())

                    lineups[Integer.parseInt(serial)].setText(result);

                else
                    lineups[Integer.parseInt(serial)].setText("");
            }
            else
            {
                if(result != null && !result.isEmpty())
                    //Toast.makeText(ctx,serial,Toast.LENGTH_SHORT).show();
                    lineups[Integer.parseInt(serial)+15].setText(result);
                else
                    lineups[Integer.parseInt(serial)].setText("");
            }
            //Toast.makeText(ctx,result,Toast.LENGTH_SHORT).show();
        }
    }

}

