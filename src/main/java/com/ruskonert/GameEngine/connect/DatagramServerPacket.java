package com.ruskonert.GameEngine.connect;

import com.ruskonert.GameEngine.GameServer;
import com.ruskonert.GameEngine.server.ConsoleSender;
import com.ruskonert.GameEngine.util.SystemUtil;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class DatagramServerPacket
{
    private DatagramSocket socket;
    public DatagramSocket getSocket() { return this.socket; }

    public DatagramServerPacket() { this(7334); }
    public DatagramServerPacket(int port)
    {
        try
        {
            socket = new DatagramSocket(port);
            GameServer.getServer().getConsoleSender().sendMessage("Datagram server was loaded.");
        }
        catch (SocketException e)
        {
            GameServer.getServer().getConsoleSender().sendMessage("Datagram server was unloaded.");
            SystemUtil.Companion.error(e);
        }
    }

    public void setSocket(DatagramSocket socket)
    {
        this.socket = socket;
    }

    protected final void task() throws Exception
    {
        Service<Void> service = new Service<Void>()
        {
            @Override
            protected Task<Void> createTask() {
                ConsoleSender sender = GameServer.getServer().getConsoleSender();
                sender.sendMessage("Datagram packet now opened1.");
                while(true)
                {
                    byte[] inbuf = new byte[256];
                    sender.sendMessage("Datagram packet now opened2.");
                    DatagramPacket packet = new DatagramPacket(inbuf, inbuf.length);
                    sender.sendMessage("Datagram packet now opened3.");
                    try {
                        socket.receive(packet);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        onReceive(SystemUtil.Companion.arrayToObject(packet.getData()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        service.start();
    }

    protected void onReceive(Object handleInstance)
    {

    }
}
