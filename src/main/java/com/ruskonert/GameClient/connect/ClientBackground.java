package com.ruskonert.GameClient.connect;

import com.ruskonert.GameEngine.entity.Player;
import com.ruskonert.GameEngine.property.ServerProperty;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientBackground
{
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    private String receivedMessage = null;

    public void connect()
    {
        try
        {
            socket = new Socket(ServerProperty.SERVER_ADDRESS, 4444);
            System.out.println("Server connected");

            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());
            System.out.println("Server message received: " + socket.getInetAddress().getHostAddress());
            while (in != null)
            {
                receivedMessage = in.readUTF();
                //TODO
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public void send(String message)
    {
        try
        {
            out.writeUTF(message);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}
