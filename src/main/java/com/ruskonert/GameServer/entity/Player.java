package com.ruskonert.GameServer.entity;

import java.net.InetAddress;

public interface Player
{
    InetAddress getInetAddress();

    String getNickname();

    String getPassword();
}
