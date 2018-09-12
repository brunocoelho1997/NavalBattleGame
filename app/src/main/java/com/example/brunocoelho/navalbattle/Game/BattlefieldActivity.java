package com.example.brunocoelho.navalbattle.Game;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.brunocoelho.navalbattle.Game.Models.Message;
import com.example.brunocoelho.navalbattle.Game.Models.Position;
import com.example.brunocoelho.navalbattle.Profiles.Profile;
import com.example.brunocoelho.navalbattle.Game.Models.Team;
import com.example.brunocoelho.navalbattle.Profiles.Result;
import com.example.brunocoelho.navalbattle.R;
import com.google.gson.Gson;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.security.Permission;
import java.util.Enumeration;

import static com.example.brunocoelho.navalbattle.Game.Constants.PORT;
import static com.example.brunocoelho.navalbattle.Game.Constants.SERVER;

public class BattlefieldActivity extends Activity {

    private FrameLayout frameLayout;
    private BattlefieldView battlefieldView;
    private NavalBattleGame navalBattleGame;

    private Context context;

    //online
    ServerSocket serverSocket=null;
    Socket socketGame = null;
    ObjectInputStream input = null;
    ObjectOutputStream output = null;

    int mode = SERVER;
    Handler procMsg = null;
    ProgressDialog pd = null;
    Gson gson;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battlefield);

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/mastodontus.ttf");
        ((TextView)findViewById(R.id.teamAName)).setTypeface(font);
        ((TextView)findViewById(R.id.teamBName)).setTypeface(font);
        ((TextView)findViewById(R.id.tvChoosePositions)).setTypeface(font);
        ((Button)findViewById(R.id.btStartGame)).setTypeface(font);
        ((Button)findViewById(R.id.btNextTurn)).setTypeface(font);




        this.context = getBaseContext();

        // Check whether we're recreating a previously destroyed instance
        if (savedInstanceState != null) {
            // Restore value of members from saved state
            navalBattleGame = (NavalBattleGame) savedInstanceState.getSerializable("restoredNavalBattleGame");

            if(navalBattleGame.isStarted())
            {
                Button buttonNextTurn = findViewById(R.id.btNextTurn);
                Button buttonStartGame = findViewById(R.id.btStartGame);

                buttonNextTurn.setVisibility(View.VISIBLE);
                buttonStartGame.setVisibility(View.GONE);
            }
        } else {
            // when is new game...
            navalBattleGame = (NavalBattleGame)getIntent().getSerializableExtra("navalBattleGame");
            navalBattleGame.setTeamsPositions();
        }

        Intent intent = getIntent();
        if (intent != null)
            mode = intent.getIntExtra("mode", SERVER);


