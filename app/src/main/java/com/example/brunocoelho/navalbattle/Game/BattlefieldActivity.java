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

import com.example.brunocoelho.navalbattle.Game.Models.Position;
import com.example.brunocoelho.navalbattle.Game.Models.Ships.Ship;
import com.example.brunocoelho.navalbattle.Game.Models.Ships.ShipFive;
import com.example.brunocoelho.navalbattle.Game.Models.Ships.ShipOne;
import com.example.brunocoelho.navalbattle.Game.Models.Ships.ShipThree;
import com.example.brunocoelho.navalbattle.Game.Models.Ships.ShipTwo;
import com.example.brunocoelho.navalbattle.Game.NavalBattleGame;
import com.example.brunocoelho.navalbattle.Game.BattlefieldView;
import com.example.brunocoelho.navalbattle.R;

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
import java.util.Timer;
import java.util.TimerTask;

import static com.example.brunocoelho.navalbattle.Game.Constants.PORT;
import static com.example.brunocoelho.navalbattle.Game.Constants.SERVER;

public class BattlefieldActivity extends Activity {

    private FrameLayout frameLayout;
    private BattlefieldView battlefieldView;
    private NavalBattleGame navalBattleGame;


    //online

    int mode = SERVER;
    Handler procMsg = null;
    ProgressDialog pd = null;




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
                    Log.d("sendProfile", "Sending profile: PERFILABC EHEHE");
                    navalBattleGame.getOutput().println("PERFILABC EHEHE");
                    navalBattleGame.getOutput().flush();
                    Log.d("sendProfile", "Sent profile");

                } catch (Exception e) {
                    Log.d("sendProfile", "Error sending a move. Error: " + e);
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

        if(navalBattleGame.existInvalidPositions())
            toast.show();
        else
        {
            navalBattleGame.startGame();

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


            //random - 0 or 1
            navalBattleGame.setTeamATurn(Math.random() < 0.5);

            navalBattleGame.setAIPositions();

            battlefieldView.invalidate();
            Log.d("onStartGame", "Game Started. TeamA playing:" + navalBattleGame.isTeamATurn());
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
                if (navalBattleGame.getServerSocket()!=null) {
                    try {
                        navalBattleGame.getServerSocket().close();
                    } catch (IOException e) {
                    }
                    navalBattleGame.setServerSocket(null);
                }
            }
        });
        pd.show();

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    navalBattleGame.setServerSocket(new ServerSocket(PORT));
                    navalBattleGame.setSocketGame(navalBattleGame.serverSocket.accept());

                    navalBattleGame.getServerSocket().close();
                    navalBattleGame.setServerSocket(null);
                    commThread.start();
                } catch (Exception e) {
                    e.printStackTrace();
                    navalBattleGame.setSocketGame(null);
                }
                procMsg.post(new Runnable() {
                    @Override
                    public void run() {
                        pd.dismiss();
                        if (navalBattleGame.getSocketGame() == null)
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
                    navalBattleGame.setSocketGame(new Socket(strIP, Port));
                } catch (Exception e) {
                    navalBattleGame.setSocketGame(null);

                }
                if (navalBattleGame.getSocketGame() == null) {
                    procMsg.post(new Runnable() {
                        @Override
                        public void run() {
                            finish();
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

                navalBattleGame.setInput(new BufferedReader(new InputStreamReader(
                        navalBattleGame.getSocketGame().getInputStream())));

                navalBattleGame.setOutput(new PrintWriter(navalBattleGame.getSocketGame().getOutputStream()));

                Log.d("commThread", "Pronta para receber e enviar mensagens...");

                sendProfile();

                Log.d("commThread", "Enviei perfil...");


                while (!Thread.currentThread().isInterrupted()) {
                    String read = navalBattleGame.getInput().readLine();
//                    final int move = Integer.parseInt(read);
                    Log.d("RPS", "Received: " + read);
//                    procMsg.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            moveOtherPlayer(move);
//                        }
//                    });
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



    protected void onPause() {
        super.onPause();
        try {
            commThread.interrupt();
            if (navalBattleGame.getSocketGame() != null)
                navalBattleGame.getSocketGame().close();
            if (navalBattleGame.getOutput()!= null)
                navalBattleGame.getOutput().close();
            if (navalBattleGame.getInput()!= null)
                navalBattleGame.getInput().close();
        } catch (Exception e) {
        }
        navalBattleGame.setInput(null);
        navalBattleGame.setOutput(null);
        navalBattleGame.setSocketGame(null);
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
