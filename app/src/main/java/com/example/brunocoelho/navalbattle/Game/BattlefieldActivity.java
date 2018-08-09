package com.example.brunocoelho.navalbattle.Game;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.brunocoelho.navalbattle.Game.Models.Message;
import com.example.brunocoelho.navalbattle.Game.Models.Position;
import com.example.brunocoelho.navalbattle.Game.Models.Profile;
import com.example.brunocoelho.navalbattle.Game.Models.Ships.Ship;
import com.example.brunocoelho.navalbattle.Game.Models.Ships.ShipFive;
import com.example.brunocoelho.navalbattle.Game.Models.Ships.ShipOne;
import com.example.brunocoelho.navalbattle.Game.Models.Ships.ShipThree;
import com.example.brunocoelho.navalbattle.Game.Models.Ships.ShipTwo;
import com.example.brunocoelho.navalbattle.Game.Models.Team;
import com.example.brunocoelho.navalbattle.Game.NavalBattleGame;
import com.example.brunocoelho.navalbattle.Game.BattlefieldView;
import com.example.brunocoelho.navalbattle.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.brunocoelho.navalbattle.Game.Constants.PORT;
import static com.example.brunocoelho.navalbattle.Game.Constants.SERVER;

public class BattlefieldActivity extends Activity {

    private FrameLayout frameLayout;
    private BattlefieldView battlefieldView;
    private NavalBattleGame navalBattleGame;


    //online
    ServerSocket serverSocket=null;
    Socket socketGame = null;
    BufferedReader input;
    PrintWriter output;

    int mode = SERVER;
    Handler procMsg = null;
    ProgressDialog pd = null;
    Gson gson;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battlefield);


        // Check whether we're recreating a previously destroyed instance
        if (savedInstanceState != null) {
            // Restore value of members from saved state
            navalBattleGame = (NavalBattleGame) savedInstanceState.getSerializable("restoredNavalBattleGame");
        } else {
            // when is new game...
            navalBattleGame = (NavalBattleGame)getIntent().getSerializableExtra("navalBattleGame");
            navalBattleGame.setTeamsPositions();
        }

        Intent intent = getIntent();
        if (intent != null)
            mode = intent.getIntExtra("mode", SERVER);


        Log.d("MINHA", "onCreate:" + mode);

        if(navalBattleGame.isTwoPlayer())
        {
            ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo == null || !networkInfo.isConnected()) {
                Toast.makeText(this, R.string.error_netconn, Toast.LENGTH_LONG).show();
                finish();
                return;
            }


            procMsg = new Handler();
            gson = new Gson();

        }


        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(Color.parseColor("#FFDB8E"));
