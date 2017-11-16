package com.ruskonert.GamblKing.client.connect;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import com.ruskonert.GamblKing.client.ClientLoader;
import com.ruskonert.GamblKing.client.connect.packet.ClientRequestedPacket;
import com.ruskonert.GamblKing.client.program.SignupApplication;
import com.ruskonert.GamblKing.client.program.UpdateApplication;
import com.ruskonert.GamblKing.property.ServerProperty;
import com.ruskonert.GamblKing.util.SecurityUtil;
import com.ruskonert.GamblKing.util.SystemUtil;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClientConnectionReceiver
{
    private Socket socket;
    public Socket getSocket() { return this.socket; }
    private DataInputStream in;
    private DataOutputStream out;
    public DataOutputStream getOutputStream() { return this.out;}

    private static Thread taskBackground;

    private String receivedMessage = null;

    public void initialize()
    {
        try
        {
            ClientLoader.setBackgroundConnection(this);
            socket = new Socket(ServerProperty.SERVER_ADDRESS, ServerProperty.SERVER_PORT);
            System.out.println("서버에 연결됨, 주소 호출 대상: " +  socket.getInetAddress().getHostAddress() + ":" + socket.getPort());

            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());
        }
        catch(IOException e)
        {
            System.out.println("서버 연결 실패, 서버가 닫혀있습니다.");
            ClientLoader.setBackgroundConnection(null);
        }
    }

    public static void refreshClientConnection()
    {
        if(ClientLoader.getBackgroundConnection() == null)
        {
            ClientConnectionReceiver background = new ClientConnectionReceiver();
            background.initialize();

            Task<Void> task = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    background.readData();
                    return null;
                }
            };

            Thread thread = new Thread(task);
            taskBackground = thread;
            taskBackground.start();
        }
    }

    private void clientRegisterLoginSocket(int status, JsonObject requestedJsonObject)
    {
        if(status == ServerProperty.REGISTER_SUCCESSED_ACCOUNT)
        {
            SystemUtil.Companion.alert("회원가입", "회원가입 완료", requestedJsonObject.get("message").getAsString(), Alert.AlertType.INFORMATION);
            Platform.runLater(() -> SignupApplication.getStage().close());
        }

        if(status == ServerProperty.REGISTER_FAILED_ACCOUNT)
        {
            SystemUtil.Companion.alert("실패", "회원가입 실패", requestedJsonObject.get("message").getAsString(), Alert.AlertType.ERROR);
        }

        if(status == ServerProperty.RECEIVED_LOGIN_FAILED)
        {
            SystemUtil.Companion.alert("로그인", "로그인 실패", requestedJsonObject.get("message").getAsString(), Alert.AlertType.ERROR);
        }

        if(status == ServerProperty.RECEVIED_LOGIN_SUCCESS)
        {
            JsonObject jo = new JsonObject();
            jo.addProperty("statusNumber", ServerProperty.RECEVIED_LOGIN_SUCCESS);
            jo.addProperty("message", "Connecting update server from " + ClientLoader.getBackgroundConnection().
                    getSocket().getInetAddress().getHostAddress());
            Platform.runLater(() -> new UpdateApplication().start(new Stage()));
        }
    }

    // 데이터를 주기적으로 읽어옵니다
    public void readData()
    {
        while (in != null)
        {
            try
            {
                // 데이터는 무조건 json 형식입니다.
                receivedMessage = in.readUTF();
            }
            catch (IOException e)
            {
                System.out.println("오류: 서버에서 연결을 끊음");
                taskBackground.interrupt();
                ClientLoader.setBackgroundConnection(null);
                break;
            }

            // 요청받은 데이터를 jsonObject로 바꿉니다.
            JsonObject requestedJsonObject = new Gson().fromJson(receivedMessage, JsonObject.class);
            int status = 0;

            try
            {
                status = requestedJsonObject.get("status").getAsInt();
            }
            catch (NullPointerException e)
            {
            }

            // 로그인과 회원가입과 관련된 패킷을 처리합니다.
            clientRegisterLoginSocket(status, requestedJsonObject);


            // 업데이트 서버에 가져온 업데이트 파일들이 클라이언트에 제대로 있는지 확인합니다.
            // 없다면, 업데이트 서버에서 파일을 다운받습니다.
            if(status == ServerProperty.SEND_UPDATE_REQURST_RECEIVED)
            {
               ClientRequestedPacket packet = new Gson().fromJson(requestedJsonObject, ClientRequestedPacket.class);
               List<String> receivedFileHashList = new ArrayList<>();
               List<String> downloadFileList = new ArrayList<>();

                for(Map.Entry<String, String> e : packet.getData().entrySet())
                {
                    try
                    {
                        File checkFile = new File("data/" + e.getValue());
                        if (checkFile.exists()) {
                            try {
                                String sha = SecurityUtil.Companion.extractFileHashSHA256(checkFile.getPath());

                                // 만약 그 파일이 있음에도 불구하고 업데이트 서버에서 받은 파일과 다른 것이라면 (오래된 파일이라면)
                                if (!e.getKey().equalsIgnoreCase(sha)) {
                                    receivedFileHashList.add(e.getKey());
                                    downloadFileList.add("data/" + e.getValue());
                                }
                            } catch (Exception ee) {
                                ee.printStackTrace();
                            }
                        } else {
                            receivedFileHashList.add(e.getKey());
                            downloadFileList.add("data/" + e.getValue());
                        }

                    }
                    catch(Exception e2)
                    {
                        e2.printStackTrace();
                    }

                }


                String receiverIp = ClientLoader.getBackgroundConnection().getSocket().getInetAddress().getHostAddress();

                JsonObject object = new JsonObject();
                object.addProperty("status", ServerProperty.SEND_UPDATE_FILE_REQUEST);
                object.addProperty("data", new Gson().toJson(receivedFileHashList.toArray(new String[receivedFileHashList.size()])));
                object.addProperty("ipAddress", receiverIp);

                UpdateReceiver.start(downloadFileList.toArray(new String[downloadFileList.size()]));
            }
        }
    }

    public void send(String message)
    {
        try
        {
            out.writeUTF(message);
        }

        catch(SocketException e)
        {
            ClientConnectionReceiver.refreshClientConnection();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}
