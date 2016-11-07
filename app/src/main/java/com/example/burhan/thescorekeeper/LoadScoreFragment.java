package com.example.burhan.thescorekeeper;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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


public class LoadScoreFragment extends Fragment{

    TextView teamName1,teamName2,score1,score2;
    TextView scorer [] = new TextView[100];
    Bundle args;
    int count;


    public LoadScoreFragment() {
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

        View frag = inflater.inflate(R.layout.activity_load_score_fragment, container, false);
        args = getActivity().getIntent().getExtras();
        final String team1 = args.getString("Team 1");
        final String team2 = args.getString("Team 2");
        count = args.getInt("matches");
        final String tId =  getActivity().getIntent().getStringExtra("Tournament id");
        int i;



        teamName1 = (TextView)frag.findViewById(R.id.tvTeam1);
        teamName2 = (TextView)frag.findViewById(R.id.tvTeam2);
        score1 = (TextView) frag.findViewById(R.id.tvScore1);
        score2 = (TextView) frag.findViewById(R.id.tvScore2);
        AdView adView = (AdView) frag.findViewById(R.id.adView);

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        teamName1.setText(team1);
        teamName2.setText(team2);

        for (i=1;i<=22;i++)
        {
            String TextViewID = "tvScorer" + i;
            int resID = getResources().getIdentifier(TextViewID, "id", "com.example.burhan.thescorekeeper");
            scorer[i] = ((TextView) frag.findViewById(resID));
        }

        BackgroundTask backgroundTask = new BackgroundTask(getActivity());
        backgroundTask.execute(tId,team1,team2,team1,String.valueOf(count));

        BackgroundTask backgroundTask1 = new BackgroundTask(getActivity());
        backgroundTask1.execute(tId,team1,team2,team2,String.valueOf(count));

        BackgroundTask2 backgroundTask2 = new BackgroundTask2(getActivity());
        backgroundTask2.execute(tId,team1,team2,String.valueOf(count));
        return frag;
    }
    private class  BackgroundTask extends AsyncTask<String,Void,String>
    {
        String add_info_url;
        Context ctx;
        String team1,team2,team,tId,response = "",count;
        BackgroundTask(Context ctx)
        {
            this.ctx = ctx;
        }
        @Override
        protected void onPreExecute()
        {
            add_info_url = "http://burhanshakir.xyz/tsk_get_scorer.php";
        }

        @Override
        protected String doInBackground(String... args)
        {

            tId = args[0];
            team1 = args[1];
            team2 = args[2];
            team = args[3];
            count = args[4];


            try {
                URL url = new URL(add_info_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data_string = URLEncoder.encode("team1","UTF-8")+"="+URLEncoder.encode(team1,"UTF-8") + "&" +
                        URLEncoder.encode("team2","UTF-8")+"="+URLEncoder.encode(team2,"UTF-8") + "&" +
                        URLEncoder.encode("team","UTF-8")+"="+URLEncoder.encode(team,"UTF-8") + "&" +
                        URLEncoder.encode("count","UTF-8")+"="+URLEncoder.encode(count,"UTF-8") + "&" +
                        URLEncoder.encode("id","UTF-8")+"="+URLEncoder.encode(tId,"UTF-8");

                bufferedWriter.write(data_string);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader((new InputStreamReader(inputStream,"iso-8859-1")));

                String line = "";

                while((line = bufferedReader.readLine())!= null)
                {
                    response = response + line;
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
            int flag1 = 1,flag2=1;
            String name= "";
            for(int i =0;i<result.length();i++)
            {
                ch = result.charAt(i);
                if(ch!='+')
                {
                    name += ch;
                }
                else
                {
                    if(team.equals(team1))
                    {
                        name = "GOAL!-" + name;
                        scorer[flag1].setText(name);
                        name = "";
                        flag1++;
                    }
                    else
                    {
                        name = "GOAL!-" + name;
                        scorer[(flag2+11)].setText(name);
                        name = "";
                        flag2++;
                    }
                }
            }
        }
    }
    private class  BackgroundTask2 extends AsyncTask<String,Void,String>
    {
        String add_info_url;
        Context ctx;
        String team1,team2,count,tId,response = "";
        ProgressDialog progress = new ProgressDialog(getActivity());
        BackgroundTask2(Context ctx)
        {
            this.ctx = ctx;
        }
        @Override
        protected void onPreExecute()
        {
            add_info_url = "http://burhanshakir.xyz/tsk_get_score.php";

            progress.setMessage("Loading Scores");
            progress.show();
        }

        @Override
        protected String doInBackground(String... args)
        {

            tId = args[0];
            team1 = args[1];
            team2 = args[2];
            count = args[3];
            try {
                URL url = new URL(add_info_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data_string = URLEncoder.encode("team1","UTF-8")+"="+URLEncoder.encode(team1,"UTF-8") + "&" +
                        URLEncoder.encode("team2","UTF-8")+"="+URLEncoder.encode(team2,"UTF-8") + "&" +
                        URLEncoder.encode("count","UTF-8")+"="+URLEncoder.encode(count,"UTF-8") + "&" +
                        URLEncoder.encode("id","UTF-8")+"="+URLEncoder.encode(tId,"UTF-8");

                bufferedWriter.write(data_string);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader((new InputStreamReader(inputStream,"iso-8859-1")));

                String line = "";

                while((line = bufferedReader.readLine())!= null)
                {
                    response = response + line;
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
            int flag1 = 1;
            String name= "";
            for(int i =0;i<result.length();i++)
            {
                ch = result.charAt(i);
                if(ch!='+')
                {
                    name += ch;
                }
                else
                {

                    if(flag1==1)
                   {
                    score1.setText(name);
                       flag1++;
                       name = "";
                   }
                    else
                       score2.setText(name);
                }
            }
            progress.dismiss();
        }
    }

}
