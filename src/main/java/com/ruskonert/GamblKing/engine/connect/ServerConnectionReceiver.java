package com.ruskonert.GamblKing.engine.connect;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import com.ruskonert.GamblKing.connect.packet.LoginConnectionPacket;
import com.ruskonert.GamblKing.connect.packet.RegisterConnectionPacket;
import com.ruskonert.GamblKing.engine.GameServer;
import com.ruskonert.GamblKing.engine.framework.entity.PlayerFramework;

import com.ruskonert.GamblKing.entity.Player;
import com.ruskonert.GamblKing.property.ServerProperty;
import com.ruskonert.GamblKing.util.SecurityUtil;
import com.ruskonert.GamblKing.util.SystemUtil;

import javafx.application.Platform;
import javafx.concurrent.Task;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class ServerConnectionReceiver
{
    private static DataInputStream in;
    public static DataInputStream getInputStream() { return in; }

    private static DataOutputStream out;
    public static DataOutputStream getOutputStream() { return out; }

    private String jsonReceivedMessage;
    private InetAddress address;

    public ServerConnectionReceiver(Socket socket) throws IOException
    {
        out = new DataOutputStream(socket.getOutputStream());
        in = new DataInputStream(socket.getInputStream());
        this.address = socket.getInetAddress();
        this.join(socket.getInetAddress(), out);
        GameServer.getServer().getConsoleSender().log("The class ServerConnectionReceiver() was initialized from " + socket.getInetAddress().getHostAddress());
    }

    public void join(InetAddress address, DataOutputStream out)
    {
        GameServer.getServer().getConsoleSender().log(address.getHostAddress() + " joined the login server");
        ConnectionBackground.getClientMap().put(address, out);
    }

    public void leave(InetAddress address)
    {
        ConnectionBackground.getClientMap().remove(address);
        GameServer.getServer().getConsoleSender().log(address.getHostAddress() + " leaved the login server");
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


    private void sendRegisterSocket(int status, String message)
    {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("status", status);
        jsonObject.addProperty("requestIp", address.getHostName());
        jsonObject.addProperty("message", message);
        this.send(jsonObject.toString());
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

                        // 로그인이 성공했을 경우입니다. 이것은 클라이언트에서 업데이트 서버와 연결할 준비를 한다는 것과 같습니다.
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
                                GameServer.getConsoleSender().log("Generating about information: " + newPlayer.toString());
                                JsonObject success = new JsonObject();
                                success.addProperty("status", ServerProperty.REGISTER_SUCCESSED_ACCOUNT);
                                success.addProperty("message", "회원가입에 성공하였습니다! 이제 로그인하시면 됩니다.\n아아디: " + newPlayer.getId());
                                out.writeUTF(success.toString());
                            }
                        }

                        // 클라이언트에서 파일 목록을 요청받고 서버 내 업데이트 파일의 대한 정보를 보냅니다.
                        // 이때, UpdateConnectionReceiver로 보내어 업데이트 포트로 정보를 보냅니다.
                        if(connectionNumber == ServerProperty.SEND_UPDATE_REQURST)
                        {
                            Gson gsonSerialize = new Gson();
                            JsonObject object = new JsonObject();

                            object.addProperty("status", ServerProperty.SEND_UPDATE_REQURST_RECEIVED);
                            object.addProperty("data", SystemUtil.Companion.fixHashMap(gsonSerialize.toJson(Update.getUpdateFiles()).toString()));
                            UpdateConnectionReceiver.getOutputStream().writeUTF(object.toString());
                        }


                        /*
                        // 클라이언트에서 필요한 파일 목록을 가져오고 Update server socket을 열어달라는 요청을 보냅니다.
                        if(connectionNumber == ServerProperty.SEND_UPDATE_FILE_REQUEST)
                        {
                            String path = jo.get("path").getAsString();
                            Platform.runLater(() -> UpdateSender.start(jo.get("ipAddress").toString(), 8888, path));
                            UpdateConnectionReceiver.getOutputStream().writeUTF(object.toString());
                        }
                        */

                        // 업데이트가 모두 끝나고 게임 서버에 연결합니다.
                        if(connectionNumber == ServerProperty.CONNECT_GAME_SERVER)
                        {
                            String id = jo.get("id").getAsString();
                            Player player = GameServer.getPlayer(id);
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

    public void asyncStart()
    {
        Thread thread = new Thread(this.taskBackground);
        thread.start();
    }
}
