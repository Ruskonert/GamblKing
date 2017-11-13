package com.ruskonert.GameEngine.connect;

import com.ruskonert.GameEngine.GameServer;
import com.ruskonert.GameEngine.property.ServerProperty;
import javafx.concurrent.Task;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ConnectionBackground
{
    private ServerSocket serverSocket;
    private Socket socket;
    private String jsonRequestMessage;

    public static Map<InetAddress, DataOutputStream> clientMap = new HashMap<>();

    public void initialize() throws IOException
    {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() {
                Collections.synchronizedMap(clientMap);
                try
                {
                    serverSocket = new ServerSocket(ServerProperty.SERVER_PORT);
                    GameServer.getServer().getConsoleSender().log("Server started by " + serverSocket.getInetAddress().getHostAddress() + ":" +
                            serverSocket.getLocalPort());
                    while(true)
                    {
                        socket = serverSocket.accept();
                        GameServer.getServer().getConsoleSender().log(socket.getInetAddress().getHostAddress() + " 서버에 연결 요청함");

                        PlayerConnectionReceiver receiver = new PlayerConnectionReceiver(socket);
                        receiver.asyncStart();
                    }
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                return null;
            }
        };
        Thread thread = new Thread(task);
        thread.start();
    }

    public ConnectionBackground()
    {

    }

    public void start() throws IOException
    {
        this.initialize();
    }


}