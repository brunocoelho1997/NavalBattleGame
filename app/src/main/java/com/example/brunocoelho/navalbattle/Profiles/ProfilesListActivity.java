package com.example.brunocoelho.navalbattle.Profiles;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.brunocoelho.navalbattle.Game.Models.Profile;
import com.example.brunocoelho.navalbattle.R;

import java.util.ArrayList;

public class ProfilesListActivity extends AppCompatActivity {

    private ArrayList<Profile> profiles;
    private Context context;
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiles_list);

        context = getBaseContext();
        final Intent intent = getIntent();

        profiles = File.loadProfiles(context);

        if(profiles.isEmpty())
        {
            Profile profile = new Profile("Bruno");
            profile.setSelected(false);
            profiles.add(profile);

            profile = new Profile("David");
            profile.setSelected(true);
            profiles.add(profile);

            profile = new Profile("Joaquim");
            profiles.add(profile);


            Log.d("ProfilesListActivity", "Added Profile Bruno");

        }


        lv = findViewById(R.id.lvProfiles);
        lv.setAdapter(new MyAdapter());
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Intent novoIntent = new Intent(context, DetalhesActivity.class);
//                novoIntent.putExtra("Profile", profiles.get(i));
//                startActivity(novoIntent);
            }
        });


        if(!intent.getBooleanExtra("NovoJogo", false)) {
            lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ProfilesListActivity.this);

                    String mensagem = getResources().getString(R.string.delete_profile);
                    builder
                            .setTitle(R.string.delete_profile_title)
                            .setMessage(mensagem + " " + profiles.get(position).getName() + "?")
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    profiles.remove(position);
                                    lv.invalidateViews();
                                }
                            })
                            .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });

                    AlertDialog dialog = builder.create();

                    dialog.show();
                    return true;
                }
            });
        }
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return profiles.size();
        }

        @Override
        public Object getItem(int i) {
            return profiles.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View layout = getLayoutInflater().inflate(R.layout.profiles_list_item,null);

            ((TextView)layout.findViewById(R.id.tvName_Profile_Item)).setText(profiles.get(i).getName());
//            ((ImageView)layout.findViewById(R.id.ivItem_lista)).setImageResource(profiles.get(i).getImg());

            if(profiles.get(i).isSelected())
            {
                ((Button)layout.findViewById(R.id.btn_Profile_Item)).setVisibility(View.INVISIBLE);
                layout.setBackgroundColor(Color.parseColor("#9cd17b"));

            }
            return layout;
        }
    }

    @Override // to create our own menu
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list_profiles, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override // when select a item of the menu
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.newProfile:
                Intent intent = new Intent(getBaseContext(), NewProfileActivity.class);
                startActivityForResult(intent, 1);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1) {
            if(resultCode == RESULT_OK) {
                if(data.hasExtra("NewProfile")) {
                    profiles.add((Profile) data.getSerializableExtra("NewProfile"));
                    lv.invalidateViews();
                }
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        File.saveProfiles(context, profiles);
    }

    public void myClickHandler(View v)
    {

        //reset all the listView items background colours
        //before we set the clicked one..


        for (int i=0; i < lv.getChildCount(); i++)
        {
//            Log.d("ProfilesListActivity", "" + i + ": " + lv.getChildAt(i).getBackground());
            if(lv.getChildAt(i).getBackground()!=null)
            {
                lv.getChildAt(i).setBackground(null);
                ((Button)lv.getChildAt(i).findViewById(R.id.btn_Profile_Item)).setText(R.string.use_profile);
                ((Button)lv.getChildAt(i).findViewById(R.id.btn_Profile_Item)).setVisibility(View.VISIBLE);
                profiles.get(i).setSelected(false);

            }
        }

        //get the row the clicked button is in
        LinearLayout vwParentRow = (LinearLayout)v.getParent();
        vwParentRow.setBackgroundColor(Color.parseColor("#9cd17b"));

        ((Button)vwParentRow.findViewById(R.id.btn_Profile_Item)).setVisibility(View.INVISIBLE);
        ((Button)vwParentRow.findViewById(R.id.btn_Profile_Item)).setText("I've been clicked!");


        for (int i=0; i < lv.getChildCount(); i++)
        {
            if(lv.getChildAt(i).getBackground()!=null)
                profiles.get(i).setSelected(true);

        }

        vwParentRow.refreshDrawableState();
    }
}
