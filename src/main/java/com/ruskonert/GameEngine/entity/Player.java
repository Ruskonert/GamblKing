package com.ruskonert.GameEngine.entity;

import java.net.InetAddress;

public interface Player extends MessageDispatcher
{
    InetAddress getInetAddress();

    String getNickname();

    String getPassword();
}
