package com.ruskonert.GameEngine.connect;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.ruskonert.GameClient.connect.PacketConnection;
import com.ruskonert.GameClient.connect.RegisterConnection;
import com.ruskonert.GameEngine.GameServer;
import com.ruskonert.GameEngine.entity.Player;
import com.ruskonert.GameEngine.property.ServerProperty;
import com.ruskonert.GameEngine.util.SystemUtil;
import javafx.concurrent.Task;

import java.io.DataInputStream;
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
                    serverSocket = new ServerSocket(4444);
                    GameServer.getServer().getConsoleSender().log("Server started by " + serverSocket.getInetAddress().getHostAddress() + ":" +
                    serverSocket.getLocalPort());
                    socket = serverSocket.accept();
                    GameServer.getServer().getConsoleSender().log(socket.getInetAddress().getHostAddress() + " join the game.");

                    PlayerConnectionReceiver receiver = new PlayerConnectionReceiver(socket);
                    receiver.asyncStart();
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

class PlayerConnectionReceiver
{
    private DataInputStream in;
    private DataOutputStream out;
    private String jsonReceivedMessage;

    public PlayerConnectionReceiver(Socket socket) throws IOException
    {
        out = new DataOutputStream(socket.getOutputStream());
        in = new DataInputStream(socket.getInputStream());

        this.join(socket.getInetAddress(), out);
    }

    public void join(InetAddress address, DataOutputStream out)
    {
        GameServer.getServer().getConsoleSender().log(address.getHostAddress() + "이(가) 접속하셨습니다.");
        ConnectionBackground.clientMap.put(address, out);
    }

    public void leave(Player player)
    {
        ConnectionBackground.clientMap.remove(player);
        GameServer.getServer().getConsoleSender().log(player.getNickname() + "님이 나갔습니다.");

    }

    private Task<Void> taskBackground = new Task<Void>() {
        @Override
        protected Void call() throws Exception {
            try
            {
                while(in != null)
                {
                    jsonReceivedMessage = in.readUTF();
                    System.out.println(jsonReceivedMessage + " Read in.");
                    Gson gson = new Gson();
                    try
                    {
                        PacketConnection connection = gson.fromJson(jsonReceivedMessage, RegisterConnection.class);
                        System.out.println(jsonReceivedMessage + "read reaer");

                        if(connection.getStatusNumber() == ServerProperty.CHECK_REGISTER_CONNECTION)
                        {
                            RegisterConnection registerConnection = (RegisterConnection)connection;
                            String id = registerConnection.getId();
                            GameServer.getServer().getConsoleSender().sendMessage("Player requested: CHECK_REGISTER_CONNECTION=[" + id + "]");

                        }
                    }
                    catch(JsonSyntaxException e)
                    {
                        SystemUtil.Companion.alert("syntax error", "오류", e.getMessage());
                    }
                }
            }
            catch(IOException e)
            {
                GameServer.getServer().getConsoleSender().log("leaved the game.");
            }
            return null;
        }
    };

    public void asyncStart()
    {
        Task<Void> task = this.taskBackground;
        Thread thread = new Thread(task);
        thread.start();
    }
}
