package com.ruskonert.GameClient.connect;

import com.ruskonert.GameClient.ClientLoader;
import com.ruskonert.GameEngine.property.ServerProperty;
import javafx.concurrent.Task;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

public class ClientBackground
{
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

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
            e.printStackTrace();
            System.out.println("서버 연결 실패");
            ClientLoader.setBackgroundConnection(null);
        }
    }

    public static void checkClientConnection()
    {
        if(ClientLoader.getBackgroundConnection() == null)
        {
            ClientBackground background = new ClientBackground();
            background.initialize();

            Task<Void> task = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    background.readData();
                    return null;
                }
            };

            Thread thread = new Thread(task);
            thread.start();
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
                return;
            }
            System.out.println("서버로부터 받은 메세지: " + receivedMessage);
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
            ClientBackground.checkClientConnection();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