//        view.setBackgroundResource(R.drawable.background);

        frameLayout = findViewById(R.id.positionsField);
        battlefieldView = new BattlefieldView(this, navalBattleGame);
        frameLayout.addView(battlefieldView);

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        savedInstanceState.putSerializable("restoredNavalBattleGame", navalBattleGame);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onResume() {

        if(navalBattleGame.isTwoPlayer())
        {
            super.onResume();
            if (mode == SERVER)
                server();
            else  // CLIENT
                clientDlg();

        }
        ;

    }

    private void sendProfile() {

        final Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Profile profile = new Profile("PERFILABC_" + (new Random()).nextInt());

                    String jsonProfile = gson.toJson(profile);

                    Log.d("sendProfile", "JSONProfile which will be send:" + jsonProfile);

                    output.println(jsonProfile);
                    output.flush();
                    Log.d("sendProfile", "Sent profile");

                } catch (Exception e) {
                    Log.d("sendProfile", "Error sending a move. Error: " + e);
                }
            }
        });
        t.start();
    }


    //convert object to json and send
    public void sendObject(final Object object)
    {
        try {
            final Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {

                        //convert object to JSON
                        String jsonObject = gson.toJson(object);
                        //send json
                        output.println(jsonObject);
                        output.flush();
                        Log.d("sendObject", "Sent: " + object);
                    } catch (Exception e) {
                        Log.d("sendObject", "Error sending a move. Error: " + e);
                    }
                }
            });
            t.start();
            t.join();
        } catch (InterruptedException e) {
            Log.d("sendObject", "Error por estar à espera q acabasse de enviar mensagem completa.");

        }
    }

    public void onStartGame(View v) {

        Context context = getApplicationContext();
        CharSequence text = getResources().getString(R.string.invalid_positions);
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, text, duration);

        if(navalBattleGame.existInvalidPositions())
            toast.show();
        else
        {

            //random - 0 or 1
            //se eu sou a equipa B e estamos a jogar c 2 jogadores nao fazer... pq ja recebeu por mensagem quem e' a vez de jogar...
            if(navalBattleGame.isAmITeamA() && navalBattleGame.isTwoPlayer() || !navalBattleGame.isTwoPlayer())
                navalBattleGame.setTeamATurn(Math.random() < 0.5);


            if(navalBattleGame.isTwoPlayer())
            {
                //after clicked to start game send our game to the other player
                Log.d("onStartGame", "Sending my team...");

                Team team;

                if(navalBattleGame.isAmITeamA())
                    team = navalBattleGame.getTeamA();
                else
                    team = navalBattleGame.getTeamB();

                team.setPositionedShips(true); //flag which indicates if we have already positioned the ships... (indicates that we are ready to begin the game...)

                sendObject(team);


                Log.d("onStartGame", "Sent my team. My team: " + team);

                //the TeamA (the server) is who defines who begin playing...
                if(navalBattleGame.isAmITeamA())
                    sendObject(new Message(Constants.TEAM_A_TURN + navalBattleGame.isTeamATurn()));



                if(navalBattleGame.isAmITeamA())
                {
//                    Log.d("onStartGame", "SOU EQUIPA A portanto vou verificar se a EQUIPA B ja isPositionedShips   ");
                    team = navalBattleGame.getTeamB();
                }

                else
                {
//                    Log.d("onStartGame", "SOU EQUIPA B portanto vou verificar se a EQUIPA A ja isPositionedShips   ");
                    team = navalBattleGame.getTeamA();

                }

//                Log.d("onStartGame", "navalBattleGame.getOppositeTeam(): " + team);

                if(team.isPositionedShips())
                {
//                    Log.d("onStartGame", "Equipa adversaria tem as ships posicionadas portanto vou mandar um start game e vou começar o jogo tbm");


                    sendObject(new Message(Constants.START_GAME));
                    navalBattleGame.startGame();

                    procMsg.post(new Runnable() {
                        @Override
                        public void run() {
                            if(pd!=null)
                                pd.dismiss();
                            pd = null;
                        }
                    });
                }
                else
                {
                    pd = new ProgressDialog(this);
                    pd.setMessage("Aguardando other playa");
                    pd.setTitle(R.string.serverdlg_title);
                    pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            finish();
                            if (serverSocket!=null) {
                                try {
                                    serverSocket.close();
                                } catch (IOException e) {
                                }
                                serverSocket = null;
                                navalBattleGame.restoreData();
                            }
                        }
                    });
                    pd.show();
                }
            }

