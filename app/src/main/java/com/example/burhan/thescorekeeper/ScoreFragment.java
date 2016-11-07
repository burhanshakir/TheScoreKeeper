package com.example.burhan.thescorekeeper;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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


public class ScoreFragment extends Fragment{

    TextView teamName1,teamName2,score1,score2;
    TextView scorer [] = new TextView[100];
    String team[] = new String[100];
    String scorers[] = new String[100];
    String group;
    int noOfgoals = 0,count;
    String type;
    Button kickoff,bgoal1,bgoal2,ok;
    Bundle args;
    int flag = 0,go1,go2, noOfPlayers1,noOfPlayers2;
    Spinner spinner;
    Dialog dialog;

    public ScoreFragment() {
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

        View frag = inflater.inflate(R.layout.activity_score_fragment, container, false);
        args = getActivity().getIntent().getExtras();
        final String team1 = args.getString("TeamName1");
        final String team2 = args.getString("TeamName2");
        final String tId =  getActivity().getIntent().getStringExtra("Tournament id");
        noOfPlayers1 = args.getInt("Players1");
        noOfPlayers2 = args.getInt("Players2");
        count = args.getInt("Matches");
        group = args.getString("Group");
        int i;
        type = getActivity().getIntent().getStringExtra("Type");
        final String l1[] = new String[noOfPlayers1];
        final String l2[] = new String[noOfPlayers2];



        for(i=0;i<noOfPlayers1;i++)
        {

            l1[i]= args.getString("Lineup"+(i+1));
        }
        for(i=0;i<noOfPlayers2;i++)
        {
            l2[i]= args.getString("Lineup"+(15+i+1));
        }


        dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog);
        dialog.setTitle("Goal Scored by..");



        teamName1 = (TextView)frag.findViewById(R.id.tvTeam1);
        teamName2 = (TextView)frag.findViewById(R.id.tvTeam2);
        kickoff = (Button) frag.findViewById((R.id.bKickOff));
        bgoal1 = (Button) frag.findViewById(R.id.bGoal1);
        bgoal2 = (Button) frag.findViewById(R.id.bGoal2);
        ok = (Button) dialog.findViewById(R.id.bOk);
        score1 = (TextView) frag.findViewById(R.id.tvScore1);
        score2 = (TextView) frag.findViewById(R.id.tvScore2);
        spinner = (Spinner) dialog.findViewById(R.id.spinnerScorer);
        /*AdView adView = (AdView) frag.findViewById(R.id.adView);

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);*/

        /*ScoreFragment fragment = new ScoreFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("flag",flag);
        fragment.setArguments(bundle);*/

        teamName1.setText(team1);
        teamName2.setText(team2);

        for (i=1;i<=22;i++)
        {
            String TextViewID = "tvScorer" + i;
            int resID = getResources().getIdentifier(TextViewID, "id", "com.example.burhan.thescorekeeper");
            scorer[i] = ((TextView) frag.findViewById(resID));
        }

