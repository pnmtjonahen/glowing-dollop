package com.hack.a.drone.net;

import com.hack.a.drone.utils.ByteUtils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class TransportConnection {

    private final String host;
    private final int port;
    private Socket socket;
    private String name;

    public TransportConnection(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void connect() throws IOException {
        if (socket != null && socket.isConnected()) {
            System.err.println("The socket is already open and connected");
            return;
        }

        InetAddress address = InetAddress.getByName(host);
        socket = new Socket(address, port);
    }

    public void setName(String name){
        this.name = name;
    }

    public void disconnect() throws IOException {
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
    }

    public void sendMessage(byte[] bytes, int responseSize) throws IOException {
        System.out.println(name + " >>> ");
        System.out.println(ByteUtils.bytesToHex(bytes));
        DataOutputStream output = new DataOutputStream(socket.getOutputStream());
        output.write(bytes);
        byte[] buffer = new byte[responseSize];
        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
        dataInputStream.read(buffer);
        System.out.println(name + " <<< ");
        System.out.println(ByteUtils.bytesToHex(buffer));
    }
}
