package com.ruskonert.GameClient;

import com.ruskonert.GameClient.register.SignupApplication;

import com.ruskonert.GameEngine.entity.Player;
import com.ruskonert.GameEngine.util.SystemUtil;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientLoader extends Application
{
    public static void main(String[] args) { launch(args);}
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        /*
        FXMLLoader loader = new FXMLLoader(SystemUtil.Companion.getStylePath("style/client_login.fxml"));
        primaryStage.setTitle("Ruskonert's Game");
        primaryStage.setScene(new Scene(loader.load(), 720,480));
        primaryStage.show();
        */
        ClientBackground clientBackground = new ClientBackground();
        clientBackground.connect();
    }
}
class ClientBackground {

    protected Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private String jsonMessage;
    private Player player;

    public void connect()
    {
        try
        {
            socket = new Socket("127.0.0.1", 7443);
            System.out.println("서버 연결됨.");

            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());

            //접속하자마자 닉네임 전송하면. 서버가 이걸 닉네임으로 인식을 하고서 맵에 집어넣겠지요?
            out.writeUTF("JsonMesage");
            System.out.println("클라이언트 : 메시지 전송완료");
            while(in!=null)
            {
                jsonMessage =in.readUTF();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void sendMessage(String msg2)
    {
        try
        {
            out.writeUTF(msg2);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}