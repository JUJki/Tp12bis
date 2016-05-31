package com.julien.tp12bis;

import android.app.Service;
import android.content.Intent;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import java.io.IOException;
import java.net.ServerSocket;

public class ServeurCommunication extends Service implements NsdManager.RegistrationListener {
    Messenger messager;
    ServerSocket socketAddress;
    NsdServiceInfo serviceInfo;
    private String SERVICE_NAME = "Deptinfo";
    private String SERVICE_TYPE = "_http._tcp";
    private Integer mLocalPort;
    NsdManager mNsdManager = (NsdManager) getSystemService(getApplicationContext().NSD_SERVICE);
    public ServeurCommunication() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Bundle extras = intent.getExtras();
        messager = (Messenger) extras.get("messageserver");
        Message msg = Message.obtain();
        Bundle bundle = new Bundle();
        serviceInfo = new NsdServiceInfo(); 
        serviceInfo.setServiceName(SERVICE_NAME);
        serviceInfo.setServiceType(SERVICE_TYPE);
        serviceInfo.setPort(mLocalPort);
        mNsdManager.registerService(serviceInfo,  NsdManager.PROTOCOL_DNS_SD, this);

       /* bundle.putInt("serveur", 1);
        msg.setData(bundle);
        try {
            messager.send(msg);
        }
        catch (RemoteException e){

        }*/
        return 1;
    }

    public boolean getState(){
        if(socketAddress == null){
            return false;
        }
        else {
            return true;
        }
    }

   /* public String getAdress(){

        return socketAddress.getLocalSocketAddress();
    }*/

    public int getPort(){

        return socketAddress.getLocalPort();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Runnable connect = new connectSocket();
        new Thread(connect).start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
