package com.ruskonert.GamblKing.engine.connect;

import com.ruskonert.GamblKing.engine.GameServer;
import com.ruskonert.GamblKing.engine.MessageType;
import javafx.concurrent.Task;

import java.io.*;
import java.net.Socket;

public final class UpdateSender
{
    public static void start(String fromAddress, int port, String sendFilename)
    {
        new UpdateSender(fromAddress, port, sendFilename);
    }

    Socket socket = null;

    private String host = null;

    private int port = 0;

    public UpdateSender(String address, int port, String sendFilename)
    {
        try
        {

            this.host = address.substring(1,address.length()-1);
            this.port = port;
            socket = new Socket(host, this.port);
            GameServer.getConsoleSender().sendMessage("UpdateSender(address, port, sendFilename) now running and connected from " + address + ":" + port);
            Task<Void> updateTask = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    FileSender fs = new FileSender(socket, sendFilename);
                    Thread thread = new Thread(fs);
                    thread.start();
                    return null;
                }
            };
            Thread t = new Thread(updateTask);
            t.start();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}


class FileSender extends Task<Void>
{
    private Socket socket;
    private DataOutputStream dos;
    private FileInputStream fis;
    private BufferedInputStream bis;
    private String filename;
    private int control = 0;

    String host = null;

    int port = 0;

    public FileSender(Socket socket,String fileHashName)
    {
        this.socket = socket;
        this.filename = fileHashName;
        this.host = socket.getInetAddress().getHostAddress();
        this.port = socket.getPort();
        try {
            // 데이터 전송용 스트림 생성
            dos = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public Void call() throws Exception
    {
        try
        {
                File f = new File("update/" + Update.getUpdateFiles().get(filename));
                if(!f.exists())
                {
                    GameServer.getConsoleSender().sendMessage(f.getName() + " doesn't exist file. skipping", MessageType.WARNING);
                    return null;
                }
                fis    = new FileInputStream(f);
                bis    = new BufferedInputStream(fis);

                int len;
                int size = 4096;
                byte[] data = new byte[size];
                GameServer.getConsoleSender().sendMessage("Sending the file data: update/" + Update.getUpdateFiles().get(filename));
                while ((len = bis.read(data)) != -1)
                {
                    dos.write(data, 0, len);
                }

            GameServer.getConsoleSender().sendMessage("File updating was complete from " + Update.getUpdateFiles().get(filename) + " " + socket.getInetAddress().getHostAddress()
                    + ":" + socket.getPort());

                dos.flush();
                dos.close();
                bis.close();
                fis.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}


