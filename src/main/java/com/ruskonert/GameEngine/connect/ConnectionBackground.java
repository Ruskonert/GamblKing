package com.ruskonert.GameEngine.connect;

import com.ruskonert.GameEngine.GameServer;
import com.ruskonert.GameEngine.entity.Player;
import javafx.concurrent.Task;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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

    public static Map<Player, DataOutputStream> clientMap = new HashMap<>();

    public void initialize() throws IOException
    {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() {
                Collections.synchronizedMap(clientMap);
                try {
                    serverSocket = new ServerSocket(7443);
                    GameServer.getServer().getConsoleSender().sendMessage("Server started by " + serverSocket.getInetAddress() + ":" +
                    serverSocket.getLocalPort());
                socket = serverSocket.accept();
                GameServer.getServer().getConsoleSender().sendMessage(socket.getInetAddress() + " join the game.");

                PlayerConnectionReceiver receiver = new PlayerConnectionReceiver(socket);
                receiver.asyncStart();
                } catch (IOException e) {
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
    private Player player;

    public PlayerConnectionReceiver(Socket socket) throws IOException {
        out = new DataOutputStream(socket.getOutputStream());
        in = new DataInputStream(socket.getInputStream());
        this.jsonReceivedMessage = in.readUTF();
        Player p = GameServer.getPlayer("");

        this.player = p;

        this.join(this.player, out);
    }

    public void join(Player player, DataOutputStream out)
    {
        GameServer.getServer().getConsoleSender().sendMessage(player.getNickname() + "님이 접속하셨습니다.");
        ConnectionBackground.clientMap.put(player, out);
    }

    public void leave(Player player)
    {
        ConnectionBackground.clientMap.remove(player);
        GameServer.getServer().getConsoleSender().sendMessage(player.getNickname() + "님이 나갔습니다.");

    }

    private Task<Void> taskBackground = new Task<Void>() {
        @Override
        protected Void call() throws Exception {
            try
            {
                jsonReceivedMessage = in.readUTF();
                GameServer.getServer().getConsoleSender().sendMessage("Data received: " + jsonReceivedMessage.length());
                //TODO you want to
            }
            catch(IOException e)
            {
                leave(player);
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
