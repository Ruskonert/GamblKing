package com.ruskonert.GamblKing.engine.connect;

import com.google.gson.*;
import com.ruskonert.GamblKing.client.ClientLoader;
import com.ruskonert.GamblKing.client.connect.packet.LoginConnectionPacket;
import com.ruskonert.GamblKing.client.connect.packet.RegisterConnectionPacket;
import com.ruskonert.GamblKing.engine.GameServer;
import com.ruskonert.GamblKing.engine.entity.Player;
import com.ruskonert.GamblKing.engine.framework.entity.PlayerFramework;
import com.ruskonert.GamblKing.property.ServerProperty;
import com.ruskonert.GamblKing.util.SecurityUtil;
import com.ruskonert.GamblKing.util.SystemUtil;
import javafx.concurrent.Task;

import java.io.*;
import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ServerConnectionReceiver
{
    private DataInputStream in;
    private DataOutputStream out;
    private String jsonReceivedMessage;

    private InetAddress address;

    public ServerConnectionReceiver(Socket socket) throws IOException
    {
        out = new DataOutputStream(socket.getOutputStream());
        in = new DataInputStream(socket.getInputStream());
        this.address = socket.getInetAddress();
        this.join(socket.getInetAddress(), out);
        GameServer.getServer().getConsoleSender().log("The class ServerConnectionReceiver() was reinitialized from" + socket.getInetAddress().getHostAddress());
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

    private void sendRegisterSocket(int status, String message)
    {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("status", status);
        jsonObject.addProperty("requestIp", address.getHostName());
        jsonObject.addProperty("message", message);
        this.send(jsonObject.toString());
    }

    private void send(String message)
    {
        try
        {
            out.writeUTF(message);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private <T> T getFramework(String jsonMessage, Class<T> type)
    {
        Gson gson = new Gson();
        return gson.fromJson(jsonMessage, type);
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
                    try
                    {
                        // 요청받은 메세지를 JsonObject로 가져옵니다.
                        JsonObject jo = getFramework(jsonReceivedMessage, JsonObject.class);
                        int connectionNumber = jo.get("status").getAsInt();
                        if(connectionNumber == ServerProperty.REQUEST_LOGIN)
                        {
                            LoginConnectionPacket loginConnection = getFramework(jsonReceivedMessage, LoginConnectionPacket.class);
                            String id = loginConnection.getId();
                            GameServer.getServer().getConsoleSender().sendMessage("Received the requesting: REQUEST_LOGIN_SIGNAL=[" + id + "]:" + address.getHostName());
                            if(new File("data/player/" + id + ".json").exists())
                            {

                                BufferedReader reader = new BufferedReader(new FileReader(new File("data/player/" + id + ".json")));
                                String jsonMessage = reader.readLine();

                                Player player = getFramework(jsonMessage, PlayerFramework.class);

                                if(player.getPassword().equalsIgnoreCase(SecurityUtil.Companion.sha256(loginConnection.getPassword())))
                                    sendRegisterSocket(ServerProperty.RECEVIED_LOGIN_SUCCESS, "로그인에 성공하였습니다.");
                                else
                                    sendRegisterSocket(ServerProperty.RECEIVED_LOGIN_FAILED, "비밀번호가 틀립니다. 비밀번호를 분실했을 경우 그냥 포기하세요.\n찾는 기능 그딴 것 없습니다 :D");
                            }
                            else
                            {
                                sendRegisterSocket(ServerProperty.RECEIVED_LOGIN_FAILED, "존재하지 않는 아이디입니다.");
                            }
                        }

                        // 로그인이 성공했을 경우입니다.
                        if(connectionNumber == ServerProperty.RECEVIED_LOGIN_SUCCESS)
                        {
                            JsonObject jo2 = getFramework(jsonReceivedMessage, JsonObject.class);
                            GameServer.getConsoleSender().sendMessage(jo2.get("message").toString());
                        }

                        if(connectionNumber == ServerProperty.CHECK_REGISTER_CONNECTION)
                        {
                            RegisterConnectionPacket registerConnection = getFramework(jsonReceivedMessage, RegisterConnectionPacket.class);
                            String id = registerConnection.getId();
                            GameServer.getServer().getConsoleSender().sendMessage("Player requested: CHECK_REGISTER_CONNECTION=[" + id + "]");
                            if(new File("data/player/" + id + ".json").exists())
                            {
                                JsonObject error = new JsonObject();
                                error.addProperty("status",ServerProperty.REGISTER_FAILED_ACCOUNT);
                                error.addProperty("message","해당 아이디는 이미 가입되어 있습니다.");
                                out.writeUTF(error.toString());
                                continue;
                            }
                            else
                            {
                                Player newPlayer = PlayerFramework.register(registerConnection.getId(), registerConnection.getNickname(), registerConnection.getPassword());
                                GameServer.getConsoleSender().log(newPlayer.toString());
                                JsonObject success = new JsonObject();
                                success.addProperty("status", ServerProperty.REGISTER_SUCCESSED_ACCOUNT);
                                success.addProperty("message", "회원가입에 성공하였습니다! 이제 로그인하시면 됩니다.\n아아디: " + newPlayer.getId());
                                out.writeUTF(success.toString());
                            }
                        }

                        // 업데이트 서버에 파일 목록에 대한 요청 받아 서버 내 업데이트 파일을 다시 보냄
                        if(connectionNumber == ServerProperty.SEND_UPDATE_REQURST)
                        {
                            Gson gsonSerialize = new GsonBuilder().registerTypeAdapter(Map.class, new JsonSerializer<Map<String, File>>() {
                                JsonObject obj = new JsonObject();
                                @Override
                                public JsonElement serialize(Map<String, File> src, Type typeOfSrc, JsonSerializationContext context)
                                {
                                   for(String s : src.keySet())
                                   {
                                       obj.addProperty(s, src.get(s).getPath());
                                   }
                                   return obj;
                                }
                            }).create();
                            out.writeUTF(gsonSerialize.toJson(Update.getUpdateFiles()));
                        }

                        // 작동에 필요한 파일 목록을 가져오고 Update server socket을 열어달라는 요청을 보냅니다.
                        if(connectionNumber == ServerProperty.SEND_UPDATE_FILE_REQUEST)
                        {
                            String s = jo.get("data").toString();
                            String[] split = s.split(",");
                            List<String> l = new ArrayList();
                            for(String s2 : split)
                            {
                                l.add(s2);
                            }
                            l.remove(l.size() - 1);
                            sendFileByteSocket(l.toArray(new String[l.size()]));
                        }
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                        SystemUtil.Companion.alert("예외 오류 발생", "오류가 발생했습니다. 고쳐야 할텐데..", e.getMessage());
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

    private void sendFileByteSocket(String[] hashes)
    {
        Socket socket = ClientLoader.getBackgroundConnection().getSocket();
        try
        {
        BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());
        FileInputStream filein;
            for (String hash : hashes)
            {
                filein = new FileInputStream(Update.getUpdateFiles().get(hash));
                byte[] buffer = new byte[8192];
                int bytesRead = 0;
                while ((bytesRead = filein.read(buffer)) > 0) { out.write(buffer, 0, bytesRead); }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    public void asyncStart()
    {
        Thread thread = new Thread(this.taskBackground);
        thread.start();
    }
}
