package com.julien.tp12bis;

import android.app.Service;
import android.content.Intent;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServeurCommunication extends Service implements NsdManager.RegistrationListener {
    Messenger messager;
    ServerSocket socketAddress;
    NsdServiceInfo serviceInfo;
    NsdManager mNsdManager;
    private String SERVICE_NAME = "Deptinfo";
    private String SERVICE_TYPE = "_http._tcp";
    private Integer mLocalPort;
    ServiceInfoServer serviceInfoServer;

    public ServeurCommunication() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Bundle extras = intent.getExtras();
        messager = (Messenger) extras.get("messageserver");
        Message msg = Message.obtain();
        Bundle bundle = new Bundle();
        try {
            socketAddress = new ServerSocket(0);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        mLocalPort = socketAddress.getLocalPort();
        socketAddress.getLocalSocketAddress();
        serviceInfo = new NsdServiceInfo();
        serviceInfo.setServiceName(SERVICE_NAME);
        serviceInfo.setServiceType(SERVICE_TYPE);
        serviceInfo.setPort(mLocalPort);
        mNsdManager = (NsdManager) getSystemService(getApplicationContext().NSD_SERVICE);
        mNsdManager.registerService(serviceInfo,  NsdManager.PROTOCOL_DNS_SD, this);
        serviceInfoServer = new ServiceInfoServer();
        serviceInfoServer.setPort(mLocalPort);
        serviceInfoServer.setName(SERVICE_NAME);
        if(mLocalPort != null){
            serviceInfoServer.setStatut(true);
        }
        else{
            serviceInfoServer.setStatut(false);
        }
        //initUIThread(serviceInfo.getServiceName(),serviceInfo.getPort(),true);

            bundle.putSerializable("serveurservice", serviceInfoServer);
            msg.setData(bundle);
            try {
                messager.send(msg);
            }
            catch (RemoteException e){

            }
        return 1;
    }


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Message msg = Message.obtain();
        Bundle bundle = new Bundle();
        bundle.putBoolean("stopService", true);
        msg.setData(bundle);
        try {
            messager.send(msg);
        }
        catch (RemoteException e){

        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    class connectSocket implements Runnable {
        @Override
        public void run() {
            try {
                socketAddress = new ServerSocket(0);
                mLocalPort = socketAddress.getLocalPort();
            } catch (IOException e) {
                e.printStackTrace();
            }
            while (!Thread.currentThread().isInterrupted()) {

            }
        }
    }

    @Override
    public void onRegistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {

    }

    @Override
    public void onUnregistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {

    }

    @Override
    public void onServiceRegistered(NsdServiceInfo serviceInfor) {
        serviceInfo.setServiceName(serviceInfor.getServiceName());

    }

    @Override
    public void onServiceUnregistered(NsdServiceInfo serviceInfo) {
        mNsdManager.unregisterService(this);

    }



}
