package com.ruskonert.GamblKing.engine.connect;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.ruskonert.GamblKing.engine.GameServer;
import com.ruskonert.GamblKing.property.ServerProperty;
import com.ruskonert.GamblKing.util.SystemUtil;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class UpdateConnectionReceiver
{
    private static DataInputStream in;
    public static DataInputStream getInputStream() { return in; }

    private static DataOutputStream out;
    public static DataOutputStream getOutputStream() { return out; }

    private String jsonReceivedMessage;

    private InetAddress address;

    private <T> T getFramework(String jsonMessage, Class<T> type)
    {
        Gson gson = new Gson();
        return gson.fromJson(jsonMessage, type);
    }

    private List<Thread> fileTaskBackground = new ArrayList<>();

    private Task<Void> taskBackground = new Task<Void>() {
        @Override
        protected Void call() throws Exception
        {
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

                        // 업데이트 서버에 파일 목록에 대한 요청을 받고 서버 내 업데이트 파일의 대한 정보를 보냄
                        if(connectionNumber == ServerProperty.SEND_UPDATE_REQURST)
                        {
                            Gson gsonSerialize = new Gson();
                            JsonObject object = new JsonObject();

                            object.addProperty("status", ServerProperty.SEND_UPDATE_REQURST_RECEIVED);
                            object.addProperty("data", SystemUtil.Companion.fixHashMap(gsonSerialize.toJson(Update.getUpdateFiles()).toString()));
                            out.writeUTF(object.toString());
                        }


                        // 필요한 파일 요청이 들어왔을 경우
                        if(connectionNumber == ServerProperty.SEND_UPDATE_FILE_REQUEST)
                        {
                            String[] fileList = new Gson().fromJson(jo.get("data").toString().replaceAll("\\\\\"", "\"").
                                    substring(1, jo.get("data").toString().replaceAll("\\\\\"", "\"").length() -1), String[].class);
                            for(String l : fileList)
                            {
                                Task<Void> voidTask = new Task<Void>()
                                {
                                    @Override
                                    protected Void call() throws Exception
                                    {

                                        File target = new File("update/" + Update.getUpdateFiles().get(l));
                                        RandomAccessFile f = new RandomAccessFile(new File("update/" + Update.getUpdateFiles().get(l)), "r");
                                        byte[] b = new byte[(int) f.length()];
                                        f.readFully(b);

                                        JsonObject object = new JsonObject();
                                        object.addProperty("status", ServerProperty.CLIENT_FILE_RECEIVED);
                                        object.addProperty("data", new Gson().toJson(b).toString());
                                        object.addProperty("path", target.getPath().toString());
                                        out.writeUTF(object.toString());
                                        return null;
                                    }
                                };

                                Thread t = new Thread(voidTask);
                                fileTaskBackground.add(t);
                                t.start();
                                t.wait();
                            }
                           // object.addProperty("status", ServerProperty.SEND_UPDATE_FILE_REQUEST);
                            //object.addProperty("data", new Gson().toJson(receivedFileHashList.toArray(new String[receivedFileHashList.size()])));
                            //object.addProperty("ipAddress", receiverIp);
                            //this.send(object.toString());
                            //assert in != null;
                            // CLIENT_FILE_RECEIVED를 사용해 파일 다운로드 요청을 보냅니다.
                        }

                        if(connectionNumber == ServerProperty.SEND_UPDATE_FILE_REQUEST_COMPLETED)
                        {
                            // 해당 클라이언트와의 업데이트 서버 연결을 끊고 게임 서버로 접속합니다.
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
            }
            catch(EOFException e)
            {
                e.printStackTrace();
                leave(address);
            }
            catch(Exception e)
            {
                e.printStackTrace();
                leave(address);
            }
            return null;
        }
    };

    public UpdateConnectionReceiver(Socket socket) throws IOException
    {
        out = new DataOutputStream(socket.getOutputStream());
        in = new DataInputStream(socket.getInputStream());
        this.address = socket.getInetAddress();
        this.join(socket.getInetAddress(), out);
        GameServer.getServer().getConsoleSender().log("The class UpdateConnectionReceiver() was initialized from " + socket.getInetAddress().getHostAddress());
    }

    public void join(InetAddress address, DataOutputStream out)
    {
        GameServer.getServer().getConsoleSender().log(address.getHostAddress() + " connected to update server");
        ConnectionBackground.getClientMap().put(address, out);
    }

    public void leave(InetAddress address)
    {
        ConnectionBackground.getUpdateClientMap().remove(address);
        GameServer.getServer().getConsoleSender().log(address.getHostAddress() + " disconnected to update server");
    }

    public void asyncStart()
    {
        Thread thread = new Thread(this.taskBackground);
        thread.start();
    }
}
