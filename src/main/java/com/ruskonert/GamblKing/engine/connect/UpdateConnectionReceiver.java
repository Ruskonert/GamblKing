package com.ruskonert.GamblKing.engine.connect;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.ruskonert.GamblKing.engine.GameServer;
import com.ruskonert.GamblKing.engine.framework.entity.PlayerFramework;
import com.ruskonert.GamblKing.framework.PlayerEntityFramework;
import com.ruskonert.GamblKing.property.ServerProperty;
import com.ruskonert.GamblKing.util.SystemUtil;

import javafx.concurrent.Task;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UpdateConnectionReceiver
{
    private DataInputStream in;
    public DataInputStream getInputStream() { return in; }

    private DataOutputStream out;
    public DataOutputStream getOutputStream() { return out; }

    private InetAddress address;

    private FileSender sender;

    private <T> T getFramework(String jsonMessage, Class<T> type)
    {
        Gson gson = new Gson();
        return gson.fromJson(jsonMessage, type);
    }

    private List<Thread> fileTaskBackground = new ArrayList<>();

    private Task<Void> taskBackground = new Task<Void>() {
        @Override
        protected Void call()
        {
            try
            {
                while(in != null)
                {
                    String jsonReceivedMessage = in.readUTF();
                    // EOF 에러 시 해당 클라이언트는 꺼진 상태입니다.
                    try
                    {
                        // 요청받은 메세지를 JsonObject로 가져옵니다.
                        JsonObject jo = getFramework(jsonReceivedMessage, JsonObject.class);
                        int connectionNumber = jo.get("status").getAsInt();

                        // 업데이트 서버에 파일 목록에 대한 요청을 받고 서버 내 업데이트 파일의 대한 정보를 보냄
                        if(connectionNumber == ServerProperty.SEND_UPDATE_REQURST)
                        {
                            Gson gsonSerialize = new Gson();
                            JsonObject object = new JsonObject();

                            object.addProperty("status", ServerProperty.SEND_UPDATE_REQURST_RECEIVED);
                            object.addProperty("data", SystemUtil.Companion.fixHashMap(gsonSerialize.toJson(Update.getUpdateFiles())));
                            out.writeUTF(object.toString());
                        }


                        // 클라이언트가 필요한 업데이트 파일 수신 요청이 들어왔을 경우
                        else if(connectionNumber == ServerProperty.SEND_UPDATE_FILE_REQUEST)
                        {
                            String[] fileListString = new Gson().fromJson(jo.get("data").toString().replaceAll("\\\\\"", "\"").
                                    substring(1, jo.get("data").toString().replaceAll("\\\\\"", "\"").length() -1), String[].class);
                            List<File> fileList = new ArrayList<>();
                            for(String s : fileListString)
                            {
                                fileList.add(new File("update/" + Update.getUpdateFiles().get(s)));
                            }
                            // 클라이언트 리서버에게 파일을 보냅니다.
                            FileSender.start(new Socket(ServerProperty.SERVER_ADDRESS, 7746), fileList.toArray(new File[fileList.size()]));
                        }


                        // 업데이트 파일 전송이 완전히 끝났을때, 실행되는 부분입니다.
                        // 해당 클라이언트에 대한 정보를 서버에 연결시킵니다.
                        else if(connectionNumber == ServerProperty.SEND_UPDATE_FILE_REQUEST_COMPLETED)
                        {
                            PlayerEntityFramework player = (PlayerEntityFramework) GameServer.getPlayer(jo.get("id").getAsString());

                            player.setLastConnected(GameServer.getServer().getDateFormat().format(new Date()));
                            player.setHostAddress(address.getHostAddress());

                            // 클라이언트가 서버 접속를 허가하도록 패킷을 보냅니다.
                            JsonObject object = new JsonObject();
                            object.addProperty("status", ServerProperty.CONNECT_GAME_SERVER);
                            object.addProperty("player", new Gson().toJson(player));
                            GameServer.getConsoleSender().log(player.getNickname() + "(" + player.getId() + ") joined the game.");
                            out.writeUTF(object.toString());
                        }
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                        String message = e.getMessage();
                        if(message == null) { message = "알수 없는 오류"; }
                        SystemUtil.Companion.alert("예외 오류 발생", "오류가 발생했습니다.", message);
                    }
                }
            } catch(Exception e)
            {
                e.printStackTrace();
                System.out.println(address.getHostAddress() + " was disconnected. reset the update data");
                ConnectionBackground.leaveFromStream(out);
            }
            return null;
        }
    };

    public UpdateConnectionReceiver(Socket socket) throws IOException
    {
        out = new DataOutputStream(socket.getOutputStream());
        in = new DataInputStream(socket.getInputStream());
        this.address = socket.getInetAddress();
        UpdateConnectionReceiver.join(socket.getInetAddress(), out);
        GameServer.getServer().getConsoleSender().log("The function UpdateConnectionReceiver() was initialized by " + socket.getInetAddress().getHostAddress());
    }

    public static void join(InetAddress address, DataOutputStream out)
    {
        GameServer.getServer().getConsoleSender().log(address.getHostAddress() + " connected the update server");
        ConnectionBackground.getClientMap().put(address.getHostAddress(), out);
    }

    public static void leave(InetAddress address)
    {
        leave(address.getHostAddress());
    }

    public static void leave(String address)
    {
        ConnectionBackground.getUpdateClientMap().remove(address);
        GameServer.getServer().getConsoleSender().log(address + " disconnected the update server");
    }


    public synchronized void asyncStart()
    {
        ConnectionBackground.getUpdateClientMap().put(address.getHostAddress(), out);
        Thread thread = new Thread(this.taskBackground);
        thread.start();
    }
}
