package com.ruskonert.GameEngine.connect;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.ruskonert.GameClient.connect.PacketConnection;
import com.ruskonert.GameClient.connect.RegisterConnection;
import com.ruskonert.GameEngine.GameServer;
import com.ruskonert.GameEngine.entity.Player;
import com.ruskonert.GameEngine.framework.entity.PlayerFramework;
import com.ruskonert.GameEngine.property.ServerProperty;
import com.ruskonert.GameEngine.util.SystemUtil;
import javafx.concurrent.Task;

import java.io.*;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.Socket;

public class PlayerConnectionReceiver
{
    private DataInputStream in;
    private DataOutputStream out;
    private String jsonReceivedMessage;

    private InetAddress address;

    public PlayerConnectionReceiver(Socket socket) throws IOException
    {
        out = new DataOutputStream(socket.getOutputStream());
        in = new DataInputStream(socket.getInputStream());
        this.address = socket.getInetAddress();
        this.join(socket.getInetAddress(), out);
        GameServer.getServer().getConsoleSender().log("The class PlayerConnectionReceiver() was reinitialized from" + socket.getInetAddress().getHostAddress());
    }

    public void join(InetAddress address, DataOutputStream out)
    {
        GameServer.getServer().getConsoleSender().log(address.getHostAddress() + "이(가) 서버에 접속하셨습니다.");
        ConnectionBackground.clientMap.put(address, out);
    }

    public void leave(InetAddress address)
    {
        ConnectionBackground.clientMap.remove(address);
        GameServer.getServer().getConsoleSender().log(address.getHostAddress() + "님이 서버에서 나갔습니다.");

    }

    private Task<Void> taskBackground = new Task<Void>() {
        @Override
        protected Void call() throws Exception {
            try
            {
                while(in != null)
                {
                    jsonReceivedMessage = in.readUTF();
                    // EOF 에러 시 해당 클라이언트는 꺼진 상태입니다.
                    Gson gson = new Gson();
                    try
                    {
                        PacketConnection connection = gson.fromJson(jsonReceivedMessage, RegisterConnection.class);
                        if(connection.getStatusNumber() == ServerProperty.CHECK_REGISTER_CONNECTION)
                        {
                            RegisterConnection registerConnection = (RegisterConnection)connection;
                            String id = registerConnection.getId();
                            GameServer.getServer().getConsoleSender().sendMessage("Player requested: CHECK_REGISTER_CONNECTION=[" + id + "]");
                            if(new File("data/player/" + id + ".json").exists())
                            {
                                JsonObject error = new JsonObject();
                                error.addProperty("status","503");
                                error.addProperty("message","해당 아이디는 이미 가입되어 있습니다.");
                                out.writeUTF(error.toString());
                                continue;
                            }
                            else
                            {
                                Player newPlayer = PlayerFramework.register(registerConnection.getId(), registerConnection.getNickname(), registerConnection.getPassword());
                                GameServer.getConsoleSender().log(newPlayer.toString());
                            }
                        }
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                        SystemUtil.Companion.alert("syntax error", "오류", e.getMessage());
                    }
                }
            }
            catch(EOFException e)
            {
                leave(address);
            }
            catch(IOException e)
            {
                e.printStackTrace();
                leave(address);
            }
            return null;
        }
    };

    public void asyncStart()
    {
        Thread thread = new Thread(this.taskBackground);
        thread.start();
    }
}
