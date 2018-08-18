package com.example.brunocoelho.navalbattle.Profiles;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.brunocoelho.navalbattle.Game.Models.Profile;
import com.example.brunocoelho.navalbattle.R;

public class DetailsActivity extends Activity {

    private Context context;
    private Intent intent;
    private Profile profile;
    private ImageView img;
    private TextView tvName;
    private ListView list;
    private TextView sh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        context = getBaseContext();
        intent = getIntent();
        profile= (Profile) intent.getSerializableExtra("Profile");

        img = findViewById(R.id.ivDetails);
        tvName = findViewById(R.id.tvNameDetails);
        list = findViewById(R.id.lvHistory);
        sh = findViewById(R.id.tvWithouHistory);

        tvName.setText(profile.getName());
//        img.setImageResource(perfil.getImg());

        if(profile.hasHistory()) {
            sh.setVisibility(View.GONE);
            //final String[] strs = {"historico 1", "historico 2", "historico 3", "historico 4"};
            final String[] strs = profile.getTitles();
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, strs);

            list.setAdapter(adapter);

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(DetailsActivity.this);
                    builder
                            .setTitle(strs[position])
                            .setMessage(getHistoryDescription(profile.getHistory(position)))
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });

                    AlertDialog dialog = builder.create();

                    dialog.show();
                }
            });
        } else {
            list.setVisibility(View.GONE);
        }
    }

    private String getHistoryDescription(History h) {
        String str = "";

        str += "Team A: " + h.getProfileTeamA().getName() + '\n';
        str += "Team B: " + h.getProfileTeamB().getName() + '\n';
        str += "Date: " + h.getDate() + '\n';
        str += "Hour: " + h.getHour();

        switch (h.getWinner()){
            case TeamA:
                str += "\nWinner: Team A";
                break;
            case TeamB:
                str += "\nWinner: Team B";
                break;
            case Canceled:
                str += "\nThe game was cancelled.";
                break;
            case NotDefined:
                break;
        }

        return str;
    }


}