//        Log.d("MINHA", "onCreate:" + mode);

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
        battlefieldView = new BattlefieldView(this, navalBattleGame, output);
        frameLayout.addView(battlefieldView);

        setProfilePanels();
    }

    private void setProfilePanels() {
        LinearLayout teamAPanel = findViewById(R.id.teamAPanel);
        LinearLayout teamBPanel = findViewById(R.id.teamBPanel);

        TextView textView = teamAPanel.findViewById(R.id.teamAName);
        ImageView imageView = teamAPanel.findViewById(R.id.teamAImage);
        if(navalBattleGame.getProfileTeamA()!= null)
        {
            textView.setText(navalBattleGame.getProfileTeamA().getName());
            if(navalBattleGame.getProfileTeamA().getFilePathPhoto()!=null) {
                try {
                    imageView.setImageBitmap(navalBattleGame.getProfileTeamA().getImage(context,50,50));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }

            //its AI thus we need to define the icon...
            if(!navalBattleGame.isTwoPlayer() && !navalBattleGame.isAmITeamA())
            {
                imageView.setImageResource(0);
                imageView.setBackgroundResource(Constants.ICON_AI);
            }
        }

        textView = teamBPanel.findViewById(R.id.teamBName);
        imageView = teamBPanel.findViewById(R.id.teamBImage);
        if(navalBattleGame.getProfileTeamB()!= null)
        {
            textView.setText(navalBattleGame.getProfileTeamB().getName());


            if(navalBattleGame.getProfileTeamB().getFilePathPhoto()!=null)
            {
                try {
                    imageView.setImageBitmap(navalBattleGame.getProfileTeamB().getImage(context,50,50));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }

            //its AI thus we need to define the icon...
            if(!navalBattleGame.isTwoPlayer() && navalBattleGame.isAmITeamA())
            {
                imageView.setImageResource(0);
                imageView.setBackgroundResource(Constants.ICON_AI);
            }
        }

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
        super.onResume();

        if(navalBattleGame.isTwoPlayer())
        {
            if (mode == SERVER)
                server();
            else  // CLIENT
                clientDlg();
        }
    }



    private void sendProfile() {

        final Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final Profile profile = navalBattleGame.getSelectedProfile();

                    String jsonProfile = gson.toJson(profile);

                    Log.d("sendProfile", "JSONProfile which will be send:" + jsonProfile);

                    output.writeObject(jsonProfile);
                    output.flush();
                    Log.d("sendProfile", "Sent profile");

                    if(profile.getFilePathPhoto()!=null)
                    {
                        Log.d("sendProfile", "JSONProfile which will be send has an image in:" + profile.getFilePathPhoto());

                        sendPhoto(profile.getFilePathPhoto());

                    }

                } catch (Exception e) {
                    Log.d("sendProfile", "Error sending a profile. Error: " + e);
                }
            }
        });
        t.start();
    }


    //convert object to json and send
    public void sendObject(final Object object)
    {
        final Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    //convert object to JSON
                    String jsonObject = gson.toJson(object);
                    //send json
                    output.writeObject(jsonObject);
                    output.flush();
                    Log.d("sendObject", "Sent: " + object);
                } catch (Exception e) {
                    Log.d("sendObject", "Error sending a move. Error: " + e);
                }
            }
        });
        t.start();
    }

    public void onStartGame(View v) {

        Context context = getApplicationContext();
        CharSequence text = getResources().getString(R.string.invalid_positions);
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, text, duration);


        //if only 1 player....
        if(!navalBattleGame.isTwoPlayer())
            navalBattleGame.setAIPositions();


        if(navalBattleGame.existInvalidPositions())
            toast.show();
        else
        {

            //random - 0 or 1
            //se eu sou a equipa B e estamos a jogar c 2 jogadores nao fazer... pq ja recebeu por mensagem quem e' a vez de jogar...
            if(navalBattleGame.isAmITeamA() && navalBattleGame.isTwoPlayer() || !navalBattleGame.isTwoPlayer())
            {
//                boolean aux = Math.random() < 0.5;

                boolean aux = true;

                navalBattleGame.setTeamATurn(aux);
            }

            if(!navalBattleGame.isTwoPlayer())
                navalBattleGame.startGame();
            else
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

                    //delay para chegar la primeiro a equipa contraria (a indicar q as posicoes estao definidas)... so dps e q mandamos start game...
                    procMsg.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            sendObject(new Message(Constants.START_GAME));
                            navalBattleGame.startGame();
                            battlefieldView.invalidate(); //after send start game need to invalidate view...

                            procMsg.post(new Runnable() {
                                @Override
                                public void run() {
                                    if(pd!=null)
                                        pd.dismiss();
                                    pd = null;
                                }
                            });

                        }
                    }, Constants.DELAY);
                }
                else
                {
                    pd = new ProgressDialog(this);
                    pd.setMessage(getString(R.string.waiting_other_player));
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


            processNextTurnButton();

//            if(navalBattleGame.isMyTurnToPlay())
//                buttonNextTurn.setBackgroundColor(Color.parseColor("#238EA6"));
//            else
//                buttonNextTurn.setBackgroundColor(Color.parseColor("#EE6C4D"));


            battlefieldView.invalidate();
        }
    }

    public void onNextTurn(View v) {

        if(navalBattleGame.isAvaibleNextTurn())
        {
            Button buttonNextTurn = findViewById(R.id.btNextTurn);

            if(navalBattleGame.isTeamATurn())
                buttonNextTurn.setBackgroundColor(Color.parseColor("#EE6C4D"));
            else
                buttonNextTurn.setBackgroundColor(Color.parseColor("#238EA6"));

            if(!navalBattleGame.isTwoPlayer())
            {
                if(navalBattleGame.isMyTurnToPlay())
                    buttonNextTurn.setText(R.string.next_turn_ai_fired);
                else
                    buttonNextTurn.setText(R.string.next_turn);
            }


            navalBattleGame.setChangedShipPosition(false);
            navalBattleGame.setMayChangeShipPosition(false);
            navalBattleGame.nextTurn();
            battlefieldView.invalidate();

            if(navalBattleGame.isTwoPlayer())
                sendObject(new Message(Constants.NEXT_TURN));
        }
        else
        {
            if(navalBattleGame.isMyTurnToPlay())
                Toast.makeText(getApplicationContext(),
                        R.string.fire_3_positions, Toast.LENGTH_LONG)
                        .show();
            else
                Toast.makeText(getApplicationContext(),
                        R.string.waiting_other_player, Toast.LENGTH_LONG)
                        .show();
        }
    }

    public void onCloseBattlefield(View v) {

        Log.d("onCloseSetPositions", "Clicked onCloseSetPositions");
        navalBattleGame.setTwoPlayer(false);
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
        AlertDialog ad = new AlertDialog.Builder(this).setTitle(R.string.client_dlg_title)
                .setMessage(R.string.client_dlg_text).setView(edtIP)
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
//                input = new BufferedReader(new InputStreamReader(socketGame.getInputStream()));
//                output = new PrintWriter(socketGame.getOutputStream());

                output = new ObjectOutputStream(socketGame.getOutputStream());
                input = new ObjectInputStream(socketGame.getInputStream());

//                navalBattleGame.setInput(input);
//                navalBattleGame.setOutput(output);
                battlefieldView.setOutput(output);

                sendProfile();

                while (!Thread.currentThread().isInterrupted()) {

                    final Object objectReceived = input.readObject();

                    if(objectReceived instanceof String)
                    {
                        Log.d("commThread", "Received JSON: " + (String)objectReceived);
                        processJSON((String)objectReceived);

                    }
                }
            } catch (Exception e) {
                procMsg.post(new Runnable() {
                    @Override
                    public void run() {
                        //finish();



                        //if isn't a winner defined the game continue
                        if(navalBattleGame.getHistory().getWinner().equals(Result.NotDefined))
                        {

                            Profile ai = navalBattleGame.generateAIProfile();
                            if(navalBattleGame.isAmITeamA())

                                navalBattleGame.setProfileTeamB(ai);

                            else
                                navalBattleGame.setProfileTeamA(ai);

                            Toast.makeText(getApplicationContext(),
                                    R.string.game_finished, Toast.LENGTH_LONG)
                                    .show();
                            navalBattleGame.setTwoPlayer(false);

                            setProfilePanels();

                            //if isn't my turn to play... need to AI playing...
                            if(!navalBattleGame.isMyTurnToPlay())
                            {
                                int msg = R.string.ai_fired_positions;

                                if(navalBattleGame.getOppositeTeam().isPositionedShips())
                                {
                                    navalBattleGame.AIFire();
                                    msg = R.string.ai_fired_positions;
                                }
                                else
                                {
                                    navalBattleGame.setAIPositions();
                                    msg = R.string.ai_setted_positions;
                                }

                                Toast.makeText(getApplicationContext(),
                                        msg, Toast.LENGTH_LONG)
                                        .show();

                                battlefieldView.invalidate();
                            }
                        }
                    }
                });
            }
        }
    });


    int bytesRead;

