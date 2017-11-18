package com.ruskonert.GamblKing.engine.connect;

import com.ruskonert.GamblKing.connect.ClientReceiver;
import com.ruskonert.GamblKing.engine.GameServer;
import com.ruskonert.GamblKing.entity.Player;
import com.ruskonert.GamblKing.property.ServerProperty;

import javafx.concurrent.Task;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class ConnectionBackground
{
    private ServerSocket serverSocket;
    private Socket socket;

    private ServerSocket updateServerSocket;
    private Socket updateSocket;

    private static Map<InetAddress, DataOutputStream> clientMap = new HashMap<>();
    public static Map<InetAddress, DataOutputStream> getClientMap() { return clientMap; }

    private static Map<Player, ClientReceiver> gameClientMap = new ConcurrentHashMap<>();
    public static Map<Player, ClientReceiver> getGameClientMap() { return gameClientMap; }

    private static Map<InetAddress, DataOutputStream> updateClientMap = new HashMap<>();
    public static Map<InetAddress, DataOutputStream> getUpdateClientMap() { return updateClientMap; }


    public void initialize() throws IOException
    {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() {
                Collections.synchronizedMap(clientMap);
                try
                {
                    serverSocket = new ServerSocket(ServerProperty.SERVER_PORT);
                    GameServer.getServer().getConsoleSender().log("Login server started by " + serverSocket.getInetAddress().getHostAddress() + ":" +
                            serverSocket.getLocalPort());
                    while(true)
                    {
                        socket = serverSocket.accept();
                        GameServer.getServer().getConsoleSender().log(socket.getInetAddress().getHostAddress() + " requested connecting the login server");

                        ServerConnectionReceiver receiver = new ServerConnectionReceiver(socket);
                        receiver.asyncStart();
                    }
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                    GameServer.getServer().getConsoleSender().log("Server open failed: Port is closed or Server is already running");
                }
                return null;
            }
        };

        Task<Void> updateTask = new Task<Void>()
        {
            @Override
            protected Void call()
            {
                Collections.synchronizedMap(updateClientMap);
                try
                {
                    updateServerSocket = new ServerSocket(ServerProperty.SERVER_UPDATE_PORT);
                    GameServer.getServer().getConsoleSender().log("Update server started by " + updateServerSocket.getInetAddress().getHostAddress() + ":" +
                            updateServerSocket.getLocalPort());
                    while(true)
                    {
                        updateSocket = updateServerSocket.accept();
                        GameServer.getServer().getConsoleSender().log(socket.getInetAddress().getHostAddress() + " requested connecting the update server");

                        UpdateConnectionReceiver receiver = new UpdateConnectionReceiver(updateSocket);
                        receiver.asyncStart();
                    }
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                    GameServer.getServer().getConsoleSender().log("Server open failed: Port is closed or Update server is already running");
                }
                return null;
            };
        };

        Thread thread = new Thread(task);
        thread.start();

        Thread updateThread = new Thread(updateTask);
        updateThread.start();

    }

    public void start() throws IOException
    {
        this.initialize();
    }
}