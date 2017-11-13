package com.ruskonert.GameEngine;

import com.ruskonert.GameEngine.entity.OfflinePlayer;
import com.ruskonert.GameEngine.entity.Player;
import com.ruskonert.GameEngine.server.Channel;
import com.ruskonert.GameEngine.server.ConsoleSender;
import com.ruskonert.GameEngine.server.Server;

import java.util.Collection;
import java.util.Map;

/**
 * 게임 서버와 관련한 정보를 가져오거나 확인할 수 있습니다. 외부 클래스에서 이 서버의 정보를 얻어오고자 할 때,
 * 해당 클래스에 구현되어 있는 정적 메소드로 값을 호출할 수 있습니다.
 * 이 클래스는 정적 클래스입니다. 생성자를 허용하지 않습니다.
 * @author Ruskonert(Junwon Kim)
 * @version 1.0.0
 */
public final class GameServer
{
    private GameServer() {}
    private static Server server;

    /**
     * 서버 정보를 담은 프레임워크를 가져옵니다.
     * @return 서버 프레임워크
     */
    public static Server getServer() { return server; }

    public static Collection<? extends Player> getPlayers() { return server.getPlayers(); }

    public static Player getPlayer(String id) { return server.getPlayer(id); }

    public static Collection<? extends OfflinePlayer> getOfflinePlayer() { return server.getOfflinePlayer(); }

    public static Channel getChannel(String channelName) { return  server.getChannel(channelName); }

    public static ConsoleSender getConsoleSender() { return server.getConsoleSender(); }

    public static Map<String, Channel> getChannels() { return server.getChannels(); }
}