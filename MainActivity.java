package com.julien.tp12bis;

import android.app.Activity;
import android.content.Intent;
import android.net.nsd.NsdServiceInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
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
    private TextView textViewServiceinfo, ndsServiceStarted, textViewMessageerceivedFromClient, DiscoveringStart, textViewMyRunnableservice, onServiceResolved;
    private int socketServerPort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            super.handleMessage(msg);
            Bundle bundle = msg.getData();


        }
    };

    private Handler handlerClient = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            DiscoveringStart.setText("le service de decouverte a débuté ");
            socketServerPort = bundle.getInt("clientSocket");
            onServiceResolved.setText("le service trouvé et connecté: ");
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
                    Socket socket = new Socket("192.168.1.12",socketServerPort);
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
