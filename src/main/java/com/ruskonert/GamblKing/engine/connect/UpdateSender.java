package com.ruskonert.GamblKing.engine.connect;

import com.ruskonert.GamblKing.engine.GameServer;
import javafx.concurrent.Task;

import java.io.*;
import java.net.Socket;

public class UpdateSender
{
    public static void start(String address, int port, String[] sendFilename)
    {
        new UpdateSender(address, port, sendFilename);
    }

    Socket socket = null;

    public UpdateSender(String address, int port, String[] sendFilename)
    {
        try
        {
            socket = new Socket(address, port);
            GameServer.getConsoleSender().sendMessage("the update client was connected from " + address + ":" + port);
            Task<Void> updateTask = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    FileSender fs = new FileSender(socket, sendFilename);
                    Thread thread = new Thread(fs);
                    thread.start();
                    return null;
                }
            };
            Thread t = new Thread();
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
    Socket socket;
    DataOutputStream dos;
    FileInputStream fis;
    BufferedInputStream bis;
    String[] filename;
    int control = 0;

    public FileSender(Socket socket,String[] filename)
    {
        this.socket = socket;
        this.filename = filename;
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
            String[] fName = filename;
            for(String path : fName)
            {
                File f = new File(path);
                fis    = new FileInputStream(f);
                bis    = new BufferedInputStream(fis);

                int len;
                int size = 4096;
                byte[] data = new byte[size];

                while ((len = bis.read(data)) != -1)
                {
                    control++;
                    if(control % 10000 == 0)
                    {
                        GameServer.getConsoleSender().sendMessage("Sending the file data " + f.getName()
                               + " process: " + control/10000);
                    }
                    dos.write(data, 0, len);
                }
                dos.flush();
                dos.close();
                bis.close();
                fis.close();
            }

            GameServer.getConsoleSender().sendMessage("File updating was complete from" + socket.getInetAddress().getHostAddress()
            + ":" + socket.getPort());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}


