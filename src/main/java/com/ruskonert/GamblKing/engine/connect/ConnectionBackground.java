package com.ruskonert.GamblKing.engine.connect;

import com.ruskonert.GamblKing.connect.packet.RoomRefreshPacket;
import com.ruskonert.GamblKing.engine.GameServer;
import com.ruskonert.GamblKing.entity.Player;
import com.ruskonert.GamblKing.entity.Room;
import com.ruskonert.GamblKing.property.ServerProperty;

import javafx.concurrent.Task;
import java.io.DataOutputStream;
import java.io.IOException;
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

    private static Map<String, DataOutputStream> clientMap = new HashMap<>();
    public static Map<String, DataOutputStream> getClientMap() { return clientMap; }

    // 게임 클라이언트 리시버러를 Player별로 나눕니다.
    private static Map<Player, Socket> gameClientMap = new ConcurrentHashMap<>();
    public static Map<Player, Socket> getGameClientMap() { return gameClientMap; }
    public static DataOutputStream getPlayerOutputStream(Player player) {
        try {
            return new DataOutputStream(gameClientMap.get(player).getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 클라이언트 리시버를 IP 별로 나눕니다.
    private static Map<String, DataOutputStream> updateClientMap = new HashMap<>();
    public static Map<String, DataOutputStream> getUpdateClientMap() { return updateClientMap; }


    // 게임 방과 관련한 쓰레드 작업을 돌립니다.
    private static Thread roomThread;
    public static Thread getRoomThread() { return ConnectionBackground.roomThread; }

    private static Map<Player, Room> roomMap = new ConcurrentHashMap<>();
    public static Map<Player, Room> getRoomMap() { return roomMap; }


    public static void refreshRoom(Player target)
    {
        RoomRefreshPacket packet = new RoomRefreshPacket(roomMap.values().toArray(new Room[roomMap.size()]));
        try {
            packet.send(getGameClientMap().get(target).getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


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
                    GameServer.getServer().getConsoleSender().log("Server open failed: Port is used another service or The Server was already running");
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
                    GameServer.getServer().getConsoleSender().log("Server open failed: Port is used another service or The update server was already running");
                }
                return null;
            }
        };

        Task<Void> roomTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception
            {
                while(true)
                {
                    for (Player p : ConnectionBackground.getGameClientMap().keySet()) {
                        ConnectionBackground.refreshRoom(p);
                    }
                    Thread.sleep(3000L);
                    return null;
                }
            }
        };


        Thread thread = new Thread(task);
        thread.start();

        Thread updateThread = new Thread(updateTask);
        updateThread.start();

        Thread rThread = new Thread(roomTask);
        ConnectionBackground.roomThread = rThread;

        rThread.start();

    }

    public void start() throws IOException
    {
        this.initialize();
    }
}