        kickoff.setOnClickListener( new View.OnClickListener()
        {
            public void onClick(View v)
            {
                if(flag==0)
                {
                    kickoff.setText("Full-Time!");
                    bgoal1.setVisibility(View.VISIBLE);
                    bgoal2.setVisibility(View.VISIBLE);

                    flag++;
                }
                else
                {
                    flag++;
                    bgoal1.setVisibility(View.INVISIBLE);
                    bgoal2.setVisibility(View.INVISIBLE);

                    BackgroundTask backgroundTask = new BackgroundTask(getActivity());
                    backgroundTask.execute(team1,team2,String.valueOf(go1),String.valueOf(go2),tId,String.valueOf(count),group);

                    //Toast.makeText(getActivity(),"No of goals ="+noOfgoals,Toast.LENGTH_LONG).show();
                    for(int i = 1; i<=noOfgoals; i++)
                    {
                        BackgroundTask1 backgroundTask1 = new BackgroundTask1(getActivity());
                        backgroundTask1.execute(team1,team2,team[i],scorers[i],tId,String.valueOf(count));
                    }
                    for (int i = 0; i < noOfPlayers1; i++) {
                        String serial = String.valueOf((i+1));
                        BackgroundTask2 backgroundTask2 = new BackgroundTask2(getActivity());
                        backgroundTask2.execute(team1, serial, l1[i], tId, String.valueOf(count));
                    }
                    for (int i = 0; i < noOfPlayers2; i++) {
                        String serial = String.valueOf((i+1));
                        BackgroundTask3 backgroundTask3 = new BackgroundTask3(getActivity());
                        backgroundTask3.execute(team2, serial, l2[i], tId, String.valueOf(count));
                    }
                }
            }

        }
        );
        bgoal1.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v)
            {
                String g1 = score1.getText().toString();
                go1 = Integer.parseInt(g1);
                go1++;
                score1.setText(String.valueOf(go1));
                noOfgoals++;

                ArrayAdapter<String> adapter= new ArrayAdapter<>(getActivity().getBaseContext(),R.layout.support_simple_spinner_dropdown_item,l1);
                adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
                dialog.show();

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        String selectedItem = (String) parent.getItemAtPosition(position);
                        String f1 = score1.getText().toString();
                        int f = Integer.parseInt(f1);
                        scorer[f].setText("GOAL!-"+selectedItem);
                        scorer[f].setTypeface(null, Typeface.BOLD);
                        team[noOfgoals] = team1;
                        scorers[noOfgoals] = selectedItem;

                        ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        });
        bgoal2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v)
            {
                String g2 = score2.getText().toString();
                go2 = Integer.parseInt(g2);

                go2++;
                score2.setText(String.valueOf(go2));
                noOfgoals++;

                ArrayAdapter<String> adapter= new ArrayAdapter<>(getActivity().getBaseContext(),R.layout.support_simple_spinner_dropdown_item,l2);
                adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);

                dialog.show();

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String selectedItem = (String) parent.getItemAtPosition(position);
                        String f = score2.getText().toString();
                        int score2 = Integer.parseInt(f);
                        scorer[(11+score2)].setText("GOAL!-"+selectedItem);
                        scorer[(11+score2)].setTypeface(null, Typeface.BOLD);

                        team[noOfgoals] = team2;
                        scorers[noOfgoals] = selectedItem;
                        ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        });

        return frag;
    }
    private class  BackgroundTask extends AsyncTask<String,Void,String>
    {
        String add_info_url;
        Context ctx;
        ProgressDialog progress = new ProgressDialog(getActivity());
        BackgroundTask(Context ctx)
        {
            this.ctx = ctx;
        }
        @Override
        protected void onPreExecute()
        {
            add_info_url = "http://burhanshakir.xyz/tsk_add_score.php";

            progress.setMessage("Adding Scores. Please Wait");
            progress.show();
        }

        @Override
        protected String doInBackground(String... args)
        {
            String team1,team2,score1,score2,tId,count,group;
            team1 = args[0];
            team2 = args[1];
            score1 = args[2];
            score2 = args[3];
            tId = args[4];
            count = args[5];
            group = args[6];
            if(group == null || group.isEmpty())
                group = "";
            try {
                URL url = new URL(add_info_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data_string = URLEncoder.encode("team1","UTF-8")+"="+URLEncoder.encode(team1,"UTF-8") + "&" +
                        URLEncoder.encode("team2","UTF-8")+"="+URLEncoder.encode(team2,"UTF-8") + "&" +
                        URLEncoder.encode("score1","UTF-8")+"="+URLEncoder.encode(score1,"UTF-8") + "&" +
                        URLEncoder.encode("score2","UTF-8")+"="+URLEncoder.encode(score2,"UTF-8") + "&" +
                        URLEncoder.encode("count","UTF-8")+"="+URLEncoder.encode(count,"UTF-8") + "&" +
                        URLEncoder.encode("group","UTF-8")+"="+URLEncoder.encode(group,"UTF-8") + "&" +
                        URLEncoder.encode("id","UTF-8")+"="+URLEncoder.encode(tId,"UTF-8");

                bufferedWriter.write(data_string);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                inputStream.close();
                httpURLConnection.disconnect();
                return "Information Saved!";

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
            Toast.makeText(ctx,result,Toast.LENGTH_LONG).show();
            progress.dismiss();
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
            add_info_url = "http://burhanshakir.xyz/tsk_add_scorer.php";
        }

        @Override
        protected String doInBackground(String... args)
        {
            String team1,team2,team,scorer,tId,count;
            team1 = args[0];
            team2 = args[1];
            tId = args[4];
            team = args[2];
            scorer = args[3];
            count = args[5];
            try {
                URL url = new URL(add_info_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data_string = URLEncoder.encode("team1","UTF-8")+"="+URLEncoder.encode(team1,"UTF-8") + "&" +
                        URLEncoder.encode("team2","UTF-8")+"="+URLEncoder.encode(team2,"UTF-8") + "&" +
                        URLEncoder.encode("team","UTF-8")+"="+URLEncoder.encode(team,"UTF-8") + "&" +
                        URLEncoder.encode("scorer","UTF-8")+"="+URLEncoder.encode(scorer,"UTF-8") + "&" +
                        URLEncoder.encode("count","UTF-8")+"="+URLEncoder.encode(count,"UTF-8") + "&" +
                        URLEncoder.encode("id","UTF-8")+"="+URLEncoder.encode(tId,"UTF-8");

                bufferedWriter.write(data_string);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                inputStream.close();
                httpURLConnection.disconnect();
                return "Adding scorers..";

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
    }
    private class  BackgroundTask2 extends AsyncTask<String,Void,String> {
        String add_info_url;
        Context ctx;

        BackgroundTask2(Context ctx) {
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

    private class  BackgroundTask3 extends AsyncTask<String,Void,String>
    {
        String add_info_url;
        Context ctx;
        BackgroundTask3(Context ctx)
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
    }


}
