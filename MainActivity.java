package com.julien.tp12bis;

import android.app.Activity;
import android.content.Intent;
import android.net.nsd.NsdServiceInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;


public class MainActivity extends Activity  {


    private TextView tvServiceInfo;
    private NsdServiceInfo serviceInfo;
    private TextView textViewNdsServiceStart;
    private String stateserveur;
    private TextView textViewServiceinfo, ndsServiceStarted, textViewMessageerceivedFromClient, DiscoveringStart, textViewMyRunnableservice, onServiceResolved,textViewConnecting,textViewOnServiceResolved;
    private int socketServerPort;
    private String hostSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        initView();

    }
    private void initView(){
        onServiceResolved = (TextView) findViewById(R.id.onServiceResolved);
        DiscoveringStart = (TextView) findViewById(R.id.DiscoveringStart);
        ndsServiceStarted = (TextView) findViewById(R.id.ndsServiceStarted);
        textViewServiceinfo = (TextView) findViewById(R.id.textViewServiceinfo);
        textViewNdsServiceStart = (TextView) findViewById(R.id.ndsServiceStarted);
        textViewMessageerceivedFromClient = (TextView) findViewById(R.id.messageReceivingFromClient);
        textViewMyRunnableservice = (TextView) findViewById(R.id.myRunnableServiceService);
        textViewConnecting =(TextView) findViewById(R.id.connecting);
        textViewOnServiceResolved =(TextView) findViewById(R.id.onServiceResolved);

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            if (bundle.getSerializable("serveurservice") != null) {
                ServiceInfoServer serviceinfoServer = (ServiceInfoServer) bundle.getSerializable("serveurservice");
                if(serviceinfoServer.isStatut()){
                    stateserveur = "actif";
                }
                else{
                    stateserveur = "inactif";
                }
                if (serviceinfoServer != null) {
                    textViewServiceinfo.setText("le service à débuté sur le port : " + serviceinfoServer.getPort());
                    ndsServiceStarted.setText("le service est : " + stateserveur);
                    textViewMyRunnableservice.setText("le service de registration est : " + serviceinfoServer.getPort());
                }
            }
            if(bundle.getBoolean("stopService")){
                stateserveur = "inactif";
                ndsServiceStarted.setText("le service est : " + stateserveur);
                textViewServiceinfo.setText("le service est arreté");
            }
            if (bundle.getString("serveurSetSocketMessage") != null) {
                textViewMessageerceivedFromClient.setText("Le message : " + bundle.getString("serveurSetSocketMessage"));
            }
        }
    };

    private Handler handlerClient = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            if (bundle.getSerializable("serveurclient") != null) {
                Log.i("testtt","testt");
                ServiceInfoServer serviceinfoServer = (ServiceInfoServer) bundle.getSerializable("serveurclient");
                DiscoveringStart.setText("le service de decouverte a débuté ");
                socketServerPort = serviceinfoServer.getPort();
                hostSocket = serviceinfoServer.getHost();
                textViewConnecting.setText("le service trouvé est connecté au port "+ serviceinfoServer.getPort()+" et à l'adresse "+serviceinfoServer.getHost());
                /*textViewOnServiceResolved.setText("Le service est connecté au host :" +serviceinfoServer.getHost());*/
                onServiceResolved.setText("le service trouvé est connecté: " + serviceinfoServer.getName());
            }
            if(bundle.getBoolean("stopService")){
                DiscoveringStart.setText("le service de decouverte est arreté ");
                textViewOnServiceResolved.setText("");
                textViewConnecting.setText("");
                onServiceResolved.setText("");
            }
        }
    };

    public void startServiceDerverDNS_SD(View view) {
        Messenger messager = new Messenger(handler);
        Intent serviceServeurDSN = new Intent(this, ServeurCommunication.class);
        serviceServeurDSN.putExtra("messageserver", messager);
        this.startService(serviceServeurDSN);
    }

    public void stopServiceServerDND_SD(View view) {
        Intent intent = new Intent(this, ServeurCommunication.class);
        stopService(intent);
    }


    public void startDiscoring(View view){
        Messenger messager = new Messenger(handlerClient);
        Intent intentClient = new Intent();
        intentClient.setClass(this,ClientService.class);
        intentClient.putExtra("messageclient", messager);
        startService(intentClient);
    }

    public void stopDiscovering(View view){
        Intent intent = new Intent(this, ClientService.class);
        stopService(intent);
    }


    @Override
    protected void onResume() {
        super.onResume();

    }


    @Override
    protected void onPause() {
        super.onPause();


    }

    public void sedMessage(View view){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket socket = new Socket(hostSocket,socketServerPort);
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(socket.getOutputStream())
                    );
                    writer.write("Bonjour connection à votre service");
                    writer.flush();
                    writer.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
