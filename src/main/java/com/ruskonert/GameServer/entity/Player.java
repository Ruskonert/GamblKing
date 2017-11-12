package com.ruskonert.GameServer.entity;

import java.net.InetAddress;

public interface Player extends MessageDispatcher
{
    InetAddress getInetAddress();

    String getNickname();

    String getPassword();
}
