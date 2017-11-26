package com.ruskonert.GamblKing.engine.connect;

import com.ruskonert.GamblKing.engine.GameServer;
import com.ruskonert.GamblKing.engine.connect.packet.RoomRefreshPacket;
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
    private static Map<Player, DataOutputStream> gameClientMap = new ConcurrentHashMap<>();
    public static Map<Player, DataOutputStream> getGameClientMap() { return gameClientMap; }
    public static DataOutputStream getPlayerOutputStream(Player player)
    {
        if(gameClientMap.get(player) == null)
        {
            for(Player p : getGameClientMap().keySet())
            {
                if(p.getId().equalsIgnoreCase(player.getId()))
                {
                    return gameClientMap.get(p);
                }
            }
            return null;
        }
        else
        {
            return gameClientMap.get(player);
        }
    }

    // 플레이어 정보를 새로 저장합니다.
    public static void refreshPlayer(Player player)
    {
        for(Player p : getGameClientMap().keySet())
        {
            if(p.getId().equalsIgnoreCase(player.getId()) && p.getNickname().equalsIgnoreCase(player.getNickname()))
            {
                DataOutputStream stream = getGameClientMap().get(p);
                getGameClientMap().remove(p);
                getGameClientMap().put(player, stream);
                break;
            }
        }
    }

    // 클라이언트 리시버를 IP 별로 나눕니다.
    private static Map<String, DataOutputStream> updateClientMap = new HashMap<>();
    public static Map<String, DataOutputStream> getUpdateClientMap() { return updateClientMap; }


    // 게임 방과 관련한 쓰레드 작업을 돌립니다.
    private static Thread roomThread;
    public static Thread getRoomThread() { return ConnectionBackground.roomThread; }

    private static Map<Player, Room> roomMap = new ConcurrentHashMap<>();
    public static Map<Player, Room> getRoomMap() { return roomMap; }

    public static Player getPlayerFromStream(DataOutputStream stream)
    {
        for(Player p : gameClientMap.keySet())
        {
            DataOutputStream o = gameClientMap.get(p);
            if(o == stream)
                return p;
        }
        return null;
    }



    public synchronized static void refreshRoom(Player target)
    {
        try {
            if (target.isEnteredRoom()) return;
            RoomRefreshPacket packet = new RoomRefreshPacket(roomMap.values().toArray(new Room[roomMap.size()]));
            packet.send(getGameClientMap().get(target));
        }
        catch(NullPointerException e)
        {
            e.printStackTrace();
        }
    }

    public synchronized static void refreshRoom(DataOutputStream o)
    {
        try{
            if (getPlayerFromStream(o).isEnteredRoom()) return;
            RoomRefreshPacket packet = new RoomRefreshPacket(roomMap.values().toArray(new Room[roomMap.size()]));
            packet.send(o);
        }
        catch(NullPointerException e)
        {
            e.printStackTrace();
        }
    }


    public static void leaveFromStream(DataOutputStream stream)
    {
        for(Player p : ConnectionBackground.getGameClientMap().keySet())
        {
            if(ConnectionBackground.getGameClientMap().get(p) == stream || ConnectionBackground.getUpdateClientMap().get(p.getHostAddress()) == stream)
            {
                String address = p.getHostAddress();
                ConnectionBackground.getRoomMap().remove(p);
                ConnectionBackground.getClientMap().remove(address);
                ConnectionBackground.getUpdateClientMap().remove(address);
                ConnectionBackground.getGameClientMap().remove(p);
                return;
            }
        }
    }


    public void initialize()
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


        /**
         *
         */
        Task<Void> roomTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception
            {
                while(true)
                {
                    Player p = null;
                    try {
                        for (Player p2 : ConnectionBackground.getGameClientMap().keySet()) {
                            p = p2;
                            ConnectionBackground.refreshRoom(p2);
                            Thread.sleep(3700L);
                        }
                    }
                    catch(RuntimeException e)
                    {

                        // 최종적으로 오류를 검증합니다.
                        // 만약 이 플레이어가 방까지 만들었다면, 제거합니다.
                        ConnectionBackground.getRoomMap().remove(p);
                        // 게임을 비정상적으로 나간 경우입니다. 이런 경우 강제로 없앱니다.
                        ConnectionBackground.getGameClientMap().remove(p);
                        e.printStackTrace();
                    }
                }
            }
        };


        Thread thread = new Thread(task);
        thread.start();

        Thread updateThread = new Thread(updateTask);
        updateThread.start();

        ConnectionBackground.roomThread = new Thread(roomTask);

        roomThread.start();

    }

    public void start() throws IOException
    {
        this.initialize();
    }
}