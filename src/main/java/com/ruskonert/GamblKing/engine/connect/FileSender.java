package com.ruskonert.GamblKing.engine.connect;

import com.ruskonert.GamblKing.engine.GameServer;
import javafx.concurrent.Task;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.Socket;

public class FileSender
{
    public static void start(Socket socket, File[] files)
    {
        new FileSender(socket, files);
    }
    private Socket socket;
    private FileSender(Socket socket, File[] files)
    {
        this.socket = socket;
        Task<Void> backgroundTask = new Task<Void>()
        {
            @Override
            protected Void call() throws Exception
            {
                    BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());
                    DataOutputStream dos = new DataOutputStream(bos);

                    dos.writeInt(files.length);
                    GameServer.getConsoleSender().log("Sending the files to " + socket.getInetAddress().getHostAddress());
                    for(File file : files)
                    {
                        long len = file.length();
                        dos.writeLong(len);

                        String name = file.getPath();
                        dos.writeUTF(name);

                        FileInputStream fis = new FileInputStream(file);
                        BufferedInputStream bis = new BufferedInputStream(fis);
                        int size = 0;
                        while((size = bis.read()) != -1) bos.write(size);

                        bis.close();
                    }
                    dos.close();
                GameServer.getConsoleSender().log("Sending complete to " + socket.getInetAddress().getHostAddress());
                return null;
            }
        };
        Thread background = new Thread(backgroundTask);
        background.start();
    }
}
