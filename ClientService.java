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

public class ClientService extends Service implements NsdManager.DiscoveryListener {

    NsdManager mNsdManager = (NsdManager) getSystemService(getApplicationContext().NSD_SERVICE);
    Messenger messengeClient;
    Message msg;

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
        super.onStartCommand(intent, flags, startId);
        Bundle bundle = intent.getExtras();
        messengeClient = (Messenger) bundle.get("messageclient");
        msg = Message.obtain();
        if(mNsdManager == null){
            mNsdManager = (NsdManager) getSystemService(getApplicationContext().NSD_SERVICE);
            mNsdManager.discoverServices("_http._tcp",  NsdManager.PROTOCOL_DNS_SD, this);
        }


        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mNsdManager.stopServiceDiscovery(this);
    }

    @Override
    public void onStartDiscoveryFailed(String serviceType, int errorCode) {

    }

    @Override
    public void onStopDiscoveryFailed(String serviceType, int errorCode) {

    }

    @Override
    public void onDiscoveryStarted(String serviceType) {

    }

    @Override
    public void onDiscoveryStopped(String serviceType) {

    }

    @Override
    public void onServiceFound(NsdServiceInfo serviceInfo) {

        serviceInfo.getServiceName();
        serviceInfo.getServiceType();

        NsdManager.ResolveListener mResolveListener = new NsdManager.ResolveListener() {
            public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {

            }
            public void onServiceResolved(NsdServiceInfo serviceInfo) {

                serviceInfo.getServiceName();
                serviceInfo.getPort();
                serviceInfo.getHost().getHostAddress();

            }
        };
        mNsdManager.resolveService(serviceInfo, mResolveListener);

    }

    @Override
    public void onServiceLost(NsdServiceInfo serviceInfo) {

    }
}