//            navalBattleGame.startGame();

            //close panels of choose positions
            LinearLayout linearLayoutChoosePanel = findViewById(R.id.choosePanel);
            linearLayoutChoosePanel.setVisibility(View.GONE);
            Button buttonStartGame = findViewById(R.id.btStartGame);
            buttonStartGame.setVisibility(View.GONE);


            //open panel of players
            LinearLayout linearLayoutPlayerPanel = findViewById(R.id.playersPanel);
            linearLayoutPlayerPanel.setVisibility(View.VISIBLE);
            Button buttonNextTurn = findViewById(R.id.btNextTurn);
            buttonNextTurn.setVisibility(View.VISIBLE);




            //if only 1 player....
            if(!navalBattleGame.isTwoPlayer())
                navalBattleGame.setAIPositions();

            battlefieldView.invalidate();
        }
    }

    public void onNextTurn(View v) {

        if(navalBattleGame.isAvaibleNextTurn())
        {
            navalBattleGame.nextTurn();
            battlefieldView.invalidate();


            navalBattleGame.setChangedShipPosition(false);
        }
    }

    public void onCloseSetPositions(View v) {

        Log.d("onCloseSetPositions", "Clicked onCloseSetPositions");

        finish();
    }



    /*
    ONLINE METHODS
     */

    void server() {
        String ip = getLocalIpAddress();
        pd = new ProgressDialog(this);
        pd.setMessage(getString(R.string.serverdlg_msg) + "\n(IP: " + ip
                + ")");
        pd.setTitle(R.string.serverdlg_title);
        pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                finish();
                if (serverSocket!=null) {
                    try {
                        serverSocket.close();
                    } catch (IOException e) {
                    }
                    serverSocket = null;
                    navalBattleGame.restoreData();

                }
            }
        });
        pd.show();

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    serverSocket = new ServerSocket(PORT);
                    socketGame = serverSocket.accept();

                    serverSocket.close();
                    serverSocket = null;
                    commThread.start();
                } catch (Exception e) {
                    e.printStackTrace();
                    socketGame = null;
                    navalBattleGame.restoreData();

                }
                procMsg.post(new Runnable() {
                    @Override
                    public void run() {
                        pd.dismiss();
                        if (socketGame == null)
                            finish();
                    }
                });
            }
        });
        t.start();
    }

    void clientDlg() {
        final EditText edtIP = new EditText(this);
        edtIP.setText("192.168.1.68"); // emulator's default ip
        AlertDialog ad = new AlertDialog.Builder(this).setTitle("RPS Client")
                .setMessage("Server IP").setView(edtIP)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        client(edtIP.getText().toString(), PORT); // to test with emulators: PORTaux);
                    }
                }).setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        finish();
                    }
                }).create();
        ad.show();
    }

    void client(final String strIP, final int Port) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("RPS", "Connecting to the server  " + strIP);
                    socketGame = new Socket(strIP, Port);
                } catch (Exception e) {
                    socketGame = null;

                }
                if (socketGame == null) {
                    procMsg.post(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                            navalBattleGame.restoreData();

                        }
                    });
                    return;
                }
                commThread.start();
            }
        });
        t.start();
    }

    Thread commThread = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                input = new BufferedReader(new InputStreamReader(
                        socketGame.getInputStream()));
                output = new PrintWriter(socketGame.getOutputStream());

                navalBattleGame.setInput(input);
                navalBattleGame.setOutput(output);

                sendProfile();

                while (!Thread.currentThread().isInterrupted()) {
                    String read = input.readLine();
                    processJSON(read);
                    Log.d("RPS", "Received: " + read);
                }
            } catch (Exception e) {
                procMsg.post(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                        Toast.makeText(getApplicationContext(),
                                R.string.game_finished, Toast.LENGTH_LONG)
                                .show();
                    }
                });
            }
        }
    });

    private void processJSON(String jsonReceived) {
        if(jsonReceived.contains(Constants.CLASS_PROFILE))
        {
            Profile profile = gson.fromJson(jsonReceived, Profile.class);
            Log.d("commThread", "Received the profile: " + profile);
        }
        else if(jsonReceived.contains(Constants.CLASS_TEAM))
        {
            Team oppositeTeam = gson.fromJson(jsonReceived, Team.class);

            Log.d("commThread", "RECEBI OPOSSITE TEAM: " + oppositeTeam);

//            if(navalBattleGame.isAmITeamA())
//                Log.d("commThread", "SOU EQUIPA A: ");
//            else
//                Log.d("commThread", "SOU EQUIPA B: ");


            navalBattleGame.defineShipsType(oppositeTeam);

            navalBattleGame.setOppositeTeam(oppositeTeam);

        }
        else if(jsonReceived.contains(Constants.CLASS_MESSAGE))
        {
            Message message = gson.fromJson(jsonReceived, Message.class);
            if(message.getContent().contains("_#"))
                processMessage(message);
        }
    }

    private void processMessage(Message message) {

        switch (message.getContent())
        {
            case Constants.START_GAME:
                //se a equipa atual tem as ships posicionadas... ou seja, ja clicou em start game e esta' 'a espera do outro...
                if(navalBattleGame.getAtualTeam().isPositionedShips())
                {
                    navalBattleGame.startGame();
                    if(pd!= null && pd.isShowing())
                    {
                        procMsg.post(new Runnable() {
                            @Override
                            public void run() {
                                pd.dismiss();
                                pd = null;
                                battlefieldView.invalidate(); //tem de ter um invalidae.... pq no cliente (teamB nao atualizava ecra)
                            }
                        });
                    }
                }
                break;
            case Constants.TEAM_A_TURN + "true":
                navalBattleGame.setTeamATurn(true);
                break;
            case Constants.TEAM_A_TURN + "false":
                navalBattleGame.setTeamATurn(false);
                break;
        }
    }


    protected void onPause() {
        super.onPause();

        super.onPause();
        try {
            commThread.interrupt();
            if (socketGame != null)
                socketGame.close();
            if (output != null)
                output.close();
            if (input != null)
                input.close();
        } catch (Exception e) {
        }
        input = null;
        output = null;
        socketGame = null;
    };

    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()
                            && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }

}
