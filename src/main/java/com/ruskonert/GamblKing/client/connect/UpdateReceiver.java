package com.ruskonert.GamblKing.client.connect;

import com.google.gson.JsonObject;
import com.ruskonert.GamblKing.client.ClientLoader;
import com.ruskonert.GamblKing.property.ServerProperty;
import javafx.concurrent.Task;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class UpdateReceiver
{
    private List<Thread> background = new ArrayList<Thread>();
    private ServerSocket serverSocket;
    private Socket socket;
    private String[] filename;

    private int priorty = 0;

    public static void start(String[] receivedFilename)
    {
        new UpdateReceiver(receivedFilename);
    }

    public UpdateReceiver(String[] receivedFilename)
    {
            Task<Void> socketOpen = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    // 리스너 소켓 생성 후 대기
                    serverSocket = new ServerSocket(8888); // socket(),bind();
                    socket = serverSocket.accept(); // listen(),accept();
                    filename = receivedFilename;
                    long start = System.currentTimeMillis();

                    Task<Void> task = new Task<Void>() {
                        @Override
                        protected Void call() throws Exception {
                            FileReceiver fr = new FileReceiver(socket,filename[priorty],start);
                            Thread thread = new Thread(fr);
                            thread.start();
                            return null;
                        }
                    };

                    while(priorty == filename.length)
                    {
                        Thread thread = new Thread(task);
                        background.add(thread);
                        thread.start();

                        JsonObject object = new JsonObject();
                        object.addProperty("status", ServerProperty.SEND_UPDATE_FILE_REQUEST);
                        object.addProperty("path", filename[priorty]);
                        object.addProperty("ipAddress", socket.getInetAddress().getHostAddress());

                        ClientLoader.getBackgroundConnection().getOutputStream().writeUTF(object.toString());
                        priorty++;
                    }
                    return null;
                }
            };

            Thread mainThread = new Thread(socketOpen);
            mainThread.start();
    }
}

class FileReceiver extends Task<Void>
{
    Socket socket;
    DataInputStream dis;
    FileOutputStream fos;
    BufferedOutputStream bos;

    String filename;
    long start;
    int control = 0;

    public FileReceiver(Socket socket, String filestr, long starttime)
    {
        this.socket = socket;
        this.filename = filestr;
        this.start = starttime;
    }

    @Override
    protected Void call() throws Exception
    {
        try
        {
            dis = new DataInputStream(socket.getInputStream());
            // 파일을 생성하고 파일에 대한 출력 스트림 생성
            File f = new File(filename);
            if (f.exists()) {
                // updating the file.
                f.delete();
            }

            fos = new FileOutputStream(f);
            bos = new BufferedOutputStream(fos);
            // 바이트 데이터를 전송받으면서 기록
            int len;
            int size = 4096;
            byte[] data = new byte[size];
            System.out.println(f.getName() + " 수신중..." + control / 10000);
            while ((len = dis.read(data)) != -1)
            {
                        bos.write(data, 0, len);
                    }
                    long end = System.currentTimeMillis();
                    System.out.println("Elapsed Time (seconds) : " + (end - start) / 1000.0);
                bos.flush();
                bos.close();
                fos.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}