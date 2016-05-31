package com.julien.tp12bis;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

public class ClientService extends Service implements NsdManager.DiscoveryListener {

    NsdManager mNsdManager;
    Messenger messengeClient;
    Message msg;
    ServiceInfoServer serviceInfoServer;

    public ClientService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.i("aaaaaaa","aaaaaaa");
        Bundle bundle = intent.getExtras();
        messengeClient = (Messenger) bundle.get("messageclient");
        msg = Message.obtain();
        if(mNsdManager == null){
            mNsdManager = (NsdManager) getSystemService(getApplicationContext().NSD_SERVICE);
            mNsdManager.discoverServices("_http._tcp",  NsdManager.PROTOCOL_DNS_SD, this);
        }
        return 1;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mNsdManager.stopServiceDiscovery(this);
        Message msg = Message.obtain();
        Bundle bundle = new Bundle();
        bundle.putBoolean("stopService", true);
        msg.setData(bundle);
        try {
            messengeClient.send(msg);
        }
        catch (RemoteException e){

        }

    }

    @Override
    public void onStartDiscoveryFailed(String serviceType, int errorCode) {
        Log.i("aaaaaaa","1");
    }

    @Override
    public void onStopDiscoveryFailed(String serviceType, int errorCode) {
        Log.i("aaaaaaa","2");
    }

    @Override
    public void onDiscoveryStarted(String serviceType) {
        Log.i("aaaaaaa","3");
        Log.i("aaaaaaa",serviceType);
    }

    @Override
    public void onDiscoveryStopped(String serviceType) {
        Log.i("aaaaaaa","4");
    }

    @Override
    public void onServiceFound(NsdServiceInfo serviceInfo) {
        Log.i("aaaaaaa","5");
        NsdManager.ResolveListener mResolveListener = new NsdManager.ResolveListener() {
            String localHost;
            int localPort;
            public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {

            }
            public void onServiceResolved(NsdServiceInfo serviceInfo) {

                localHost = serviceInfo.getHost().getHostAddress();
                localPort = serviceInfo.getPort();
                Message msg = Message.obtain();
                Bundle bundle = new Bundle();
                serviceInfoServer = new ServiceInfoServer();
                serviceInfoServer.setPort(localPort);
                serviceInfoServer.setName(serviceInfo.getServiceName());
                serviceInfoServer.setHost(localHost);
                bundle.putSerializable("serveurclient", serviceInfoServer);
                msg.setData(bundle);
                try {
                    messengeClient.send(msg);
                }
                catch (RemoteException e){

                }
               /* try {
                    /*initUIThread(serviceInfo.getServiceName(),serviceInfo.getPort(),true);*/
                /*} catch (RemoteException e) {
                    e.printStackTrace();
                }*/
            }
        };
        String test = serviceInfo.getServiceName();
        if (test.equals("Deptinfo")) {
            mNsdManager.resolveService(serviceInfo, mResolveListener);
        }
    }

    @Override
    public void onServiceLost(NsdServiceInfo serviceInfo) {

    }
}
