package com.ruskonert.GameEngine.connect;

import javafx.concurrent.Task;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class DatagramServerPacket extends Task<Void>
{
    private DatagramSocket socket;
    private DatagramPacket packet;

    @Override
    protected Void call() throws Exception
    {
        while(true)
        {
            byte[] inbuf = new byte[256];
            this.packet = new DatagramPacket(inbuf, inbuf.length);

            socket.receive(packet);
        }
    }

    public Object arrayToObject(byte[] buf) throws IOException, ClassNotFoundException
    {
        ByteArrayInputStream bis = new ByteArrayInputStream(buf);
        ObjectInput in = new ObjectInputStream(bis);
        return in.readObject();
    }

    public byte[] objectToArray(Object obj) throws IOException
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(bos);
        out.writeObject(obj);
        return bos.toByteArray();
    }
}
