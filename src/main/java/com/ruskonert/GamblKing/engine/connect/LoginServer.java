package com.ruskonert.GamblKing.engine.connect;

import com.ruskonert.GamblKing.entity.Player;

import java.util.List;
import java.util.Map;

public final class LoginServer
{
    private static Map<String, Player> connected;
    public static Map<String, Player> getConnectedPlayer() { return connected; }

    private static List<String> wait_connect;
    public static List<String> getWait() { return wait_connect; }
}
