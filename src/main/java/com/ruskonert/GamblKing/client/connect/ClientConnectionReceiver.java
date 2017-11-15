package com.ruskonert.GamblKing.client.connect;

import com.google.gson.*;
import com.ruskonert.GamblKing.property.ServerProperty;
import com.ruskonert.GamblKing.util.SystemUtil;
import com.ruskonert.GamblKing.client.ClientLoader;
import com.ruskonert.GamblKing.client.program.SignupApplication;
import com.ruskonert.GamblKing.client.program.UpdateApplication;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class ClientConnectionReceiver
{
    private Socket socket;
    public Socket getSocket() { return this.socket; }
    private DataInputStream in;
    private DataOutputStream out;

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
            jo.addProperty("message", "Connecting update server");

            Platform.runLater(() -> new UpdateApplication().start(new Stage()));
        }
    }


    public void readData()
    {
        while (in != null) {
            try
            {
                receivedMessage = in.readUTF();
            }
            catch (IOException e)
            {
                System.out.println("오류: 서버에서 연결을 끊음");
                taskBackground.interrupt();
                ClientLoader.setBackgroundConnection(null);
                break;
            }

            JsonObject requestedJsonObject = new Gson().fromJson(receivedMessage, JsonObject.class);
            int status = 0;

            try
            {
                status = requestedJsonObject.get("status").getAsInt();
            }
            catch (NullPointerException e)
            {
            }

            // Processing
            clientRegisterLoginSocket(status, requestedJsonObject);
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