//    private void receivePhoto(byte[] buffer)
//    {
//
//
//            try {
//
//                String filePathPhoto;
//
//                if(navalBattleGame.isAmITeamA())
//                    filePathPhoto = navalBattleGame.getProfileTeamB().getFilePathPhoto();
//                else
//                    filePathPhoto = navalBattleGame.getProfileTeamA().getFilePathPhoto();
//
//
//                Log.d("receivePhoto", "filePathPhoto: " + filePathPhoto);
//
//
//                if(os==null)
//                {
//                    Log.d("receivePhoto", "criei um file em:" + filePathPhoto);
//                    java.io.File imageFile = new java.io.File(filePathPhoto);
//                    imageFile.createNewFile();
//                    os = new FileOutputStream(imageFile);
//                }
//
//
//
//
//
//                if(buffer == null)
//                {
//                    //flush OutputStream to write any buffered data to file
//                    os.flush();
//                    os.close();
////                    os = null;
//
//                    setProfilePanels();
//                }
//                else
//                {
//
//                    bytesRead = buffer.length;
//
////                    if(Build.VERSION.SDK_INT>22){
////                        requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
////                    }
//
//                    os.write(buffer, 0, bytesRead);
//                }
//
//
////                    //read from is to buffer
////                    while((bytesRead = is.read(buffer)) !=-1){
////                        os.write(buffer, 0, bytesRead);
////                    }
//
//
//
//
//            } catch (Exception e) {
//                Log.d("receivePhoto", "Error receiving photo a profile. Error: " + e);
//            }
//
//
//
//    }
//
////    @Override
////    public void onRequestPermissionsResult(int requestCode,
////                                           String permissions[], int[] grantResults) {
////        switch (requestCode) {
////            case 1: {
////
////                // If request is cancelled, the result arrays are empty.
////                if (grantResults.length > 0
////                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
////
////                    // permission was granted, yay! Do the
////                    // contacts-related task you need to do.
////                } else {
////
////                    // permission denied, boo! Disable the
////                    // functionality that depends on this permission.
////                    Toast.makeText(this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
////                }
////                return;
////            }
////
////            // other 'case' lines to check for other
////            // permissions this app might request
////        }
////    }

    private void processJSON(String jsonReceived) {

        String bytes;

        if(jsonReceived.contains(Constants.CLASS_PROFILE))
        {
            final Profile profile = gson.fromJson(jsonReceived, Profile.class);
            Log.d("commThread", "Received the profile: " + profile);

            if(profile.getFilePathPhoto()!=null)
                receivePhoto(profile.getFilePathPhoto());

            procMsg.postDelayed(new Runnable() {
                @Override
                public void run() {
                    navalBattleGame.setProfile(profile);
                    setProfilePanels();
                }
            }, Constants.DELAY);

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
        else if(jsonReceived.contains(Constants.CLASS_POSITION))
        {
            final Position onUPosition = gson.fromJson(jsonReceived, Position.class);

            procMsg.postDelayed(new Runnable() {
                @Override
                public void run() {
                    navalBattleGame.onUpOtherTeam(onUPosition);
//                    Log.d("commThread", "getFiredPositionsTemp: " + navalBattleGame.getFiredPositionsTemp());

                    battlefieldView.invalidate();
                }
            }, Constants.DELAY);
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
                processNextTurnButton();



                break;
            case Constants.TEAM_A_TURN + "false":
                navalBattleGame.setTeamATurn(false);
                processNextTurnButton();



                break;
            case Constants.NEXT_TURN:

                procMsg.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        ((Button)findViewById(R.id.btNextTurn)).setText(R.string.next_turn);

                        navalBattleGame.nextTurn();
                        processNextTurnButton();



                        battlefieldView.invalidate();
                        navalBattleGame.setChangedShipPosition(false);
                    }
                }, Constants.DELAY);


                break;
        }
    }

    void processNextTurnButton()
    {
        Button buttonNextTurn = findViewById(R.id.btNextTurn);
        if(navalBattleGame.isTeamATurn())
            buttonNextTurn.setBackgroundColor(Color.parseColor("#238EA6"));
        else
            buttonNextTurn.setBackgroundColor(Color.parseColor("#EE6C4D"));

//        if(!navalBattleGame.isMyTurnToPlay())
//            buttonNextTurn.setText(R.string.waiting_other_player);
//        else
//            buttonNextTurn.setText(R.string.waiting_other_player);

    }
    private void receivePhoto(String filePathPhoto) {

        try {
            Log.d("receivePhoto", "receivePhoto: perfil com imagem");

            java.io.File imageFile = new java.io.File(filePathPhoto);
            imageFile.createNewFile();
            FileOutputStream fout = null;
            fout = new FileOutputStream(imageFile);

            byte [] buffer = new byte[Constants.BYTES];
            int nBytes;

            while ((nBytes = input.read(buffer, 0, Constants.BYTES)) != -1) {
                fout.write(buffer, 0, nBytes);
                Log.d("receivePhoto", "receivePhoto: recebidos = " + nBytes + " bytes");
            }

            if (nBytes == -1) {
                input.readObject();
                Log.d("receivePhoto", "receivePhoto: fim da receção");
            }

            fout.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }

    private void sendPhoto(String filePathPhoto) {

        try {
            java.io.File imageFile = new java.io.File(filePathPhoto);
            FileInputStream fin = null;
            fin = new FileInputStream(imageFile);
            byte [] buffer = new byte[Constants.BYTES];
            int nBytes;
            while ((nBytes = fin.read(buffer)) != -1) {
                output.write(buffer, 0, nBytes);
                output.flush();
                Log.d("sendPhoto", "sendPhoto: enviados = " + nBytes + " bytes");
            }
            fin.close();
            Log.d("sendPhoto", "waitClient: ficheiro enviado");

            output.writeObject(null);
            output.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void onPause() {
        super.onPause();

        if(navalBattleGame.isTwoPlayer())
        {
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
        }
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
