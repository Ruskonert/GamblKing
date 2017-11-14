package com.ruskonert.GamblKing.client.connect;

import com.google.gson.*;
import com.ruskonert.GamblKing.client.ClientLoader;
import com.ruskonert.GamblKing.client.program.SignupApplication;
import com.ruskonert.GamblKing.client.program.UpdateApplication;
import com.ruskonert.GamblKing.engine.connect.server.Update;
import com.ruskonert.GamblKing.engine.property.ServerProperty;
import com.ruskonert.Gamblking.util.SystemUtil;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.*;
import java.net.InetSocketAddress;
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
        while (in != null)
        {
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

            try { status = requestedJsonObject.get("status").getAsInt(); } catch(NullPointerException e) { }

            // Processing
            clientRegisterLoginSocket(status, requestedJsonObject);


            // Requesting send the updated file
            /*
            if(status == 0)
            {
                Gson gson = new GsonBuilder().registerTypeAdapter(Map.class,
                        (JsonDeserializer<Map<String, File>>) (json, typeOfT, context) -> {
                    Map<String, File> m = new HashMap<>();
                    for(Map.Entry<String, JsonElement> e : json.getAsJsonObject().entrySet())
                    {
                        m.put(e.getKey(), new File(e.getValue().getAsJsonObject().get("path").toString().replaceAll("\"", "")));
                    }
                    return m;
                }).create();
                Map<String, File> updateFile = gson.fromJson(requestedJsonObject.toString(), Map.class);
                for(String k : UpdatePacket.getUpdateFiles().keySet())
                {
                    updateFile.remove(k);
                }
                JsonObject jo = new JsonObject();
                jo.addProperty("statusNumber", ServerProperty.SEND_UPDATE_FILE_REQUEST);
                StringBuilder builder = new StringBuilder();
                for(String s : updateFile.keySet())
                {
                    builder.append(s + ",");
                }
                jo.addProperty("data", builder.toString());
                this.send(jo.toString());
            }

            if(status == ServerProperty.SIGNAL_FILE_UPLOADED)
            {
                JsonObject jo = new Gson().fromJson(receivedMessage, JsonObject.class);
                Task<Void> task = new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        fileReceived(new Gson().fromJson(jo.get("data").toString(), Set.class));
                        return null;
                    }
                };
                Thread thread = new Thread(task);
                thread.start();
            }
            */
        }
    }

    private void fileReceived(String[] key)
    {
        Socket s = new Socket();
        try {
            s.connect(new InetSocketAddress(ServerProperty.SERVER_ADDRESS, 22644));
            InputStream in = s.getInputStream();
            FileOutputStream out = null;
            for(String hash : key)
            {
                out = new FileOutputStream(Update.getUpdateFiles().get(hash));
                byte[] buffer = new byte[8192];
                int bytesRead = 0;
                while((bytesRead = in.read(buffer)) > 0)
                {
                    out.write(buffer, 0, bytesRead);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
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
