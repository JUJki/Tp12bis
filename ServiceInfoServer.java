package com.julien.tp12bis;

import java.io.Serializable;

/**
 * Created by julien on 31/05/2016.
 */
public class ServiceInfoServer implements Serializable {

    private Integer port;
    private String address;
    private boolean statut;
    private String host;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isStatut() {
        return statut;
    }

    public void setStatut(boolean statut) {
        this.statut = statut;
    }

    public ServiceInfoServer() {
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
